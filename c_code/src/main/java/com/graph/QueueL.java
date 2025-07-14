package com.graph;
import java.util.LinkedList;

/**
 * Criao de um TAD pilha via uma lista ligada oferecida por java.util.LinkedList;
 * @author Andr Felipe Rodrigues
 *
 */
public class QueueL {
	
	private LinkedList list = new LinkedList();
	
	public void push(Object v) 
	{ list.addFirst(v); }
	
	public Object pop() // para virar fila em vez de pilha
	{ return list.removeLast(); }
	
	public Object top() 
	{ return list.getFirst(); }
	
	public void clear() 
	{ list.clear(); }
	
	public boolean isEmpty() 
	{ return list.isEmpty(); }
	

	
}
