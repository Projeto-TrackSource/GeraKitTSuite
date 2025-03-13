/*
 * Criada em 01/09/2010
 */

package br.org.tracksource.gerakittsuite.zipador;

import br.org.tracksource.gtm211api.domain.Map;
import br.org.tracksource.gtm211api.io.GtmIO;
import br.org.tracksource.gerakittsuite.Configuracoes;
import br.org.tracksource.gerakittsuite.GeraKitTSuite;
import br.org.tracksource.gerakittsuite.JanelaPrincipal;
import br.org.tracksource.gerakittsuite.Util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Esta classe encerra as funcionalidade para zipAdd um mapa para compilacao.
 *
 * @author ur5m
 */
public class Zipador {

    /**
     * Esta funcao zipa para compilacao o mapa passado como parametro.
     * O ZIP gerado tem o mesmo nome do GTM do mapa, mas com a extensao ZIP.
     */
    public static void ziparKitSetupTSuite(String nomeZip) throws FileNotFoundException, IOException{

        //instanciar o objeto para o calculo de checksum
        CRC32 crc = new CRC32();
        //instanciar o objeto stream que faz a compressao ZIP
        ZipOutputStream zs;
        zs = new ZipOutputStream((OutputStream) new FileOutputStream(nomeZip));
        //definir o nivel de compressao
        zs.setLevel(6);

        // adiciona os arquivos no zip
        zipAdd ("TSuite.jar", crc, zs);
        zipAdd ("..\\changes.txt", crc, zs);
        zipAdd ("Licenca.txt", crc, zs);
        zipAdd ("Manual_TSuite.pdf", crc, zs);

        //encerra e fecha o arquivo ZIP
        zs.finish();
        zs.close();
    }
    
    /**
     * 
     */
    private static void zipAdd (String nome_arq, CRC32 crc, ZipOutputStream zs) throws FileNotFoundException, IOException {
        File arq = new File(nome_arq);
        if (arq.exists()) {
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Adicionando \""+ nome_arq + "\" no kit zipado.");
            byte[] buf = new byte[(int) arq.length()];
            FileInputStream fis = null;
            fis = new FileInputStream(nome_arq);
            fis.read(buf, 0, buf.length);
            fis.close();
            //instanciar um novo sub-arquivo para o zip
            ZipEntry entry = new ZipEntry(Util.semAcento((new File(nome_arq)).getName()));
            entry.setSize((long) buf.length);
            crc.reset();
            crc.update(buf);
            entry.setCrc(crc.getValue());
            //adicionar o sub-arquivo ao zip
            zs.putNextEntry(entry);
            zs.write(buf, 0, buf.length);
        }
        else
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_AVISO, "ATENÇÃO: arquivo \""+ nome_arq + "\" não encontrado para incluir no kit zipado.");
    }

    /**
     * Método para descompactar arquivos ZIP
     * @param zipname Nome completo do arquivo a ser descompactado
     *        outputPath   Pasta de destino onde os arquivos serão extraidos
     */
    public static void unziparArquivo (String zipname, String outputPath) throws FileNotFoundException, IOException{
        String destinationPath;
        if (!outputPath.isEmpty()) {
            if (outputPath.charAt(outputPath.length()-1) == File.separatorChar)
                destinationPath = outputPath;
            else
                destinationPath = outputPath + File.separator;
        }
        else {
            destinationPath = "";
        }
        FileInputStream fis = new FileInputStream(zipname);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
        ZipEntry entry;
        // Descompacta cada entrada do arquivo zip
        while ((entry = zis.getNextEntry()) != null) {
             int size;
             byte[] buffer = new byte[2048];
             FileOutputStream fos = new FileOutputStream(destinationPath + entry.getName());
             BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
             while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                 bos.write(buffer, 0, size);
             }
             bos.flush();
             bos.close();
        }
        zis.close();
        fis.close();
    }
}
