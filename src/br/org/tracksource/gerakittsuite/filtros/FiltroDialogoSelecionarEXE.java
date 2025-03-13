/*
 * FiltroDialogoAbrirArquivoProjeto.java
 *
 * Criado em 19, 2011, 3:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package br.org.tracksource.gerakittsuite.filtros;

import br.org.tracksource.gerakittsuite.Util;
import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Classe usada para passar para um JFileChooser a fim de mostrar apenas pastas e arquivos .EXE.
 * @author Alexandre Loss
 */
public class FiltroDialogoSelecionarEXE extends FileFilter{
    
    /** Creates a new instance of FiltroDialogoSelecionarEXE */
    public FiltroDialogoSelecionarEXE() {
    }
    
   /**
     *  Programado para aceitar diretorios e arquivos com a extensao EXE (Executavel do Windows).
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        if(Util.getExtension(f).equals("exe")){
            return true;
        }
        
        return false;
    }

    public String getDescription() {
        return "Aplicativo (*.EXE)";
    }
}
