package com.javasim.teste.basic;

import java.io.IOException;
import java.util.Random;

import org.javasim.RestartException;
import org.javasim.SimulationException;
import org.javasim.SimulationProcess;
import org.javasim.streams.ExponentialStream;

public class Front extends SimulationProcess
{
    private ExponentialStream taxa;
    private Cliente cliente;
    public double tempoDeServico = 0.0;
    
	public Front(double media)
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
			while (!Controle.filaDoFront.isEmpty())
			{
				inicioAtividade = currentTime();

				Controle.filaDoFront.checkFila++;
				Controle.filaDoFront.clientesEmFila += Controle.filaDoFront.queueSize();
				cliente = Controle.filaDoFront.dequeue();

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

				vazio = Controle.filaDoCPU.isEmpty();
				Controle.filaDoCPU.enqueue(cliente);

				if (vazio)
				{
					try
					{
						Controle.cpu.activate();
					}
					catch (SimulationException e)
					{
					}
					catch (RestartException e)
					{
					}
				}

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
