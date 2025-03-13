/*
 * Trcknome1.java
 *
 * Criada em 28 de Dezembro de 2008, 10:18
 *
 */

package br.org.tracksource.gtm211api.domain;

import br.org.tracksource.gtm211api.Util;
import br.org.tracksource.gtm211api.errors.ErrorObjectNotDefined;
import br.org.tracksource.gtm211api.errors.ErrorValueOutOfRange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * The Trknome1 class represents a tracklog header, containing information such as name, color, line style, etc.
 * The tracklog description records begin following the tracklog point records of a GTM file per the 211 GTM format specification and appear
 * as many times as specified by the <code>n_tk</code> field of the <i>header</i> record.<br>
 * <br>
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class Trknome1 extends Record{
    
    /**
     *Tracklog name.
     */
    protected String tname;

    /**
     *Tracklog drawing style:<br>
     *<img src="doc-files/tracklog_styles.jpg"/>
     */
    protected byte ttype;

    /**
     *Tracklog color (RGB).  Actually BGR, for example red is 255.
     */
    protected int tcolor;

    /**
     *Maximum scale in Kilometers that the Tracklog appears on screen (Default = 0).
     */
    protected float tscale;

    /**
     *Reserved for future use.  Should be zero.
     */
    protected byte tlabel;

    /**
     *Reserved for future use.  Should be zero per the specification, but this value is known to be used to specify
     *detail level display in some applications.
     */
    protected short tlayer;

    /**
     *Classe da via, se for roteável (-1 na inicialização)
     */    
    protected byte roadclass;
    
    /**
     *Velocidade da via, se for roteável (-1 na inicialização)
     */    
    protected byte roadvelocity;
    
    /**Collection of tracklog points. */
    protected ArrayList<Trcks1> colTrcks1;

    //the MBR
    private float minLat = (float) 999.999999;
    private float minLon = (float) 999.999999;
    private float maxLat = (float) -999.999999;
    private float maxLon = (float) -999.999999;
    
    /** Used to store the value returned by #getHighLevelType() in the first time it is called.
     * On subsequent calls this value is used instead of calculating it again for performance reasons.
     */
    protected int high_level_type = -1;

    /** Static array of the tracklog icons.
     *  This array is populated lasily, by demand of #getIcon() calls in the instances.
     */
    private static ImageIcon icons[] = new ImageIcon[217];

    /** Constructor.
     * @throws ErrorObjectNotDefined
     * @param parent_map Map.
     */
    public Trknome1(Map parent_map) throws ErrorObjectNotDefined {
        super(parent_map);
        colTrcks1 = new ArrayList<Trcks1>();
        roadclass = -1;
        roadvelocity = -1;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public byte getTtype() {
        return ttype;
    }

    public void setTtype(byte ttype) throws ErrorValueOutOfRange {
        if(ttype < 1 || ttype > 9 && ttype < 17 || ttype > 32) throw new ErrorValueOutOfRange("1 to 9 or 17 to 32", String.valueOf(ttype));
        this.high_level_type = -1;
        this.ttype = ttype;
    }

    public int getTcolor() {
        return tcolor;
    }

    public void setTcolor(int tcolor) {
        this.high_level_type = -1;
        this.tcolor = tcolor;
    }

    public float getTscale() {
        return tscale;
    }

    public void setTscale(float tscale) {
        this.tscale = tscale;
    }

    public byte getTlabel() {
        return tlabel;
    }

    public void setTlabel(byte tlabel) {
        this.tlabel = tlabel;
    }

    public short getTlayer() {
        return tlayer;
    }

    public void setTlayer(short tlayer) {
        this.tlayer = tlayer;
    }
    
    /**
     * Retorna a Classe da Rodovia
     * @return roadclass
     */
    public byte getRoadClass() {
        return roadclass;
    }
    
    /**
     * Seta a Classe da Rodovia
     * @param roadclass
     */
    public void setRoadClass(byte proadclass) {
        this.roadclass=proadclass;
    }
    
    /**
     * Retorna a Velocidade da Rodovia
     * @return roadvelocity
     */
    public byte getRoadVelocity() {
        return roadvelocity;
    }
    
    /**
     * Seta a Velocidade da Rodovia
     * @param roadvelocity
     */
    public void setRoadVelocity(byte proadvelocity) {
        this.roadvelocity=proadvelocity;
    } 
        
    /* Returns the size of this Trknome1 in meters */
    public double getTsize() {
        Trcks1 pi, pf;
        double size = 0.0d;
        for(int i = 0; i < (this.colTrcks1.size()-1); i++){
            //take 2 tracks points forming an edge.
            pi = this.colTrcks1.get(i);
            pf = this.colTrcks1.get(i+1);
            size += Util.calculateGeodeticLengthSphere_m(pi.getLatitude(),pi.getLongitude(), pf.getLatitude(),pf.getLongitude());
        }
        return size;
    }

    public void writeToStream(OutputStream stream) throws IOException {
        stream.write(this.convertStringToByteArray(this.tname, true));
        stream.write(this.ttype);
        stream.write(this.convertIntTo4bytes(this.tcolor));
        stream.write(this.convertFloatTo4bytes(this.tscale));
        stream.write(this.tlabel);
        stream.write(this.convertShortTo2bytes(this.tlayer));
    }
    
    
    /**
     *Adds a {@link Trcks1} object to the {@link #colTrcks1} collection.<br>
     */
    public void addTrcks1(Trcks1 trcks1){
        this.colTrcks1.add(trcks1);
        this.expandMBR(trcks1);
    }

    /**
     *Returns the number of track points of this tracklog.
     */
    public int getTrcks1Count(){
        return this.colTrcks1.size();
    }
    
    /**
     *Returns an Iterator object for the trackpoints collection of this tracklog.
     */
    public Iterator<Trcks1> getTrackPointIterator(){
        return this.colTrcks1.iterator();
    }
    
    /**
     *Returns true if this tracklog, representing a polygon, is wound clockwise using the area calculation method (negative area
     * values result from clockwise polygons).  Result is unpredictable if the tracklog
     *is not a closed polygon.
     *@see #isClosedPoly()
     */
    public boolean isClockwise(){
        Trcks1 pi, pf;
        double area = 0.0d;
        for(int i = 0; i < (this.colTrcks1.size()-1); i++){
            //take 2 tracks points forming an edge.
            pi = this.colTrcks1.get(i);
            pf = this.colTrcks1.get(i+1);
            area += ((pi.getLongitude() * pf.getLatitude()) - (pf.getLongitude() * pi.getLatitude()));
        }
        return area < 0.0d; //the area value is actually abs(area)/2
    }
    
    /**
     *Inverts the tracklog's track points order.
     */
    public void invertTrackPointsOrder(){
        ArrayList<Trcks1> newColTrcks1 = new ArrayList<Trcks1>();
        for(int i = (this.colTrcks1.size()-1); i >= 0; i--)
            newColTrcks1.add(this.colTrcks1.get(i));
        this.colTrcks1.clear();
        this.colTrcks1 = newColTrcks1;
    }
    
    /**
     * Informs whether the track is a yellow map background or not.
     */
    public boolean isYellowMapBackground(){
        return (this.getTtype() == 17) && (this.getTcolor() == (255 | (255 << 8) | (230 << 16)));
    }


    /**
     * Informs whether the track is an international border.
     */
    public boolean isInternationalBorder(){
        return (this.getTtype() == 5) && (this.getTcolor() == (255 | (255 << 8)));
    }


    /**
     * Informs whether the track is a state border.
     */
    public boolean isStateBorder(){
        return (this.getTtype() == 5) && (this.getTcolor() == (128 | (255 << 8) | (255 << 16)));
    }

    /**
     * Informs whether the track is a municipality border.
     */
    public boolean isMunicipalityBorder(){
        return (this.getTtype() == 4) && (this.getTcolor() == (128 | (128 << 8) | (192 << 16)));
    }

    /**
     * Informs whether the track is a map green border.
     */
    public boolean isMapGreenBorder(){
        return (this.getTtype() == 5) && (this.getTcolor() == 8454016);
    }

    /**
     * Informs whether the track is a map yellow border.
     */
    public boolean isMapYellowBorder(){
        return this.getHighLevelType() == Trknome1.TYPE_INTERNATIONAL_BORDER;
    }

    /**
     * Informas whether the track belongs to a road type.
     */
    public boolean isRoad(){
        int type = this.getHighLevelType();
        return type == Trknome1.TYPE_SIMPLE_ROAD ||
               type == Trknome1.TYPE_DUPLICATED_ROAD ||
               type == Trknome1.TYPE_MAIN_DIRTY_ROAD;
    }

    /**
     * Informs whether the track is eligible to be included in Remaped maps.
     * A "Remaped map" is a map generated by REMAP tool
     * All kind of tracks are allowed (return = True), EXCEPT (return = False):
     *   - Municipality Border
     *   - State Border
     *   - International Border
     *   - Yellow Map Background
     */
    public boolean isREMAPable(){
        return (!(this.isMunicipalityBorder()  ||
                  this.isStateBorder()         ||
                  this.isInternationalBorder() ||
                  this.isYellowMapBackground()));
    }

    /**
     * Informs whether the track has a filled polygon style.  It can be solid or pattern filled.
     */
    public boolean isFilledPolygon(){
        return this.getTtype() >= 17;
    }
    
    /**
     * Informs whether the track forms a closed polygonal line or the starting Trcks1 object of #colTrcks1
     * has the same coordinates of its last one.  Of course, if the track has less than two Trcks1, this
     * method returns false.
     */
    public boolean isClosedPoly(){
        if(this.colTrcks1.size() < 2)
            return false;
        Trcks1 startingPoint = this.colTrcks1.get(0);
        Trcks1 endingPoint = this.colTrcks1.get(this.colTrcks1.size()-1);
        return startingPoint.coincidesWith(endingPoint);
    }

    public boolean isGeographic() {
        return true;
    }

    /**
     * Informs if this track is a Rodovia Principal Azul
     */
    public boolean isTypeRodoviaPrincipalAzul(){
        return this.getTtype() == 3 && this.getTcolor() == 16711680;
    }
    
    /**
     * Informs if this track is a Rodovia Principal Vermelha
     */
    public boolean isTypeRodoviaPrincipalVermelha(){
        return this.getTtype() == 3 && this.getTcolor() == 255;                           
    }
    
    /**
     * Informs if this track is a Avenida Pavimentada Principal
     */
    public boolean isTypeAvenidaPavimentadaPrincipal(){
        return this.getTtype() == 4 && this.getTcolor() == 16711680;
    }
    
    /**
     * Informs if this track is a Pista Dupla Pavimentada
     */
    public boolean isTypePistaDuplaPavimentada(){
        return this.getTtype() == 2 && this.getTcolor() == 255;
    }
    
     /**
     * Informs if this track is a Rua Pavimentada Principal
     */
    public boolean isTypeRuaPavimentadaPrincipal(){
        return this.getTtype() == 2 && this.getTcolor() == 0;
    }
    
     /**
     * Informs if this track is a Via de Acesso
     */
    public boolean isTypeViaAcesso(){
        return this.getTtype() == 2 && this.getTcolor() == 8421504;
    }
    
     /**
     * Informs if this track is a Estrada de Terra Principal
     */
    public boolean isTypeEstradaTerraPrincipal(){
        return this.getTtype() == 2 && this.getTcolor() == 128;
    }
    
    /**
     * Informs if this track is a Rua Pavimentada Secundaria
     */
    public boolean isTypeRuaPavimentadaSecundaria(){
        return this.getTtype() == 1 && this.getTcolor() == 0;
    }
    
    /**
     * Informs if this track is a Estrada de Terra
     */
    public boolean isTypeEstradaTerra(){
        return this.getTtype() == 1 && this.getTcolor() == 128;
    }
    
    /**
     * Informs if this track is a Trilha 4x4
     */
    public boolean isTypeTrilha4x4(){
        return this.getTtype() == 1 && this.getTcolor() == 32896;
    }
    
    /**
     * Informs if this track is a Trilha Caminhada/Bicicleta
     */
    public boolean isTypeTrilhaCaminhadaBicicleta(){
        return this.getTtype() == 1 && this.getTcolor() == 4227327;
    }
    
    /**
     * Informs if this track is a Trilha de Moto
     */
    public boolean isTypeTrilhaMoto(){
        return this.getTtype() == 8 && this.getTcolor() == 32896;
    }
    
    /**
     * Informs if this track is a Linha Maritma
     */
    public boolean isTypeLinhaMaritma(){
        return this.getTtype() == 7 && this.getTcolor() == 0;
    }
    
    /**
     * Informs if this track is a Class A, each means it is one of the following kind of track:
     *   - Rodovia Principal Azul
     *   - Rodovia Principal Vermelha
     *   - Avenida Pavimentada Principal
     *   - Pista Dupla Pavimentada
     */
    public boolean isTrackClassA(){
        return this.isTypeRodoviaPrincipalAzul()  ||  
               this.isTypeRodoviaPrincipalVermelha() ||
               this.isTypeAvenidaPavimentadaPrincipal () ||
               this.isTypePistaDuplaPavimentada();
    }
    
     /**
     * Informs if this track is a Class B, each means it is one of the following kind of track:
     *   - Rua Pavimentada Principal
     *   - Via de Acesso
     *   - Estrada de Terra Principal
     */
    public boolean isTrackClassB(){
        return this.isTypeRuaPavimentadaPrincipal() ||
               this.isTypeViaAcesso() ||
               this.isTypeEstradaTerraPrincipal();
               
    }

    /**
     * Informs if this track is a Class C, each means it is one of the following kind of track:
     *   - Rua Pavimentada Secundária
     *   - Estrada de Terra
     *   - Trilha 4x4
     *   - Trilha de Moto
     *   - Trilha Caminhada/Bicicleta
     *   - Linha Maritma
     */
    public boolean isTrackClassC(){
        return this.isTypeRuaPavimentadaSecundaria() ||
               this.isTypeEstradaTerra() ||
               this.isTypeTrilha4x4() ||
               this.isTypeTrilhaCaminhadaBicicleta() ||
               this.isTypeTrilhaMoto() ||
               this.isTypeLinhaMaritma();
    }
    
    /**
     * Returns the latitudes of each vertex as an array of doubles.
     */
    public double[] getArrayOfLatitudes(){
        double r[] = new double[this.colTrcks1.size()];
        for(int i = 0; i < this.colTrcks1.size(); i++)
            r[i] = this.colTrcks1.get(i).getLatitude();
        return r;
    }

    /**
     * Returns the longitudes of each vertex as an array of doubles.
     */
    public double[] getArrayOfLongitudes(){
        double r[] = new double[this.colTrcks1.size()];
        for(int i = 0; i < this.colTrcks1.size(); i++)
            r[i] = this.colTrcks1.get(i).getLongitude();
        return r;
    }
    
    /**
     *  Returns a tracklog point given its index.
     */
    public Trcks1 getTracklogPoint(int i){
        return this.colTrcks1.get(i);
    }

    /**
     * Returns the graphic icon used to represent this tracklog style in dialogs.
     * This is a convenient method that wraps a call to the #getIconGraphic(int) class method.
     */
    public Icon getIconGraphic(){
        return Trknome1.getIconGraphic(this.getHighLevelType());
    }

    /**
     * Returns the graphic icon used to represent the given tracklog style code in dialogs.
     * The stored ImageIcon objects have their Description property set to T+type_code, such as T9, meaning
     * that the icon refer to a tracklog style icon whose code is 9.
     */
    public static ImageIcon getIconGraphic(int high_level_type_code){
        //lasy loading of the tracklog icons
        if(Trknome1.icons[high_level_type_code] == null){
            URL imgURL = Trknome1.class.getResource("tracklog_icons/" + String.valueOf(high_level_type_code) + ".png");
            if(imgURL == null)
                imgURL = Trknome1.class.getResource("tracklog_icons/unknown.png");
            Trknome1.icons[high_level_type_code] = new ImageIcon(imgURL);
            Trknome1.icons[high_level_type_code].setDescription("T" + String.valueOf(high_level_type_code));
        }
        return Trknome1.icons[high_level_type_code];
    }

    /**
     * Returns the string of name of the the high level type identified by the parameter.
     * If the combination of color and line style does not match a high level type
     * The text "generic track" is returned.
     */
    public static String getHighLevelTypeName(int high_level_type_code){
        switch(high_level_type_code){
            case Trknome1.TYPE_SECONDARY_STREET: return "Rua Pavimentada Secundária";
            case Trknome1.TYPE_MAIN_STREET: return "Rua Pavimentada Principal.";
            case Trknome1.TYPE_ACCESS_WAY: return "Via de Acesso";
            case Trknome1.TYPE_DUPLICATED_ROAD: return "Rodovia Principal Azul";
            case Trknome1.TYPE_SIMPLE_ROAD: return "Rodovia Principal Vermelha";
            case Trknome1.TYPE_HIGHWAY: return "Avenida Pavimentada Principal";
            case Trknome1.TYPE_AVENUE: return "Pista Dupla Pavimentada";
            case Trknome1.TYPE_MAIN_DIRTY_ROAD: return "Estrada de Terra Principal";
            case Trknome1.TYPE_SECONDARY_DIRTY_ROAD: return "Estrada de Terra Secundária";
            case Trknome1.TYPE_OFFROAD_TRACK: return "Trilha 4x4";
            case Trknome1.TYPE_MOUNTAINBIKE_TRACK: return "Trilha de Moto/Mountainbike";
            case Trknome1.TYPE_PEDESTRIAN_STREET: return "Trilha de Caminhada/Bicicleta";
            case Trknome1.TYPE_FERRY_BOAT_LINE: return "Linha Marítima";

            //non-routable line types
            case Trknome1.TYPE_INTERNATIONAL_BORDER: return "Fronteira Internacionaal";
            case Trknome1.TYPE_STATE_BORDER: return "Divisa Estadual";
            case Trknome1.TYPE_MUNICIPALITY_BORDER: return "Limite Municipal";
            case Trknome1.TYPE_RAILROAD: return "Linha Férrea";
            case Trknome1.TYPE_CARGO_RAILROAD: return "Linha Férrea de Carga";
            case Trknome1.TYPE_TOURISTIC_RAILROAD: return "Linha Férrea Turística";
            case Trknome1.TYPE_LARGE_RIVER: return "Grande Rio";
            case Trknome1.TYPE_RIVER_OR_CANAL: return "Rio/Canal";
            case Trknome1.TYPE_INTERMITTENT_RIVER: return "Rio Intermitente";

            //filled polygon types
            case Trknome1.TYPE_FLOODED_AREA_NO_OUTLINE: return "Superfícies Alagadas";
            case Trknome1.TYPE_AIRPORT_AREA_NO_OUTLINE: return "Aeroporto";
            case Trknome1.TYPE_MILITARY_AREA_NO_OUTLINE: return "Área Militar";
            case Trknome1.TYPE_BUILDING_NO_OUTLINE: return "Construções";
            case Trknome1.TYPE_CEMETERY_NO_OUTLINE: return "Cemitério";
            case Trknome1.TYPE_URBAN_AREA: return "Área Urbana";
            case Trknome1.TYPE_SWAMP_AREA: return "Área de Pântano";
            case Trknome1.TYPE_MAP_YELLOW_BACKGROUND_NO_OUTLINE: return "Fundo de Mapa Amarelo";
            case Trknome1.TYPE_MOUNTAIN_RANGE: return "Região Montanhosa";
            case Trknome1.TYPE_WATER_BODY_II_NO_OUTLINE: return "Hidrografia II";
            case Trknome1.TYPE_URBAN_PARK_NO_OUTLINE: return "Parque Urbano";
            case Trknome1.TYPE_WATER_BODY_I_NO_OUTLINE: return "Hidrografia I";
            case Trknome1.TYPE_STATE_PARK_NO_OUTLINE: return "Parque Nacional/Estadual";
            case Trknome1.TYPE_DESERT: return "Região Desértica";
            case Trknome1.TYPE_GLACIER_OR_SALT_LAKE_NO_OUTLINE: return "Salar/Glaciar";
            case Trknome1.TYPE_EXPOSED_EARTH_NO_OUTLINE: return "Superfícies de Terra";

            case Trknome1.TYPE_UNINDENTIFIED_TRACK_TYPE: return "Track Genérico";
            default: return "Tipo nao identificado";
        }
    }

    /**
     * Sets #Tcolor and #Type accordingly to the high level type passed as parameter
     * since the high level type is not an actual field of a tracklog.
     * Note: #TYPE_UNINDENTIFIED_TRACK_TYPE defaults to #Tcolor = black and
     * #Ttype = solid thin line, equivalent to Secondary Street
     */
    public void setHighLevelType(int high_level_type_code){
        try{
        switch(high_level_type_code){
            case Trknome1.TYPE_SECONDARY_STREET:
                this.setTcolor(0);
                this.setTtype((byte)1);
                break;
            case Trknome1.TYPE_MAIN_STREET:
                this.setTcolor(0);
                this.setTtype((byte)2);
                break;
            case Trknome1.TYPE_ACCESS_WAY:
                this.setTcolor(8421504);
                this.setTtype((byte)2);
                break;
            case Trknome1.TYPE_DUPLICATED_ROAD:
                this.setTcolor(16711680);
                this.setTtype((byte)3);
                break;
            case Trknome1.TYPE_SIMPLE_ROAD:
                this.setTcolor(255);
                this.setTtype((byte)3);
                break;
            case Trknome1.TYPE_HIGHWAY:
                this.setTcolor(16711680);
                this.setTtype((byte)4);
                break;
            case Trknome1.TYPE_AVENUE:
                this.setTcolor(255);
                this.setTtype((byte)2);
                break;
            case Trknome1.TYPE_MAIN_DIRTY_ROAD:
                this.setTcolor(128);
                this.setTtype((byte)2);
                break;
            case Trknome1.TYPE_SECONDARY_DIRTY_ROAD:
                this.setTcolor(128);
                this.setTtype((byte)1);
                break;
            case Trknome1.TYPE_OFFROAD_TRACK:
                this.setTcolor(32896);
                this.setTtype((byte)1);
                break;
            case Trknome1.TYPE_MOUNTAINBIKE_TRACK:
                this.setTcolor(32896);
                this.setTtype((byte)8);
                break;
            case Trknome1.TYPE_PEDESTRIAN_STREET:
                this.setTcolor(4227327);
                this.setTtype((byte)1);
                break;
            case Trknome1.TYPE_FERRY_BOAT_LINE:
                this.setTcolor(0);
                this.setTtype((byte)7);
                break;

            //non-routable line types
            case Trknome1.TYPE_INTERNATIONAL_BORDER:
                this.setTcolor(65535);
                this.setTtype((byte)5);
                break;
            case Trknome1.TYPE_STATE_BORDER:
                this.setTcolor(16777088);
                this.setTtype((byte)5);
                break;
            case Trknome1.TYPE_MUNICIPALITY_BORDER:
                this.setTcolor(12615808);
                this.setTtype((byte)4);
                break;
            case Trknome1.TYPE_RAILROAD:
                this.setTcolor(0);
                this.setTtype((byte)6);
                break;
            case Trknome1.TYPE_CARGO_RAILROAD:
                this.setTcolor(8421440);
                this.setTtype((byte)6);
                break;
            case Trknome1.TYPE_TOURISTIC_RAILROAD:
                this.setTcolor(16711808);
                this.setTtype((byte)6);
                break;
            case Trknome1.TYPE_LARGE_RIVER:
                this.setTcolor(16744448);
                this.setTtype((byte)2);
                break;
            case Trknome1.TYPE_RIVER_OR_CANAL:
                this.setTcolor(16762880);
                this.setTtype((byte)1);
                break;
            case Trknome1.TYPE_INTERMITTENT_RIVER:
                this.setTcolor(16107520);
                this.setTtype((byte)7);
                break;

            //filled polygon types
            case Trknome1.TYPE_FLOODED_AREA_NO_OUTLINE:
                this.setTcolor(16107520);
                this.setTtype((byte)31);
                break;
            case Trknome1.TYPE_AIRPORT_AREA_NO_OUTLINE:
                this.setTcolor(15724527);
                this.setTtype((byte)17);
                break;
            case Trknome1.TYPE_MILITARY_AREA_NO_OUTLINE:
                this.setTcolor(8421440);
                this.setTtype((byte)17);
                break;
            case Trknome1.TYPE_BUILDING_NO_OUTLINE:
                this.setTcolor(9338481);
                this.setTtype((byte)17);
                break;
            case Trknome1.TYPE_CEMETERY_NO_OUTLINE:
                this.setTcolor(10928895);
                this.setTtype((byte)17);
                break;
            case Trknome1.TYPE_URBAN_AREA:
                this.setTcolor(12566463);
                this.setTtype((byte)17);
                break;
            case Trknome1.TYPE_SWAMP_AREA:
                this.setTcolor(6592255);
                this.setTtype((byte)17);
                break;
            case Trknome1.TYPE_MAP_YELLOW_BACKGROUND_NO_OUTLINE:
                this.setTcolor(15138815);
                this.setTtype((byte)17);
                break;
            case Trknome1.TYPE_MOUNTAIN_RANGE:
                this.setTcolor(6718103);
                this.setTtype((byte)21);
                break;
            case Trknome1.TYPE_WATER_BODY_II_NO_OUTLINE:
                this.setTcolor(16762880);
                this.setTtype((byte)17);
                break;
            case Trknome1.TYPE_URBAN_PARK_NO_OUTLINE:
                this.setTcolor(46848);
                this.setTtype((byte)17);
                break;
            case Trknome1.TYPE_WATER_BODY_I_NO_OUTLINE:
                this.setTcolor(16744448);
                this.setTtype((byte)17);
                break;
            case Trknome1.TYPE_STATE_PARK_NO_OUTLINE:
                this.setTcolor(10551200);
                this.setTtype((byte)17);
                break;
            case Trknome1.TYPE_DESERT:
                this.setTcolor(12503270);
                this.setTtype((byte)17);
                break;
            case Trknome1.TYPE_GLACIER_OR_SALT_LAKE_NO_OUTLINE:
                this.setTcolor(15321034);
                this.setTtype((byte)23);
                break;
            case Trknome1.TYPE_EXPOSED_EARTH_NO_OUTLINE:
                this.setTcolor(10485759);
                this.setTtype((byte)17);
                break;
            case Trknome1.TYPE_UNINDENTIFIED_TRACK_TYPE:
                this.setTcolor(0);
                this.setTtype((byte)1);
                break;
            default: return ;
        }
        }catch(Exception ex){ //exceptions are not expected under non-user accessible execution
        }
    }


    /**
     * Returns the high level track type.  See TYPE_* constants.
     * This information is not directly stored in the GTM file but derived from the combination of the color and type fields.
     */
    public int getHighLevelType(){
        if(this.high_level_type != -1) return this.high_level_type;

        //routable line types
        if(this.getTcolor() == 0 && this.getTtype() == 1)
            return this.high_level_type = Trknome1.TYPE_SECONDARY_STREET;
        if(this.getTcolor() == 0 && this.getTtype() == 2)
            return this.high_level_type = Trknome1.TYPE_MAIN_STREET;
        if(this.getTcolor() == 8421504 && this.getTtype() == 2)
            return this.high_level_type = Trknome1.TYPE_ACCESS_WAY;
        if(this.getTcolor() == 16711680 && this.getTtype() == 3)
            return this.high_level_type = Trknome1.TYPE_DUPLICATED_ROAD;
        if(this.getTcolor() == 255 && this.getTtype() == 3)
            return this.high_level_type = Trknome1.TYPE_SIMPLE_ROAD;
        if(this.getTcolor() == 16711680 && this.getTtype() == 4)
            return this.high_level_type = Trknome1.TYPE_HIGHWAY;
        if(this.getTcolor() == 255 && this.getTtype() == 2)
            return this.high_level_type = Trknome1.TYPE_AVENUE;
        if(this.getTcolor() == 128 && this.getTtype() == 2)
            return this.high_level_type = Trknome1.TYPE_MAIN_DIRTY_ROAD;
        if(this.getTcolor() == 128 && this.getTtype() == 1)
            return this.high_level_type = Trknome1.TYPE_SECONDARY_DIRTY_ROAD;
        if(this.getTcolor() == 32896 && this.getTtype() == 1)
            return this.high_level_type = Trknome1.TYPE_OFFROAD_TRACK;
        if(this.getTcolor() == 32896 && this.getTtype() == 8)
            return this.high_level_type = Trknome1.TYPE_MOUNTAINBIKE_TRACK;
        if(this.getTcolor() == 4227327 && this.getTtype() == 1)
            return this.high_level_type = Trknome1.TYPE_PEDESTRIAN_STREET;
        if(this.getTcolor() == 0 && this.getTtype() == 7)
            return this.high_level_type = Trknome1.TYPE_FERRY_BOAT_LINE;

        //non-routable line types
        if(this.getTcolor() == 65535 && this.getTtype() == 5)
            return this.high_level_type = Trknome1.TYPE_INTERNATIONAL_BORDER;
        if(this.getTcolor() == 16777088 && this.getTtype() == 5)
            return this.high_level_type = Trknome1.TYPE_STATE_BORDER;
        if(this.getTcolor() == 12615808 && this.getTtype() == 4)
            return this.high_level_type = Trknome1.TYPE_MUNICIPALITY_BORDER;
        if(this.getTcolor() == 0 && this.getTtype() == 6)
            return this.high_level_type = Trknome1.TYPE_RAILROAD;
        if(this.getTcolor() == 8421440 && this.getTtype() == 6)
            return this.high_level_type = Trknome1.TYPE_CARGO_RAILROAD;
        if(this.getTcolor() == 16711808 && this.getTtype() == 6)
            return this.high_level_type = Trknome1.TYPE_TOURISTIC_RAILROAD;
        if(this.getTcolor() == 16744448 && this.getTtype() == 2)
            return this.high_level_type = Trknome1.TYPE_LARGE_RIVER;
        if(this.getTcolor() == 16762880 && this.getTtype() == 1)
            return this.high_level_type = Trknome1.TYPE_RIVER_OR_CANAL;
        if(this.getTcolor() == 16107520 && this.getTtype() == 7)
            return this.high_level_type = Trknome1.TYPE_INTERMITTENT_RIVER;
        
        //filled polygon types
        if(this.getTcolor() == 16107520 && this.getTtype() == 31)
            return this.high_level_type = Trknome1.TYPE_FLOODED_AREA_NO_OUTLINE;
        if(this.getTcolor() == 15724527 && this.getTtype() == 17)
            return this.high_level_type = Trknome1.TYPE_AIRPORT_AREA_NO_OUTLINE;
        if(this.getTcolor() == 8421440 && this.getTtype() == 17)
            return this.high_level_type = Trknome1.TYPE_MILITARY_AREA_NO_OUTLINE;
        if(this.getTcolor() == 9338481 && this.getTtype() == 17)
            return this.high_level_type = Trknome1.TYPE_BUILDING_NO_OUTLINE;
        if(this.getTcolor() == 10928895 && this.getTtype() == 17)
            return this.high_level_type = Trknome1.TYPE_CEMETERY_NO_OUTLINE;
        if(this.getTcolor() == 12566463 && this.getTtype() == 17)
            return this.high_level_type = Trknome1.TYPE_URBAN_AREA;
        if(this.getTcolor() == 6592255 && this.getTtype() == 17)
            return this.high_level_type = Trknome1.TYPE_SWAMP_AREA;
        if(this.getTcolor() == 15138815 && this.getTtype() == 17)
            return this.high_level_type = Trknome1.TYPE_MAP_YELLOW_BACKGROUND_NO_OUTLINE;
        if(this.getTcolor() == 6718103 && this.getTtype() == 21)
            return this.high_level_type = Trknome1.TYPE_MOUNTAIN_RANGE;
        if(this.getTcolor() == 16762880 && this.getTtype() == 17)
            return this.high_level_type = Trknome1.TYPE_WATER_BODY_II_NO_OUTLINE;
        if(this.getTcolor() == 46848 && this.getTtype() == 17)
            return this.high_level_type = Trknome1.TYPE_URBAN_PARK_NO_OUTLINE;
        if(this.getTcolor() == 16744448 && this.getTtype() == 17)
            return this.high_level_type = Trknome1.TYPE_WATER_BODY_I_NO_OUTLINE;
        if(this.getTcolor() == 10551200 && this.getTtype() == 17)
            return this.high_level_type = Trknome1.TYPE_STATE_PARK_NO_OUTLINE;
        if(this.getTcolor() == 12503270 && this.getTtype() == 17)
            return this.high_level_type = Trknome1.TYPE_DESERT;
        if(this.getTcolor() == 15321034 && this.getTtype() == 23)
            return this.high_level_type = Trknome1.TYPE_GLACIER_OR_SALT_LAKE_NO_OUTLINE;
        if(this.getTcolor() == 10485759 && this.getTtype() == 17)
            return this.high_level_type = Trknome1.TYPE_EXPOSED_EARTH_NO_OUTLINE;

        return this.high_level_type = Trknome1.TYPE_UNINDENTIFIED_TRACK_TYPE;
    }

    /**
     * Returns whether this tracklog represents a road, strees, passway, or any
     * type of path used by motor vehicles, bicicles, pedestrians and ferry boats.
     *
     */
    public boolean isRoutable(){
        switch(this.getHighLevelType()){
            case Trknome1.TYPE_ACCESS_WAY:
            case Trknome1.TYPE_AVENUE:
            case Trknome1.TYPE_DUPLICATED_ROAD:
            case Trknome1.TYPE_FERRY_BOAT_LINE:
            case Trknome1.TYPE_HIGHWAY:
            case Trknome1.TYPE_MAIN_DIRTY_ROAD:
            case Trknome1.TYPE_MAIN_STREET:
            case Trknome1.TYPE_MOUNTAINBIKE_TRACK:
            case Trknome1.TYPE_OFFROAD_TRACK:
            case Trknome1.TYPE_PEDESTRIAN_STREET:
            case Trknome1.TYPE_SECONDARY_DIRTY_ROAD:
            case Trknome1.TYPE_SECONDARY_STREET:
            case Trknome1.TYPE_SIMPLE_ROAD:
                return true;
            default:
                return false;
        }
    }

    /**
     * Returns whether this tracklog represents some kind of border, or either
     * #isMunicipalityBorder(), #isStateBorder or isInternationalBorder() return true.
     */
    public boolean isSomeKindOfBorder(){
        return this.isMunicipalityBorder() || this.isStateBorder() || this.isInternationalBorder();
    }
    
    /**
     * Removes the track point given its index.
     * The index must be between zero and the value returned by #getTrcks1Count() less 1.
     */
    public void removeTrackPoint(int i){
        this.colTrcks1.remove(i);
    }

    /**
     * Returns a new tracklog object that is a simplification (has fewer trackpoints) of
     * this tracklog. The simplified polygon is also guaranteed to have all its edges inside
     * this polygon as well as having all its track points in common with this polygon.
     * If this tracklog is not a polygon, unpredictable behavior may occur.
     * Call #isClosedPoly() first to make sure.  The new tracklog shares the Trcks1 references with this tracklog.
     * The new tracklog is NOT added to this tracklog parent Map object tracklog collection, although the new tracklog inherits the parent Map reference.
     * The new tracklog inherits all properties of this tracklog such as style, name, etc, including the parent Map reference.
     * @param step Sets the skip steps of neighbouring track points that can be removed per iteration in order to keep the simplified polygon as similar as
     * possible to this polygon. The lower this number the less similar is the simplified polygon, but target is reached faster.  The greater
     * this parameter, the better the distribution of deleted points but target takes longer to reach.
     * @param target Sets the targeted number of tracklog points in the simplified polygon.  The algorithm stops when this number is reached,
     * even though further simplification is possible.  Specify 0 for target to return the simplest polygon possible. If target
     * numer is greater than or equal the total number of points of this tracklog, then no simplification occurs and the returned tracklog
     * is just a copy of this tracklog sharing the same track point references. The algorithm may stop before target is reached.
     * @see #isClosedPoly()
     */
    public Trknome1 getSimplifiedInscribedPolygon(int step, int target) throws ErrorObjectNotDefined, ErrorValueOutOfRange{
        //the three points forming an angle.  The middle point, triplet[1], is the vertex of the angle.
        Trcks1 triplet[] = new Trcks1[3];

        //creating a new tracklog header inheriting the parent map reference of this tracklog
        Trknome1 ntk = new Trknome1(this.parentMapReference);
        
        //copying properties
        ntk.setTcolor(this.getTcolor());
        ntk.setTlabel(this.getTlabel());
        ntk.setTlayer(this.getTlayer());
        ntk.setTname(this.getTname());
        ntk.setTscale(this.getTscale());
        ntk.setTtype(this.getTtype());
        
        //populating the track point collection
        Iterator<Trcks1> itkp = this.colTrcks1.iterator();
        while(itkp.hasNext())
            ntk.addTrcks1(itkp.next());

        boolean is_clockwise = ntk.isClockwise();
        
        /////simplification loop (stops if target number is reached)
        while(ntk.getTrcks1Count() > target){
            boolean a_simplification_occurred = false;
            for (int i = 1; i < ntk.getTrcks1Count() - 1 && ntk.getTrcks1Count() > target; i++) {
                triplet[0] = ntk.getTracklogPoint(i - 1);
                triplet[1] = ntk.getTracklogPoint(i);
                triplet[2] = ntk.getTracklogPoint(i + 1);
                if (is_clockwise && Util.getCounterClockwiseAngleBetweenVectors(
                        triplet[1].getLongitude(),
                        triplet[1].getLatitude(),
                        triplet[0].getLongitude(),
                        triplet[0].getLatitude(),
                        triplet[2].getLongitude(),
                        triplet[2].getLatitude()) < Math.PI) //if the polygon is wound clockwise and angle at triplet[1] is less than 180
                { //then I can delete triplet[1], thus simplifying the polygon while assuring it is still inscribed
                    ntk.removeTrackPoint(i);
                    a_simplification_occurred = true;
                    i += step;
                }else if (!is_clockwise && Util.getCounterClockwiseAngleBetweenVectors(
                        triplet[1].getLongitude(),
                        triplet[1].getLatitude(),
                        triplet[0].getLongitude(),
                        triplet[0].getLatitude(),
                        triplet[2].getLongitude(),
                        triplet[2].getLatitude()) > Math.PI) //if the polygon is wound counter-clockwise and angle at triplet[1] is greater than 180
                { //then I can delete triplet[1], thus simplifying the polygon while assuring it is still inscribed
                    ntk.removeTrackPoint(i);
                    a_simplification_occurred = true;
                    i += step;
                }

            }
            //if no simplification took place, stop because no further simplification is possible.
            if(!a_simplification_occurred)
                break;
        }
        
        return ntk;
    }

    @Override
    public String toString(){
        return "TRACK:" + this.getTname();
    }

    /**
     * If the newly added Tracklog point object relies outside de current
     * MBR (defined by the four [min|max][lat|lon] fields) its
     * is updated accordingly.
     */
    private void expandMBR(Trcks1 p){
        if(this.colTrcks1.isEmpty()){
            this.maxLat = (float) p.getLatitude();
            this.maxLon = (float) p.getLongitude();
            this.minLat = (float) p.getLatitude();
            this.minLon = (float) p.getLongitude();
        }else{
            if (p.getLatitude() > this.getMBRMaxLat()) {
                this.maxLat = (float) p.getLatitude();
            }
            if (p.getLatitude() < this.getMBRMinLat()) {
                this.minLat = (float) p.getLatitude();
            }
            if (p.getLongitude() > this.getMBRMaxLon()) {
                this.maxLon = (float) p.getLongitude();
            }
            if (p.getLongitude() < this.getMBRMinLon()) {
                this.minLon = (float) p.getLongitude();
            }
        }
    }


    // Constants specifying a high level track type
    public static final int TYPE_UNINDENTIFIED_TRACK_TYPE = 0;
    //Routable lines
    public static final int TYPE_DUPLICATED_ROAD = 1;
    public static final int TYPE_SIMPLE_ROAD = 2;
    public static final int TYPE_HIGHWAY = 3;
    public static final int TYPE_AVENUE = 4;
    public static final int TYPE_MAIN_STREET = 5;
    public static final int TYPE_ACCESS_WAY = 6;
    public static final int TYPE_SECONDARY_STREET = 7;
    public static final int TYPE_MAIN_DIRTY_ROAD = 8;
    public static final int TYPE_SECONDARY_DIRTY_ROAD = 9;
    public static final int TYPE_OFFROAD_TRACK = 10;
    public static final int TYPE_MOUNTAINBIKE_TRACK = 11;
    public static final int TYPE_PEDESTRIAN_STREET = 12;
    public static final int TYPE_FERRY_BOAT_LINE = 13;
    //Non-routable lines
    public static final int TYPE_INTERNATIONAL_BORDER = 100;
    public static final int TYPE_STATE_BORDER = 101;
    public static final int TYPE_MUNICIPALITY_BORDER = 102;
    public static final int TYPE_RAILROAD = 103;
    public static final int TYPE_CARGO_RAILROAD = 104;
    public static final int TYPE_TOURISTIC_RAILROAD = 105;
    public static final int TYPE_LARGE_RIVER = 106;
    public static final int TYPE_RIVER_OR_CANAL = 107;
    public static final int TYPE_INTERMITTENT_RIVER = 108;
    //Filled polygons (areas)
    public static final int TYPE_FLOODED_AREA_NO_OUTLINE = 201;
    public static final int TYPE_AIRPORT_AREA_NO_OUTLINE = 202;
    public static final int TYPE_MILITARY_AREA_NO_OUTLINE = 203;
    public static final int TYPE_BUILDING_NO_OUTLINE = 204;
    public static final int TYPE_CEMETERY_NO_OUTLINE = 205;
    public static final int TYPE_URBAN_AREA = 206;
    public static final int TYPE_SWAMP_AREA = 207;
    public static final int TYPE_MAP_YELLOW_BACKGROUND_NO_OUTLINE = 208;
    public static final int TYPE_MOUNTAIN_RANGE = 209;
    public static final int TYPE_WATER_BODY_II_NO_OUTLINE = 210;
    public static final int TYPE_URBAN_PARK_NO_OUTLINE = 211;
    public static final int TYPE_WATER_BODY_I_NO_OUTLINE = 212;
    public static final int TYPE_STATE_PARK_NO_OUTLINE = 213;
    public static final int TYPE_DESERT = 214;
    public static final int TYPE_GLACIER_OR_SALT_LAKE_NO_OUTLINE = 215;
    public static final int TYPE_EXPOSED_EARTH_NO_OUTLINE = 216;

    public float getMBRMinLat() {
        return minLat;
    }

    public float getMBRMinLon() {
        return minLon;
    }

    public float getMBRMaxLat() {
        return maxLat;
    }

    public float getMBRMaxLon() {
        return maxLon;
    }

    /**
     * Trivially tests whether a point is inside a polygon by using its MBR (minimum bounding rectangle) plus some tolerance.
     * Points ON MBR limits are considered INSIDE.
     * This method performs better in tests where the probability of the points
     * to be outside is high.
     * @param tolerance Specifies a margin, in degrees, to inside or to outside the actual MBR of this tracklog.
     * Set to zero to use the exact MBR.  Values greater than zero expands the actual MBR.  Larger values may hinder performance.
     */
    public boolean isInsideMBR(PointRecord p, double tolerance){
        double plat = p.getLatitude();
        double plon = p.getLongitude();
        if(plat < this.minLat - tolerance) return false;
        else if(plat > this.maxLat + tolerance) return false;
        else if(plon < this.minLon - tolerance) return false;
        else if(plon > this.maxLon + tolerance) return false;
        else return true;
    }

    /**
     * Creates a new Trknome1 object using the same properties of this Trknome1 except that the
     * new one doesn't have Tracklog points AND its map owner is passed as argument
     * @param ownerMap: Map owner of the returned Trknome1 object
     */
    public Trknome1 deriveNewEmptyTrknome1 (Map ownerMap) throws ErrorObjectNotDefined, ErrorValueOutOfRange {
        Trknome1 tk_new = new Trknome1(ownerMap);
        tk_new.setTcolor(this.getTcolor());
        tk_new.setTlabel(this.getTlabel());
        tk_new.setTlayer(this.getTlayer());
        tk_new.setTname(this.getTname());
        tk_new.setTscale(this.getTscale());
        tk_new.setTtype(this.getTtype());
        return (tk_new);
    }

    /**
     * Creates a new Trknome1 object using the same properties of this Trknome1 except that the
     * new one doesn't have Tracklog points
     */
    public Trknome1 deriveNewEmptyTrknome1 () throws ErrorObjectNotDefined, ErrorValueOutOfRange {
        return (this.deriveNewEmptyTrknome1(this.getParentMapReference()));
    }

    /**
     *  Produces a new tracklog object with the same properties of this tracklog,
     * including copies of all its track points and the same parent map reference.
     * Note: the new tracklog IS NOT added to the parent map tracklog collection and
     * it is up to the caller to add it.
     */
    public Trknome1 copy() throws ErrorObjectNotDefined, ErrorValueOutOfRange{
        Trknome1 t = this.deriveNewEmptyTrknome1();
        Iterator<Trcks1> itp = this.getTrackPointIterator();
        while(itp.hasNext())
            t.addTrcks1(itp.next().copy());
        return t;
    }

    /**
     * Identifies if this track is more important/relevant than the one passed as parameter (comparedTrack)
     * The importance is based on the HighLevel type of the tracks
     * @param comparedTrack
     * @return true if this is more important than comparedTrack
     *         false if this is less important than comparedTrack or they have the same importance
     */
    public boolean isMoreRelevantThan (Trknome1 comparedTrack) {
        if (this.getHighLevelType() < comparedTrack.getHighLevelType())  // Verifica se this eh mais importante que comparedTrack
            return true;
        else {
            if (this.getHighLevelType() > comparedTrack.getHighLevelType()) // Verifica se this eh menos importante que comparedTrack
                return false;
            else  // Se a importancia for a mesma, usa critério alfabetico
                if (this.getTname().compareTo(comparedTrack.getTname()) < 0)
                    return true;
                else
                    return false;
        }
    }

    /**
     * Removes all trackpoints.
     */
    public void removeAllTrackPoints(){
        //clear trackpoint collection
        this.colTrcks1.clear();
        //reset MBR
        minLat = (float) 999.999999;
        minLon = (float) 999.999999;
        maxLat = (float) -999.999999;
        maxLon = (float) -999.999999;
    }

    /**
     * Test for polygon equality on topology level, meaning that vertex sequences must be the same for
     * equality, even if both polygons represent the same shape.
     * The thracklog does not need to be a closed poly.
     */
    public boolean isSameTopology( Trknome1 other_track ){

        //test for count first
        if( this.getTrcks1Count() != other_track.getTrcks1Count() )
            return false;

        //test vertex by vertex
        Iterator<Trcks1> my_points_i = this.getTrackPointIterator();
        Iterator<Trcks1> other_points_i = other_track.getTrackPointIterator();
        while( my_points_i.hasNext() ){
            Trcks1 my_point = my_points_i.next();
            Trcks1 other_point = other_points_i.next();
            if( !my_point.coincidesWith( other_point ) )
                return false;
        }

        return true;

    }

    /**
     * Test for tangency against the given tracklog.  Two tracklogs are tangent
     * if one has at least one track point at the same place as one track point of
     * the other track.<br>
     * TODO: improve efficiency.  This is running in O(n^2) time.
     */
    public boolean isTangent( Trknome1 other_track ){
        for( Trcks1 point : this.colTrcks1 ){
            for( Trcks1 other_point : other_track.colTrcks1 ){
                if( point.coincidesWith( other_point ) )
                    return true;
            }
        }
        return false;
    }

}
