package com.javasim.teste.basic;

import java.io.IOException;
import java.util.Random;

import org.javasim.RestartException;
import org.javasim.SimulationException;
import org.javasim.SimulationProcess;
import org.javasim.streams.ExponentialStream;

public class CPU extends SimulationProcess
{
    private ExponentialStream taxa;
    private Cliente cliente;
    public double tempoDeServico = 0.0;
    
	public CPU(double media)
    {
        taxa = new ExponentialStream(media);
        cliente = null;
    }

    public void run ()
    {
        double inicioAtividade, fimAtividade;
        boolean vazio = false;

        while (!terminated())
        {
			while (!Controle.filaDoCPU.isEmpty())
			{
				inicioAtividade = currentTime();

				Controle.filaDoCPU.checkFila++;
				Controle.filaDoCPU.clientesEmFila += Controle.filaDoCPU.queueSize();
				cliente = Controle.filaDoCPU.dequeue();

                try
                {
                    hold(serviceTime());
                }
                catch (SimulationException e)
                {
                }
                catch (RestartException e)
                {
                }

                fimAtividade = currentTime();
                tempoDeServico += fimAtividade - inicioAtividade;
                Controle.totalServico += tempoDeServico;

				Controle.clientesProcessados++;
				cliente.finished();


            }

        
            try
            {
                cancel();
            }
            catch (RestartException e)
            {
            }
        }
    }

    public double serviceTime ()
    {
        try
        {
            return taxa.getNumber();
        }
        catch (IOException e)
        {
            return 0.0;
        }
    }

}
