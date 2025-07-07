// -----------------------------------------------------------------------------
// Código gerado com o ASDA - Ambiente de Simulação Distribuída Automático
// -----------------------------------------------------------------------------

package com.javasim.teste.basic;

public class Main
{
    public static void main (String[] args)
    {
   
        Controle m = new Controle();

        // Para a thread principal e da controle do programa para a classe Controle
        m.await();

        System.exit(0);
    }
}

