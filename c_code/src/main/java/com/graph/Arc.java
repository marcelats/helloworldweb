package com.graph;
import java.io.*;


/**  
  * Respons vel pelas liga  es entre os v rtices do grafo. Indica o n  
  * de origem e o n  de destino de cada liga  o.
  *  
  * @author Thais Souza Aiza
  * @version 1.0
  */ 
public class Arc implements Serializable {

    
    /**
     * Indica qual   o n  de origem do arco.
     */       
    public Node begin;

	/**
	 * Indica qual   o n  de destino do arco.
	 */       
    public Node end;
    
    /**
     * Indica qual a idNo (identifica  o) do n  de destino do arco.
     */
    
    public int  idNo;
    
    /**
     * Indica qual o tipo do n  de destino do arco.
     */
    public int  tipoNo;
    
    /**
     * Indica qual a probabilidade do n  de origem ir para o n  de destino.
     */
    public String probabilidade;

    /**
     * Construtor da classe.
     * @param begin n  de origem do arco
     * @param end n  de destino do arco
     * @param idNo identifica o n  de destino do arco
     * @param tipoNo identifica o tipo do n  de destino do arco
     * @param probabilidade indica a probabilidade do n  de origem ir para o n  de destino do arco
     */
    public Arc(Node begin, Node end, int idNo, int tipoNo, String probabilidade) {

        this.begin   = begin;
        this.end     = end;
        this.idNo    = idNo; 
        this.tipoNo  = tipoNo; 
        this.probabilidade = probabilidade;
    }

    
    /**
     * Retorna a identifica  o do n  de destino do arco.
     * @return Um valor <code>integer</code> especificando a idNo do n  de destino.
     */
    public int getIdNoArc() {
        return idNo;
    }

    /**
     * Retorna qual   a coordenada inicial (origem) x para desenhar o arco.
     * @return Um valor <code>integer</code> especificando a coordenada inicial x0. 
     */
    public int getX0() {
        return begin.getX();
    }

    /**
     * Retorna qual   a coordenada inicial (origem) y para desenhar o arco.
     * @return Um valor <code>integer</code> especificando a coordenada inicial y0.
     */
    public int getY0() {
        return begin.getY();
    }

    /**
     * Retorna qual   a coordenada final (destino) x para desenhar o arco.     *  
     */
    public int getX1() {
        return end.getX();
    }

    /**
     * Retorna qual   a coordenada final (destino) y para desenhar o arco.
     */
    public int getY1() {
        return end.getY();
    }


    /**
     * Retorna o n  de origem do arco. Todos os m todos da classe <code>Node</code> podem
	 * ser acessados.
     * @return Um objeto <code>Node</code>.
     */    
    public Node getNodeA() {
        return this.begin;
    }

	/**
	 * Retorna o n  de destino do arco. Todos os m todos da classe <code>Node</code> podem
	 * ser acessados.
	 */	
	public Node getNodeB() {
		return this.end;
	}


    
    /**
     * Configura a probabilidade do n  de origem ir para o n  de destino.
     * @param probabilidade - Um valor <code>String</code> no formato de um numero real cujo 
     * faixa de valores varia de 0 a 100 para especificar a probabilidade.
     */
    public void setProbabilidade(String probabilidade){
    	this.probabilidade = probabilidade;
    }
    
    /**
     * Retorna o valor da probabilidade do n  de origem ir para o n  de destino.
     * @return Um valor <code>String</code>.
     */
    //Retorna a probabilidade
    public String getProbabilidade(){
    	return this.probabilidade;
    }
    	
}


/*--- Formatted in Sun Java Convention Style on Qua, Abr 28, '04 ---*/


/*------ Formatted by Jindent 3.51 Gold 1.12 Trial --- http://www.jindent.com ------*/
