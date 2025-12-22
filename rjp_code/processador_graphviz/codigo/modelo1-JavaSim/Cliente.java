package com.javasim.teste.basic;

import org.javasim.RestartException;
import org.javasim.Scheduler;
import org.javasim.SimulationException;

public class Cliente
{
    private double tempoResposta;
    private double tempoChegada;

    public Cliente()
    {
        boolean vazio = false;

        tempoResposta = 0.0;
        tempoChegada = Scheduler.currentTime();
		vazio = Controle.filaDoFront.isEmpty();
		Controle.filaDoFront.enqueue(this);
        Controle.totalClientes++;

        if (vazio)
        {
            try
            {
				Controle.front.activate();
            }
            catch (SimulationException e)
            {
            }
            catch (RestartException e)
            {
            }
        }
    }
    public void finished ()
    {
        tempoResposta = Scheduler.currentTime() - tempoChegada;
        Controle.tempoRespostaTotal += tempoResposta;
    }

}

