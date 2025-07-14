package com.avaliador;
import java.io.*;
/**
 * Classe que implementa o m   dulo Avaliador N   vel 1A. Respons   vel por verificar se as 
 * liga      es entre os n   s do grafo est   o corretas.
 * @author Thais Souza Aiza
 * @version 1.0
 */
public class AvaliadorNivelUmA implements Serializable {

    /**
     * Retorna true se a liga      o est    correta e false se a liga      o est    incorreta.
     * Liga      es corretas: Fonte para Centro de Servi   o, Centro de Servi   o para Centro de 
     * Servi   o e Centro de Servi   o para Sorvedouro.
     * Todas as demais liga      es est   o incorretas. 
     * @param origem N    de origem do arco
     * @param destino N    de destino do arco
     * @return Um valor <code>boolean</code>.
     */
    public boolean verifica(int origem, int destino) {

        if(((origem == 1) && (destino == 3))
                || ((origem == 1) && (destino == 1))
                || ((origem == 3) && (destino == 3))
                || ((origem == 3) && (destino == 1))
                || ((origem == 3) && (destino == 2))
                || ((origem == 2) && (destino == 1))) {
            return false;
        } else {
            return true;
        }
        //Desenho desenho = new Desenho(); desenho.debugg();
    }
}


