/*
 * Criada em 28/07/2012
 */

package br.org.tracksource.gerakittsuite.limites;

import br.org.tracksource.gerakittsuite.Configuracoes;
import br.org.tracksource.gerakittsuite.GeraKitTSuite;
import br.org.tracksource.gerakittsuite.JanelaPrincipal;
import br.org.tracksource.gerakittsuite.Util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Classe que centraliza as funcionalidades relativas ao gerenciamento do GTM de limites dos mapas do Tracksource
 * que é um arquivo chamado base-AUXILIAR-BR.gtm que fica na pasta do executável do TSuite.
 * 
 * limite municipal: mapa no TRC
 * limite vermelho: mapa no TRU
 * limite verde: mapa sem desenvolvimento
 *
 * @author Paulo Carvalho
 */
public class Limites {

    public static ArrayList<String> lstLimitesAlterados = new ArrayList<String>();

    /**
     * Verifica o MD5 do GTM de limites se o usuário não mexeu.
     */
    public static boolean isMD5LimitesOk( ){
        try {
            return Configuracoes.strMD5Limites.equals(Util.MD5("base-AUXILIAR-BR.gtm"));
        } catch (Exception ex) {
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, "Erro ao verificar MD5 do GTM de limites.");
            return false;
        }
    }
    
    /**
     * Verifica o se o GTM de limites local eh mais antigo que o do site
     */
    public static boolean isDesatualizado( ){
        String dataGtmLocal = Util.getFileLastDateTimeUpdate("base-AUXILIAR-BR.gtm").substring(0, 12);
        String dataGtmSite = lerTimestampGTMLimitesDoSite();
        return dataGtmLocal.compareTo(dataGtmSite) < 0;
    }

    /**
     * Acessa o site do projeto para obter o timestamp do GTM de limites que é baixado do site.
     * Em caso de falha ao acessar o php que retorna o timestamp, é usado o default 20120728000000.
     */
    public static String lerTimestampGTMLimitesDoSite( ) {
        BufferedReader br;
        String resultado = "20120728000000";
        String urlCompleta  = Configuracoes.urlTimestampDoGTMdeLimitesDoSite;
        InputStream is = Util.getStreamDownload( urlCompleta );
        if(is == null){ //se nao conseguir o stream, lança exceção
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, "Não foi possível se conectar ao Tracksource para obter o timestamp do GTM de limites baixado do site. Será usado o default 20120728000000");
            return resultado;
        }
        //abrindo o stream
        try{
            br = new BufferedReader(new InputStreamReader(is));
            String s;
            while ((s = br.readLine()) != null) {
                resultado = s;
            }
            br.close(); //close do stream de download
        }catch(Exception ex){
            System.out.println( Util.getStackTrace(ex) );
        }finally{
            return resultado;
        }
    }

    /**
     * Faz o download do arquivo base-AUXILIAR-BR.zip (depois descompacta para GTM)
     * do site do Tracksource.
     * Como o arquivo é grande, este download pode ser interrompido pelo usuário.
     */
    public static boolean baixarLimites(){
        byte[] buffer = new byte[4096];
        int len = 0;
        int n;
        String animacao[] = {"-", "\\", "|", "/",};

        try {

            //abrindo stream de download do XMZ.
            InputStream is = Util.getStreamDownload(Configuracoes.urlZipTodosOsLimites);
            if(is == null){ //se nao conseguir o stream, aborta a tentativa de baixar os limites
                return false;
            }

            //abrindo o stream de gravação do ZIP no disco local.
            //lendo do stream de download.
            //como esta parte pode demorar, permite que o usuário interrompa.
            OutputStream output = new FileOutputStream(new File("base-AUXILIAR-BR.zip"));
            int ianim = 0;
            while ((n = is.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
            output.close();

            //abrindo o ZIP, descompactando seu conteúdo
            //isso é normalmente rápido, não tem lógica de interrupção
            ZipInputStream zin;
            zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(new File("base-AUXILIAR-BR.zip"))));
            ZipEntry entry;
            byte data[] = new byte[2048];
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Descompactando o ZIP com os limites...");
            while ((entry = zin.getNextEntry()) != null) {
                int BUFFER = 2048;
                FileOutputStream fos = new FileOutputStream(entry.getName()); //getName() == nome do GTM
                BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                while ((len = zin.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, len);
                }
                dest.flush();
                dest.close();
            }
            zin.close();

            //apagar o zip
            (new File("base-AUXILIAR-BR.zip")).delete();

            //como o gtm de limites foi baixado com sucesso
            //inauguro o timestamp da última atualização como sendo
            //o dia em que o ZIP lá do site foi feito.
            Configuracoes.strTimeStampUltAtualizLimites = Limites.lerTimestampGTMLimitesDoSite();

            //atualizo o MD5 do GTM de limites
            Configuracoes.strMD5Limites = Util.MD5("base-AUXILIAR-BR.gtm");
            return true;
        } catch (Exception ex) {
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, "Erro ao baixar o ZIP com os limites do site:\n " + Util.getStackTrace(ex));
            return false;
        }
    }

    /**
     * Retorna uma lista com os códigos dos mapas cujos limites foram alterados.
     * A lista é fornecida pelo site.
     */
    public static boolean getLimitesAlterados(){
        Limites.lstLimitesAlterados.clear();
        String urlCompleta  = Configuracoes.urlListaLimitesAlterados.replace("%%1", /*"20120722180000"*/Configuracoes.strTimeStampUltAtualizLimites);
        InputStream is = Util.getStreamDownload( urlCompleta );
        if(is == null){ //se nao conseguir o stream, aborta a tentativa de atualizar
            return false;
        }
        try{
            //abrindo o stream para baixar o texto
            BufferedReader br = new BufferedReader(new InputStreamReader(is, Configuracoes.charsetNameXML2Java));
            String s;
            while ((s = br.readLine()) != null) {
                if (s.compareTo("<br />")==0) { // Se há alguma problema no site, exibe mensagem e sai do loop
                    GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_AVISO,  "Problema ao tentar obter a lista de limites alterados no site.\nA sua conexão ou o site do Tracksource pode estar com problema.");
                    br.close();
                    return false;
                }
                if (!s.trim().isEmpty()) {
                    Limites.lstLimitesAlterados.add(s);
                }
            }
            br.close(); //close do stream de download do texto
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    /**
     * Faz o download dos GTMs dos limites alterados cujos códigos estejam na lista
     * Limites.lstLimitesAlterados.
     */
    public static boolean atualizaLimitesAlterados() {
        byte[] buffer = new byte[4096];
        int len = 0;
        int n;
        int andamento = 0;
        String limitesComErro = "";

        try {

            //carregar o GTM de limites
            if( Limites.lstLimitesAlterados.size() > 0 )
                BaseLimites.carregarGTMLimites();
            
            for(String codigo : Limites.lstLimitesAlterados ){
                String urlCompleta  = Configuracoes.urlBaixaLimiteZip.replace("%%1", codigo);

                //abrindo stream de download do GTM.
                InputStream is = Util.getStreamDownload(urlCompleta);
                if(is == null){ //se nao conseguir o stream, aborta a tentativa de baixar os limites
                    return false;
                }

                //abrindo o stream de gravação do ZIP no disco local.
                //lendo do stream de download.
                OutputStream output = new FileOutputStream(new File(codigo + ".zip"));

                while ((n = is.read(buffer)) != -1 ) {
                    output.write(buffer, 0, n);
                }
                output.close();

                String nome_GTM = "xpto";

                //abrindo o ZIP, descompactando seu conteúdo
                ZipInputStream zin;
                zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(new File(codigo + ".zip"))));
                ZipEntry entry;
                byte data[] = new byte[2048];
                //GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "    - Descompactando o ZIP com o limite " + codigo + "...");
                GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, 
                                              String.format("    - (%d) Descompactando o ZIP com o limite %s...", ++andamento, codigo));
                while ((entry = zin.getNextEntry()) != null) {
                    int BUFFER = 2048;
                    nome_GTM = "LIM" + entry.getName();  //getName() == nome do GTM
                    FileOutputStream fos = new FileOutputStream( nome_GTM );
                    BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                    while ((len = zin.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, len);
                    }
                    dest.flush();
                    dest.close();
                }
                zin.close();

                //apagar o zip
                (new File(codigo + ".zip")).delete();

                //atualizar ou acrescentar o limite à base.
                try {
                    BaseLimites.atualizarLimite(codigo, nome_GTM);
                }
                catch (Exception exond) {
                    limitesComErro += "- " + nome_GTM + "\n";
                }
                //apagar o GTM baixado
                (new File( nome_GTM )).delete();

            }

            //gravar o GTM de limites
            if( Limites.lstLimitesAlterados.size() > 0 )
                BaseLimites.salvaGTMLimites();

            //desalocar o objeto com todos os limites
            BaseLimites.desalocarGTMLimites();

            //como o gtm de limites foi atualizado com sucesso
            //atualizo o timestamp da última atualização
            Configuracoes.strTimeStampUltAtualizLimites = Util.getTimeStampAgora();

            //atualizo o MD5 do GTM de limites
            Configuracoes.strMD5Limites = Util.MD5("base-AUXILIAR-BR.gtm");

            if (!limitesComErro.isEmpty())
                GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_AVISO, "Houve problema na carga dos seguintes limites municipais. Favor avisar a Administração:\n"+limitesComErro);
            return true;
        } 
        catch (Exception ex) {
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, ex.getMessage());
            return false;
        }
    }

    /**
     * Executa #getLimitesAlterados() em uma thread separada.
     */
    public static boolean lerLimitesAlteradosEmThread(){
        GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_INFORMACAO, "Consultando limites alterados do site...");
        return Limites.getLimitesAlterados();
    }

}
