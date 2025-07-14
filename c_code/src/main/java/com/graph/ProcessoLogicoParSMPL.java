package com.graph;
import java.io.Serializable;

/*
 * Created on 11/05/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author andrefr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ProcessoLogicoParSMPL implements Serializable {
	
	boolean processoPai; // se   processo pai da simula  o ou n o
	int tid; // identificador do processo
	double lookAhead;	
	
	private String nomeMaquina;
	private String slaveName;
	
	int valorSemente;
	
	
	public int getValorSemente() {
		return valorSemente;
	}

	public void setValorSemente(int valorSemente) {
		this.valorSemente = valorSemente;
	}

	//	boolean maqEspecificada;
	public ProcessoLogicoParSMPL()
	{
		this(0,true);
	}
	
	public ProcessoLogicoParSMPL(int tid, boolean pai)
	{
	//	super();
		this.tid = tid;
		this.processoPai = pai;
	}
	

	
	/**
	 * @return Returns the slaveName.
	 */
	public String getSlaveName() {
		return slaveName;
	}
	/**
	 * @param slaveName The slaveName to set.
	 */
	public void setSlaveName(String slaveName) {
		this.slaveName = slaveName;
	}
	/**
	 * @return Returns the nomeMaquina.
	 */
	public String getNomeMaquina() {
		return nomeMaquina;
	}
	/**
	 * @param nomeMaquina The nomeMaquina to set.
	 */
	public void setNomeMaquina(String nomeMaquina) {
		this.nomeMaquina = nomeMaquina;
	}
	/**
	 * @return Returns the processoPai.
	 */
	public boolean isProcessoPai() {
		return processoPai;
	}
	/**
	 * @param processoPai The processoPai to set.
	 */
	public void setProcessoPai(boolean processoPai) {
		this.processoPai = processoPai;
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

	
	/**
	 * @return Returns the lookAhead.
	 */
	public double getLookAhead() {
		return lookAhead;
	}
	/**
	 * @param lookAhead The lookAhead to set.
	 */
	public void setLookAhead(double lookAhead) {
		this.lookAhead = lookAhead;
	}

	
	/*public String toString()
	{
		return "PL" + getTid();
	}*/
}
