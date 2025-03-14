/*
 * Configuracoes.java
 *
 * Criado em February 18, 2010, 2:44 PM
 *
 */

package br.org.tracksource.gerakittsuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Classe que contem valores de configuracao que devem estar visiveis em todo o codigo, por exemplo, o nome do servidor Proxy.
 *
 * @author ur5m
 */
public class Configuracoes {
    
    /** Creates a new instance of Configuracoes */
    public Configuracoes() {
    }
    public static String strPastaTSuiteAtualizado           = null; // caminho do TSuite.jar atualizado que será origem do kit
    public static String strPastaDestinoPoisTracksXml       = null; // destino da copia de pois.xml tracks.xml
    public static String strPastaDestinoTabelaMunicipioXml  = null; // destino da copia de tabela_municipios.xml
    public static String strIssTSuite                       = null; // caminho do TSuite.iss
    public static String strInnoSetup                       = null; // caminho do InnoSetup

    public static String urlXMLRegrasPOIs = "https://www.tracksource.org.br/desenv/xml_pois.php";
    public static String urlXMLRegrasTracks = "https://www.tracksource.org.br/desenv/xml_tracks.php";
    public static String urlXMLMunicipios = "https://www.tracksource.org.br/desenv/xml_municipios.php";
    public static String urlXMZRegrasPOIs = "https://www.tracksource.org.br/desenv/xmz_pois.php";
    public static String urlXMZRegrasTracks = "https://www.tracksource.org.br/desenv/xmz_tracks.php";
    public static String urlXMZMunicipios = "https://www.tracksource.org.br/desenv/xmz_municipios.php";
    public static String urlValoresVerificacaoXMLs = "https://www.tracksource.org.br/desenv/xml_timestamp_m.php";
    public static String urlListaLimitesAlterados = "https://www.tracksource.org.br/desenv/tsuite_limites_alterados.php?timestamp=%%1";
    public static String urlBaixaLimiteZip = "https://www.tracksource.org.br/desenv/tsuite_limite_baixa.php?codigo=%%1";
    public static String urlZipTodosOsLimites = "https://www.tracksource.org.br/ftp/desenvolvimento/base-AUXILIAR-BR.zip";
    public static String urlTesteConexao = "https://www.google.com.br";
    public static String urlTimestampDoGTMdeLimitesDoSite = "https://www.tracksource.org.br/desenv/tsuite_base_data.php";
    
    public static final String charsetNameXML2Java = "ISO-8859-1";
    
    //estes valores devem ser lidos da url https://www.tracksource.org.br/desenv/xml_timestamp.php
    public static String timeStampXMLpoisOficial = null;
    public static String timeStampXMLtracksOficial = null;
    public static String MD5XMLpoisOficial = null;
    public static String MD5XMLtracksOficial = null;
    public static String timeStampXMLmunicipiosOficial = null;
    public static String MD5XMLmunicipiosOficial = null;
    
    /**
     * Guarda o timestamp da última execução do TSuite, ou seja, o momento em que ele
     * Foi fechado pela última vez;
     */
    public static String strTimeStampUltAtualizLimites = null;
    public static String strMD5Limites = "";

    // REFERENCIA... SERÁ APAGADO...
    // Path completo do GTM TrackMaker (ambiente Windows apenas)
    public static String commandTrackMaker = "";

    public static void carregarConfiguracoes() throws ParserConfigurationException, SAXException, IOException{
        File arquivo = new File("gerakittsuite_config.xml");
        if(arquivo.exists()){
            ManipuladorParsingXMLDeConfiguracoes manipuladorParsingXML = new ManipuladorParsingXMLDeConfiguracoes();
            SAXParserFactory fact = SAXParserFactory.newInstance();
            SAXParser parser = fact.newSAXParser();
            parser.parse(arquivo, manipuladorParsingXML);
        }
        
        arquivo = new File("tsuite_update.xml");
        if(arquivo.exists()){
            ManipuladorParsingXMLDeUpdates manipuladorParsingXML = new ManipuladorParsingXMLDeUpdates();
            SAXParserFactory fact = SAXParserFactory.newInstance();
            SAXParser parser = fact.newSAXParser();
            parser.parse(arquivo, manipuladorParsingXML);
        }
    }
    
    public static void salvarConfiguracoes() throws FileNotFoundException{
        Configuracoes.salvarConfig();
        Configuracoes.salvarUpdate();
    }
    
    public static void salvarConfig() throws FileNotFoundException{
        String texto = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n<CONFIGURACOES>\n";
        if(Configuracoes.strPastaTSuiteAtualizado != null)
            texto += "\t<PASTA_TSUITE_ATUALIZADO VALOR=\"" + Configuracoes.strPastaTSuiteAtualizado + "\"/>\n";
        if(Configuracoes.strPastaDestinoPoisTracksXml != null)
            texto += "\t<PASTA_DESTINO_POIS_TRACKS_XML VALOR=\"" + Configuracoes.strPastaDestinoPoisTracksXml + "\"/>\n";
        if(Configuracoes.strPastaDestinoTabelaMunicipioXml != null)
            texto += "\t<PASTA_DESTINO_TABELA_MUNICIPIOS_XML VALOR=\"" + Configuracoes.strPastaDestinoTabelaMunicipioXml + "\"/>\n";
        if(Configuracoes.strIssTSuite != null)
            texto += "\t<TSUITE_ISS VALOR=\"" + Configuracoes.strIssTSuite + "\"/>\n";
        if(Configuracoes.strInnoSetup != null)
            texto += "\t<INNO_SETUP_EXE VALOR=\"" + Configuracoes.strInnoSetup + "\"/>\n";
        texto += "</CONFIGURACOES>\n";
        PrintWriter writer;
        writer = new PrintWriter(new FileOutputStream("gerakittsuite_config.xml", false));
        writer.println(texto);
        writer.close();
    }
    
    public static void salvarUpdate() throws FileNotFoundException{
        //salvando o arquivo que guarda informacoes de atualizacoes de tabelas como a de limites
        String texto = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n<ATUALIZACOES>\n";
        if( Configuracoes.strTimeStampUltAtualizLimites != null )
            texto += "\t<TIMESTAMP_ULTIMA_ATUALIZ_LIMITES VALOR=\"" + Configuracoes.strTimeStampUltAtualizLimites + "\"/>\n";
        texto += "\t<HASH_LIMITES VALOR=\"" + Configuracoes.strMD5Limites + "\"/>\n";
        texto += "</ATUALIZACOES>\n";
        PrintWriter writer;
        writer = new PrintWriter(new FileOutputStream("tsuite_update.xml", false));
        writer.println(texto);
        writer.close();
    }
}
/**
 * Classe usada para parsear o arquivo de configuracoes, que estah em XML.
 * Ela eh composta de callbacks que sao chamadas pelo parser XML
 * toda vez que um evento de parsing acontece, tal como o fechamento
 * de uma tag de um elemento.
 *
 */
class ManipuladorParsingXMLDeConfiguracoes extends DefaultHandler{

    
    /** Construtor default. */
    public ManipuladorParsingXMLDeConfiguracoes() {
    }

    @Override
    public void characters(char[] c, int i, int i0) throws SAXException {
    }

    @Override
    public void startElement(String string, String string0, String string1, Attributes attributes) throws SAXException {
        if(string1.equals("PASTA_TSUITE_ATUALIZADO"))
            Configuracoes.strPastaTSuiteAtualizado = attributes.getValue(0);
        if(string1.equals("PASTA_DESTINO_POIS_TRACKS_XML"))
            Configuracoes.strPastaDestinoPoisTracksXml = attributes.getValue(0);
        if(string1.equals("PASTA_DESTINO_TABELA_MUNICIPIOS_XML"))
            Configuracoes.strPastaDestinoTabelaMunicipioXml = attributes.getValue(0);
        if(string1.equals("TSUITE_ISS"))
            Configuracoes.strIssTSuite = attributes.getValue(0);
        if(string1.equals("INNO_SETUP_EXE"))
            Configuracoes.strInnoSetup = attributes.getValue(0);
    }

    @Override
    public void endElement(String string, String string0, String string1) throws SAXException {
    }

}

/**
 * Classe usada para parsear o arquivo de updates, que estah em XML.
 * Ela eh composta de callbacks que sao chamadas pelo parser XML
 * toda vez que um evento de parsing acontece, tal como o fechamento
 * de uma tag de um elemento.
 *
 */
class ManipuladorParsingXMLDeUpdates extends DefaultHandler{

    
    /** Construtor default. */
    public ManipuladorParsingXMLDeUpdates() {
    }

    @Override
    public void characters(char[] c, int i, int i0) throws SAXException {
    }

    @Override
    public void startElement(String string, String string0, String string1, Attributes attributes) throws SAXException {
        if(string1.equals("TIMESTAMP_ULTIMA_ATUALIZ_LIMITES"))
            Configuracoes.strTimeStampUltAtualizLimites = attributes.getValue(0);

        if(string1.equals("HASH_LIMITES"))
            Configuracoes.strMD5Limites = attributes.getValue(0);
    }

    @Override
    public void endElement(String string, String string0, String string1) throws SAXException {
    }
}