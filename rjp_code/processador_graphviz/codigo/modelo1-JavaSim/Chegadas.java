package com.javasim.teste.basic;

import java.io.IOException;

import org.javasim.RestartException;
import org.javasim.SimulationException;
import org.javasim.SimulationProcess;
import org.javasim.streams.ExponentialStream;

public class Chegadas extends SimulationProcess
{
    private ExponentialStream taxa;

    public Chegadas(double media)
    {
        taxa = new ExponentialStream(media);
    }

    public void run ()
    {
        while (!terminated())
        {
            try
            {
                hold(taxa.getNumber());
            }
            catch (SimulationException e)
            {
            }
            catch (RestartException e)
            {
            }
            catch (IOException e)
            {
            }

            new Cliente();
        }
    }

}
