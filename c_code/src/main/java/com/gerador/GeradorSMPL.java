package com.gerador;

import com.graph.Chegada;
import com.graph.Graph;
import com.graph.Node;
import com.graph.QueueL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;


/**
 *  Classe que gera c   digo na extens   o funcional SMPL (MacDougall).
 *  @author Andr    Felipe Rodrigues
 *  @version 1.0
 */

public class GeradorSMPL extends Gerador {
	
	public static final String TAXA_SERVICO = "Ts";
	public static final String TAXA_CHEGADA = "Ta";
		
	private boolean auxiliarLinhaNotDefinedYet;
	private boolean alreadyClosed;
	
	
	private boolean stopByUsers;
	private int numClientes;
	private String antigoMaxClientes;
	/**
	 * Vari   vel utilizada para armazenar as informa      es que ser   o gravadas no arquivo de  
	 * sa   da.
	 */
	private StringBuffer buffer = new StringBuffer(120);	
	
	
	/**
	 * Para impress   o do relat   rio de estat   sticas
	 */
		
	/* Defini      o de constantes */	
	private static final int tempoExecDefault = 20000;
	private static final String pontoVirgula = ";";
	
	private Controler c;
	
	
	
	/**
	 * Construtor da Classe.
	 * @param graph Recebe o modelo que ir    gerar o programa de simula      o.
	 */
	public GeradorSMPL(Graph graph) {
  	
	  super(graph);
	  auxiliarLinhaNotDefinedYet = true;	 
	  alreadyClosed = false;
	  stopByUsers = false;
	  
	  int numInitReq = 2, numInitRel = 3;
		int contFonte = 0;
		
		
		for (int i = 0; i < graph.getSize(); i++){ // conta o n   mero de fontes do grafo
			if (graph.getNode(i).isPrimRec()){
				contFonte++;																		
			}
		}
		
		numInitReq = contFonte+1;
		numInitRel = contFonte+2;		
		
		
		c = new Controler(graph, numInitReq, numInitRel); // gera n   meros de case apropriados para request e release de cada centro de servi   o

	}

	
	/**
	 * Cria o arquivo que ir    conter o programa de simula      o.
	 */
	public void criaArquivo(){
		
		filename = graph.getNomeModelo();
		
		filename = filename + ".c";
			File f = new File(filename);		
			if (f.exists()) 
				f.delete();

	}


	/**
	 * Quando tempo de warm-up     definido como autom   tico, ele     setado como 5%
	 * do tempo total de simula      o
	 * @return
	 */
	private double generateWarmUpTime()
	{
		if ((graph.getTempoExecucao() != null) && (graph.getTamWarmUp().equals("0")))
		{
			double valor;
			valor = 0.05*Double.parseDouble(graph.getTempoExecucao());
			return valor;
		}
		else
			return 0;
	}
	
	
	/**
	 * Grava o valor da vari   vel buffer no arquivo.
	 * @param buffer Vari   vel que cont   m as informa      es a serem gravadas no arquivo.
	 */
	private void gravaArquivo(StringBuffer buffer) {
				
		
		try {          
            
			FileWriter out = new FileWriter(new File(filename),true);
			out.write(buffer.toString());
			out.write('\n');
			
			out.close();
            
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}//

	

	/**
	 * Gera o comando para limitar a execu      o da simula      o:
	 * - por tempo;
	 * - por n   mero de clientes que deixam o sistema ou
	 * - pelo n   mero de ciclos do cliente no sisema.
	 */
	private void defineTempoMax(){
		// defini      o do tempo m   ximo da simula      o
		buffer.delete(0,119);
		buffer.append(" float Te = ");
		if (graph.getTempoExecucao().equals("0")){
			JOptionPane.showMessageDialog(
					null,
					"Tempo de simula      o definido automaticamente\n" +
					"Tempo padr   o = " + tempoExecDefault,
					"Tempo de execu      o n   o especificado",
					JOptionPane.INFORMATION_MESSAGE);
			graph.setTempoExecucao(String.valueOf(tempoExecDefault));
		}
		buffer.append(graph.getTempoExecucao());
		buffer.append(pontoVirgula);
		gravaArquivo(buffer);	

		
		// defini      o do n   mero m   ximo de entidades no sistema simulado
		// se for definido como 0, o simula      o n   o p   ra devido ao n   mero de entidades		
	/*	if (!graph.getNumeroMaximoEntidades().equals("0")){
			buffer.delete(0,119);
			buffer.append(" unsigned int Maximo_Entidades = 0, Num_Max_Entidades = ");
			buffer.append(graph.getNumeroMaximoEntidades());
			buffer.append(pontoVirgula);
			gravaArquivo(buffer);
		}*/
		
		/* VER O QUE FAZER EM RELA      O AO N   MERO DE CICLOS :-( */
			
		/*if (Double.parseDouble(graph.getNumeroCiclos()) != 0){
			buffer.delete(0,119);
			buffer.append(" unsigned int Num_Voltas = 0, Num_Max_Voltas = ");
			buffer.append(graph.getNumeroCiclos());
			buffer.append(pontoVirgula);
			gravaArquivo(buffer);;
			
		}*/
		// Se     definido pelo usu   rio,     setada uma vari   vel para o reset
	
		
	}
	
	private void defineMaximoClientes()
	{
		buffer.delete(0,119);
		
		stopByUsers = valueOfStopByUsers(); // temos agora stopByUsers e numClientes;
		if (numClientes > 1) // se mais de um cliente, coloca numero maximo de clientes como numClientes
			                 // mesmo se o usu   rio tinha definido um n   mero para o n   mero m   ximo de clientes
							// nesse caso, ele teria sido inconsistente em sua modelagem
		{
			antigoMaxClientes = graph.getNumeroMaximoEntidades();		
			graph.setNumeroMaximoEntidades(String.valueOf(numClientes));
		}
		
		if ( (graph.getNumeroMaximoEntidades() != null) && (!graph.getNumeroMaximoEntidades().equals("0")) ) {
			buffer.append(" unsigned int Maximo_Entidades = 0, Num_Max_Entidades = ");
			buffer.append(graph.getNumeroMaximoEntidades());
			buffer.append(";");
			gravaArquivo(buffer);
		}
	}
	
	private void defineVarWarmUp()
	{
		
		buffer.delete(0,119);
		buffer.append(" char flag_reset = 0;\n");
		buffer.append(" float timeWarmUp = ");
		if ( (graph.getTamWarmUp() != null ) && (!graph.getTamWarmUp().equals("0")) ){
			buffer.append(graph.getTamWarmUp());			
		}
		else
		{
			buffer.append(String.valueOf(generateWarmUpTime()));
		}
		buffer.append(pontoVirgula);
		gravaArquivo(buffer);
		
		
	}
	
	/**
	 * Gera o comando para determinar qual a seq      ncia a ser utilizada na gera      o do 
	 * n   mero aleat   rio. Quando o comando smpl     executado a seq      ncia     setada para 1 
	 * e incrementada a medida em que     utilizada.
	 * @param sequencia Um valor <code>Integer</code> compreendido no intervalo 1..15.
	 * @param espaco Para formata      o do arquivo de sa   da.
	 */
	private void geraStream(int indice, String espaco,boolean fonte){
		
		if (!fonte)
		{
			if ( (graph.getNode(indice).getSequencia() != null) && (!graph.getNode(indice).getSequencia().equals("0")))
			{	buffer.delete(0,119);
				buffer.append(espaco);
				buffer.append("stream(");
				buffer.append(graph.getNode(indice).getSequencia());
				buffer.append(");");
				gravaArquivo(buffer);
			}
		}
		else
		{
			if ( (graph.getNode(indice).getSequenciaFonte() != null) && (!graph.getNode(indice).getSequenciaFonte().equals("0")))
			{
				buffer.delete(0,119);
				buffer.append(espaco);
				buffer.append("stream(");
				buffer.append(graph.getNode(indice).getSequenciaFonte());
				buffer.append(");");
				gravaArquivo(buffer);
			}
			
		}
	}
	
	
	/**
	 * Gera a primitiva break.
	 *
	 */
	private void geraBreak(){
		
		buffer.delete(0,119);
		buffer.append("          break;");
		gravaArquivo(buffer);
			
	}

	
	/**
	 * Gera as vari   veis para os tempos m   dios entre chegadas e servi   o.
	 *
	 */
	private void defineVarTempos(){
		
		buffer.delete(0,119);
		
		String nomeA = "Ta"; 	// Taxa Arrival
		String nomeB = "Ts";	// Taxa service
		
		buffer.delete(0,119);
		buffer.append(" float ");
				
		

		
		// define tempo de chegada de centros de servi   os que s   o primeiro recurso
		for (int i = 0; i < graph.getSize(); i++)
		{   // Centro de servi   o de chegada			
			if ((graph.getNode(i).getTipoNo() == 2) && (graph.getNode(i).isPrimRec())){
				buffer.append(nomeA);
				buffer.append(String.valueOf(graph.getNode(i).getIdNo()));
				buffer.append(" = ");
				buffer.append(graph.getNode(i).getMediaFonte());
			}	
		}				
			
		for (int i = 0; i< graph.getSize(); i++)
		{	// Define a taxa de servi   o de todos centros de servi   os
			if (graph.getNode(i).getTipoNo() == 2){
				buffer.append(", ");
				buffer.append(nomeB);
				buffer.append(String.valueOf(graph.getNode(i).getIdNo()));
				buffer.append(" = ");
				buffer.append(graph.getNode(i).getMedia());
			}
		}
		buffer.append(";\n");
		buffer.append(" int i = 0;\n");
		gravaArquivo(buffer);
		
	}


	
	/**
	 * Gera as vari   veis para as defini      es dos recursos que compoem o modelo a ser 
	 * implementado.
	 *
	 */
	private void defineVarServer(){
		
		buffer.delete(0,119);
		buffer.append(" int ");
		
		
		for (int i = 0; i < graph.getSize(); i++){
			if (graph.getNode(i).getTipoNo() == 2){		
				buffer.append(graph.getNode(i).getNomeCentroServico());
				buffer.append(", ");
			}			
		}
		// retira ultima virgula
		int temp = buffer.lastIndexOf(", ");
		buffer.delete(temp, buffer.length());
		// adiciona o ponto e v   rgula
		buffer.append(";");
		gravaArquivo(buffer);
			        

	}
	
	/*
	 * M   todo de aux   lio para n   o redefinir a mesma linha no c   digo usado
	 * para os dois tipos de estat   sticas
	 */
	private void defineLinhaTotClientes()
	{
		if (auxiliarLinhaNotDefinedYet)
		{
			buffer.delete(0,119);
			buffer.append(" unsigned int Total_Clientes = 0; ");
			gravaArquivo(buffer);
			
			auxiliarLinhaNotDefinedYet = false;
		}
	}


	/**
	 * Define as vari   veis que ser   o utilizadas para os c   lculos estat   sticos:
	 * tamanho m   ximo e m   nimo da fila associada ao recurso especificado.
	 *
	 */
	private void defineEstatMaxMin(){
		
		String nomeMax = "Max";
		String nomeMin = "Min";
		
		
		buffer.delete(0,119);
		int cont = 0;
		
		int i = 0;
		for (i=0; i< graph.getSize(); i++) {
			if ( (graph.getNode(i).getTipoNo() == 2) && (graph.getNode(i).getComprimentoMaxMin()) )
				cont++;
		}
		
		if (cont != 0) // s    gera se o grafo define estat   sticas
		{
			buffer.append(" unsigned int ");
			
			for (i = 0; i < graph.getSize(); i++)
			{
				Node temp = graph.getNode(i);
				if (  (temp.getTipoNo() == 2) && (temp.getComprimentoMaxMin()) ) // o n    mede estat   stica fila vazia
				{
					buffer.append(nomeMax);
					buffer.append(temp.getIdNo());
					buffer.append(" = 0");
					buffer.append(", ");
					buffer.append(nomeMin);
					buffer.append(temp.getIdNo());
					buffer.append(" = 1000");
					cont--;
					if (cont == 0) // n   o tem mais vari   veis para adicionar, finaliza com ;
						buffer.append(";");
					else
						buffer.append(",");							
				}				
			}
			gravaArquivo(buffer);
			
			defineLinhaTotClientes();
		}	
	}
	
	/**
	 * Define as vari   veis que ser   o utilizadas para os c   lculos estat   sticos: porcentagem
	 * de vezes em que o cliente encontra a fila vazia, associada ao recurso especificado.
	 */
	private void defineEstatFilaVazia(){
		
		String nomeTotal = "Tot";
		String nomeVaz = "Vaz";
		
		
		buffer.delete(0,119);
		int cont = 0;
		
		int i = 0;
		for (i=0; i< graph.getSize(); i++) {
			if ( (graph.getNode(i).getTipoNo() == 2) && (graph.getNode(i).getEstatisticaFilaVazia()) )
				cont++;
		}
		
		if (cont != 0) // s    gera se o grafo define estat   sticas
		{
			buffer.append(" unsigned int ");
			
			for (i = 0; i < graph.getSize(); i++)
			{
				Node temp = graph.getNode(i);
				if (  (temp.getTipoNo() == 2) && (temp.getEstatisticaFilaVazia()) ) // o n    mede estat   stica fila vazia
				{
					buffer.append(nomeTotal);
					buffer.append(temp.getIdNo());
					buffer.append(" = 0");
					buffer.append(", ");
					buffer.append(nomeVaz);
					buffer.append(temp.getIdNo());
					buffer.append(" = 0");
					cont--;
					if (cont == 0) // n   o tem mais vari   veis para adicionar, finaliza com ;
						buffer.append(";");
					else
						buffer.append(",");							
				}				
			}
			gravaArquivo(buffer);
			
			defineLinhaTotClientes();
		}		
		
	}
	
	/**
	 * Define as vari   veis necess   rias para utilizar o m   todo de an   lise Batch Means.
	 *
	 */
	private void defineBMeans(){
		if ( (graph.getTamanhoBatch() != null) && (!graph.getTamanhoBatch().equals("0")) ){
			buffer.delete(0,119);
			buffer.append(" float TBatch = 0;\n");
			buffer.append(" int r = 0;");
			gravaArquivo(buffer);
		}
	}
	
	
	//sem fila
	/*public void defineReesc(){
		
		
		
	}
	
	//sem fila
	public void defineVarBackoff(){
		
		int flag = 1;
		long ind = 1;
		
		
	}*/
	
	
	/**
	 * Declara e abre para escrita o arquivo de sa   da no c   digo do programa de simula      o.
	 * Este arquivo de sa   da conter    o relat   rio da simula      o.
	 */
	private void defineArqSaida(){
		
		buffer.delete(0,119);
		buffer.append(" FILE *p, *saida;\n");
		buffer.append(" saida = fopen(\"");
		buffer.append(graph.getNomeModelo());
		buffer.append(".out\",\"w\");\n"); // chegou a quase 100 caracteres j   
		gravaArquivo(buffer);
		buffer.delete(0,119);
		buffer.append(" if ((p = sendto(saida)) == NULL)\n");
		buffer.append("    printf(\"Erro na saida \\n\");");
		gravaArquivo(buffer);
		
		
	}
	
	/**
	 * Gera o comando SMPL para a inicializa      o do modelo de simula      o.
	 *
	 */
	private void nomeParametro(){
		
		buffer.delete(0,119);
		buffer.append(" smpl(0,\" ");
		buffer.append(graph.getNomeModelo());
		buffer.append("\");");
		gravaArquivo(buffer);
		
	}
	
	
	/**
	 * Gera o comando <code>facility</code> para a defini      o dos recursos que compoem o 
	 * sistema.
	 */
	private void geraDefServer(){
		
		for (int i = 0; i < graph.getSize() ; i++)
		{			
			if (graph.getNode(i).getTipoNo() == 2){				
				buffer.delete(0,119);
				buffer.append(" ");
				buffer.append(graph.getNode(i).getNomeCentroServico());
				buffer.append(" = facility(\"");
				buffer.append(graph.getNode(i).getNomeCentroServico());
				buffer.append("\","); 
				buffer.append(graph.getNode(i).getNumServidores());
				buffer.append(");");
				gravaArquivo(buffer);				
			}		
			
		}
		
	}
	
	/**
	 * Seta o valor de stopByUsers e ainda o retorna apropriadamente
	 * Se o criador da simula      o coloca mais de um cliente chegando no sistema na m   o,
	 * ent   o a simula      o deve ser parada pelo numero m   ximo de clientes, mesmo que ele n   o queira
	 * porque sen   o a simula      o geraria resultados n   o muito verdadeiros
	 * seta tamb   m o n   mero de clientes apropriado do sistema
	 * @return
	 */
	private boolean valueOfStopByUsers()
	{
		stopByUsers = false;
		numClientes = 0;
		
		if (graph.getChegadaSize() > 1)
		{
			stopByUsers = true;
		}
		
		for (int i=0; i < graph.getChegadaSize(); i++)
		{
			
			Chegada temp = graph.getChegada(i);
			numClientes += temp.getNumeroClientes();
			if (temp.getNumeroClientes() > 1)
				stopByUsers = true;
		}
		return stopByUsers;
		
	}
	
	/**
	 * Escalona o primeivo evento a ocorrer no caso de sistema com uma    nica entrada, ou todos
	 * os eventos que devem ser escalonados antes do in   cio da simula      o para modelos de
	 * sistemas fechados.
	 */
	private void geraPrimeiroEvento(){
			
		int id, numCase = 1;
		//numClientes = 0;

		
		buffer.delete(0,119);
		
		for (int i = 0; i < graph.getSize(); i++){ // conta o n   mero de fontes do grafo e acerta os numCase
			if (graph.getNode(i).isPrimRec()){
				id = graph.getNode(i).getIdNo();
				c.setCase(numCase,id);
			    numCase++;
			}
		}
		
		if (graph.getTipoModelo().equals("aberto")){
				
			if (graph.getChegadaSize() >= 1)
			{
				for (int i = 0; i < graph.getChegadaSize(); i++)
				{
					Chegada temp = graph.getChegada(i);
					if (temp.getNumeroClientes() > 1)
					{
						buffer.append("   for (i = 0; i < ");
						buffer.append(temp.getNumeroClientes());
						buffer.append(" ; i++)\n");
						buffer.append("      schedule(");
	
					}
					else 
						buffer.append("   schedule(");
				
					buffer.append(c.getCase(graph.getNode(temp.getNodeIndex()).getIdNo()) + ",");
					buffer.append(temp.getTempoChegada());
					buffer.append(", Customer);");
					gravaArquivo(buffer);
					buffer.delete(0,119);
				}
			}
			
		}		
	}
	
	/**
	 * Gera o comando de repeti      o, limitando a simula      o por:
	 * - tempo,
	 * - n   mero de clientes que passam pelo sistema,
	 * - n   mero de voltas no sistema 
	 * - m   todo de an   lise Batch Means.
	 *
	 */
	private void geraLoop(){
		
		buffer.delete(0,119);

		if ( (graph.getNumeroCiclos() != null) && (!graph.getNumeroCiclos().equals("0")) ) {
			buffer.append(" while (Num_Voltas <= Num_Max_Voltas) ");
		} 
		else {
			if (  ( (graph.getTempoExecucao() != null) && (!graph.getTempoExecucao().equals("0") )
					&& (graph.getNumeroMaximoEntidades().equals("0") ) )  ) 
			{
				buffer.append(" while ( (time() < Te) "); // somente por tempo
				
				if ( (graph.getTamanhoBatch() != null) && (!graph.getTamanhoBatch().equals("0")) ){
					buffer.append("&& (r == 0) )");
				} else {
					buffer.append(")");
				}
			}
			else {
				if ( ((graph.getTempoExecucao().equals("0") || (graph.getTempoExecucao()==null)) )
						&& (graph.getNumeroMaximoEntidades() != null)) {
					buffer.append(" while ((Maximo_Entidades < Num_Max_Entidades) ");
					if (graph.getTamanhoBatch() != null) {
						buffer.append("&& (r == 0) )");
					} else {
						buffer.append(")");
					}
				} else {
					buffer.append(" while( (time() < Te) && (Maximo_Entidades < Num_Max_Entidades)");
					if ( (graph.getTamanhoBatch() != null) && (!graph.getTamanhoBatch().equals("0")) ) {
						buffer.append("&& (r== 0) )");
					} else {
						buffer.append(")");
					}
				}
			}
		}		
		gravaArquivo(buffer);
			
	}
	
	/**
	 * Implementa o comando <code>cause</code>.
	 * 
	 */
	private void geraCause(){
		
		buffer.delete(0,119);
		buffer.append("    cause(&Event,&Customer);");
		gravaArquivo(buffer);
		
	}
	
	/**
	 * Gera o comando <code>switch</code>.
	 *
	 */
	private void geraSwitch(){
		buffer.delete(0,119);
		buffer.append("    switch(Event)");
		gravaArquivo(buffer);	
		
	}
	
	/**
	 * Gera a primitiva case.
	 * @param numeroEvento Um valor <code>Integer</code>.
	 */
	private void geraCase(int numeroEvento){
		
		buffer.delete(0,119);
		buffer.append("        case ");
		buffer.append(numeroEvento);
		buffer.append(" : ");
		gravaArquivo(buffer);
	}
	
		
	/**
	 * Gera o comando schedule para o primeiro recurso.
	 * @param numeroEvento
	 * @param numeroRecurso Um valor <code>Integer</code> que indica o recurso corrente. (indice do nodes graph)
	 */

	private void geraProximaChegada(int numReq, int numeroRecurso, int numEvento){
		
		buffer.delete(0,119);
		// geraSchedule(numReq,numeroRecurso,false,null);
		geraSchedule(numReq,"0.0","          ");
		
		geraStream(numeroRecurso,"          ", true);
		
	
	    if (numClientes == 1)
	    {
			buffer.delete(0,119);
			String secParam = geraStringSchedule(numeroRecurso,true);
			geraSchedule(numEvento,secParam,"          ");
	    	buffer.delete(0,119);
	    }
			
	}	
	
	/**
	 * Fun      o que gera o reset para o warm-up
	 *
	 */
	private void geraWarmUp()
	{
		buffer.delete(0,119);
		buffer.append("   if ( (!flag_reset) && (time() > timeWarmUp) )\n   {");
		gravaArquivo(buffer);
		buffer.delete(0,119);
		buffer.append("      reset();\n");
		buffer.append("      flag_reset = 1;");
		gravaArquivo(buffer);
		
		buffer.delete(0,119);
		
		
		// resetando valores de Fila Vazia e Comprimento Max/Min
		boolean totClientes = false;
		boolean temEst;
		boolean ident;
		
		for (int i = 0; i < graph.getSize(); i++)
		{
			Node temp = graph.getNode(i);
			if (temp.getTipoNo() == 2)
			{
				ident = false;
				temEst = false;
				if (temp.getComprimentoMaxMin()) // especifica comprimento max/min
				{
					totClientes = true;
					temEst = true;
					buffer.append("      Max");
					ident = true;
					buffer.append(temp.getIdNo());
					buffer.append(" = 0; ");
					buffer.append("Min");
					buffer.append(temp.getIdNo());
					buffer.append(" = 1000; ");
				}
				
				if (temp.getEstatisticaFilaVazia())
				{
					totClientes = true;
					temEst = true;
					if (!ident)
						buffer.append("      Tot"); // conserto de identa      o
					else
						buffer.append("Tot");
					buffer.append(temp.getIdNo());
					buffer.append(" = 0; ");
					buffer.append("Vaz");
					buffer.append(temp.getIdNo());
					buffer.append(" = 0;");					
				}
				
				if (temEst) //temEst serve para identificar se houve necessidade 
				{								// de gera      o de c   digo para esse cs
					gravaArquivo(buffer);
					buffer.delete(0,119);
				}
			}
		}

		if (totClientes)
		{
			buffer.delete(0,119);
			buffer.append("      Total_Clientes = 0;");
			gravaArquivo(buffer);
			
		}
		
		if ( (graph.getNumeroMaximoEntidades()!=null) && (!graph.getNumeroMaximoEntidades().equals("0")))
		{
			buffer.delete(0,119);
			buffer.append("      Maximo_Entidades = 0;");
			gravaArquivo(buffer);
		}
		
		buffer.delete(0,119);
		buffer.append("   }");
		gravaArquivo(buffer);		
		
	}
	
	private void geraBatch(String distribuicao)
	{
		buffer.delete(0,119);
		buffer.append("             ");
		buffer.append("TBatch = ");
		buffer.append(distribuicao);
		buffer.append(";\n");
		buffer.append("             ");
		buffer.append(" r = obs(TBatch);");
		gravaArquivo(buffer);		
	}
	

	/**
	 * Gera a linha: schedule (destino, distri(TsIDCS), Customer); se true
	 * 			   e schedule (destino, 0.0, Customer); se false
	 * @param destino 
	 * @param numeroRecurso
	 * @param distribuicao true o false
	 * @author Andr    Felipe Rodrigues
	 */
	/*private void geraSchedule(int destino, int numeroRecurso, boolean distribuicao, String tipo)
	{
		buffer.delete(0,119);
		buffer.append("            schedule(");
		buffer.append(destino+", ");
		if (distribuicao)
		{
			buffer.append(graph.getNode(numeroRecurso).getDistribuicaoServico());
			buffer.append("(" + tipo);
			buffer.append(graph.getNode(numeroRecurso).getIdNo()+ ")");			
		}
		else
			buffer.append(" 0.0");
		
		buffer.append(", Customer);");
		gravaArquivo(buffer);		
	}*/
	
	
	/**
	 * gera schedule adicionado para a utiliza      o de outras probabilidades (hiperexponencial, etc)
	 * @param destino primeiro parametro do schedule
	 * @param tempo segundo parametro do schedule
	 * @param espaco o tanto de espa   o de identa      o que se quer dar
	 * @author Andr    Felipe Rodrigues
	 */
	
	private String geraStringSchedule(int indice, boolean chegada)
	{
		String ret = "0.0";
		Node temp = graph.getNode(indice);
		if (chegada)
		{
			if (temp.isPrimRec())
			{
				if (temp.getDistribuicaoChegada().equals("expntl"))
				{
					ret = temp.getDistribuicaoChegada() 
						+ "(" + GeradorSMPL.TAXA_CHEGADA 
						+ temp.getIdNo() 
						+ ")";					
				}
				else if ( (temp.getDistribuicaoChegada().equals("hyperx")) 
							|| (temp.getDistribuicaoChegada().equals("normal")) 
							|| (temp.getDistribuicaoChegada().equals("erlang"))
							|| (temp.getDistribuicaoChegada().equals("uniform"))
							)
				{
					ret = temp.getDistribuicaoChegada() 
					+ "(" + GeradorSMPL.TAXA_CHEGADA 
					+ temp.getIdNo() 
					+ ", "
					+ temp.getDesvioPadraoFonte()
					+ ")";	
				}				
			}
		}
		else
		{
			if (temp.getDistribuicaoServico().equals("expntl"))
			{
				ret = temp.getDistribuicaoServico() 
					+ "(" + GeradorSMPL.TAXA_SERVICO 
					+ temp.getIdNo() 
					+ ")";					
			}
			else if ( (temp.getDistribuicaoServico().equals("hyperx")) 
						|| (temp.getDistribuicaoServico().equals("normal")) 
						|| (temp.getDistribuicaoServico().equals("erlang"))
						|| (temp.getDistribuicaoServico().equals("uniform"))
						)
			{
				ret = temp.getDistribuicaoServico() 
				+ "(" + GeradorSMPL.TAXA_SERVICO 
				+ temp.getIdNo() 
				+ ", "
				+ temp.getDesvioPadrao()
				+ ")";	
			}	
			
		}		
		return ret;
		
	}
	
	private void geraSchedule(int destino, String tempo, String espaco)
	{
		buffer.delete(0,119);
		buffer.append(espaco);
		buffer.append("schedule(" + destino + ",");
		buffer.append(tempo);
		buffer.append(", Customer);");
		gravaArquivo(buffer);		
	}
	
	/**
	 * Implementa a primitiva para a liberacao do recurso (facilidade).
	 * @param numeroEvento 
	 * @param numeroRecurso Um valor <code>Integer</code> que indica o recurso corrente.
	 */
	private void geraRequest(int numeroRecurso){
	
				
		/*if (graph.getNode(numeroRecurso).getComprimentoMaxMin() == true){
			geraEstatisticaMaxMin(numeroRecurso);
		}
		if (graph.getNode(numeroRecurso).getEstatisticaFilaVazia() == true){
			geraEstatisticaFilaVazia(numeroRecurso);
		}*/
		buffer.delete(0,119);
		buffer.append("          if (request(");
		
		buffer.append(graph.getNode(numeroRecurso).getNomeCentroServico());		
		buffer.append(", Customer,0) == 0)");
		gravaArquivo(buffer);

	}
	
	
	/**
	 * Gera o comando <code>release</code> para a libera      o do recurso.
	 * @param numeroRecurso Um valor <code>Integer</code> que indica o recurso corrente. 
	 * @param numeroEvento Um valor <code>Integer</code>.
	 * @param totalRecurso Um valor <code>Integer</code> que indica o n   mero de recursos 
	 */
	private void geraRelease(int numeroRecurso){		
		buffer.delete(0,119);
		buffer.append("          release(");
		buffer.append(graph.getNode(numeroRecurso).getNomeCentroServico());
		buffer.append(", Customer);");
		gravaArquivo(buffer);
	}
	
	
	private void geraContagemMaximoEntidades(int indice, String espaco)
	{
		if ( (graph.getNumeroMaximoEntidades()!= null) && (!graph.getNumeroMaximoEntidades().equals("0")) )
		{	
			buffer.delete(0,119);
			if (espaco!=null)
				buffer.append(espaco);
			buffer.append("          Maximo_Entidades++;");
			gravaArquivo(buffer);
		}
	}
	
	private void geraCodeEstatisticas(int indice)
	{
		buffer.delete(0,119);
		
		Node temp = graph.getNode(indice);
		if ( (temp.getEstatisticaFilaVazia()) || (temp.getComprimentoMaxMin()) )
		{	
			buffer.append("          Total_Clientes = inq(");
			buffer.append(temp.getNomeCentroServico());
			buffer.append(");");
			gravaArquivo(buffer);
			buffer.delete(0,119);
		}

		buffer.delete(0,119);
		
		if (temp.getComprimentoMaxMin())
		{
			buffer.append("          if (Total_Clientes > Max");
			buffer.append(temp.getIdNo());
			buffer.append(")\n");
			buffer.append("             Max");
			buffer.append(temp.getIdNo());
			buffer.append(" = Total_Clientes;\n");
			buffer.append("         else\n");
			buffer.append("          if (Total_Clientes < Min");
			buffer.append(temp.getIdNo());
			buffer.append(")\n");
			buffer.append("             Min");
			buffer.append(temp.getIdNo());
			buffer.append(" = Total_Clientes;");
			gravaArquivo(buffer);
			buffer.delete(0,119);
			
		}
		
		buffer.delete(0,119);
		
		if (temp.getEstatisticaFilaVazia())
		{
			buffer.append("          if (Total_Clientes == 0)\n");
			buffer.append("             Vaz");
			buffer.append(temp.getIdNo());
			buffer.append("++;\n");
			buffer.append("          Tot");
			buffer.append(temp.getIdNo());
			buffer.append("++;");
			gravaArquivo(buffer);
			buffer.delete(0,119);
		}
			
	}
	
	private int correctID(int id)
	{
		int ret = -1;
		
		for (int i = 0; ((i < graph.getSize()) && (ret == -1)); i++)
			if (graph.getNode(i).getIdNo() == id)
				ret = i;
		
		return ret;
		
	}

	private void geraIfAleatorio(int numInicial, int numFinal)
	{
		buffer.delete(0,119);
		buffer.append("          if (( " + 
				numInicial + " <= Aleatorio) && ( Aleatorio <= " + numFinal + ") )");
		gravaArquivo(buffer);
		
	}
	
	private void geraComentario(String comment, String espaco)
	{
		buffer.delete(0,119);
		if ((comment != null) && (espaco != null))
			buffer.append("\n" + espaco + "/* " + comment + " */" );
		gravaArquivo(buffer);
		buffer.delete(0,119);
		
	}
	
	/**
	 * M   todo que analisa e verifica se j    houve empilhamento do centro de servi   o
	 * @param v Vetor que armazena o id de n   s que j    foram empilhados
	 * @param id O valor do id do centro de servi   o que est    consultando
	 * @return Retorna true se j    foi empilhado, e false caso contr   rio
	 * @author Andr    Felipe Rodrigues
	 */
	private boolean jahFoiEmpilhado(Vector v, int id)
	{
		boolean flag = false;
		int i = 0;
		boolean ret;

		if (v.size() == 0 ) // se vetor vazio
			ret = false;
		else
		{
			while ( (!flag) && ( i < v.size()) ) // percorre o vetor
			{
				String temp = (String)v.get(i);
				if ( Integer.parseInt(temp) == id ) // achou ocorr   ncia do id no vetor
					flag = true;
				else
					i++;
			}		
			ret = ( i < v.size() ? true : false );
		}		
		return ret;
	}
	
	private void geraChaves(String espaco, boolean abre)
	{
		buffer.delete(0,119);
		if (abre)
			buffer.append(espaco + "{");
		else
			buffer.append(espaco + "}");
		gravaArquivo(buffer);
	}
	/**
	 * Gera os eventos que constituem a simula      o.
	 * @author Andr    Felipe Rodrigues
	 *
	 */
	private void geraEventos(){
		
		int id = 0;		// vari   vel que armazena o id do n    que est    gerando eventos
		int indice;		// vari   vel que armazena o indice desse n    no vetor de n   s do grafo - corretivo
		int numCase;
		
		QueueL stack = new QueueL(); 		// pilha que armazena os centros de seri   o que ser   o processados
		Vector jahEmpilhados = new Vector(0); // armazena os id de centros de servi   os que j    foram processados
		
		for (int i = 0; i < graph.getSize(); i++){ // conta o n   mero de fontes do grafo
			if (graph.getNode(i).isPrimRec()){
				id = graph.getNode(i).getIdNo();
				indice = correctID(id);
				numCase = c.getCase(id);
				geraCase(numCase);
			    geraProximaChegada(c.getRequest(id),indice,numCase);
				if (!jahFoiEmpilhado(jahEmpilhados, id))  // verifica se o id do cs j    foi empilhado antes
				{	stack.push(String.valueOf(id)); // armazendo como Strings pq     object
					jahEmpilhados.add(String.valueOf(id));
				}
				geraBreak();
			}
		}		
		
		
		// ***** la   o de gera      o de todos eventos *******
		while (!stack.isEmpty()) // enquanto a pilha n   o est    vazia
		{
			
			id = Integer.parseInt((String)stack.pop()); // desempilha
			
			indice = correctID(id);	 
			
			// gerando coment   rio do centro de servi   o
			geraComentario(" centro de servi   o = " 
					+ graph.getNode(indice).getNomeCentroServico(),"        ");
			
			// ****** gerando request  ****
			geraCase(c.getRequest(id)); // gerando request do n    com identifica      o id			
			
			geraCodeEstatisticas(indice);
			geraStream(indice,"          ", false);
			
			geraRequest(indice);
			String secParam = geraStringSchedule(indice,false);
			if ( (graph.getTamanhoBatch()!=null) && (!graph.getTamanhoBatch().equals("0")) )
			{
				geraChaves("          ",true);
				geraBatch(secParam);
				geraSchedule(c.getRelease(id),"TBatch","             ");
				geraChaves("          ", false);
			}
			else // para gerar estat   sticas Batch
				geraSchedule(c.getRelease(id),secParam,"             ");
				
			geraBreak();
				
			// **** libera      o do recurso ****
			geraCase(c.getRelease(id)); 
			geraRelease(indice);

			
			// ****** verifica as conex   es do grafo e gera os schedule apropriados ********
			if (graph.getNode(indice).getSize() >= 2) // tem duas liga      es - tb   m tem que verificar fim do grafo
			{
				// nesse caso, sempre as conex   es ser   o por probabilidade
				graph.getNode(indice).setProb(true);
				if (graph.getNode(indice).isProb()) // se     por probabilidade
				{
					buffer.delete(0,119);
					buffer.append("          Aleatorio = randomX(1,10000);");
					gravaArquivo(buffer);
					int cont = 0;
					int limiteInf = 1;
					int limiteSup;
					int idTemp; //,indiceProx ;
					while (cont < graph.getNode(indice).getSize() )
					{
						if (graph.getNode(indice).getArc(cont).getNodeB().getTipoNo() == 2) //     centro de servi   o
						{
							double tempNumber;
							tempNumber = Double.parseDouble(graph.getNode(indice).getArc(cont).getProbabilidade());
							tempNumber = tempNumber*100 + limiteInf - 1;
							limiteSup = (int)tempNumber;
														
							geraIfAleatorio(limiteInf, limiteSup);
							limiteInf = limiteSup + 1;
							idTemp = graph.getNode(indice).getArc(cont).getNodeB().getIdNo();
							//indiceProx = correctID(idTemp);
							// String secParam2 = geraStringSchedule(indiceProx,false);
							geraSchedule(c.getRequest(idTemp),"0.0","           ");
							buffer.delete(0,119); 
							
							if (!jahFoiEmpilhado(jahEmpilhados, idTemp)) // depois de gerado if, empilha o recurso
							{
								stack.push(String.valueOf(idTemp)); // empilha recurso	
								jahEmpilhados.add(String.valueOf(idTemp));
							}							
						}
						else
						{
							if ( 	(graph.getNode(indice).getArc(cont).getNodeB().getTipoNo() == 3) 
									&& (graph.getNumeroMaximoEntidades()!= null) 
									&& (!graph.getNumeroMaximoEntidades().equals("0")) ){ // est    ligado ao n    final e gera MaximoEntidades
								double tempNumber;
								tempNumber = Double.parseDouble(graph.getNode(indice).getArc(cont).getProbabilidade());
								tempNumber = tempNumber*100 + limiteInf - 1;
								limiteSup = (int)tempNumber;
															
								geraIfAleatorio(limiteInf, limiteSup);
								limiteInf = limiteSup + 1;
								geraContagemMaximoEntidades(indice,"   ");
								buffer.delete(0,119);
							}							
						}
						cont++;	
					}
				}
				geraBreak();
			
			}
			else // s    tem uma liga      o poss   vel - verificar se n   o     fim do grafo
			{
				if (graph.getNode(indice).getArc(0).getNodeB().getTipoNo() == 2) // n   o est    ligado ao destino
				{
					Node temp = graph.getNode(indice).getArc(0).getNodeB();
					id = temp.getIdNo();
					indice = correctID(id);
				//	String secParam2 = geraStringSchedule(indice,false);
					geraSchedule(c.getRequest(id),"0.0" /*secParam2*/,"             ");
					
					if (!jahFoiEmpilhado(jahEmpilhados, id))
					{
						stack.push(String.valueOf(id)); // empilha recurso	
						jahEmpilhados.add(String.valueOf(id));
					}
				}
				else if (graph.getNode(indice).getArc(0).getNodeB().getTipoNo() == 3){ // est    ligado a sa   da
					geraContagemMaximoEntidades(indice,"");
				}
				geraBreak();
			}
		}	
	}
	
	private void fechaArquivo()
	{
		if (!alreadyClosed)
		{
			buffer.delete(0,119);
			buffer.append("   fclose(saida);");
			gravaArquivo(buffer);
		}
			
	}
	
	/**
	 * Gera o relat   rio final (padr   o do smpl) no programa de simula      o.
	 *
	 */
	private void geraRelatorioFinal(){
		
		buffer.delete(0,119);
		buffer.append("   report();");
		gravaArquivo(buffer);
	}
	
	
	/**
	 * Gera relat   rio com as estat   sticas das filas dos recursos especificados pelo usu   rio.
	 *
	 */
	private void geraRelatEstMaxMin(){
		
		buffer.delete(0,119);
		
		boolean title = false;
		
		for (int i = 0; i < graph.getSize(); i++)
		{
			Node temp = graph.getNode(i);
			if (temp.getTipoNo() == 2)
			{
				if (temp.getComprimentoMaxMin()) // tem que gerar para ComprimentoMaxMin
				{
					buffer.delete(0,119);
					if (!title)  // escreve t   tulo da gera      o de relat   rios para esta estat   stica
					{
						buffer.append("   fprintf(saida,\"\\n\\nRelat   rio - M   ximo e M   nimo das Filas \\n \"); ");
						gravaArquivo(buffer);
						buffer.delete(0,119);
						title = true;
					}
					
					buffer.append("   fprintf(saida,\"\\n Maximo clientes recurso "
							+ temp.getNomeCentroServico() 
							+ " : %i \", Max"
							+ temp.getIdNo()
							+ ");");
					gravaArquivo(buffer);
					
					buffer.delete(0,119);
					buffer.append("   fprintf(saida,\"\\n M   nimo clientes recurso "
							+ temp.getNomeCentroServico() 
							+ " : %i \", Min"
							+ temp.getIdNo()
							+ ");");
					gravaArquivo(buffer);
				}
								
			}
		}
		
	}
		

	/**
	 * Gera relat   rio com as estat   sticas das filas dos recursos especificados pelo usu   rio: 
	 * porcentagem de vezes na qual o clienet encontra a fila vazia.
	 */
	private void geraRelatFilaVazia(){
		
		buffer.delete(0,119);
		
		boolean title = false;
		
		for (int i = 0; i < graph.getSize(); i++)
		{
			Node temp = graph.getNode(i);
			if (temp.getTipoNo() == 2)
			{
				if (temp.getEstatisticaFilaVazia()) // tem que gerar para ComprimentoMaxMin
				{
					buffer.delete(0,119);
					if (!title)  // escreve t   tulo da gera      o de relat   rios para esta estat   stica
					{
						buffer.append("   fprintf(saida,\"\\n\\nRelat   rio - Total de Vezes - Fila Vazia \\n \"); ");
						gravaArquivo(buffer);
						buffer.delete(0,119);
						title = true;
					}
					
					buffer.append("   fprintf(saida,\"\\n Total de Clientes do recurso "
							+ temp.getNomeCentroServico() 
							+ " : %i \", Tot"
							+ temp.getIdNo()
							+ ");");
					gravaArquivo(buffer);
					
					buffer.delete(0,119);
					buffer.append("   fprintf(saida,\"\\n Total clientes que encontraram fila vazia no recurso "
							+ temp.getNomeCentroServico() 
							+ " : %i \", Vaz"
							+ temp.getIdNo()
							+ ");");
					gravaArquivo(buffer);
				}
								
			}
		}
		
	
	}
	
	/**
	 * Atrav   s dos comandos do arquivo GABARITO.DAT determina a pr   xima primitiva a 
	 * ser executada.
	 *
	 */
	public void leGabarito(String gabarito){		
		FileReader arq;
		try {
			arq = new FileReader(gabarito);
			BufferedReader sai = new BufferedReader(arq);		
			String linha = new String();
			while ((linha = sai.readLine()) != null){
				if (linha.indexOf("%") != 0){ // se n   o come   a com %, ent   o copia toda a linha	
					buffer.delete(0,119);
					buffer.append(linha);
					gravaArquivo(buffer);
					buffer.delete(0,119);
				}								
				else{					
					//primitivas do gerador devem ser executadas
					switch (linha.charAt(1))
					{
						case '0': defineTempoMax(); 
								  break;
						case '1': //defini      es
								  defineVarTempos(); 
								  defineVarWarmUp();
								  defineVarServer();
								  defineEstatMaxMin();
								  defineEstatFilaVazia();
								  defineMaximoClientes();
								  defineBMeans();
								  defineArqSaida();	
								  break;
						case '2': nomeParametro();
								break;
								//defini      o dos recursos do modelo
						case '3': geraDefServer();
								break;
								//escalona eventos antes do inicio da simula      o
						case '4': geraPrimeiroEvento(); 
								break;
								//limitante da simula      o*/
						case '5': geraLoop();
								break;
						case '6': geraCause();
								break;
						case '7': geraSwitch();
								break;
								//eventos que constituem a simula      o
						case '8': geraEventos();
								break;
								//relat   rio padr   o - colocar o if
						case '9': geraRelatorioFinal();
								break;
						/*		  //defini      es
						case 'A': geraContadores();
								  break;
								  */
								  //relat   rio de estat   sticas
						case 'C': geraRelatEstMaxMin();
								  break;
								  //relat   rio de estat   sticas
						case 'D': geraRelatFilaVazia();
								  break;
								
								  //implementa warmup
						case 'G': geraWarmUp();
								  break;
						
						case 'E': fechaArquivo();
								  break;
						          
					}
				
			}
			
			}
			sai.close();
			
			if (antigoMaxClientes!=null)
			{
				graph.setNumeroMaximoEntidades(antigoMaxClientes);
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
					
	}
	
	/* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& CLASSE INTERNA CONTROLER &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */
	/**
	 * Classe interna que auxilia a gera      o de c   digo
	 * respons   vel por organizar o n   mero do evento para cada n    (os cases)
	 * Com essa classe agora     poss   vel, sabendo-se o id do n    do grafo,
	 * saber qual o n   mero do evento request e release respectivos
	 * @author Andr   
	 */
	private class Controler
	{
		private int nRequest[];
		private int nRelease[];
		private int nCase[];
		private int id[];
		
		
		public Controler(Graph g)
		{
			this(g,2,3);
		}
		
		/**
		 * Construtor da classe Controler
		 * Cria os vetores de armazenamento j    com os valores corretos
		 * para os releases e requests de cada n   .
		 * @param g Grafo da classe gerador j    consistente e existente
		 */
		public Controler(Graph g, int num1, int num2)
		{
			int n = 0;
			
			for (int i = 0; i < graph.getSize(); i++)
			{
				if (g.getNode(i).getTipoNo() == 2) // contagem dos n   s centro de servi   o
					n++;
			}
			
			nRequest = new int[n];  // alocagem de mem   ria
			nRelease = new int[n];
			id = new int[n];
			nCase = new int[n];
			
			for (int i=0; i < n; i++){ // zera todos valores nCase
				nCase[i] = 0;
			}
			
			int contador1=num1, contador2=num2; // contadores auxilires come   ando com 2 e 3
			
			for (int i = 0, j = 0; i < g.getSize(); i++)  // atribui      o dos valores request e release
			{
				if (g.getNode(i).getTipoNo() == 2)
				{
					id[j] = g.getNode(i).getIdNo();
					nRequest[j] = contador1;
					nRelease[j] = contador2;
					contador1 += 2;
					contador2 += 2;					
					j++;								
				}
			}
		}
		
		/**
		 * retorna o valor do case request para o no <code> idN </code>
		 * @param idN id do n    que se quer saber o request especificado
		 * @return retorna o valor do request especificado para idN
		 * retorna 0 se n   o encontrado (note que n   o ser    gerado nenhum case 0:
		 * portanto, o retorno de zero representa algum erro
		 */
		public int getRequest(int idN)
		{
			int retorno = 0;
			int i = 0;
			boolean flag = true;
			while ( (flag) && (i < id.length)) // procura do n    pelo vetor
			{
				if (id[i] == idN)
				{
					retorno = nRequest[i];
					flag = false;
				}
				i++;
			}			
			return retorno; 			
		}
		
		/**
		 * retorna o valor do case release para o no <code> idN </code>
		 * @param idN id do n    que se quer saber o request especificado
		 * @return retorna o valor do release especificado para idN
		 * retorna 0 se n   o encontrado (note que n   o ser    gerado nenhum case 0:
		 * portanto, o retorno de zero representa algum erro
		 */
		public int getRelease(int idN)
		{
			int retorno = 0;
			int i = 0;
			boolean flag = true;
			while ( (flag) && (i < id.length))
			{
				if (id[i] == idN)
				{
					retorno = nRelease[i];
					flag = false;
				}
				i++;
			}			
			return retorno;			
		}
		
		/**
		 * retorna o valor do case armazenado para o n    <code> idN </code>
		 * @param idN id do n    que estamos procurando o valor do case
		 * @return retorna o n   mero do case apropriado para o n   
		 */
		public int getCase(int idN)
		{
			int retorno = 0;
			int i = 0;
			boolean flag = true;
			while ( (flag) && (i < id.length))
			{
				if (id[i] == idN)
				{
					retorno = nCase[i];
					flag = false;
				}
				i++;
			}			
			return retorno;	
		}
		
		/**
		 * coloca o valor de case inicial para o n    (usado para n   s fonte (source))
		 * @param valorCase valor do case que queremos colocar
		 * @param idNo id do n    que deve ser colocado o valor do case inicial
		 */
		public void setCase(int valorCase, int idNo)
		{
			int i = 0;
			boolean flag = true;
			while ( (flag) && (i < id.length))
			{
				if (id[i] == idNo) // achou o o lugar certo para a atribui      o
				{
					nCase[i] = valorCase;
					flag = false;
				}
				i++;
			}				
		}
		
		/**
		 * M   todo que imprime no console os valores dessa estrutura inteira
		 * M   todo utilizado para testes da classe
		 */
		public void print()
		{
			for (int i = 0; i < id.length; i++)
			{
				System.out.println("\nID = " + id[i]);
				System.out.println("Numero Request = " + nRequest[i]);
				System.out.println("Numero Release = " + nRelease[i]);
				System.out.println("Valor do case (valor = 0 -> n   o n    source): " + nCase[i]);
			}
		}		
		
		/* &&&&&&&&&&&&&&&&&&&&&&&&&& FIM DA CLASSE INTERNA &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */
		
		

	}

}
