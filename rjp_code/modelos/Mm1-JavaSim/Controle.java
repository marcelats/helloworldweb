package com.javasim.teste.basic;

import org.javasim.RestartException;
import org.javasim.Simulation;
import org.javasim.SimulationException;
import org.javasim.SimulationProcess;

public class Controle extends SimulationProcess
{

	public static CPU cpu = null;
	public static Fila filaDoCPU = new Fila();


    public static double tempoRespostaTotal = 0.0;
    public static long totalClientes = 0;
    public static long clientesProcessados = 0;
    public static double totalServico = 0;


    public Controle()
    {
        
    }

    public void run ()
    {
        try
        {
			Chegadas chegadas = new Chegadas(9);
			Controle.cpu = new CPU(0.1);
            chegadas.activate();

            Simulation.start();

			hold(2000);

            System.out.println("Tempo total = "+currentTime());
            System.out.println("Total de clientes presentes no sistema = " + totalClientes);
            System.out.println("Total de clientes processados = " + clientesProcessados);
            System.out.println("Tempo de resposta total = " + tempoRespostaTotal);
            System.out.println("Tempo médio de resposta = "
                + (tempoRespostaTotal / clientesProcessados));
            
			System.out.println("Utilização do CPU = " + Controle.cpu.tempoDeServico);
			System.out.println("Comprimento médio de filaCPU = "+ (Controle.filaDoCPU.clientesEmFila / Controle.filaDoCPU.checkFila));

            Simulation.stop();

            chegadas.terminate();
			Controle.cpu.terminate();

            SimulationProcess.mainResume();
        }
        catch (SimulationException e)
        {
        }
        catch (RestartException e)
        {
        }
    }
    public void await ()
    {
        this.resumeProcess();
        SimulationProcess.mainSuspend();
    }

}
