package com.graph;
import java.io.*;


/**
 * Adiciona e remove as chegadas de clientes no sistema.
 * @author Thais Souza Aiza
 * @author Andr  Felipe Rodrigues 
 * 			(adi  o do parametro nodeIndex e m todos relacionados)
 * @version 1.0 
 */
public class Chegada implements Serializable {

    /**
     * N mero de clientes que chegam no sistema.
     */
    private int   numeroClientes;
    /**
     * Tempo de chegada dos clientes no sistema.
     */
    private float tempoChegada;
    
    /**
     * Indice do n  o qual a chegada se refere
     */
    private int nodeIndex;

    /**
     * Construtor da classe.
     * @param numeroClientes Um valor <code>Integer</code> que representa o n mero de 
     * clientes que chegam no sistema.
     * @param tempoChegada Um valor <code>float</code> que representa o tempo de chegada 
     * dos clientes no sistema.
     */
    public Chegada(int numeroClientes, float tempoChegada, int nodeIndex) {
        this.numeroClientes = numeroClientes;
        this.tempoChegada   = tempoChegada;
        this.nodeIndex = nodeIndex;
    }

    /**
     * Retorna o numero de clientes que chegam no sistema.
     * @return Um valor <code>Integer</code> maior ou igual a 0 (zero).
     */    
    public int getNumeroClientes() {
        return numeroClientes;
    }

    /**
     * Retorna o tempo de chegada dos clientes no sistema.
     * @return Um valor <code>float</code> maior ou igual a 0 (zero).
     */
    public float getTempoChegada() {
        return tempoChegada;
    }

    /**
     * Configura o n mero de clientes que chegam no sistema.
     * @param numeroClientes Um valor <code>Integer</code> maior ou igual a 0 (zero).
     */
    public void setNumeroClientes(int numeroClientes) {
        this.numeroClientes = numeroClientes;
    }
    /**
     * retorna o  ndice do n  a qual essa chegada se refere
     * @return indice de n  no vetor nodes de graph
     */
	public int getNodeIndex() {
		return nodeIndex;
	}

	/**
	 * seta o indice corretamente
	 * @param nodeIndex
	 */
	public void setNodeIndex(int nodeIndex) {
		this.nodeIndex = nodeIndex;
	}

	/**
    * Configura o tempo de chegada dos clientes no sistema.
    * @param tempoChegada Um valor <code>float</code> maior ou igual a 0 (zero).
    */
	public void setTempoChegada(float tempoChegada) {
		this.tempoChegada = tempoChegada;
	}

}

