package com.gerador;

import graph.Chegada;
import graph.Graph;
import graph.Node;
import graph.ProcessoLogicoParSMPL;
import graph.QueueL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import javax.swing.JOptionPane;




/**
 * @author Andr    Felipe Rodrigues
 * 
 * TODO COMENTAR O C   DIGO E RETIRAR FUN   OES DESNECESS   RIAS NO C   DIGO
 */
public class GeradorParSMPL extends Gerador {

	public static final String TAXA_SERVICO = "Ts";
	public static final String TAXA_CHEGADA = "Ta";
		
	// private boolean auxiliarLinhaNotDefinedYet;
	private boolean alreadyClosed;
	
	private int tidPai;
	private int tidPrimRec;

	

	
	Node processoLogicoVet[];
	
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
	
	private QueueL stack[];
	
//	private Vector jahEmpilhados = new Vector(0);
	
	
	
	/**
	 * Construtor da Classe.
	 * @param graph Recebe o modelo que ir    gerar o programa de simula      o.
	 */
	public GeradorParSMPL(Graph graph) {
  	
	  super(graph);
//	  auxiliarLinhaNotDefinedYet = true;	 
	  alreadyClosed = false;
	  
	  int numInitReq, numInitRel; // n   meros inicias para request e release
	  int contFonte = 0;
	  
	  
		boolean jahAchou = false;
		for (int i = 0; i < graph.getSize(); i++){ // conta o n   mero de fontes do grafo
			if (graph.getNode(i).isPrimRec()){
				contFonte++;
				if (!jahAchou)
				{
					tidPrimRec = graph.getNode(i).getTid();
					jahAchou = true;  // grava o Tid do primeiro recurso - recurso de entrada de tokens
				}
			}
		}
		
		numInitReq = contFonte+1;
		numInitRel = contFonte+2;	
		
		c = new Controler(graph, numInitReq, numInitRel); // gera n   meros de case apropriados para request e release de cada centro de servi   o
		
		stack = new QueueL[graph.getPLogicos().size()];
		for (int i = 0; i < graph.getPLogicos().size(); i++)
		{
			stack[i] = new QueueL();
		}
		

				
		for (int i=0; i < graph.getSize(); i++)
		{
			// se     centro de servi   o
			int id = graph.getNode(i).getIdNo();
			if (graph.getNode(i).getTipoNo() == 2){
				stack[graph.getNode(i).getTid()].push(String.valueOf(id));  // montando vetores
				// OBSERVA      O: AGORA N   O HAVER    MAIS UMA ORDEM L   GICA SEMI-GARANTIDA NA CRIA      O DOS CASES
			}			
		}

		
	}

	
	/**
	 * Cria o arquivo que ir    conter o programa de simula      o.
	 */
	public void criaArquivo(){
		
	//	filename = graph.getNomeModelo();
		if ((filename != null) && (!filename.equals("")))
		{
			filename = filename + ".c";
				File f = new File(filename);
				if (f.exists()) 
					f.delete();
		}
	}
	
	public void criaArquivo(String filename)
	{
		this.filename = filename;
		criaArquivo();
	}



	/**
	 * Quando tempo de warm-up     definido como autom   tico, ele     setado como 5%
	 * do tempo total de simula      o
	 * @return
	 */
	/*private double generateWarmUpTime()
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
	*/
	
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
	private void defineTempoMax(int tid){
		// defini      o do tempo m   ximo da simula      o
		
		
		// s    gera se o processo for um processo pai
		
		if (tid == tidPai)
		{
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
	/*		if (!graph.getNumeroMaximoEntidades().equals("0")){
				buffer.delete(0,119);
				buffer.append(" unsigned int Maximo_Entidades = 0, Num_Max_Entidades = ");
				buffer.append(graph.getNumeroMaximoEntidades());
				buffer.append(pontoVirgula);
				gravaArquivo(buffer);
			}*/
			
			/* VER O QUE FAZER EM RELA      O AO N   MERO DE CICLOS :-( TODO*/
				
			/*if (Double.parseDouble(graph.getNumeroCiclos()) != 0){
				buffer.delete(0,119);
				buffer.append(" unsigned int Num_Voltas = 0, Num_Max_Voltas = ");
				buffer.append(graph.getNumeroCiclos());
				buffer.append(pontoVirgula);
				gravaArquivo(buffer);;
				
			}*/
			// Se     definido pelo usu   rio,     setada uma vari   vel para o reset
		}
	
		
	}
	
	/**
	 * por enquanto, s    cria a vari   vel de parada
	 * @param tid
	 */
/*	private void defineVariaveisAuxiliares(int tid)
	{
		buffer.delete(0,119);
		buffer.append(" int fim = 0;");
		gravaArquivo(buffer);
		
	}*/
	private void defineLookAheadPL(int tid)
	{   // TODO VERIFICAR COM SARITA :-(
		// TODO - definir lookahed do processo l   gico (um double)
		buffer.delete(0,119);
		buffer.append(" double lookAhead = " + 
				((ProcessoLogicoParSMPL)graph.getPLogicos().get(tid)).getLookAhead() + ";");	
		gravaArquivo(buffer);
		
	}
	
	private void defineMaximoClientes()
	{
		buffer.delete(0,119);
		if ( (graph.getNumeroMaximoEntidades() != null) && (!graph.getNumeroMaximoEntidades().equals("0")) ) {
			buffer.append(" unsigned int Maximo_Entidades = 0, Num_Max_Entidades = ");
			buffer.append(graph.getNumeroMaximoEntidades());
			buffer.append(";");
			gravaArquivo(buffer);
		}
	}
	
	/**
	 * s    gera controle de warm-up se o processo l   gico for o pai ( que controla do tempo da simula      o)
	 * @param tid
	 */
	/*private void defineVarWarmUp(int tid)
	{
	
		if (tid == tidPai)
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
	}
	*/
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
	private void defineVarTempos(int tid){
		
		buffer.delete(0,119);
		
		String nomeA = "Ta"; 	// Taxa Arrival
		String nomeB = "Ts";	// Taxa service
		
		buffer.delete(0,119);

					
		boolean colocouReal1 = false;

		// n   o tem problema em criar a real (agora float) antes, pois cada processo l   gico deve ter pelo menos um n   
		// define tempo de chegada de centros de servi   os que s   o primeiro recurso
		for (int i = 0; i < graph.getSize(); i++)
		{   // Centro de servi   o de chegada		
			int tidP = graph.getNode(i).getTid(); // pega o tid do n    - ver se precisa as vari   veis
			if ((graph.getNode(i).getTipoNo() == 2) && (graph.getNode(i).isPrimRec()) && (tidP == tid)){
				if (!colocouReal1)
				{
					buffer.append(" float ");
					colocouReal1 = true;
				}
				buffer.append(nomeA);
				buffer.append(String.valueOf(graph.getNode(i).getIdNo()));
				buffer.append(" = ");
				buffer.append(graph.getNode(i).getMediaFonte());
				buffer.append(";");
			}	
		}	
		
		gravaArquivo(buffer);
		buffer.delete(0,119);
			
		buffer.append(" float ");
		for (int i = 0; i< graph.getSize(); i++)
		{	// Define a taxa de servi   o de todos centros de servi   os
			int tidP = graph.getNode(i).getTid();
//			boolean jahVirg = false;
			if ((graph.getNode(i).getTipoNo() == 2) && (tidP == tid)){
				//buffer.append(", ");
				buffer.append(nomeB);
				buffer.append(String.valueOf(graph.getNode(i).getIdNo()));
				buffer.append(" = ");
				buffer.append(graph.getNode(i).getMedia());
				buffer.append(", ");
			}
		}
		
        int ivirg = buffer.lastIndexOf(",");
        if (ivirg!=-1)
			buffer.deleteCharAt(buffer.lastIndexOf(",")); // deleta virgula final
        buffer.append(";");
        
		gravaArquivo(buffer);
		
	}


	private void defineAuxiliares(int tid)
	{
		buffer.delete(0,119);
		buffer.append(" int i = 0;\n");
	    buffer.append(" int info;\n"); // contador
	    buffer.append(" int fim=0;");
	  //  buffer.append(" char nome_recurso[50];");
		gravaArquivo(buffer);
	}
	
	private void defineArqSaida(int tid){
		
		buffer.delete(0,119);
		buffer.append(" FILE *p, *saida;");
		gravaArquivo(buffer);
		buffer.delete(0,119);
		
		StringBuffer buffer2 = new StringBuffer(300); // para suportar tamanhos maiores
		buffer2.delete(0,299);
		buffer2.append(" saida = fopen(\"");
		buffer2.append(System.getProperty("user.dir") + "/models/");
		buffer2.append(graph.getNomeModelo()+"_"+tid);
		buffer2.append(".out\",\"w\");\n"); // chegou a quase 100 caracteres j   
		gravaArquivo(buffer2);
		
		
	}
	
	/**
	 * Gera o comando SMPL para a inicializa      o do modelo de simula      o.
	 *
	 */
	private void nomeParametro(int tid){
		
		buffer.delete(0,119);
		buffer.append("  smpl(0,\"");
		buffer.append(graph.getNomeModelo() + "_" + tid);
		buffer.append("\");");
		gravaArquivo(buffer);
		
	}
	
	
	/**
	 * Gera o comando <code>facility</code> para a defini      o dos recursos que compoem o 
	 * sistema.
	 */
	private void geraDefServer(int tid){
		
		for (int i = 0; i < graph.getSize() ; i++)
		{			
			if ((graph.getNode(i).getTipoNo() == 2) && (tid == graph.getNode(i).getTid())){				
				buffer.delete(0,119);  // agora n   o associa mais com a vari   vel criada
			/*	buffer.append(" ");
				buffer.append(graph.getNode(i).getNomeCentroServico());
				buffer.append(" = facility(\"");*/
				buffer.append(" facility(\"");
				buffer.append(graph.getNode(i).getNomeCentroServico());
				buffer.append("\","); 
				buffer.append(graph.getNode(i).getNumServidores());
				buffer.append(");");
				gravaArquivo(buffer);				
			}		
			
		}
		
	}
	
	/**
	 * Escalona o primeivo evento a ocorrer no caso de sistema com uma    nica entrada, ou todos
	 * os eventos que devem ser escalonados antes do in   cio da simula      o para modelos de
	 * sistemas fechados.
	 */
	private void geraPrimeiroEvento(int tid){
			
		int id, numCase = 1;

		
		buffer.delete(0,119);
		
		for (int i = 0; i < graph.getSize(); i++){ // conta o n   mero de fontes do grafo e acerta os numCase
			if (graph.getNode(i).isPrimRec() && (tid == graph.getNode(i).getTid())){
				id = graph.getNode(i).getIdNo();
				c.setCase(numCase,id);
			    numCase++;
			}
		}
		
		if (tid == tidPrimRec)
		{
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
						/*buffer.append(", Customer);");*/
						
						buffer.append(", Customer, ");
						buffer.append(tid + ");");
						
						gravaArquivo(buffer);
						buffer.delete(0,119);
					}
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
		
		buffer.append("  while ( !fim )");

		gravaArquivo(buffer);
			
	}
	
	private void geraParada(int tid)
	{
		if (tid == tidPai)
		{
			int indice = indicePLogico(tid);
			int tidProx=0;
			if (graph.getSize() > 3)
				tidProx = ((ProcessoLogicoParSMPL)graph.getPLogicos().get(indice+1)).getTid();
			
			geraComentario("verificando o tempo de simula      o","      ");
			buffer.delete(0,119);
			buffer.append("    if (clock_sim() > Te)");
			gravaArquivo(buffer);
			
			geraSchedule(1000,"0.0","       ",tidProx);			
		}
		
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

	private void geraProximaChegada(int numReq, int numeroRecurso, int numEvento, int tid){
		
		buffer.delete(0,119);
		// geraSchedule(numReq,numeroRecurso,false,null);
		geraSchedule(numReq,"0.0","          ",tid);
		
		geraStream(numeroRecurso,"          ", true);
		
		//chama agora o proximo recurso e nao a fonte
	//	if (graph.getNode(numeroRecurso).isProb()){
			buffer.delete(0,119);
			String secParam = geraStringSchedule(numeroRecurso,true);
			geraSchedule(numEvento,secParam,"          ", tid);
	//		geraSchedule(numEvento,numeroRecurso,true,GeradorSMPL.TAXA_CHEGADA);

			buffer.delete(0,119);
		//}		
	}	
	
	
	
	/**
	 * @param distribuicao
	 * 
	 * TAMB   M N   O SEI SE IMPLEMENTA ESTAT   STICAS BATCH :-(
	 */
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
	
	private void geraSchedule(int destino, String tempo, String espaco, int tid)
	{
		buffer.delete(0,119);
		buffer.append(espaco);
		buffer.append("schedule(" + destino + ",");
		buffer.append(tempo);
		buffer.append(", Customer, PL" + tid + ");");
		gravaArquivo(buffer);		
	}
	
	/**
	 * Implementa a primitiva para a liberacao do recurso (facilidade).
	 * @param numeroEvento 
	 * @param numeroRecurso Um valor <code>Integer</code> que indica o recurso corrente.
	 */
	private void geraRequest(int numeroRecurso, int tid){
	
		buffer.delete(0,119);
		buffer.append("          if (request(");
		String temp = graph.getNode(numeroRecurso).getNomeCentroServico();
		temp.replace(' ','_');
		buffer.append("\"" + temp + "\"");		
		buffer.append(", Event, Customer, 0, PL" + tid + ") == 0)");
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
		String temp = graph.getNode(numeroRecurso).getNomeCentroServico();
		temp.replace(' ','_');
		buffer.append("\"" + temp + "\"");
		buffer.append(", Customer);");
		
	   gravaArquivo(buffer);
	}
	
	
	/**
	 * @param indice
	 * @param espaco
	 * 
	 * VER SE VAI TER ESSAS ESTAT   STICAS NO PARSMPL TODO :-(
	 */
	
	/**
	 * @param indice
	 * VER SE VAI TER ESTATISTICAS NO PARSMPL TODO :-(
	 */

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
	
	private int indicePLogico(int tid)
	{
		for (int i=0; i < graph.getPLogicos().size(); i++)
		{
			if (((ProcessoLogicoParSMPL)graph.getPLogicos().get(i)).getTid() == tid)
			{
				return i;
			}
		}
		
		return -1;
			
	}
	
	/**
	 * @param tid
	 * 
	 */
	private void geraCase1000(int tid)
	{
		geraComentario("T   rmino da simula      o","        ");
		buffer.delete(0,119);
		if (tid == tidPai) // gera o case 1000 do processo pai
		{
			geraCase(1001);
			
			buffer.delete(0,119);
			buffer.append("          fim = 1;");
			gravaArquivo(buffer);
			
			geraBreak();		
		}
		else
		{
			// descobrindo em que indice do vetor de pLogicos est    o tid
			// se indice = final - encontramos o ultimo pLogico - gera um case soh especial
			// sen   o, gera 2 cases, um para pl(indice+1) e outro para pl(indice-1)
			// OBS: n   o importa a ordem de parada dos processos l   gicos - ent   o tomou-se a abordagem sequencial
			int indice = indicePLogico(tid);
			if (indice == -1)
				JOptionPane.showMessageDialog(null,"Problema com aloca      o de tids - c   digo gerado est    incorreto\n +" +
						"Conserto Manual");
			else
			{
				int tidAnt, tidPos;
				tidAnt = ((ProcessoLogicoParSMPL)graph.getPLogicos().get(indice-1)).getTid();
				if (indice == graph.getPLogicos().size()-1) // caso dp PL final
				{
					geraCase(1000);
					geraSchedule(1001,"0.0","          ",tidAnt);
					
					buffer.delete(0,119);
					buffer.append("          fim = 1;");
					gravaArquivo(buffer);
					
					geraBreak();
				}
				else // outros PLS do meio (1000 passa sinal para frente, 1001 retorna sinal)
				{
					tidPos = ((ProcessoLogicoParSMPL)graph.getPLogicos().get(indice+1)).getTid();
					geraCase(1000);
					geraSchedule(1000,"0.0","          ",tidPos);
					geraBreak();
					
					geraCase(1001);
					geraSchedule(1001,"0.0","          ",tidAnt);
					
					buffer.delete(0,119);
					buffer.append("          fim = 1;");
					gravaArquivo(buffer);
					
					geraBreak();
					
				}
			}
					
		}

	}
	
	/**
	 * M   todo que analisa e verifica se j    houve empilhamento do centro de servi   o
	 * @param v Vetor que armazena o id de n   s que j    foram empilhados
	 * @param id O valor do id do centro de servi   o que est    consultando
	 * @return Retorna true se j    foi empilhado, e false caso contr   rio
	 * @author Andr    Felipe Rodrigues
	 */

	
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
	 */
	private void geraEventos(int tid){
		
		int id = 0;		// vari   vel que armazena o id do n    que est    gerando eventos
		int indice = 0;		// vari   vel que armazena o indice desse n    no vetor de n   s do grafo - corretivo
		int numCase;
		
	//	StackL stack = new StackL(); 		// pilha que armazena os centros de seri   o que ser   o processados
	//	Vector jahEmpilhados = new Vector(0); // armazena os id de centros de servi   os que j    foram processados
		

		if (tid == tidPrimRec)
		{
			for (int i = 0; i < graph.getSize(); i++){ // conta o n   mero de fontes do grafo
				if (graph.getNode(i).isPrimRec()){
					id = graph.getNode(i).getIdNo();
					indice = correctID(id);
					numCase = c.getCase(id);
					geraCase(numCase);
				    geraProximaChegada(c.getRequest(id),indice,numCase,graph.getNode(i).getTid());
	/*				if (!jahFoiEmpilhado(jahEmpilhados, id))  // verifica se o id do cs j    foi empilhado antes
					{	stack.push(String.valueOf(id)); // armazendo como Strings pq     object
						jahEmpilhados.add(String.valueOf(id));
					}*/
					geraBreak();
					break; // OBSERVAR AKI TBEM
				}
			}	
		}
			
		
		// ***** la   o de gera      o de todos eventos *******
		while (!stack[tid].isEmpty()) // enquanto a pilha n   o est    vazia
		{
			
			id = Integer.parseInt((String)stack[tid].pop()); // desempilha
			
			indice = correctID(id);	 
			
			// gerando coment   rio do centro de servi   o
			geraComentario(" centro de servi   o = " 
					+ graph.getNode(indice).getNomeCentroServico(),"        ");
			
			// ****** gerando request  ****
			geraCase(c.getRequest(id)); // gerando request do n    com identifica      o id			
			
		//	geraCodeEstatisticas(indice);
		//	geraStream(indice,"          ", false); n   o gera stream para cada n    no ParSMPL -     definido no pai
			
			// o tid no request n   o muda
			// o tid desses schedules tamb   m n   o (    atendimento de servi   o, gera um schedule para o release
			// o release est    sempre no mesmo processo l   gico em que o n    est   
			geraRequest(indice, graph.getNode(indice).getTid());
			String secParam = geraStringSchedule(indice,false);
			if ( (graph.getTamanhoBatch()!=null) && (!graph.getTamanhoBatch().equals("0")) )
			{
				geraChaves("          ",true);
				geraBatch(secParam);
				geraSchedule(c.getRelease(id),"TBatch","             ", graph.getNode(indice).getTid());
				geraChaves("          ", false);
			}
			else // para gerar estat   sticas Batch
				geraSchedule(c.getRelease(id),secParam,"             ", graph.getNode(indice).getTid());
				
			geraBreak();
				
			// **** libera      o do recurso ****
			geraCase(c.getRelease(id)); 
			geraRelease(indice);

			
			// ****** verifica as conex   es do grafo e gera os schedule apropriados ********
			if (graph.getNode(indice).getSize() >= 2) // tem duas liga      es - tb   m tem que verificar fim do grafo
			{
				graph.getNode(indice).setProb(true);
				if (graph.getNode(indice).isProb()) // se     por probabilidade
				{
					buffer.delete(0,119);
					buffer.append("          Aleatorio = randompar(1,10000);");
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
							geraSchedule(c.getRequest(idTemp),"0.0","           ",graph.getNode(indice).getArc(cont).getNodeB().getTid());
							buffer.delete(0,119); 
							
					
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
			//					geraContagemMaximoEntidades(indice,"   ");								
								buffer.delete(0,119);
							}
							
				//			geraCase1000(tid); // case de finaliza      o (saida do sistema)
							
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
			//		String secParam2 = geraStringSchedule(indice,false);
					geraSchedule(c.getRequest(id),"0.0","             ", temp.getTid());
					
				}
				else if (graph.getNode(indice).getArc(0).getNodeB().getTipoNo() == 3){ // est    ligado a sa   da
			//		geraContagemMaximoEntidades(indice,"");
					//geraCase1000(tid);
				}
				geraBreak();
			}
		}
		geraCase1000(tid);
	}
	
	private void fechaArquivo()
	{
		if (!alreadyClosed)
		{
			buffer.delete(0,119);
			buffer.append("  fclose(saida);");
			gravaArquivo(buffer);
		}
			
	}
	
	/**
	 * Gera o relat   rio final (padr   o do smpl) no programa de simula      o.
	 *
	 */
	private void geraRelatorioFinal(int tid){
		
		buffer.delete(0,119);
		
		buffer.append("  fprintf(saida,\"TempoSimulado: %f\\n\", clock_sim() );\n");
		gravaArquivo(buffer);
		
		buffer.delete(0,119);
		for (int i=0; i < graph.getSize(); i++)
		{
			Node temp = graph.getNode(i);
			if ((temp.getTipoNo()==2) && (temp.getTid() == tid))
			{
				buffer.append("  fprintf(saida,\"Utilizacao (\\\"" + temp.getNomeCentroServico() + "\\\") = %g\\n\", utilizacao_recurso(\"" + 
						temp.getNomeCentroServico() + "\"));");
				gravaArquivo(buffer);
				buffer.delete(0,119);
				buffer.append("  fprintf(saida,\"Comprimento medio fila (\\\"" + temp.getNomeCentroServico() + "\\\") = %g\\n\", comprimento_medio_fila(\"" + 
						temp.getNomeCentroServico() + "\"));");
				gravaArquivo(buffer);
				buffer.delete(0,119);
				buffer.append("  fprintf(saida,\"Periodo medio ocupado (\\\"" + temp.getNomeCentroServico() + "\\\") = %g\\n\", periodo_medio_ocupado(\"" + 
						temp.getNomeCentroServico() + "\"));");
				gravaArquivo(buffer);
				buffer.delete(0,119);
				
			}
		}
		gravaArquivo(buffer);
	}
	
	
	private void geraDefPLogicos()
	{
		ProcessoLogicoParSMPL temp;
		for (int i=0; i < graph.getPLogicos().size(); i++)
		{
			temp = (ProcessoLogicoParSMPL)graph.getPLogicos().get(i);
			buffer.delete(0,119);
			buffer.append("#define PL" + temp.getTid() + "   " + temp.getTid());
			gravaArquivo(buffer);
		}
		
	}
	
	private void paiRegistraPvm(int tid)
	{
		if (tid == tidPai)
		{
			buffer.delete(0,119);
			geraComentario("identifica processo pai", "  ");
			buffer.append("  tid_processo_pai = pvm_mytid();");
			gravaArquivo(buffer);
			buffer.delete(0,119);
		}
	}
	
	
	private void geraPvmSpawn()
	{
		buffer.delete(0,119);
		
		for (int i=0; i < graph.getPLogicos().size(); i++)
		{
			int tidAtual = ((ProcessoLogicoParSMPL)(graph.getPLogicos().get(i))).getTid();
		
			if (tidAtual != tidPai)
			{
				buffer.append("  info = pvm_spawn(SLAVENAME" + tidAtual + ", (char**)0, 0, \"\", 1, &tid_pl" + tidAtual + ");");
				gravaArquivo(buffer);
				buffer.delete(0,119);
				buffer.append("  if ( info < 1)\n");
				buffer.append("  {\n       printf(\"Trouble spawning slaves. Aborting. Error codes are:\\n\");");
				gravaArquivo(buffer);
				buffer.delete(0,119);
				buffer.append("       printf(\"TID %d\\n\", tid_pl" + tidAtual + ");");
				gravaArquivo(buffer);
				buffer.delete(0,119);
				
				for (int j=i; j >= 0; j--) // gera pvm_kill de processos j    startados
				{
					int tidAux = ((ProcessoLogicoParSMPL)(graph.getPLogicos().get(j))).getTid();
					if (tidAux != tidPai)  
					{
						buffer.delete(0,119);
						buffer.append("       pvm_kill( tid_pl" + tidAux + ");");
						gravaArquivo(buffer);
					}
				}
				
				buffer.delete(0,119);
				buffer.append("       pvm_exit();\n       exit(1);\n  }");
				gravaArquivo(buffer);
				buffer.delete(0,119);
				
			}
				
		}
			
	}
	
	private void paiPreparaTabelas(int tid)
	{
		
		buffer.delete(0,119);
		if (tid == tidPai)
		{
			geraComentario("inicializa tabela de tids","  ");
			buffer.delete(0,119);
			buffer.append("  for (i=0; i < " + graph.getPLogicos().size() + "; i++)\n");
			buffer.append("  {\n");
			buffer.append("      tabela_tids[i] = 0;\n");
			buffer.append("  }");
			gravaArquivo(buffer);
			
			geraPvmSpawn();

			buffer.delete(0,119);
			buffer.append("  tabela_tids[PL" + tidPai + "]");
			buffer.append(" = tid_processo_pai;");
			gravaArquivo(buffer);
			
			// associando tids de escravos     tabela
			for (int i=0; i < graph.getPLogicos().size(); i++)
			{
				int tidAtual = ((ProcessoLogicoParSMPL)(graph.getPLogicos().get(i))).getTid();
				if (tidAtual != tidPai)
				{
					buffer.delete(0,119);
					buffer.append("  tabela_tids[PL" + tidAtual + "] = tid_pl" + tidAtual + ";");
					gravaArquivo(buffer);
				}
			}
		}
	}
	
	
	private void defineSlaveNames()
	{
	
		for (int i=0; i < graph.getPLogicos().size(); i++)
		{
			ProcessoLogicoParSMPL temp = (ProcessoLogicoParSMPL)(graph.getPLogicos().get(i));
			int tidAtual = temp.getTid();
			
			if (tidAtual != tidPai)
			{
				StringBuffer stemp = new StringBuffer(filename);
				stemp.delete(stemp.length()-2,stemp.length());
				
			
				buffer.delete(0,119);
				// colocando caminho completo
				buffer.append("#define SLAVENAME" + tidAtual + "   \"" + System.getProperty("user.dir") + "/src/" + graph.getNomeModelo() + "_" + tidAtual + "\"");
				gravaArquivo(buffer);
			}
		}
	}
	
	private void paiEnviaTabelas()
	{
		buffer.delete(0,119);
		buffer.append("  pvm_pkint(tabela_tids," + graph.getPLogicos().size() + ", 1);");
		gravaArquivo(buffer);
		buffer.delete(0,119);
		 
		geraComentario(" Envia o valor da semente para os processos filhos "," ");
		
		buffer.delete(0,119);
		buffer.append("  pvm_pkint(&valor_semente,1,1);");
		gravaArquivo(buffer);
		
		// enviando os dados para os filhos
		for (int i=0; i < graph.getPLogicos().size(); i++)
		{
			int tidAtual = ((ProcessoLogicoParSMPL)(graph.getPLogicos().get(i))).getTid();
			if (tidAtual != tidPai)
			{
				buffer.delete(0,119);
				buffer.append("  pvm_send(tid_pl" + tidAtual + ", 0);");
				gravaArquivo(buffer);
			}
		}
		
		
	}
	
	
	private void defineVarPls()
	{
		buffer.delete(0,119); 
		buffer.append(" int tid_processo_pai, ");
		for (int i = 0; i < graph.getPLogicos().size(); i++)
		{
			int tidAtual = ((ProcessoLogicoParSMPL)(graph.getPLogicos().get(i))).getTid();
			if (tidAtual != tidPai)
			{
				buffer.append("tid_pl" + tidAtual);
				buffer.append(", ");				
			}
		}
		buffer.deleteCharAt(buffer.lastIndexOf(","));
		buffer.append(";");
		gravaArquivo(buffer);
	}
	
	private void iniciaClockCanais(int tid)
	{
		{
			buffer.delete(0,119);
			buffer.append("  proc_local = PL" + tid + ";");
			gravaArquivo(buffer);
			
			for (int i=0; i < graph.getPLogicos().size(); i++)
			{
				int tidAtual = ((ProcessoLogicoParSMPL)(graph.getPLogicos().get(i))).getTid();
				if (tidAtual != tid)
				{
					buffer.delete(0,119);
					buffer.append("  clock_canais[PL" + tidAtual + "] = 0.0;");
					gravaArquivo(buffer);
				}
			}
		}
	}
	
	private void geraStream(String arg)
	{
		buffer.delete(0,119);
		buffer.append("  stream(" + arg + ");");
		gravaArquivo(buffer);
	}
	
	private void defineMyTid(int tid)
	{
		if (tid != tidPai)
		{
			buffer.delete(0,119);
			buffer.append(" int my_tid;");
			gravaArquivo(buffer);
		}
	}
	
	private void defineValorSemente(int tid)
	{
	//	if (tid == tidPai)
	//	{
			buffer.delete(0,119);
			buffer.append(" int valor_semente = 1;");
			gravaArquivo(buffer);
//		}
	}
	
	private void escravoRecebeTabelaTid(int tid)
	{
		if (tid != tidPai)
		{
			buffer.delete(0,119);
			buffer.append("  pvm_recv(tid_processo_pai, " + tidPai + ");\n");
			buffer.append("  pvm_upkint(tabela_tids, " + 
					graph.getPLogicos().size() + ", 1);\n");
			buffer.append("  pvm_upkint(&valor_semente,1,1);");
			gravaArquivo(buffer);
		}
		
	}
	
	
	/**
	 * 
	 */
	public void leGabarito(String gabarito)
	{
		// criar um gabarito de controle
		// aqui vai chamando a leitura de gabarito - se for filho, chama um gabarito, se for pai chama outro
		
		int plAtual=0;
		int pl=0;
		int plPai=0;
		boolean achouPai=false;
		
		
	    
		while (pl < graph.getPLogicos().size())
		{
			if ( (!achouPai) && ((ProcessoLogicoParSMPL)graph.getPLogicos().get(pl)).isProcessoPai())
			{
				plPai = pl;
				achouPai = true;
			}
			
			plAtual = ((ProcessoLogicoParSMPL)graph.getPLogicos().get(pl)).getTid();
			if ((achouPai) && (plAtual == plPai))
			{  
			    criaArquivo(graph.getNomeModelo()+ "_PAI_" + plAtual); // cria o pl pai
			    leGabarito2("gabaritos/GABARITO_PAI.DAT", plAtual); // gabarito para o pai			    
			}
			else
			{			
				criaArquivo(graph.getNomeModelo()+ "_" + plAtual); // para cada processo l   gico vai criar um arquivo .c
				leGabarito2("gabaritos/GABARITO_PL.DAT", plAtual); //  gabarito para o filho
			}		
			
			pl++;		
			
		}
		
		tidPai = plPai;
		criaMakeFile();
		
		
	}
	
	
	/**
	 * Cria o Makefile do ParSMPL de acordo com os nomes dos arquivos criados
	 * 
	 */
	private void criaMakeFile()
	{
		File f = new File("src/Makefile.aimk");

		if (f.exists())
			f.delete();
		
			try {
				FileWriter fw = new FileWriter(f,true);
				
				FileReader fr = new FileReader("gabaritos/MAKEFILEGAB.DAT");
				BufferedReader gab = new BufferedReader(fr);
				String linha = new String();
				while (( linha = gab.readLine()) != null)
				{
					if (linha.indexOf("%")!=0) //copiar toda a linha
					{
						fw.write(linha);	
						fw.write("\n");
					}
					else
					{
						switch (linha.charAt(1))
						{
							case '1':
								fw.write(new String("CPROGS	=	"));
								for (int i=0; i < graph.getPLogicos().size(); i++)
								{
									ProcessoLogicoParSMPL pl = (ProcessoLogicoParSMPL)graph.getPLogicos().get(i);
									if (pl.isProcessoPai())
									{
										fw.write(graph.getNomeModelo() + "_PAI_" + pl.getTid()+ "\t");
									}
									else
									{
										fw.write(graph.getNomeModelo() + "_" + pl.getTid()+ "\t");
									}
								}
								fw.write("\n");
								break;
							case '2':
								for (int i=0; i < graph.getPLogicos().size(); i++)
								{
									ProcessoLogicoParSMPL pl = (ProcessoLogicoParSMPL)graph.getPLogicos().get(i);
									if (pl.isProcessoPai())
									{
										fw.write(graph.getNomeModelo() + "_PAI_" + pl.getTid() + 
												": $(SDIR)/" + graph.getNomeModelo() + "_PAI_" + pl.getTid() + ".c" + 
												" $(XDIR) $(CC) $(CFLAGS) -o $@ $(SDIR)/" + 
												graph.getNomeModelo() + "_PAI_" + pl.getTid() + ".c" + 
												" ../randpar.o ../parsmpl.o $(LFLAGS) $(LIBS)\n\n");
									}
									else
									{
										fw.write(graph.getNomeModelo() + "_" + pl.getTid() + 
												": $(SDIR)/" + graph.getNomeModelo() + "_" + pl.getTid() + ".c" + 
												"$(XDIR)	$(CC) $(CFLAGS) -o $@ $(SDIR)/" + 
												graph.getNomeModelo() + "_" + pl.getTid() + ".c" +
												 " ../randpar.o ../parsmpl.o $(LFLAGS) $(LIBS)\n\n");
									}
								}								
								break;
						    
						}
					}
				}
					fw.close();
				
				
			} catch (IOException e) {
				JOptionPane.showMessageDialog(
						null,
						"ASDA - ERRO",
						"N   o foi poss   vel criar arquivo Makefile.aimk",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
			
			
		}
		
	
	
	/**
	 * Atrav   s dos comandos do arquivo gabarito determina a pr   xima primitiva a 
	 * ser executada.
	 *
	 */
	private void leGabarito2(String gabarito, int tid){
		
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
						case '0': defineTempoMax(tid); 
								  break;
						case '1': //defini      es
								  defineVarTempos(tid); 

								  defineMaximoClientes();

								  defineLookAheadPL(tid);
								  defineValorSemente(tid);
								  defineVarPls();
								  defineMyTid(tid); // soh para filhos
								  defineAuxiliares(tid);
								  defineArqSaida(tid);	
								  break;
						case '2': nomeParametro(tid);
						          geraStream("valor_semente");
								break;
								//defini      o dos recursos do modelo
						case '3': geraDefServer(tid);
								break;
								//escalona eventos antes do inicio da simula      o
						case '4': geraPrimeiroEvento(tid); 
								break;
								//limitante da simula      o*/
						case '5': geraLoop();

								break;
						case '6': geraCause();
								break;
						case '7': geraSwitch();
								break;
								//eventos que constituem a simula      o
						case '8': geraEventos(tid);
								break;
								//relat   rio padr   o - colocar o if
						case '9': geraRelatorioFinal(tid);
								break;
						/*		  //defini      es
						case 'A': geraContadores();
								  break;
								  */
								  //relat   rio de estat   sticas
						case 'C': //geraRelatEstMaxMin();
								  break;
								  //relat   rio de estat   sticas
						case 'D': //geraRelatFilaVazia();
								  break;
								
								  //implementa warmup - not yet
						case 'G': //geraWarmUp();
								  break;
						
						case 'E': fechaArquivo();
								  break;	
								  
						case 'H': geraDefPLogicos();
							      break;
					    case 'I': defineSlaveNames(); 
					              break;
					    case 'J': paiRegistraPvm(tid);
					    		  break;
					    case 'K': paiPreparaTabelas(tid);
				    		  break;
					    case 'L':  paiEnviaTabelas();
				    		  break;
					    case 'M': // escravoIniciaTid();
				    		  break;
					    case 'N':  escravoRecebeTabelaTid(tid);
				    		  break;
					    case 'O': iniciaClockCanais(tid);
				    		  break;
				        case 'P': // parada da simula      o no pai
				        	  geraParada(tid);
				        	  break;
					    
					}
				
				}
			
			}
			sai.close();
			


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
