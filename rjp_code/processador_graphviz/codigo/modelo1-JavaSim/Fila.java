package com.javasim.teste.basic;

import java.util.NoSuchElementException;

public class Fila
{
    private Lista inicio;
    private long tamanho;
    public long clientesEmFila;
    public long checkFila;

    public Fila()
    {
        inicio = null;
        tamanho = 0;
        clientesEmFila = 0;
        checkFila = 0;
    }

    public boolean isEmpty ()
    {
        if (tamanho == 0)
            return true;
        else
            return false;
    }

    public long queueSize ()
    {
        return tamanho;
    }

    public Cliente dequeue () throws NoSuchElementException
    {
        if (isEmpty())
            throw new NoSuchElementException();

        Lista ptr = inicio;
        inicio = inicio.proximo;

        tamanho--;

        return ptr.cliente;
    }

    public void enqueue (Cliente toadd)
    {
        if (toadd == null)
            return;

        Lista ptr = inicio;

        if (isEmpty())
        {
            inicio = new Lista();
            ptr = inicio;
        }
        else
        {
            while (ptr.proximo != null)
                ptr = ptr.proximo;

            ptr.proximo = new Lista();
            ptr = ptr.proximo;
        }

        ptr.proximo = null;
        ptr.cliente = toadd;
        tamanho++;
    }

    
}

class Lista
{
    public Lista()
    {
        cliente = null;
        proximo = null;
    }

    public Cliente cliente;

    public Lista proximo;
}
