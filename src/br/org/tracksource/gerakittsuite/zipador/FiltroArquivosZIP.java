/**
 * Pacote com as classes principais do Projeto "TSuite"
 */

package br.org.tracksource.gerakittsuite.zipador;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * Classe para FiltroArquivosZIP...
 *   -- Alexandre Loss Pereira Machado (alexandre.loss@gmail.com)
 *   -- Belo Horizonte, MG - Brasil
 *   -- Criação:     04-Jun-2011
 *   -- Atualização: 04-Jun-2011
 */
public class FiltroArquivosZIP implements FilenameFilter {
    private ArrayList<String> listaArquivos = null;

    /**
     * Constructor
     * @param lArquivos lista de arquivos que serão filtrados
     */
    public FiltroArquivosZIP (ArrayList<String> lArquivos) {
        super();
        this.listaArquivos = lArquivos;
    }

    public FiltroArquivosZIP() {
        super();
    }

    public boolean accept(File dir, String s) {
        boolean retorno = false;
        String fileName = s.toLowerCase();
        if (fileName.endsWith(".zip")) {
            if (this.listaArquivos == null)
                retorno = true;
            else
                for (int i = 0; i < this.listaArquivos.size() && !retorno; i++) {
                    retorno = fileName.substring(0, fileName.length()-4).compareTo(this.listaArquivos.get(i)) == 0;
                }
        }
        return retorno;
    }
}
