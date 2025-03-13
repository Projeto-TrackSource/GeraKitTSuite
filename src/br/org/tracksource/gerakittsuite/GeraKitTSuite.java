/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.tracksource.gerakittsuite;

import br.org.tracksource.gerakittsuite.limites.Limites;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Alexandre Loss
 */
public class GeraKitTSuite {

    public static JanelaPrincipal janelaPrincipal = null;
    
        public GeraKitTSuite() {
    }
    
    /**
     * Efetua os procedimentos para encerrar o programa.
     */
    public static void encerrar(){
        try {
            Configuracoes.salvarConfiguracoes();
        } catch (FileNotFoundException ex) {
            System.out.println("Erro ao salvar gerakittsuite_config.xml: " + ex.getMessage());
        }
        System.out.println("Programa encerrado de forma normal.");
        System.exit(0);
    }

    /**
     * Baixa o ZIP com todos os limites do site.
     * Isso é feito apenas quando o TSuite não encontra
     * o GTM dos limites.  Normalmente o TSuite mantém o GTM
     * atualizado baixando apenas os limites que foram alterados.
     */
    public static boolean baixarZIPGTMLimites ( ) {
        if(Util.isConexaoInternetOk()){
            if(!((new File("base-AUXILIAR-BR.gtm")).exists())){
                GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Arquivo de limites não encontrado.  Baixando do site...");
                return Limites.baixarLimites();
            }else if( ! Limites.isMD5LimitesOk() ){
                GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Arquivo de limites não íntegro.  Baixando do site...");
                return Limites.baixarLimites();
            }else if( Limites.isDesatualizado() ){
                GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Arquivo de limites desatualizado. Baixando do site...");
                return Limites.baixarLimites();
            }else{
                GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO,"Arquivo de limites atualizado e íntegro.");
                return true;
            }
        }else{
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO,"Problema ao acessar a internet. Não foi possível baixar o GTM de limites.  Não será possível marcar o mapa como válido.");
            return false;
        }
    }

    /**
     * Faz o download do arquivo pois.xmz (depois descompacta para XML)
     * atualizado do site do Tracksource.
     */
    public static boolean atualizarBasePoisXML(){
        byte[] buffer = new byte[4096];
        int len = 0;
        int n;

        GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Atualizando XML de regras de validacao de POIs...");
        try {

            //abrindo stream de download do XMZ.
            InputStream is = Util.getStreamDownload(Configuracoes.urlXMZRegrasPOIs);
            if(is == null){ //se nao conseguir o stream, aborta a tentativa de baixar o XMZ
                GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, "Erro ao atualizar XMZ de regras de validacao de POIs");
                return false;
            }

            //abrindo o stream de gravação do XMZ no disco local.
            //lendo do stream de download.
            OutputStream output = new FileOutputStream(new File("pois.xmz"));
            int ianim = 0;
            while ((n = is.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
            output.close();

            //abrindo o XMZ, descompactando seu conteúdo
            ZipInputStream zin;
            zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(new File("pois.xmz"))));
            ZipEntry entry;
            byte data[] = new byte[2048];
            while ((entry = zin.getNextEntry()) != null) {
                int BUFFER = 2048;
                FileOutputStream fos = new FileOutputStream(entry.getName()); //getName() == nome do XML
                BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                while ((len = zin.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, len);
                }
                dest.flush();
                dest.close();
            }
            zin.close();
            return true;
        } catch (Exception ex) {
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, "Erro ao atualizar XML (Zipado) de regras de validacao de POIs:\n " + Util.getStackTrace(ex));
            return false;
        }
    }
    
        /**
     * Faz o download do arquivo tracks.xmz (depois descompacta para XML)
     * atualizado do site do Tracksource.
     */
    public static boolean atualizarBaseTracksXML(){
        byte[] buffer = new byte[4096];
        int len = 0;
        int n;
        
        GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Atualizando XML de regras de validacao de tracks...");
        try {
            //abrindo stream de download do XMZ.
            InputStream is = Util.getStreamDownload(Configuracoes.urlXMZRegrasTracks);
            if(is == null){ //se nao conseguir o stream, aborta a tentativa de baixar o XMZ
                return false;
            }

            //abrindo o stream de gravação do XMZ no disco local.
            //lendo do stream de download.
            OutputStream output = new FileOutputStream(new File("tracks.xmz"));
            int ianim = 0;
            while ((n = is.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
            output.close();

            //abrindo o XMZ, descompactando seu conteúdo
            ZipInputStream zin;
            zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(new File("tracks.xmz"))));
            ZipEntry entry;
            byte data[] = new byte[2048];
            while ((entry = zin.getNextEntry()) != null) {
                int BUFFER = 2048;
                FileOutputStream fos = new FileOutputStream(entry.getName()); //getName() == nome do XML
                BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                while ((len = zin.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, len);
                }
                dest.flush();
                dest.close();
            }
            zin.close();
            return true;
        } catch (Exception ex) {
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, "Erro ao atualizar XML (Zipado) de regras de validacao de Tracks:\n " + Util.getStackTrace(ex));
            return false;
        }
    }
    
    /**
     * Faz o download do arquivo tabela_municipios.xmz (depois descompacta para XML)
     * atualizado do site do Tracksource.
     */
    public static boolean atualizarBaseMunicipiosXML(){
        byte[] buffer = new byte[4096];
        int len = 0;
        int n;
        GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Atualizando XML de municípios...");
        try {

            //abrindo stream de download do XMZ.
            InputStream is = Util.getStreamDownload(Configuracoes.urlXMZMunicipios);
            if(is == null){ //se nao conseguir o stream, aborta a tentativa de baixar o XMZ
                return false;
            }

            //abrindo o stream de gravação do XMZ no disco local.
            //lendo do stream de download.
            OutputStream output = new FileOutputStream(new File("tabela_municipios.xmz"));
            int ianim = 0;
            while ((n = is.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
            output.close();

            //abrindo o XMZ, descompactando seu conteúdo
            ZipInputStream zin;
            zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(new File("tabela_municipios.xmz"))));
            ZipEntry entry;
            byte data[] = new byte[2048];
            while ((entry = zin.getNextEntry()) != null) {
                int BUFFER = 2048;
                FileOutputStream fos = new FileOutputStream(entry.getName()); //getName() == nome do XML
                BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                while ((len = zin.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, len);
                }
                dest.flush();
                dest.close();
            }
            zin.close();
            return true;
        } catch (Exception ex) {
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, "Erro ao atualizar XML (Zipado) de municipios:\n " + Util.getStackTrace(ex));
            return false;
        }
    }
    
    /**
     * Verifica se precisa atualizar os XMLs (poi, tracks, municipio) e os carrega
     *  Todo o bloco a seguir tem a ver com a atualizacao e verificacao dos XMLs locais de regras de validacao de POIs e Tracks,
     *  e da tabela de municipios, bem como opcao de uso dos XMLs embutidos no JAR em caso de problemas com os XMLs locais
     */
    
    public static boolean atualizarXMLs() {
        
        if(Configuracoes.MD5XMLpoisOficial != null && 
           Configuracoes.MD5XMLtracksOficial != null && 
           Configuracoes.MD5XMLmunicipiosOficial != null &&      
           Configuracoes.timeStampXMLpoisOficial != null && 
           Configuracoes.timeStampXMLtracksOficial != null &&
           Configuracoes.timeStampXMLmunicipiosOficial != null){
            return (atualizarBasePoisXML() && atualizarBaseTracksXML() && atualizarBaseMunicipiosXML());
        }else{ //houve problemas para pegar as informacoes do site para validar os XMLs locais, usar os XMLs defaults
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, "Nao foi possivel conectar-se ao Tracksource para atualizar XMLs.");
            return false;
        }
    }
    
    /**
     * Verifica se precisa atualizar limites do GTM de limites.
     */
    public static boolean atualizarLimites ( ) {
        if(Util.isConexaoInternetOk()){
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Verificando limites alterados...");
            if (!Limites.lerLimitesAlteradosEmThread()) return false;
            if( Limites.lstLimitesAlterados.isEmpty() ){
                GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Nenhum limite foi alterado no site.");
                return true;
            }else{
                GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Baixando limites alterados (" + Limites.lstLimitesAlterados.size() + ")...");
                return Limites.atualizaLimitesAlterados();
            }
        }else{
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, "Problema ao acessar a internet. Não foi possível verifica a atualização de limites. Não será possível marcar o mapa como válido.");
            return false;
        }
    }
    
    /**
     * Carrega os valores de timeStampXMLpoisOficial, timeStampXMLtracksOficial, timeStampXMLmunicipiosOficial,
     * MD5XMLpoisOficial, MD5XMLtracksOficial, MD5XMLmunicipiosOficial
     * do site http://www.tracksource.org.br/desenv/xml_timestamp_m.php .
     */
    public static boolean lerValoresVerificacaoXMLdoSite(){
        BufferedReader br;
        GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Lendo valores de verificação dos XMLs...");
        try {
            InputStream is = Util.getStreamDownload(Configuracoes.urlValoresVerificacaoXMLs);
            if(is == null){ //se nao conseguir o stream, aborta a tentativa de atualizar os valores
                GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, "Não foi possível abrir conexão com o Tracksource para verificar os XMLs locais.");
                return false;
            }
            //abrindo o stream para baixar o XML
            br = new BufferedReader(new InputStreamReader(is));
            String s;
            while ((s = br.readLine()) != null) {
                String valores[] = s.split(" ");
                if(valores.length == 6){
                    Configuracoes.timeStampXMLpoisOficial = valores[0];
                    Configuracoes.timeStampXMLtracksOficial = valores[1];
                    Configuracoes.timeStampXMLmunicipiosOficial = valores[4];
                    Configuracoes.MD5XMLpoisOficial = valores[2];
                    Configuracoes.MD5XMLtracksOficial = valores[3];
                    Configuracoes.MD5XMLmunicipiosOficial = valores[5];
                    GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "    - Timestamp XML POIs oficial = " + Configuracoes.timeStampXMLpoisOficial);
                    GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "    - Timestamp XML Tracks oficial = " + Configuracoes.timeStampXMLtracksOficial);
                    GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "    - Timestamp XML Municipios oficial = " + Configuracoes.timeStampXMLmunicipiosOficial);
                    GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "    - MD5 XML POIs oficial = " + Configuracoes.MD5XMLpoisOficial);
                    GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "    - MD5 XML Tracks oficial = " + Configuracoes.MD5XMLtracksOficial);
                    GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "    - MD5 XML Municipios oficial = " + Configuracoes.MD5XMLmunicipiosOficial);
                }
            }
            br.close(); //close do stream de download do XML do site
            return true;
        } catch (Exception ex) {
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, "Erro ao tentar ler os valores de verificacao de XMLs:\n " + Util.getStackTrace(ex));
            return false;
        }
    }
    
    /**
     * O programa comeca a executar aqui.
     *
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {

        //mostra a interface do programa
        GeraKitTSuite.janelaPrincipal = new JanelaPrincipal();
        GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Configurações carregadas.");
        try {
            Configuracoes.carregarConfiguracoes();
        } catch (Exception ex) {
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, "Erro ao carregar gerakittsuite_config.xml: " + ex.getMessage());
        }

    }
}
