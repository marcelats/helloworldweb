package com.graph;
import java.util.Vector;
import java.io.*;
import javax.swing.ImageIcon;

/**
 * Armazena os n s, arcos e todas as informa  es do modelo. 
 * @author Thais Souza Aiza
 * @author Andr  Felipe Rodrigues - dele  o e cria  o de alguns atributos
 * 			e corre  o do m todo remove
 * @version 1.0
 *
 */


public class Graph implements Serializable{
	
	
	/**	 
	 * Armazena o nome do modelo.
	 */
	private String nomeModelo;
	/**
	 * Armazena o tempo de
	 */
	private String tempoExecucao;
	/**
	 * 
	 */
	private String numeroCiclos;
	/**
	 * 
	 */
	private String tamanhoBatch;
	/**
	 * N mero m ximo de entidades que poder o ocorrer durante a simula  o
	 */
	private String numeroMaximoEntidades;


	/**
	 * Diz se o modelo   aberto ou fechado
	 */
	private String tipoModelo;
	
	/**
	 * Armazena se o WarmUp   definido pelo usu rio ou autom tico
	 * autom tico = "automatico"
	 * definido = "definido"
	 */
	private String warmUp; 
	/**
	 * Armazena o tempo no qual todas as vari veis ser o resetadas.
	 * Se o WarmUp   definido armazena o valor do WarmUp. 
	 */
	private String tamWarmUp;
	
	/**
	 * Vetor que armazena <code>chegada</code>.
	 */
	public Vector chegadas = new Vector(0);


    /**
     * Vetor que armazena os n s do grafo.
     */
	public Vector nodes = new Vector(0);
	
	
	
	/** 
	 * Vetor que armazena as arestas do grafo	 
	 */
    
//	Vector arcs = new Vector(0);
	
	
	public Vector pLogicos = new Vector(0);
	
	/**
	 * @return Returns the Vector of Logical Process.
	 */
	public Vector getPLogicos() {
		return pLogicos;
	}
	/**
	 * @param logicos The pLogicos to set.
	 */
	public void setPLogicos(Vector logicos) {
		pLogicos = logicos;
	}
	/**
	 * Adiciona o n mero de clientes e o tempo de chegada ao vetor de chegadas.
	 * @param chegada Um objeto do tipo <code>Chegada</code>.
	 * @see Chegada#setNumeroClientes
	 * @see Chegada#setTempoChegad	 
	 */ 
	public void addChegada(Chegada chegada){
			chegadas.add(chegada);		
	}
	
	/**
	 * Configura o nome do modelo que identifica o modelo que foi especificado pelo usu rio.   o mesmo nome que 
	 * o usu rio usou para salvar o modelo.   configurado autom ticamente. 
	 * @param nomeModelo Um valor <code>String</code> que compreende os caracteres alfanum ricos.
	 */
	public void setNomeModelo(String nomeModelo){
		this.nomeModelo = nomeModelo;
	}
	
	/**
	 * Configura o tempo de execu  o.
	 * @param tempoExecucao Um valor <code>String</code> que compreende caracteres num ricos maiores ou 
	 * iguais a 0 (zero).
	 */
	public void setTempoExecucao(String tempoExecucao){
		this.tempoExecucao = tempoExecucao;
	}
	
	/**
	 * Configura o n mero m ximo de entidades.
	 * @param numeroMaximoEntidades Um valor <code>String</code> que compreende caracteres num ricos maiores ou 
	 * iguais a 0 (zero).
	 */
	public void setNumeroMaximoEntidades(String numeroMaximoEntidades){
		this.numeroMaximoEntidades = numeroMaximoEntidades;
	}
	
	/**
	 * Configura o tamanho do batch.
	 * @param tamanhoBatch Um valor <code>String</code> que compreende caracteres num ricos maiores ou 
	 * iguais a 0 (zero).
	 */
	public void setTamanhoBatch(String tamanhoBatch){
		this.tamanhoBatch = tamanhoBatch;
	}
	
	/**
	 * Configura se o valor do WarmUp   definido pelo usu rio ou autom tico.
	 * @param warmUp Um valor <code>String</code> que pode ser autom tico ou definido.
	 */
	public void setWarmUp(String warmUp){
		this.warmUp = warmUp;
	}

    /**
     * Configura o valor do WarmUp quando o campo   definido pelo usu rio. 
     * @param tamWarmUp Um valor <code>String</code> que compreende caracteres num ricos maiores ou 
	 * iguais a 0 (zero).
     */
	public void setTamWarmUp(String tamWarmUp){
		this.tamWarmUp = tamWarmUp;
	}
	
	/**
	 * Configura o tipo do modelo. Se   aberto, fechado ou misto.
	 * @param tipoModelo Um valor <code>String</code> que pode ser aberto, fechado ou misto.
	 */
	public void setTipoModelo(String tipoModelo){
		this.tipoModelo = tipoModelo;
	}
	
	/**
	 * Configura a <code>chegada</code>. Armazena os valores dos par metros <code>numeroClientes</code>
	 * e <code>tempoChegada</code>.
	 * @param numeroClientes Um valor <code>Integer</code> maior ou igual a 0 (zero).
	 * @param tempoChegada Um valor <code>float</code> maior ou igual a 0 (zero).
	 */
	public void setChegada(int numeroClientes, float tempoChegada, int nodeIndex){
		Chegada chegada = new Chegada(numeroClientes,tempoChegada, nodeIndex);
		chegadas.add(chegada);
	}		
	
	/**
	 * Remove a posi  o selecionada no vetor de <code>chegadas</code>.
	 * @param posicao Um valor <code>Integer</code> que indica a posi  o a ser removida do vetor.
	 */
	public void removeChegada(int posicao){
		this.chegadas.remove(posicao);
	}
	
	/**
	 * Retorna o nome do modelo.
	 * @return Um valor <code>String</code> que compreende os caracteres alfanum ricos.
	 */
	public String getNomeModelo(){
		return nomeModelo;
	}
    
    /**
     * Retorna o tempo de execu  o.
     * @return Um valor <code>String</code> que compreende caracteres num ricos maiores ou 
	 * iguais a 0 (zero). 
     */
	public String getTempoExecucao(){
		return tempoExecucao;
	}
	
	/**
	 * Retorna o n mero de ciclos.
	 * @return Um valor <code>String</code> que compreende caracteres num ricos maiores ou 
	 * iguais a 0 (zero).
	 */
	public String getNumeroCiclos(){
		return numeroCiclos;
	}
	
	/**
	 * Retorna o n mero m ximo de entidades.
	 * @return Um valor <code>String</code> que compreende caracteres num ricos maiores ou 
	 * iguais a 0 (zero).
	 */
	public String getNumeroMaximoEntidades(){
			return numeroMaximoEntidades;
	}
	
	/**
	 * Retorna o tamanho do batch.
	 * @return Um valor <code>String</code> que compreende caracteres num ricos maiores ou 
	 * iguais a 0 (zero).
	 */
	public String getTamanhoBatch(){
		return tamanhoBatch;
	}
	
    /**
     * Retorna se o campo WarmUp ser  definido pelo usu rio ou autom tico.
     * @return Um valor <code>String</code> que pode ser definido ou autom tico.
     */
	public String getWarmUp(){
		return warmUp;
	}

    /**
     * Retorna o tamanho do WarmUp para o caso dele ser definido pelo usu rio.
     * @return Um valor <code>String</code> que compreende caracteres num ricos maiores ou 
	 * iguais a 0 (zero).
     */
	public String getTamWarmUp(){
		return tamWarmUp;
	}
	
	/**
	 * Retorna o tipo do modelo. O m dulo avaliador n vel 1B   quem determina o tipo do modelo.
	 * @return Um valor <code>String</code> que pode ser aberto, fechado ou misto.
	 */
	public String getTipoModelo(){
		return tipoModelo;
	}
    
    /**
     * Retorna a <code>Chegada</code> armazenada no vetor de chegadas conforme a posi  o que 
     *   passada.
     * @param i Indica a posi  o no vetor de chegadas.
     * @return Um objeto <code>Chegada</code>.
     */
	public Chegada getChegada(int i){
		return (Chegada)chegadas.get(i);
	}
	
	/**
	 * Retorna o tamanho do vetor de chegadas.
	 * @return Um valor <code>Integer</code> maior ou igual a 0 (zero).
	 */
	public int getChegadaSize(){
		return chegadas.size();
	}
	
	/**
	 * Adiciona um n  ao vetor de n s. O novo n    sempre adicionado no final do vetor.
	 * @param node Um objeto <code>Node</code>.
	 * @see Node
	 */
	public void addNode(Node node){
		nodes.add(node);
	}
	
	/**
	 * Adiciona um n  ao vetor de n s na posi  o indicada.
	 * @param indice Posi  o em que o n  deve ser inserido no vetor de n s. Um valor maior 
	 * ou igual a 0 (zero).
	 * @param node Um objeto <code>Node</code>.
	 * @see Node
	 */
	public void addNodesIndex(int indice, Node node){
		this.nodes.add(indice,node);
	}
	
	
	/**
	 * Retorna o n  armazenado na posi  o desejada.
	 * @param i Posi  o em que deseja recuperar as informa  es do n .
	 * @return Um objeto <code>Node</code>.
	 */
	public Node getNode(int i){
		return (Node)nodes.get(i);
	}
	
		
	/**
	 * Retorna o tamanho do vetor de n s.
	 * @return Um valor <code>Integer</code> maior ou igual a 0 (zero).
	 */
	public int getSize(){		
		return nodes.size();
	}
		
	/**
	 * Remove todos os n s do vetor de n s.
	 */
	public void removeAllElements(){
		this.nodes.removeAllElements();
		this.pLogicos.removeAllElements();
		this.chegadas.removeAllElements();
	}
	
	/**
	 * Remove o elemento conforme o  ndice .
	 * @param indice Posi  o em que se deseja remover o n . Um valor <code>Integer</code> 
	 * maior ou igual a 0 (zero).
	 */
	public void remove(int indice){	
		// primeiro percorre todo o grafo procurando por liga  es a esse  ndice
		
		for (int i=0; i < getSize(); i++ ) // percorre n s
		{
			Node temp = getNode(i);
			for (int j = 0; j < temp.getSize(); j++) // percorre os arcos dos n s
			{
				if (temp.getArc(j).getNodeB() == getNode(indice)) // se   a mesma refer ncia
				{
					temp.getArcs().remove(j);						  // remove o arco
				}
			}
		}
		this.nodes.remove(indice);	
		
	}
	
	/**
	 * Retorna o tipo do n  armazenado na posi  o indicada.
	 * @param i Posi  o em que se deseja saber o tipo do n  armazenado.
	 * @return O tipo do n . Um valor <code>Integer</code> maior ou igual a 0 (zero).
	 */
	public int getTipoNo(int i){
		Node node = (Node)nodes.get(i);		
		return node.getTipoNo();
	}
	
	/**
	 * Retorna a identifica  o do n .
	 * @param i Posi  o em que se deseja saber a identifica  o do n  armazenado.
	 * @return A identifica  o do n . Um valor <code>Integer</code> maior ou igual a
	 * 0 (zero).
	 */
	public int getIdNo(int i){
		Node node = (Node)nodes.get(i);
		return node.getIdNo();	
	}
	
	public int getNodeIndex(int x, int y){

		for(int i=0;i<nodes.size();i++){
			Node node = (Node)nodes.get(i);
			int width = node.getImage().getIconWidth();
			int height = node.getImage().getIconHeight();
			if ((x > node.getX() - width/2)&&
					(x<node.getX() + width/2)&&(y > node.getY() - height/2)&&(y < node.getY()+height/2)) 
				return i;
		}

		return -1;
		
	}
	
	/**
	 * Retorna o modelo. Isto  , abre o modelo escolhido.
	 * @param filename Nome do modelo a ser aberto.
	 * @return Um objeto <code>Graph</code>.
	 * @deprecated Agora quem tem controle sobre a abertura do modelo   a classe Desenho
	 */
	public Graph open(String filename){
		
		Graph graph = null;

		try{		
			//FileInputStream fos = new FileInputStream(filename);
			//ObjectInputStream in = new ObjectInputStream(fos);

			//graph = (Graph)in.readObject();
			
			//in.close();

		} catch (Exception ex){
			ex.printStackTrace();
		}

		return graph;
	}

	/**
	 * Recebe um nome para o modelo e salva a especifica  o do modelo.
	 * @param filename Nome do modelo + .mod (extens o).
	 * @param nomeArquivo Nome do modelo - .mod (extens o).
	 * @deprecated Agora quem tem controle sobre o processo de salvar o modelo   a classe Desenho
	 */
	public void save(String filename, String nomeArquivo){
		
		try{		
			
			if (!filename.endsWith(".mod")) filename = filename + ".mod";
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			
			this.nomeModelo = nomeArquivo;

			out.writeObject(this);

			out.close();

		} catch (IOException ex){
			ex.printStackTrace();
		}
	}

/*	public Vector getArcs() {
		return arcs;


	public void setArcs(Vector arcs) {
		this.arcs = arcs;
	}*/

	public Vector getChegadas() {
		return chegadas;
	}

	public void setChegadas(Vector chegadas) {
		this.chegadas = chegadas;
	}

	public Vector getNodes() {
		return nodes;
	}

	public void setNodes(Vector nodes) {
		this.nodes = nodes;
	}

	public void setNumeroCiclos(String numeroCiclos) {
		this.numeroCiclos = numeroCiclos;
	}
}
