/*
 * Util.java
 *
 * Criada em February 18, 2010, 2:33 PM
 *
 */

package br.org.tracksource.gerakittsuite;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.net.ssl.HttpsURLConnection;

/**
 * Alguns metodos estaticos para fazer processamento generico, nao relacionado diretamente com o trabalho com os mapas.
 *
 * @author ur5m
 */
public class Util {
    
    private static String passfrase="TrackSourceKeys";
    
    /** Creates a new instance of Util */
    public Util() {
    }
    
    
    /**
     * Retorna a extensao do nome de um arquivo, sem o ponto e em letras minusculas, ex.: txt.
     * Se o nome nao tiver extensao ou por alguma outra razao ela nao puder ter sido encontrada na String do nome do File
     * passado como parâmetro, entao eh retornada uma String vazia.
     */  
    public static String getExtension(File f) {
        String ext = "";
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
        /**
     * Retorna a data/hora de atualizacao de um arquivo
     * Se arquivo não existir, retorna null
     */
    public static String getFileLastDateTimeUpdate(String arquivo){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        File f = new File (arquivo);
        if(f.exists())
            return dateFormat.format(f.lastModified());
        else
            return null;
    }

    /**
     * Retorna o nome de um arquivo sem a extensão, ex.: receita_de_bolo de receita_de_bolo.txt.
     * Se o nome nao tiver extensao ou por alguma outra razao ela nao puder ter sido encontrada na String do nome do File
     * passado como parâmetro, entao eh retornado exatamente o nome do arquivo.
     */
    public static String getNomeSemExtensao(File f) {
        String ext = f.getName();
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length()) {
            ext = s.substring(0, i);
        }
        return ext;
    }
    
    /**
     * break a path down into individual elements and add to a list.
     * example : if a path is /a/b/c/d.txt, the breakdown will be [d.txt,c,b,a]
     * @param f input file
     * @return a List collection with the individual elements of the path in reverse order
     */
    private static List getPathList(File f) {
        List l = new ArrayList();
        File r;
        try {
            r = f.getCanonicalFile();
            while(r != null) {
                if(r.isDirectory() && r.getName().isEmpty() && r.getParentFile() == null)
                    l.add(r.getPath()); //problema de getName() em caminhos como C:\ vir em branco
                else
                    l.add(r.getName());
                r = r.getParentFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            l = null;
        }
        return l;
    }
    
    /**
     * figure out a string representing the relative path of
     * 'f' with respect to 'r'
     * @param r home path
     * @param f path of file
     */
    private static String matchPathLists(List r,List f) {
        int i;
        int j;
        String s;
        // start at the beginning of the lists
        // iterate while both lists are equal
        s = "";
        i = r.size()-1;
        j = f.size()-1;
        
        // first eliminate common root
        while((i >= 0)&&(j >= 0)&&(r.get(i).equals(f.get(j)))) {
            i--;
            j--;
        }
        
        // for each remaining level in the home path, add a ..
        for(;i>=0;i--) {
            s += ".." + File.separator;
        }
        
        // for each level in the file path, add the path
        for(;j>=1;j--) {
            s += f.get(j) + File.separator;
        }
        
        // file name
        s += f.get(j);
        return s;
    }
    
    /**
     * get relative path of File 'f' with respect to 'home' directory
     * example : home = /a/b/c
     *           f    = /a/d/e/x.txt
     *           s = getRelativePath(home,f) = ../../d/e/x.txt
     * @param home base path, should be a directory, not a file, or it doesn't make sense
     * @param f file to generate path for
     * @return path from home to f as a string
     */
    public static String getRelativePath(File home,File f){
        File r;
        List homelist;
        List filelist;
        String s;
        
        homelist = getPathList(home);
        filelist = getPathList(f);
        s = matchPathLists(homelist,filelist);
        
        return s;
    }

    /**
     *Retorna na string o rastro da pilha de chamadas que geraram o erro passado por par?metro.
     */
    public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

    /**
     * Retorna o MD5 (em hexadecimal, sem hifens) de um arquivo cujo caminho eh passado como parametro.
     */
    public static String MD5(String caminho) throws FileNotFoundException, NoSuchAlgorithmException, IOException{
        File arquivo = new File(caminho);
        long tam_arq = 0;
        if(arquivo.exists()){
            tam_arq = arquivo.length();
        }else{
            throw new FileNotFoundException("Arquivo nao encontrado.");
        }
        byte dados_arq[] = new byte[(int)tam_arq];
        MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream is = new FileInputStream(caminho);
        try {
            is = new DigestInputStream(is, md);
            // carregar o arquivo para gerar o MD5...
            is.read(dados_arq);
        } finally {
            is.close();
        }
        byte messageDigest[] = md.digest();
        //gerar o texto hexa
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String texto = "00" + Integer.toHexString(0xFF & messageDigest[i]);
            hexString.append(texto.substring(texto.length() - 2, texto.length()));
        }
        return hexString.toString();
    }

    /**
     * Retorna o MD5 (em hexadecimal, sem hifens) de uma string passada como parametro
     */
    public static String StringToMD5(String sequencia) throws NoSuchAlgorithmException{
        byte[] sequenciaBytes = sequencia.getBytes();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(sequenciaBytes);
        byte messageDigest[] = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i=0;i<messageDigest.length;i++) {
            String texto = "00" + Integer.toHexString(0xFF & messageDigest[i]);
            hexString.append(texto.substring(texto.length() - 2, texto.length()));
        }
        return hexString.toString();
    }

    /**
     * Tenta obter um stream para download do recurso cuja URL eh
     * passada como parametro.  Proxies, senhas erradas e outros fatores
     * podem impedir a obtencao desse stream.  Em caso de falha,
     * a causa eh informada numa caixa de mensagens e o retorno
     * eh null.
     */
    public static InputStream getStreamDownload(String pURL){

        URL url;
        URLConnection urlConn;
        try {
            while(true){ //loop de tentativas de login no proxy (se o proxy requerer autenticacao
                url = new URL(pURL);
                urlConn = url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setUseCaches(false);
                if (urlConn instanceof HttpsURLConnection) {
                    HttpsURLConnection httpcon = (HttpsURLConnection) urlConn;
                    if (httpcon.getResponseCode() == 407) { //erro de autenticacao, o proxy requer login e senha
                            return null;
                    }else if(httpcon.getResponseCode() == 200){ //ok, tudo certo
                        return httpcon.getInputStream();
                    }else{ //veio outro codigo == problema imprevisto
                        JOptionPane.showMessageDialog(GeraKitTSuite.janelaPrincipal, "Servidor retornou status " + String.valueOf(httpcon.getResponseCode()) + " (" + httpcon.getResponseMessage() + ").", "Erro de HTTP", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Erro durante a tentativa de baixar " + pURL + ":\n " + Util.getStackTrace(ex));
            return null;
        }
    }
    
    /**
     * Encodificador para uso de caracteres especiais em URLs
     */
    
    public static String encode (String texto) {
        try {
            return URLEncoder.encode(texto, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            System.out.println ("Erro no encode: " + ex.getMessage());
            return null;
        }
    }

  /**
   * Returns the intersection point of two lines.
   *
   * @param   line1   First line
   * @param   line2   Second line
   * @return  The Point object where the two lines intersect. This method
   * returns null if the two lines do not intersect OR when the two lines
   * have more than one intersection point.
   */
  static public Point2D getIntersectionPoint( Line2D line1, Line2D line2 )
  {
    double dyline1, dxline1;
    double dyline2, dxline2, e, f;
    double x1line1, y1line1, x2line1, y2line1;
    double x1line2, y1line2, x2line2, y2line2;

    // line1 doesn't intersect line2
    if ( !line1.intersectsLine( line2 ) )
      return null;

    /* first, check to see if the segments intersect by parameterization
       on s and t; if s and t are both between [0,1], then the
       segments intersect */
    x1line1 = (double)line1.getX1();
    y1line1 = (double)line1.getY1();
    x2line1 = (double)line1.getX2();
    y2line1 = (double)line1.getY2();

    x1line2 = (double)line2.getX1();
    y1line2 = (double)line2.getY1();
    x2line2 = (double)line2.getX2();
    y2line2 = (double)line2.getY2();

    /* check to see if the segments have any endpoints in common. If they do,
       then return the endpoints as the intersection point */
    if ((x1line1==x1line2) && (y1line1==y1line2))
    {
      return (new Point2D.Double( x1line1, y1line1));
    }
    if ((x1line1==x2line2) && (y1line1==y2line2))
    {
      return (new Point2D.Double( x1line1, y1line1));
    }
    if ((x2line1==x1line2) && (y2line1==y1line2))
    {
      return (new Point2D.Double( x2line1, y2line1));
    }
    if ((x2line1==x2line2) && (y2line1==y2line2))
    {
      return (new Point2D.Double( x2line1, y2line1));
    }

    dyline1 = -( y2line1 - y1line1 );
    dxline1 = x2line1 - x1line1;

    dyline2 = -( y2line2 - y1line2 );
    dxline2 = x2line2 - x1line2;

    e = -(dyline1 * x1line1) - (dxline1 * y1line1);
    f = -(dyline2 * x1line2) - (dxline2 * y1line2);

    /* compute the intersection point using
      ax+by+e = 0 and cx+dy+f = 0
      If there is more than 1 intersection point between two lines,
    */
    if( (dyline1 * dxline2 - dyline2 * dxline1) == 0 ) return null;

    return (new Point2D.Double(
       (-(e * dxline2 - dxline1 * f)/(dyline1 * dxline2 - dyline2 * dxline1)),
       (-(dyline1 * f - dyline2 * e)/(dyline1 * dxline2 - dyline2 * dxline1))));
  }


    /**
     * Round a double value to a specified number of decimal
     * places.
     *
     * @param val the value to be rounded.
     * @param places the number of decimal places to round to.
     * @return val rounded to places decimal places.
     */
    public static double round(double val, int places) {
	long factor = (long)Math.pow(10,places);

	// Shift the decimal the correct number of places
	// to the right.
	val = val * factor;

	// Round to the nearest integer.
	long tmp = Math.round(val);

	// Shift the decimal the correct number of places
	// back to the left.
	return (double)tmp / factor;
    }




    /**
     * Round a float value to a specified number of decimal
     * places.
     *
     * @param val the value to be rounded.
     * @param places the number of decimal places to round to.
     * @return val rounded to places decimal places.
     */
    public static float round(float val, int places) {
        return (float)round((double)val, places);
    }

    /**
     *Cria um texto colocando o total especificado de zeros à esquerda.  Se o total de dígitos do número
     *for maior ou igual ao total de zeros, nenhum zero à esquerda será adicionado.
     */
    public static String zerosAAEsquerda(long numero, int numero_de_zeros){
        int contador;
        String texto = String.valueOf(numero);
        contador = numero_de_zeros - texto.length();
        for(int i = contador; i > 0; i--){
            texto = "0" + texto;
        }
        return texto;
    }

    /**
     * Formata uma coordenada em micrograus como texto em graus com ponto decimal.
     * Exemplos: -42123456 retorna -42.123456; 1234 retorna 0.001234.
     */
    public static String formataComoGrausComPontoDecimal(long coordenada){
        String temp = "";
        boolean ehNegativo = coordenada < 0;
        coordenada = Math.abs(coordenada);
        temp = Util.zerosAAEsquerda(coordenada, 8);
        temp = temp.substring(0, temp.length()-6) + "." + temp.substring(temp.length()-6, temp.length());
        if(ehNegativo) temp = "-" + temp;
        return temp;
    }

    //Substituido em 20101015
    // /**
    // * Retorna o string correspondente a coordenada arredondada aa sexta decimal.
    // */
    //public static String getTextoArredondamento6aCasa(double valor){
    //    long r = Math.round(valor * 1000000d);
    //    return Util.formataComoGrausComPontoDecimal(r);
    //}

    /**
     * Retorna o string correspondente a coordenada arredondada aa sexta decimal.
     */
    public static String getTextoArredondamento6aCasa(double valor){
        double result = valor;
        boolean tem_sinal = result < 0;
        if(tem_sinal)
            result = Math.abs(result);
        result = Math.round(result * 1000000);
        if(tem_sinal)
            result = -result;
        return Util.formataComoGrausComPontoDecimal((long)result);
    }



  /**
  * Colocar texto da área de transferência (operação copy).
  */
  public static void setTextoAreaTransferencia(String texto) {
    StringSelection stringSelection = new StringSelection(texto);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(stringSelection, new ClipboardOwner() {
            public void lostOwnership(Clipboard clipboard, Transferable contents) {
                ; //se quiser ser notificado desse evento, coloque o código aqui
            }
        });
  }

    /**
     * Efetua um teste de conexão a um site conhecido (ex.: Google) para
     * saber se há conexão à internet.
     */
    public static boolean isConexaoInternetOk(){

        BufferedReader br;
        String resultado = "";
        try {

            InputStream is = Util.getStreamDownload(Configuracoes.urlTesteConexao);
            if(is == null){ //se nao conseguir o stream, aborta a tentativa de ler
                return false;
            }

            //abrindo o stream para ler
            br = new BufferedReader(new InputStreamReader(is, Configuracoes.charsetNameXML2Java));
            String s;

            br.close(); //close do stream

            return true;
        } catch (Exception ex) {
            System.out.println("Erro ao testar conexão à internet:\n " + Util.getStackTrace(ex));
            return false;
        }
    }

    /**
     * Informa se os XMLs usados na validação são os mesmos que estão disponíveis no site.
     */
    public static boolean isXMLsAtualizados(){
        try {
            String md5_xml_pois_local = Util.MD5("pois.xml");
            String md5_xml_tracks_local = Util.MD5("tracks.xml");
            String md5_xml_pois_site = Configuracoes.MD5XMLpoisOficial;
            String md5_xml_tracks_site = Configuracoes.MD5XMLtracksOficial;
            if(md5_xml_pois_site != null && md5_xml_pois_local != null && md5_xml_pois_site.equals(md5_xml_pois_local)){
                if(md5_xml_tracks_site != null && md5_xml_tracks_local != null && md5_xml_tracks_site.equals(md5_xml_tracks_local)){
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            System.out.println("Erro ao verificar XMLs:\n" + Util.getStackTrace(ex));
            return false;
        }
    }

    /**
     * Informa se os sistema operacional é Windows.
     * Lista com todos os valores já coletados para os.name: http://lopica.sourceforge.net/os.html
     */
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);
    }
    /**
     * Informa se os sistema operacional é Macintosh.
     * Lista com todos os valores já coletados para os.name: http://lopica.sourceforge.net/os.html
     */
    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("mac") >= 0);
    }
    /**
     * Informa se os sistema operacional é Unix/Linux.
     * Lista com todos os valores já coletados para os.name: http://lopica.sourceforge.net/os.html
     */
    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    /**
     * Retorna um texto contendo a quantidade de bytes passada como parâmetro
     * adequadamente formatada.  Por exemplo, 1267893984 é formatado como 1GB
     */
    public static String getQuantidadeBytesFormatada(long quantidade){
        double v = 0.0;
        String unidade = "";
        String valor;
        if(quantidade >= (long)(1024 * 1024 * 1024)){
            v = quantidade / (double)(1024 * 1024 * 1024);
            unidade = "GB";
        }
        else if(quantidade >= (long) (1024 * 1024))
        {
            v = quantidade / (double)(1024 * 1024);
            unidade = "MB";
        }
        else if(quantidade >= (long) (1024))
        {
            v = quantidade / (double)(1024);
            unidade = "kB";
        }
        else
        {
            v = quantidade;
            unidade = " bytes";
        }
        valor = String.format("%.2f", v);
        return valor + unidade;
    }

    /**
     * Esta função concatena uma quantidade arbirtrária de arrays de qualquer tipo não escalar (pelo menos 2)
     * passadas como parâmetros e retorna um array do mesmo tipo.
     */
    public static <T> T[] concatenarArrays(T[] primeiro, T[]... demais) {
        int tamTotal = primeiro.length;
        for (T[] array : demais) {
            tamTotal += array.length;
        }
        T[] result = Arrays.copyOf(primeiro, tamTotal);
        int offset = primeiro.length;
        for (T[] array : demais) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    /**
     * Esta função concatena uma quantidade arbirtrária de arrays de bytes (pelo menos 2)
     * passadas como parâmetros e retorna um array de bytes.
     */
    public static byte[] concatenarArraysBytes(byte[] primeiro, byte[]... demais) {
        int tamTotal = primeiro.length;
        for (byte[] array : demais) {
            tamTotal += array.length;
        }
        byte[] result = Arrays.copyOf(primeiro, tamTotal);
        int offset = primeiro.length;
        for (byte[] array : demais) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
    
    /**
     * Verifica a existencia de um arquivo
     */
    public static boolean isArquivoExiste(String caminho_completo) {
        File f = new File(caminho_completo);
        return f.exists();
    }
    
    /**
     *Converte um inteiro BGR para RGB, a fim de compatibilizar o código de cor do TXT GTM com
     *o parâmetro do construtor da classe [code]Color[/code].
     */
    public static int converterBGRparaRGB(int c){
        int R = c & 255;
        int G = c & 65280;
        int B = c & 16711680;
        
        return (R << 16) + G + (B >> 16);
    }
    
    /**
     *Determina o ângulo orientado do vetor (1, 0) horizontal ao vetor passado como parâmetro.
     *O vetor tem origem em (xV1, yV1) com extremidade em (xV2, yV2).
     */
    public static double angulo(int xV1, int yV1, int xV2, int yV2){
        double theta = Math.acos( (xV2 - xV1) / Math.sqrt((xV2-xV1)*(xV2-xV1) + (yV2-yV1)*(yV2-yV1)) );
        if((yV2-yV1) > 0)
            return theta;
        else
            return 2 * Math.PI - theta;
    }
    
    /**
     *Determina o ângulo entre dois vetores com extremidades em (e1X, e1Y) e (e2X, e2Y) com origem em
     *comum em (oX, oY).
     */
    public static double angulo(int oX, int oY, int e1X, int e1Y, int e2X, int e2Y){
        int x1 = oX;
        int y1 = oY;
        int x2 = e1X;
        int y2 = e1Y;
        int mx = e2X;
        int my = e2Y;
        int numerador = (x2-x1)*(mx-x1)+(y2-y1)*(my-y1);
        double denominador = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)) * Math.sqrt((mx-x1)*(mx-x1)+(my-y1)*(my-y1));
        return Math.acos(numerador / denominador);
    }
    
    /**
     *Função acessória do Algoritmo de Cohen-Sutherland para clippar linhas traduzido para Java de:
     *http://www.cc.gatech.edu/grads/h/Hao-wei.Hsieh/Haowei.Hsieh/code1.html
     */
    public static int CompOutCode(int x, int y, int xmin, int xmax, int ymin, int ymax){
        final int LEFT = 8;
        final int RIGHT = 4;
        final int BOTTOM = 2;
        final int TOP = 1;
        int codigo = 0;
        if(y > ymax)
            codigo = TOP;
        else if(y < ymin)
            codigo = BOTTOM;
        if(x > xmax)
            codigo = (codigo | RIGHT);
        else if(x < xmin)
            codigo = (codigo | LEFT);
        return codigo;
    }
    
    /**
     *Calcular a distância entre os pontos.
     */
    public static int distancia(int x1, int y1, int x2, int y2){
        int dx = x2 - x1;
        int dy = y2 - y1;
        return (int)Math.sqrt(dx*dx + dy*dy);
    }

    /**
     *Medida do comprimento do arco entre dois pontos da superfície terrestre em metros calculada pela fórmula 
     * comprimento = 6378000 * acos(sen(LatNo1) * sen(LatNo2) + cos(LatNo1) * cos(LatNo2) * cos(|LonNo2 - LonNo1|)).
     * As latitudes e longitudes devem se informados em micrograus.
     *Fonte: Observatório Nacional http://obsn3.on.br/~jlkm/geopath/
     */
    public static int calcularComprimentoGeodesico(int lat1, int lon1, int lat2, int lon2){
        double PI_SOBRE_180 = Math.PI / 180000000d; //lembrar que as latitudes estão em micrograus
        double lat_no1_rad = lat1 * PI_SOBRE_180;
        double lat_no2_rad = lat2 * PI_SOBRE_180;
        double lon_no1_rad = lon1 * PI_SOBRE_180;
        double lon_no2_rad = lon2 * PI_SOBRE_180;
        return (int)(6378000 * Math.acos(Math.sin(lat_no1_rad)*Math.sin(lat_no2_rad) + Math.cos(lat_no1_rad)*Math.cos(lat_no2_rad)*Math.cos(Math.abs(lon_no1_rad-lon_no2_rad))));
    }

    /**
     *Medida do comprimento do arco calculada como o módulo do vetor (teorema de Pitágoras) entre as coordenadas informadas.
     *A vantagem desse método é o cálculo mais rápido do que o cálculo de {@link #calcularComprimentoGeodesico(int,int,int,int)},
     * o que é particularmente relevante em cálculos muito repetitivos e onde se quer ter apenas a proporção correta de comprimento entre
     * os diversos arcos, embora não se deseje obter o valor correto em metros do comprimento.
     */
    public static int calcularComprimentoCartesiano(int lat1, int lon1, int lat2, int lon2){
        int dx = lon2 - lon1;
        int dy = lat2 - lat1;
        return (int)(Math.sqrt((dx*dx)+(dy*dy)));
    }
    
    /**
    * Inverte uma string passada como parametro
    *   -- Alexandre Loss Pereira Machado (alexandre.loss@gmail.com)
    *   -- Belo Horizonte, MG - Brasil
    *   -- Criacao: 03-Mar-2010
    *   -- Ultima Atualizacao: 06-Mar-2010
    */
    public static String InverteString(String A) {
        if (A.isEmpty() || A == null) {
            return A;
        }
        else {
            int Tam = A.length();
            String Invertida = "";
            for (int I = 0; I < Tam; I++) {
                Invertida = A.charAt(I) + Invertida;
            }
            return Invertida;
        }
    }

    /**
    * Retorna uma string sem acentos
    *   -- Alexandre Loss Pereira Machado (alexandre.loss@gmail.com)
    *   -- Belo Horizonte, MG - Brasil
    *   -- Criacao: 12-Mar-2010
    *   -- Ultima Atualizacao: 13-Mar-2010
    */
    public static String semAcento (String texto){
       int i;
       char[] arrayTxt;
       arrayTxt = texto.toCharArray();
       for (i=0;i < arrayTxt.length;i++){
           switch(arrayTxt[i]){
               case 'Â':
               case 'À':
               case 'Á':
               case 'Ä':
               case 'Ã': arrayTxt[i] = 'A'; break;
               case 'â':
               case 'à':
               case 'á':
               case 'ä':
               case 'ã': arrayTxt[i] = 'a'; break;
               case 'É':
               case 'Ê':
               case 'È':
               case 'Ë': arrayTxt[i] = 'E'; break;
               case 'é':
               case 'ê':
               case 'è':
               case 'ë': arrayTxt[i] = 'e'; break;
               case 'Í':
               case 'Î':
               case 'Ì':
               case 'Ï': arrayTxt[i] = 'I'; break;
               case 'í':
               case 'î':
               case 'ì':
               case 'ï': arrayTxt[i] = 'i'; break;
               case 'Ô':
               case 'Ò':
               case 'Ó':
               case 'Ö':
               case 'Õ': arrayTxt[i] = 'O'; break;
               case 'ô':
               case 'ò':
               case 'ó':
               case 'ö':
               case 'õ': arrayTxt[i] = 'o'; break;
               case 'Ú':
               case 'Û':
               case 'Ù':
               case 'Ü': arrayTxt[i] = 'U'; break;
               case 'ú':
               case 'û':
               case 'ù':
               case 'ü': arrayTxt[i] = 'u'; break;
               case 'Ç': arrayTxt[i] = 'C'; break;
               case 'ç': arrayTxt[i] = 'c'; break;
               case 'ñ': arrayTxt[i] = 'n'; break;
               case 'ÿ': arrayTxt[i] = 'y'; break;
               case 'ý': arrayTxt[i] = 'y'; break;
               case 'Ý': arrayTxt[i] = 'Y'; break;
           }

       }
       return String.valueOf(arrayTxt);
    }

    /**
    * Verifica se uma string contém apenas dígitos
    *   -- Alexandre Loss Pereira Machado (alexandre.loss@gmail.com)
    *   -- Belo Horizonte, MG - Brasil
    *   -- Criacao: 27-Mar-2010
    *   -- Ultima Atualizacao: 27-Mar-2010
    */
    public static boolean isNumeric (String texto){
        if(texto == null)  
            return false;  
        for (char letra : texto.toCharArray())  
            if(!(letra >= '0' && letra <= '9' || letra == '.'))  
                return false;  
        return true;
    }

   /**
     * Retorna a data/hora local no formato timestamp (aaaaMMddhhmmss).
     */
    public static String getTimeStampAgora( ){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        //get current date time with Date()
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Verifica se o arquivo passado como parâmetro é um arquivo Zip
     * válido.  NÃO verifica integridade dos dados.
     */
    public static boolean isZipValid(File file) {
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(file);
            return true;
        } catch (ZipException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (zipfile != null) {
                    zipfile.close();
                    zipfile = null;
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * Retorn o MAC Addres do localhost
     * @return 
     */
    public static final String getMACAdress () {
        InetAddress ip;
	try {
 		ip = InetAddress.getLocalHost();
		//System.out.println("Current IP address : " + ip.getHostAddress());
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		byte[] mac = network.getHardwareAddress();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));		
		}
		return(sb.toString());
 
	} catch (Exception e) {
            System.out.println(e.getMessage());  
            e.printStackTrace();  
            return "Unknow Mac Address";
	}
    }

}
