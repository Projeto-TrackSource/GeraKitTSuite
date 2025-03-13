/*
 * Criada em 28/07/2012
 */

package br.org.tracksource.gerakittsuite.limites;

import br.org.tracksource.gtm211api.domain.Map;
import br.org.tracksource.gtm211api.domain.Trknome1;
import br.org.tracksource.gtm211api.errors.ErrorGTMRule;
import br.org.tracksource.gtm211api.io.GtmIO;
import br.org.tracksource.gerakittsuite.Configuracoes;
import br.org.tracksource.gerakittsuite.GeraKitTSuite;
import br.org.tracksource.gerakittsuite.JanelaPrincipal;
import br.org.tracksource.gerakittsuite.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Esta classe concentra as funcionalidades relativas à manipulação
 * do GTM contendo os limites.
 * Limite municipal : mapa no TRC
 * Limite vermelho : mapa no TRU
 * Limite verde : Mapa sem desenvolvimento.
 *
 * @author Paulo Carvalho
 */
public class BaseLimites {

    private static Map GTM = null;
    private static ArrayList<Trknome1> limites = new ArrayList<Trknome1>();

    /**
     * Carrega o GTM de limites na variável estatica BaseLimites.GTM.
     * e popula a coleção limites.
     */
    public static void carregarGTMLimites(){
        try {
            BaseLimites.GTM = GtmIO.LoadGTMFile("base-AUXILIAR-BR.gtm");
            Iterator<Trknome1> x = GTM.getTracklogHeaderIterator();
            while(x.hasNext())
                limites.add( x.next() );
        } catch (Exception ex) {
            GeraKitTSuite.janelaPrincipal.printLineSysout(JanelaPrincipal.MENSAGEM_ERRO, "Erro ao carregar GTM de limites." + Util.getStackTrace(ex));
        }
    }

    /**
     * Atualiza o objeto Map com os limites.  Não salva o GTM automaticamente.
     * 
     * @param codigo Código do mapa cujo limite deve ser alterado ou acrescentado.
     * @param gtm_do_limite Nome do GTM, sem path, do limite a ser alterado ou acrescentado.
     */
    public static void atualizarLimite( String codigo, String gtm_do_limite ) throws Exception {
        Map gtm_limite = GtmIO.LoadGTMFile( gtm_do_limite );

        //assumo que há um único tracklog correspondente ao limite.
        Trknome1 track_limite = gtm_limite.getTracklog(0);

        //altero o nome do limite para colocar o código do mapa
        track_limite.setTname( track_limite.getTname() + "<F=" + codigo + ">" );

        //busco o índice do track do limite antigo, se houver
        int indice_limite = BaseLimites.GTM.findTracklogByName("^.*<F=" + codigo + ">.*$", 0);

        //se o limite já existir, remove
        String acao = "acrescentado";
        if( indice_limite != -1 ){ //>-1 == encontrou
            //remove o limite antigo
            BaseLimites.GTM.removeTrknome1( indice_limite );
            acao = "atualizado";
        }

        //troca a referência ao objeto Map pai
        track_limite.setParentMapReference( BaseLimites.GTM );

        //coloca o novo limite no GTM de limites
        BaseLimites.GTM.addTrknome1( track_limite );

        //System.out.println("Limite " + track_limite.getTname() + " " + acao + " com sucesso na base de limites local.");
    }

    /**
     * Limpa a variável estática BaseLimites.GTM. e esvazia a coleção limites.
     * Isto é um prática recomendada, pois esse mapa ocupa bastante memória.
     */
    public static void desalocarGTMLimites(){
        BaseLimites.GTM = null;
        BaseLimites.limites.clear();
    }

    /**
     * Atalho para desalocarGTMLimites() e carregarGTMLimites() chamados nesta sequência.
     */
    public static void reload(){
        BaseLimites.desalocarGTMLimites();
        BaseLimites.carregarGTMLimites();
    }

    /**
     * Salva o GTM dos limites.  Não desaloca o objeto BaseLimites.GTM.
     */
    public static void salvaGTMLimites() throws Exception{
        GtmIO.SaveGTMFile( BaseLimites.GTM, "base-AUXILIAR-BR.gtm");
    }

    /**
     * Retorna o tracklog de limite oficial.
     * Retorna null se o limite não for encontrado.
     */
    public static Trknome1 getLimiteOficial( int codigo ){
        //busco o índice do track do limite, se houver
        int indice_limite = BaseLimites.GTM.findTracklogByName("^.*<F=" + String.valueOf( codigo ) + ">.*$", 0);

        //se o limite existir, retorna
        if( indice_limite != -1 ) //>-1 == encontrou
            return BaseLimites.GTM.getTracklog( indice_limite );
        else
            return null;
    }

    /**
     * Dado um limite, retorna seu codigo
     */
    
    public static String getCodigoLimite (Trknome1 limite) {
        //obter o nome do track do limite vizinho
        String tname = limite.getTname();
        //obter o tamanho do nome do track do limite vizinho
        int tam = tname.length();
        //obter o código do limite vizinho a partir do nome do track do limite vizinho
        return tname.substring(tam - 9, tam - 1);
    }
    
    /**
     * Retorna uma lista ordenada com os UF_3 existentes na BaseLimites
     */
    public static ArrayList<String> getLitaUF3(){
        HashSet<String> lista = new HashSet<String>();
        for( Trknome1 limite : limites ){
            lista.add(BaseLimites.getCodigoLimite(limite).substring(0, 3));
        }
        ArrayList<String> retorno = new ArrayList<String>();
        retorno.addAll(lista);
        Collections.sort(retorno);
        return retorno;
    }

}
