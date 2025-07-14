package com.graph;

import java.util.Vector;
import javax.swing.*;

import avaliador.AvaliadorNivelUmA;

import java.awt.Point;
import java.io.*;

/**  
  * Representa cada v rtice (<code>Node</code>) do modelo (<code>Graph</code>). Isto  , os 
  * templates fonte, centro de servi o e sorvedouro do modelo. 
  *  
  * @author Thais Souza Aiza
  * @author Andr  Felipe Rodrigues (algumas modifica  es)
  * @version 1.0
  */ 

public class Node implements Serializable{
	
	
    /**
     * Indica qual template foi inserido no modelo (<code>Graph</code>).
     */
	private ImageIcon img;

	/**
	 * Representa em qual coordenada x a imagem (template) foi inserida.
	 */
	private int x;

	/**
	 * Representa em qual coordenada y a imagem (template) foi inserida.
	 */
	private int y;
	
	/**
	 * Indica qual o tipo de </code>Node</code> que foi inserido no modelo.
	 */	
	private int tipoNo;
	
	/**
	 * Identifica o </code>Node</code> com uma chave  nica.
	 */
	private int idNo;
	
	/**
	 * Representa cada <code>Node</code> do modelo atrav s de um nome.  
	 */	
	private String nomeCentroServico;
	
	/**
	 * Define o n mero de servidores do centro de servi o.
	 */
	private String numServidores;
	
	/**
	 * Indica qual a distribui  o de servi o para cada centro de servi o.
	 */
	private String distribuicaoServico;	
	
	/**
	 * Indica qual o par metro analisado durante a simula  o para o centro de
	 * servi o. Cada centro de servi o pode ter um ou mais par metros.
	 */
	private boolean tempoResposta;

	/**
	 * Indica qual o par metro analisado durante a simula  o para o centro de
	 * servi o. Cada centro de servi o pode ter um ou mais par metros.
	 */
	private boolean throughput;
	
	/**
	 * Indica qual o par metro analisado durante a simula  o para o centro de
	 * servi o. Cada centro de servi o pode ter um ou mais par metros.
	 */
	private boolean tamanhoFila;

	/**
	 * Indica qual a distribui  o de chegada dos clientes para o template fonte.	 
	 */	
	private String distribuicaoChegada;
	
	/**
	 * Indica se ser o geradas estat sticas da porcentagem de vezes em que o cliente
	 * encontrou fila vazia.
	 */
	private boolean estatisticaFilaVazia;

    /**
     * Indica se ser o geradas estat sticas para o c lculo do n mero de clientes em 
     * cada fila.
     */
	private boolean comprimentoMaxMin;
	
	/**
	 * Define o n mero de filas do centro de servi o. Se o recurso tiver v rias 
	 * filas em paralelo.
	 */
	private int numFilas;
	
	/**
	 * comentas 
	 */
	private String mediaFonte;
	
	/**
	 * Indica qual a seq  ncia a ser utilizada na gera  o do n mero 
	 * aleat rio (fonte).
	 */
	private String sequenciaFonte;
	
	/**
	 * comentas
	 */
	private String media;

	/**
	 * Indica qual a seq  ncia a ser utilizada na gera  o do n mero 
	 * aleat rio (centro de servi o).
	 */
	private String sequencia;
	
	/**
	 * Indica o desvio padr o - utilizado para probabilidades diferente da exponencial
	 */
	private String desvioPadrao;
	
	private String desvioPadraoFonte;
	
	
	/**
	 * Ponto mais prov vel - utilizado na distribui  o triangular
	 */
	private String pontoMaisProvavel;
	
	private String pontoMaisProvavelFonte;
	/**
	 * Indica o n mero de voltas
	 */	
	private String numVoltas;
	
	/**
	 * Indica se o centro de servi o   o primeiro recurso do modelo.
	 */
	private boolean primRec;
	
//	public Ligacao proximoRecurso; //proximo recurso
			
	
	/**
	 * Indica se a escolha do pr ximo recurso   por probabilidade.	  
	 */
	private boolean prob;
	
	/**
	 * Indica se a escolha do pr ximo recurso   por n mero de voltas.
	 */
	private boolean ciclo;	
			
	/**
	 * Indica o n mero de arestas que o n  possuiu.
	 */
	private int chega;	
	
	/**
	 * Verifica se as todas as liga  es do modelo <code>Graph</code> est o corretas.
	 */
	

	/**
	 *  Vetor que armazena as arestas (<code>Arc</code>).
	 *  Inicializado com 0, mas ele vai crescendo a medida 
	 *  que novas arestas v o sendo inseridas.
	 */
	private Vector arcs = new Vector(0);
	
	
	/**
	 * Para saber de qual processo l gico esse centro de servi o pertece
	 * Obs: somente   aceito processos l gicos previamente criados no modelo
	 */
	private int tid;
	
	
	/*
	 * Vetor que armazena todos os destinos dos arcos que originam de um determinado n 
	 */
	 

	// Vector ligacoes = new Vector(10);
	 
	/**	 
	 * Construtor da classe.
	 * @param x armazena a posi  o x (em pixels) da figura.
	 * @param y armazena a posi  o y (em pixels) da figura.
	 * @param img guarda a imagem desenhada.
	 * @param tipoNo armazena qual   o tipo do v rtice (<code>Node</code>).              
	 *               - 1: Fonte
	 * 				 - 2: Centro de Servi o
	 *               - 3: Sorvedouro 	
	 * @param chega utilizado para verificar se os n s do tipo 2 e 3 s o destinos de arcos.						
	 * @param nomeCentroServico armazena o nome do centro de servi o. Se o n  for do tipo 1 o valor 
	 * armazenado ser  "fonte" e se for do tipo 3 ser  "sorvedouro".  
	 */

	public Node(int x, int y, ImageIcon img, int tipoNo, int idNo, int chega, String nomeCentroServico){
		this.x = x;
		this.y = y;
		this.img = img;		
		this.tipoNo = tipoNo;
		this.idNo = idNo;
		this.chega = chega;
		this.nomeCentroServico = nomeCentroServico;
	
	}


	/**	 
	 * Construtor da classe.
	 * @param x armazena a posi  o x (em pixels) da figura.
	 * @param y armazena a posi  o y (em pixels) da figura.
	 * @param img  guarda a imagem desenhada.
	 * @param tipoNo armazena qual   o tipo do v rtice (<code>Node</code>).              
	 *               - 1: Fonte
	 * 				 - 2: Centro de Servi o
	 *               - 3: Sorvedouro 							
	 * @param nomeCentroServico armazena o nome do centro de servi o. Se o n  for do tipo 1 o valor 
	 * armazenado ser  "fonte" e se for do tipo 3 ser  "sorvedouro".  
	 */
	
	public Node(int x, int y, ImageIcon img, int tipoNo, int idNo, String nomeCentroServico){
		this.x = x;
		this.y = y;
		this.img = img;		
		this.tipoNo = tipoNo;
		this.idNo = idNo;
		this.nomeCentroServico = nomeCentroServico;
	}
	
	

	/**
	 * Retorna a posi  o x do n  (<code>Node</code>).
	 * @return Um valor <code>Integer</code> que especifica a posi  o x em pixels 
	 * do n .
	 */
	public int getX(){
		return this.x;
	}
	
	/**
	 * Retorna a posi  o y do n  (<code>Node</code>).
	 * @return Um valor <code>Integer</code> que especifica a posi  o y em pixels 
	 * do n .
	 */
	public int getY(){
		return this.y;
	}
	
	/**
	 * Retorna o tamanho do vetor de arcos.
	 * @return Um valor <code>Integer</code>.
	 */
	public int getSize(){        
		return arcs.size();
	}
	
	/**
	 * Configura o nome do centro de servi o.
	 * @param nomeCentroServico Um valor <code>String</code> que cont m 
	 * somente caracteres alfanum ricos (0..9,a..z,A..Z).
	 */
	public void setNomeCentroServico(String nomeCentroServico){
		this.nomeCentroServico = nomeCentroServico;
	}
	
	/**
	 * Configura o n mero de arestas que o n    destino.
	 * Toda vez que uma nova aresta   adicionada ao n  de destino o valor de
	 * <code>chega</code>   incrementado. 
	 * @param aux Um valor <code>Integer</code> que   acrescentado a <code>chega</code>.
	 */
	public void setChega(int aux){		
		this.chega = this.chega + aux;		
	}
	
	/**
	 * Configura a distribui  o de chegada dos clientes.
	 * As distribui  es podem ser:
	 *  - Exponencial (expntl);
	 *  - Hiperexponencial;
	 *  - Erlang;
	 *  - Triangular;
	 *  - Uniforme;
	 *  - Do Usu rio. 
	 * @param distribuicaoChegada Um valor <code>String</code> que recebe um valor pr -
	 * definido que depende do tipo de distribui  o escolhida. A vers o 1.0 do ASDA s  
	 * permite distribui  es exponenciais (expntl).
	 */
	public void setDistribuicaoChegada(String distribuicaoChegada){
		this.distribuicaoChegada = distribuicaoChegada;
	}

	/**
	 * Configura a distribui  o de servi o.
	 * As distribui  es podem ser:
	 *  - Exponencial (expntl);
	 *  - Hiperexponencial;
	 *  - Erlang;
	 *  - Triangular;
	 *  - Uniforme;
	 *  - Do Usu rio. 
	 * @param distribuicaoServico Um valor <code>String</code> que recebe um valor pr -
	 * definido que depende do tipo de distribui  o escolhida. A vers o 1.0 do ASDA s  
	 * permite distribui  es exponenciais (expntl).
	 */
	
	public void setDistribuicaoServico(String distribuicaoServico){
		this.distribuicaoServico = distribuicaoServico;
	}


	/*
	 * vetor sai
	 */
	 
/*	public void addLigacao(Ligacao proximoRecurso){
		ligacoes.add(proximoRecurso);		
	}	 
	
	public void setLigacao(int destino){
		Ligacao proximoRecurso = new Ligacao(destino);
		
		ligacoes.add(proximoRecurso);
		
	}
	



	public void removeLigaco(int idNo){
		this.ligacoes.remove(idNo);
	}
*/

    /**
     * Configura quais par metros ser o analisados. 
     * @param varTempoResposta Um valor <code>boolean</code> que quando verdadeiro o 
     * par metro Tempo de Resposta ser  analisado.
     */	
	public void setTempoResposta(boolean varTempoResposta){
		this.tempoResposta = varTempoResposta;
	}
	
	/**
	 * Configura quais par metros ser o analisados.
	 * @param varThroughput Um valor <code>boolean</code> que quando verdadeiro o 
     * par metro Throughput ser  analisado.
	 */	
	public void setThroughput(boolean varThroughput){
		this.throughput = varThroughput;
	}
	
	/**
	 * Configura quais par metros ser o analisados.
	 * @param varTamanhoFila Um valor <code>boolean</code> que quando verdadeiro o 
     * par metro Tamanho da Fila ser  analisado.
	 */
	public void setTamanhoFila(boolean varTamanhoFila){
		this.tamanhoFila = varTamanhoFila;
	}
	
	/**
	 * Configura se ser o geradas estat sticas para o c lculo da porcentagem de vezes em 
	 * que o cliente encontra a fila vazia
	 * @param estatisticaFilaVazia Um valor <code>boolean</code>.
	 */
	public void setEstatisticaFilaVazia(boolean estatisticaFilaVazia){
		this.estatisticaFilaVazia = estatisticaFilaVazia;
	}

	/**
	 * Configura  se ser o geradas estat sticas para o c lculo do n mero de clientes em 
     * cada fila.
	 * @param comprimentoMaxMin Um valor <code>boolean</code>.
	 */
	public void setComprimentoMaxMin(boolean comprimentoMaxMin){
		this.comprimentoMaxMin = comprimentoMaxMin;
	}
	
	/**
	 * Configura o n mero de filas do centro de servi o se o recurso tiver v rias filas em 
	 * paralelo
	 * @param numFilas Um valor <code>Integer</code> maior ou igual a 0 (zero).
	 */
	public void setNumFilas(int numFilas){
		this.numFilas = numFilas;
	}
	
	/**
	 * Configura o n mero de servidores do centro de servi o.
	 * @param numServidores Um valor <code>Integer</code> maior ou igual a 0 (zero).
	 */
	public void setNumServidores(String numServidores){
		this.numServidores = numServidores;
	}

	/**
	 * 
	 * @param media
	 */
	public void setMedia(String media){
		this.media = media;
	}
	
	/**
	 * Configura qual a seq  ncia a ser utilizada na gera  o do n mero 
	 * aleat rio. 
	 * @param sequencia Um valor <code>Integer</code> compreendido entre 0..15.
	 */
	public void setSequencia(String sequencia){
		this.sequencia = sequencia;
	}
	
	/**
	 * 
	 * @param numVoltas
	 */
	public void setNumVoltas(String numVoltas){
		this.numVoltas = numVoltas;
	}

	/*public Ligacao getLigacao(int i){
		return (Ligacao)ligacoes.get(i);
	}
	
	public int getIdNoProximoRecurso(int i){
		Ligacao ligacao = (Ligacao)ligacoes.get(i);		
		return ligacao.idNoDestino;
	}
	
	public int getLigacoesSize(){
		return ligacoes.size();
	}
	*/
    /**
     * Representa a localiza  o no espa o de coordenadas (x,y).  
     * @param p Um valor <code>Integer</code>.
     */
	public void setPosition(Point p){
		this.x = (int) p.getX();
		this.y = (int) p.getY();
	}
	
	/**
	 * Retorna a identifica  o do n  (<code>Node</code>). Cada n  possui um valor  nico que 
	 * serve para identific -lo no modelo (<code>Graph</code>). O valor inicial do primeiro 
	 * n  do modelo come a com 0 (zero).
	 * @return Um valor <code>Integer</code> maior ou igual a 0.
	 */
	public int getIdNo(){
		return this.idNo;
	}
	
	/**
	 * Retorna o tipo do n . Cada n  pode ser de 3 tipos:
	 *  1 - fonte;
	 *  2 - centro de servi o;
	 *  3 - sorvedouro. 
	 * @return Um valor <code>Integer</code> compreendido entre 1..3.
	 */
	public int getTipoNo(){
		return this.tipoNo;
	}

    /**
     * Retorna o nome do centro de servi o.  
     * @return Um valor <code>String</code> que cont m somente caracteres alfanum ricos
     * (0..9,a..z,A..Z). Se o n  for do tipo 1 o valor de retorno ser  fonte e se o n  
     * for do tipo 3 o valor retornado ser  sorvedouro.
     */
	public String getNomeCentroServico(){
		return this.nomeCentroServico;
	}
	
	/**
	 * Retorna a distribui  o de chegada escolhida pelo usu rio.
	 * @return Um valor <code>String</code>. A vers o 1.0 do ASDA s  permite distribui  es 
	 * exponenciais. Valor de retorno "expntl". 
	 */
	public String getDistribuicaoChegada(){
		return this.distribuicaoChegada;
	}

	/**
	 * Retorna a distribui  o de servi o escolhida pelo usu rio.
	 * @return Um valor <code>String</code>. A vers o 1.0 do ASDA s  permite distribui  es 
	 * exponenciais. Valor de retorno "expntl". 
	 */	
	public String getDistribuicaoServico(){
		return this.distribuicaoServico;
	}
	
	/**
	 * Retorna <code>true</code> se o par metro Tempo de Resposta ser  analisado, caso 
	 * contr rio retorna <code>false</code>.
	 * @return Um valor <code>boolean</code>.
	 */
	public boolean getTempoResposta(){
		return this.tempoResposta;
	}

	/**
	 * Retorna <code>true</code> se o par metro Throughput ser  analisado, caso 
	 * contr rio retorna <code>false</code>.
	 * @return Um valor <code>boolean</code>.
	 */	
	public boolean getThroughput(){
		return this.throughput;
	}

	/**
	 * Retorna <code>true</code> se o par metro Tamanho da Fila ser  analisado, caso 
	 * contr rio retorna <code>false</code>.
	 * @return Um valor <code>boolean</code>.
	 */	
	public boolean getTamanhoFila(){
		return this.tamanhoFila;
	}
	
	/**
	 * Retorna <code>true</code> se a porcentagem de vezes em que o cliente encontra a fila 
	 * vazia ser  calculado, caso contr rio retorna <code>false</code>.
	 * @return Um valor <code>boolean</code>.
	 */
	public boolean getEstatisticaFilaVazia(){
		return this.estatisticaFilaVazia;
	}
	
	/**
	 * Retorna <code>true</code> se ser o geradas as estat sticas para o c lculo do n mero 
	 * de clientes em cada fila.
	 * @return Um valor <code>boolean</code>.
	 */
	public boolean getComprimentoMaxMin(){
		return this.comprimentoMaxMin;
	}
	
	/**
	 * Retorna o n mero de filas do centro de servi o. Se valor maior que 0 (zero) o servidor possuiu 
	 * filas em paralelo.
	 * @return Um valor <code>Integer</code> maior ou igual a 0.
	 */
	public int getNumFilas(){
		return numFilas;
	}
	
	/**
	 * Retorna o n mero de servidores do centro de servi o.
	 * @return Um valor <code>Integer</code> maior ou igual a 0.
	 */
	public String getNumServidores(){
		return this.numServidores;
	}
	
	/**
	 * @return a media...melhorar
	 */
	public String getMedia(){
		return media;
	}
	
    /**
     * @return A quantidade de voltas...melhorar...
     */
	public String getNumVoltas(){
		return numVoltas;
	}
	
	/**
	 * Retorna qual a seq  ncia a ser utilizada na gera  o do n mero 
	 * aleat rio.
	 * @return sequencia - Um valor <code>String</code> compreendido entre 0..15. 
	 */
	public String getSequencia(){
		return sequencia;
	}
	
	/**
	 * Adiciona um arco ao vetor de arcos. Cada n  possui um vetor de arcos que indica com 
	 * quais n s o n  de origem se conecta.
	 * O arco somente   desenhando ap s a consist ncia da liga  o for confirmada. 
	 * @param node 
	 * @param ligacao
	 * @param idNo
	 * @param tipoNo
	 * @param probabilidade
	 */
	public void addArc(Node node, int idNo, int tipoNo, String probabilidade){
	    
		boolean desenha;
		
		AvaliadorNivelUmA avalia = new AvaliadorNivelUmA();
		
		Arc arc = new Arc(this,node,idNo,tipoNo,probabilidade);
		
		//Verifica se a liga  o est  correta.	
		desenha = avalia.verifica(arc.begin.tipoNo,arc.end.tipoNo);
		if (desenha) {			
			arcs.add(arc);
			
			if (arc.begin.getTipoNo() == 1){				
				node.setPrimRec(true);						
			}
/*			else{
				node.setPrimRec(false);
			}*/
			// node.setLigacao(arc.end.idNo);
			node.setChega(1);	
		}
		else{
			JOptionPane.showMessageDialog(
				null,
				"Liga  o n o permitida!!!",
				"ASDA - Erro",
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	

	/**
	 * Retorna a identifica  o do arco. Toda vez que um n  se conecta a outro n  atrav s
	 * de um arco uma posi  o no vetor de arcos para aquele n    preenchida. Cada arco 
	 * de um determinado n  possui uma identifica  o  nica que   a sua posi  o no vetor.
	 * @param i posi ao do arco no vetor de arcos.
	 * @return o arco que est  armazenado na posi  o que foi passada como par metro.
	 */
	public Arc getArc(int i){		
		return (Arc)arcs.get(i);
	}
	
	/**
	 * Armazena a imagem do template que foi inserido no modelo (<code>Graph</code>).
	 * @param img Um objeto do tipo T*.gif localizado na pasta asda\imgIcones.
	 */
	public void setImage(ImageIcon img){
		this.img = img;
	}

	/**
	 * Retorna a imagem correspondente ao n  inserido no modelo (<code>Graph</code>).
	 * @return img - Um objeto do tipo T*.gif localizado na pasta asda\imgIcones.
	 */
	public ImageIcon getImage(){
		return this.img;
	}
	
	/**
	 * Remove todos os elementos do vetor de arcos.	 
	 */
	public void removeAllElements(){
		this.arcs.removeAllElements();
	}


	/**
	 * Retorna se a escolha para o pr ximo recurso   por ciclo.
	 * @return ciclo - Um valor <code>boolean</code>.
	 */
	public boolean isCiclo() {
		return ciclo;
	}

	/**
	 * Retorna se a escolha para o pr ximo recurso   por probabilidade.
	 * @return prob - Um valor <code>boolean</code>.
	 */
	public boolean isProb() {
		return prob;
	}
	


	/**
	 * Configura se a escolha para o pr ximo recurso   por ciclo.
	 * @param ciclo - Um valor <code>boolean</code>.
	 */
	public void setCiclo(boolean ciclo) {
		this.ciclo = ciclo;
	}

	/**
	 * Configura se a escolha para o pr ximo recurso   por ciclo.
	 * @param prob - Um valor <code>boolean</code>.
	 */
	public void setProb(boolean prob) {
		this.prob = prob;
	}


	/**
	 * Retorna se o centro de servi o   o primeiro recurso. Somente h  um centro de servi o 
	 * com o valor de primRec = true.
	 * @return primRec - Um valor <code>boolean</code>.
	 */
	public boolean isPrimRec() {
		return primRec;
	}

	/**
	 * Configura se o centro de servi o   o primeiro recurso. Somente o centro de servi o que
	 * est  ligado a fonte   configurado como primeiro recurso.
	 * @param primRec - Um valor <code>boolean</code>.
	 */
	public void setPrimRec(boolean primRec) {
		this.primRec = primRec;
	}

	/**
	 * @return
	 */
	public String getMediaFonte() {
		return mediaFonte;
	}

   /**
	* Retorna qual a seq  ncia a ser utilizada na gera  o do n mero 
	* aleat rio.
	* @return sequenciaFonte - Um valor <code>String</code> compreendido entre 0..15.
	*/ 
	public String getSequenciaFonte() {
		return sequenciaFonte;
	}

	/**
	 * @param mediaFonte
	 */
	public void setMediaFonte(String mediaFonte) {
		this.mediaFonte = mediaFonte;
	}

	/**
	 * Configura qual a seq  ncia a ser utilizada na gera  o do n mero 
	 * aleat rio. 
	 * @param sequenciaFonte Um valor <code>Integer</code> compreendido entre 0..15.
	 */
	public void setSequenciaFonte(String sequenciaFonte) {
		this.sequenciaFonte = sequenciaFonte;
	}


	public void setX(int x) {
		this.x = x;
	}


	public void setY(int y) {
		this.y = y;
	}


	public void setTipoNo(int tipoNo) {
		this.tipoNo = tipoNo;
	}


	public void setIdNo(int idNo) {
		this.idNo = idNo;
	}


	public int getChega() {
		return chega;
	}



	public ImageIcon getImg() {
		return img;
	}


	public void setImg(ImageIcon img) {
		this.img = img;
	}


	public Vector getArcs() {
		return arcs;
	}


	public String getDesvioPadrao() {
		return desvioPadrao;
	}


	public void setDesvioPadrao(String desvioPadrao) {
		this.desvioPadrao = desvioPadrao;
	}


	public String getPontoMaisProvavel() {
		return pontoMaisProvavel;
	}


	public void setPontoMaisProvavel(String pontoMaisProvavel) {
		this.pontoMaisProvavel = pontoMaisProvavel;
	}


	public void setArcs(Vector arcs) {
		this.arcs = arcs;
	}
	
	public String toString()
	{
		return getNomeCentroServico();
	}


	public String getDesvioPadraoFonte() {
		return desvioPadraoFonte;
	}


	public void setDesvioPadraoFonte(String desvioPadraoFonte) {
		this.desvioPadraoFonte = desvioPadraoFonte;
	}


	public String getPontoMaisProvavelFonte() {
		return pontoMaisProvavelFonte;
	}


	public void setPontoMaisProvavelFonte(String pontoMaisProvavelFonte) {
		this.pontoMaisProvavelFonte = pontoMaisProvavelFonte;
	}

	/**
	 * @return Returns the tid.
	 */
	public int getTid() {
		return tid;
	}
	/**
	 * @param tid The tid to set.
	 */
	public void setTid(int tid) {
		this.tid = tid;
	}
}
