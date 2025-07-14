package com.graph;
import java.util.LinkedList;

/**
 * Criao de um TAD pilha via uma lista ligada oferecida por java.util.LinkedList;
 * @author Andr Felipe Rodrigues
 *
 */
public class StackL {
	
	private LinkedList list = new LinkedList();
	
	public void push(Object v) 
	{ list.addFirst(v); }
	
	public Object pop() 
	{ return list.removeFirst(); }
	
	public Object top() 
	{ return list.getFirst(); }
	
	public void clear() 
	{ list.clear(); }
	
	public boolean isEmpty() 
	{ return list.isEmpty(); }
	

	
}
