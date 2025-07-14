package com.gerador;
import graph.Graph;

/**
 * Classe abstrata gerador que cont   m os m   todos necess   rios para
 * a gera      o de c   digo de uma simula      o 
 * @author Andr    Felipe Rodrigues 
 * @version 1.0
 */

public abstract class Gerador
{
	/**
	 * Armazena os n   s, arcos e todas as informa      es do modelo.
	 */
	protected Graph graph;  	// para dar visibilidade aos filhos
	
	/**
	 * o nome do arquivo no qual o c   digo ser    gerado
	 */
	protected String filename;  // para dar visibilidade aos filhos
	
	/**
	 * Construtor b   sico e m   nimo para a classe Gerador e filhos
	 * @param graph Grafo = modelo do qual o c   digo ser    gerado
	 */
	public Gerador(Graph graph)
	{
		this.graph = graph;
	}
	
	/**
	 * @return Returns the filename.
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename The filename to set.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}



	/**
	 * M   todo do qual     respons   vel para criar o arquivo do c   digo 
	 * que ser    gerado
	 * Como a gera      o do c   digo     diferente para cada tipo de linguagem,
	 * esse m   todo deve ser implementado independentemente
	 */
	abstract public void criaArquivo();


	/**
	 * M   todo respons   vel por ler o arquivo gabarito e gerar o c   digo da simula      o
	 * @param gabarito
	 */
	abstract public void leGabarito(String gabarito);
}
