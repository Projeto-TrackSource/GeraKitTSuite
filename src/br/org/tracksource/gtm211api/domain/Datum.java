/*
 * Datum.java
 *
 * Criada em 25 de Dezembro de 2008, 09:20
 *
 */

package br.org.tracksource.gtm211api.domain;

import br.org.tracksource.gtm211api.errors.ErrorGTMRule;
import br.org.tracksource.gtm211api.errors.ErrorObjectNotDefined;
import br.org.tracksource.gtm211api.errors.ErrorValueOutOfRange;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The Datum class represents the user grid and user datum, which has effect only for GPS Trackmaker Professional (r) users,
 * except for <code>ndatum</code> field.  The datum is the second record following the header record of a GTM file per the 211 GTM format specification and appears just once.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class Datum extends Record{
    
    /**
     *Number of the predefined grid. See the table below.  See the <code>PREDEF_GRID_*</code> static constants, defined in this class.
     */
    protected short ngrid;
    
    /**
     *User Grid - Longitude of origin.
     */
    protected double origin;
    
    /**
     *User Grid - False Easting.
     */
    protected double falseeast;
    
    /**
     *User Grid - Scale Factor.
     */
    protected double scale1;
    
    /**
     *User Grid - False Northing.
     */
    protected double falsenorthing;
    
    /**
     *Number of Predefined Datum, in which the data were stored in GTM File. See the <code>DATUM_*</code> static constants, defined in this class.
     */
    protected short ndatum;
    
    /**
     *User Datum - Major Semi-Axe of the Earth.
     */
    protected double axis;
    
    /**
     *User Datum - Flattening of the Earth (Set to 0).
     */
    protected double flattenig;
    
    /**
     *User Datum - Delta X.
     */
    protected short dx;
    
    /**
     *User Datum - Delta Y.
     */
    protected short dy;
    
    /**
     *User Datum - Delta Z.
     */
    protected short dz;
    
    /** Default constructor. */
    public Datum(Map parent_map) throws ErrorObjectNotDefined {
        super(parent_map);
        if(parent_map.getHeader() == null)
            throw new ErrorObjectNotDefined("Header object of the map not defined.");
    }

    /**
     *Number of the predefined grid. See the table below.  See the <code>PREDEF_GRID_*</code> static constants, defined in this class.
     */
    public short getNgrid() {
        return ngrid;
    }

    /**
     *Number of the predefined grid. See the table below.  See the <code>PREDEF_GRID_*</code> static constants, defined in this class.
     */
    public void setNgrid(short ngrid) throws ErrorGTMRule, ErrorValueOutOfRange {
        if(!this.parentMapReference.getHeader().isRectangular() && ngrid != 0)
            throw new ErrorGTMRule("Cannot set the ngrid field different from zero if the rectangular field of header is set to false.");
        if(ngrid > 14 || ngrid < 0)
            throw new ErrorValueOutOfRange("0 to 14", String.valueOf(ngrid));
        this.ngrid = ngrid;
    }

    /**
     *User Grid - Longitude of origin.
     */
    public double getOrigin() {
        return origin;
    }

    /**
     *User Grid - Longitude of origin.
     */
    public void setOrigin(double origin) {
        this.origin = origin;
    }

    /**
     *User Grid ? False Easting.
     */
    public double getFalseeast() {
        return falseeast;
    }

    /**
     *User Grid ? False Easting.
     */
    public void setFalseeast(double falseeast) {
        this.falseeast = falseeast;
    }

    /**
     *User Grid ? Scale Factor.
     */
    public double getScale1() {
        return scale1;
    }

    /**
     *User Grid ? Scale Factor.
     */
    public void setScale1(double scale1) {
        this.scale1 = scale1;
    }

    /**
     *User Grid ? False Northing.
     */
    public double getFalsenorthing() {
        return falsenorthing;
    }

    /**
     *User Grid ? False Northing.
     */
    public void setFalsenorthing(double falsenorthing) {
        this.falsenorthing = falsenorthing;
    }

    public short getNdatum() {
        return ndatum;
    }

    public void setNdatum(short ndatum) throws ErrorValueOutOfRange {
        this.ndatum = ndatum;
        if(ndatum > 262 || ndatum < 1)
            throw new ErrorValueOutOfRange("1 to 262", String.valueOf(ndatum));
    }

    public double getAxis() {
        return axis;
    }

    public void setAxis(double axis) {
        this.axis = axis;
    }

    public double getFlattenig() {
        return flattenig;
    }

    public void setFlattenig(double flattenig) {
        this.flattenig = flattenig;
    }

    public short getDx() {
        return dx;
    }

    public void setDx(short dx) {
        this.dx = dx;
    }

    public short getDy() {
        return dy;
    }

    public void setDy(short dy) {
        this.dy = dy;
    }

    public short getDz() {
        return dz;
    }

    public void setDz(short dz) {
        this.dz = dz;
    }

    public void writeToStream(OutputStream stream) throws IOException {
        stream.write(this.convertShortTo2bytes(this.ngrid));
        stream.write(this.convertDoubleTo8bytes(this.origin));
        stream.write(this.convertDoubleTo8bytes(this.falseeast));
        stream.write(this.convertDoubleTo8bytes(this.scale1));
        stream.write(this.convertDoubleTo8bytes(this.falsenorthing));
        stream.write(this.convertShortTo2bytes(this.ndatum));
        stream.write(this.convertDoubleTo8bytes(this.axis));
        stream.write(this.convertDoubleTo8bytes(this.flattenig));
        stream.write(this.convertShortTo2bytes(this.dx));
        stream.write(this.convertShortTo2bytes(this.dy));
        stream.write(this.convertShortTo2bytes(this.dz));
    }

    /**
     *Returns a new Datum object using the same properties of this Datum object.
     *@param parent_map_reference A {@link Map} object reference that must be different from the
     *map reference of this Datum, otherwise an exception will be thrown.
     */
    public Datum makeCopy(Map parent_map_reference) throws ErrorGTMRule, ErrorObjectNotDefined{
        if(parent_map_reference == this.parentMapReference)
            throw new ErrorGTMRule("Two data cannot have the same Map reference.");
        Datum result = new Datum(parent_map_reference);
        try{
        result.setNgrid(this.ngrid);
        result.setOrigin(this.origin);
        result.setFalseeast(this.falseeast);
        result.setScale1(this.scale1);
        result.setFalsenorthing(this.falsenorthing);
        result.setNdatum(this.ndatum);
        result.setAxis(this.axis);
        result.setFlattenig(this.flattenig);
        result.setDx(this.dx);
        result.setDy(this.dy);
        result.setDz(this.dz);
        }catch(Exception ex){} //assume all parameters above are valid
        return result;
    }
    
    /**
     * Returns the name of the Datum.
     */
    public String getName(){
        return Datum.getDatumNameByCode(this.ndatum);
    }

    /** Constant representing the predefined grid UTM.*/
    public static final int PREDEF_GRID_UTM = 0;
    /** Constant representing the predefined grid User Grid (Reserved for GTM PRO).*/
    public static final int PREDEF_GRID_USER = 1;
    /** Constant representing the predefined grid New Zealand Grid.*/
    public static final int PREDEF_GRID_NEW_ZEALAND = 2;
    /** Constant representing the predefined grid Swiss Grid.*/
    public static final int PREDEF_GRID_SWISS = 3;
    /** Constant representing the predefined grid Swedish Grid.*/
    public static final int PREDEF_GRID_SWEDISH = 4;
    /** Constant representing the predefined grid British National Grid.*/
    public static final int PREDEF_GRID_BRITISH_NATIONAL = 5;
    /** Constant representing the predefined grid Irish National Grid.*/
    public static final int PREDEF_GRID_IRISH_NATIONAL = 6;
    /** Constant representing the predefined grid German Grid.*/
    public static final int PREDEF_GRID_GERMAN = 7;
    /** Constant representing the predefined grid Finnish National Grid (KKJ).*/
    public static final int PREDEF_GRID_FINNISH_NATIONAL = 8;
    /** Constant representing the predefined grid RTM (Reserved for GTM PRO).*/
    public static final int PREDEF_GRID_RTM = 9;
    /** Constant representing the predefined grid LTM (Reserved for GTM PRO).*/
    public static final int PREDEF_GRID_LTM = 10;
    /** Constant representing the predefined grid Grid of Colombia (Reserved to GTM PRO).*/
    public static final int PREDEF_GRID_COLOMBIA = 11;
    /** Constant representing the predefined grid British Uniform Grid.*/
    public static final int PREDEF_GRID_BRITISH_UNIFORM = 12;
    /** Constant representing the predefined grid Taiwan 67 Grid.*/
    public static final int PREDEF_GRID_TAIWAN_67 = 13;
    /** Constant representing the predefined grid Dutch Grid.*/
    public static final int PREDEF_GRID_DUTCH = 14;
    
    /** Adindan Burkina Faso */
    public static final int DATUM_ADINDAN_BURKINA_FASO = 1;
    /** Adindan Cameroon */
    public static final int DATUM_ADINDAN_CAMEROON = 2;
    /** Adindan Ethiopia */
    public static final int DATUM_ADINDAN_ETHIOPIA = 3;
    /** Adindan Mali */
    public static final int DATUM_ADINDAN_MALI = 4;
    /** Adindan Mean for Ethiopia ; Sudan */
    public static final int DATUM_ADINDAN_MEAN_FOR_ETHIOPIA_SUDAN = 5;
    /** Adindan Senegal */
    public static final int DATUM_ADINDAN_SENEGAL = 6;
    /** Adindan Sudan */
    public static final int DATUM_ADINDAN_SUDAN = 7;
    /** Afgooye Somalia */
    public static final int DATUM_AFGOOYE_SOMALIA = 8;
    /** Ain el Abd '70 Bahrain */
    public static final int DATUM_AIN_EL_ABD_70_BAHRAIN = 9;
    /** Ain el Abd '70 Saudi Arabia */
    public static final int DATUM_AIN_EL_ABD_70_SAUDI_ARABIA = 10;
    /** Am. Samoa '62 American Samoa Islands */
    public static final int DATUM_AM_SAMOA_62_AMERICAN_SAMOA_ISLANDS = 11;
    /** Anna 1 A'65 Cocos Islands */
    public static final int DATUM_ANNA_1_A_65_COCOS_ISLANDS = 12;
    /** Antigua Isd '43 Antigua (Leeward Islands) */
    public static final int DATUM_ANTIGUA_ISD_43_ANTIGUA_LEEWARD_ISLANDS = 13;
    /** Arc 1950 Botswana */
    public static final int DATUM_ARC_1950_BOTSWANA = 14;
    /** Arc 1950 Burundi */
    public static final int DATUM_ARC_1950_BURUNDI = 15;
    /** Arc 1950 Lesotho */
    public static final int DATUM_ARC_1950_LESOTHO = 16;
    /** Arc 1950 Malawi */
    public static final int DATUM_ARC_1950_MALAWI = 17;
    /** Arc 1950 Mean Mean for Arc 1950 */
    public static final int DATUM_ARC_1950_MEAN_MEAN_FOR_ARC_1950 = 18;
    /** Arc 1950 Swaziland */
    public static final int DATUM_ARC_1950_SWAZILAND = 19;
    /** Arc 1950 Zaire */
    public static final int DATUM_ARC_1950_ZAIRE = 20;
    /** Arc 1950 Zambia */
    public static final int DATUM_ARC_1950_ZAMBIA = 21;
    /** Arc 1950 Zimbabwe */
    public static final int DATUM_ARC_1950_ZIMBABWE = 22;
    /** Arc 1960 Mean for Kenya ; Tanzania */
    public static final int DATUM_ARC_1960_MEAN_FOR_KENYA_TANZANIA = 23;
    /** Arc 1960 Kenya */
    public static final int DATUM_ARC_1960_KENYA = 24;
    /** Arc 1960 Taanzania */
    public static final int DATUM_ARC_1960_TAANZANIA = 25;
    /** Ascension Isd '58 Ascension Island */
    public static final int DATUM_ASCENSION_ISD_58_ASCENSION_ISLAND = 26;
    /** Astro Beacon E'45 Iwo Jima */
    public static final int DATUM_ASTRO_BEACON_E_45_IWO_JIMA = 27;
    /** Astro DOS 71/4 St. Helena Island */
    public static final int DATUM_ASTRO_DOS_71_4_ST_HELENA_ISLAND = 28;
    /** Astro Tern Isd'61 Tern Island */
    public static final int DATUM_ASTRO_TERN_ISD_61_TERN_ISLAND = 29;
    /** Astron. Station '52 Marcus Island */
    public static final int DATUM_ASTRON_STATION_52_MARCUS_ISLAND = 30;
    /** Australian G. '66 Australia ; Tasmania */
    public static final int DATUM_AUSTRALIAN_G_66_AUSTRALIA_TASMANIA = 31;
    /** Australian G. '84 Australia ; Tasmania */
    public static final int DATUM_AUSTRALIAN_G_84_AUSTRALIA_TASMANIA = 32;
    /** Ayabelle Light. Djibouti */
    public static final int DATUM_AYABELLE_LIGHT_DJIBOUTI = 33;
    /** Bellevue (IGN) Efate & Erromango Islands */
    public static final int DATUM_BELLEVUE_IGN_EFATE_ERROMANGO_ISLANDS = 34;
    /** Bermuda 1957 Bermuda */
    public static final int DATUM_BERMUDA_1957_BERMUDA = 35;
    /** Bissau Guinea-Bissau */
    public static final int DATUM_BISSAU_GUINEA_BISSAU = 36;
    /** Bogota Obsty Colombia */
    public static final int DATUM_BOGOTA_OBSTY_COLOMBIA = 37;
    /** Bukit Rimpah Indonesia (Bangka and Belitung Ids) */
    public static final int DATUM_BUKIT_RIMPAH_INDONESIA_BANGKA_AND_BELITUNG_IDS = 38;
    /** Camp Area Astro Antarctica (McMurdo Camp Area) */
    public static final int DATUM_CAMP_AREA_ASTRO_ANTARCTICA_MCMURDO_CAMP_AREA = 39;
    /** Campo Inchauspe Argentina */
    public static final int DATUM_CAMPO_INCHAUSPE_ARGENTINA = 40;
    /** Canton Astro 1966 Phoenix Islands */
    public static final int DATUM_CANTON_ASTRO_1966_PHOENIX_ISLANDS = 41;
    /** Cape South Africa */
    public static final int DATUM_CAPE_SOUTH_AFRICA = 42;
    /** Cape Canaveral Bahamas ; Florida */
    public static final int DATUM_CAPE_CANAVERAL_BAHAMAS_FLORIDA = 43;
    /** Carthage Tunisia */
    public static final int DATUM_CARTHAGE_TUNISIA = 44;
    /** Chatham Isd A. '71 New Zealand (Chatham Island) */
    public static final int DATUM_CHATHAM_ISD_A_71_NEW_ZEALAND_CHATHAM_ISLAND = 45;
    /** Chua Astro Paraguay */
    public static final int DATUM_CHUA_ASTRO_PARAGUAY = 46;
    /** Corrego Alegre Brazil */
    public static final int DATUM_CORREGO_ALEGRE_BRAZIL = 47;
    /** Dabola Guinea */
    public static final int DATUM_DABOLA_GUINEA = 48;
    /** Deception Island Deception Island ; Antarctia */
    public static final int DATUM_DECEPTION_ISLAND_DECEPTION_ISLAND_ANTARCTIA = 49;
    /** Djakarta (Batavia) Indonesia (Sumatra) */
    public static final int DATUM_DJAKARTA_BATAVIA_INDONESIA_SUMATRA = 50;
    /** DOS 1968 New Georgia Islands (Gizo Island) */
    public static final int DATUM_DOS_1968_NEW_GEORGIA_ISLANDS_GIZO_ISLAND = 51;
    /** Easter Island 1967 Easter Island */
    public static final int DATUM_EASTER_ISLAND_1967_EASTER_ISLAND = 52;
    /** Estonia System '37 Estonia */
    public static final int DATUM_ESTONIA_SYSTEM_37_ESTONIA = 53;
    /** European 1950 Cyprus */
    public static final int DATUM_EUROPEAN_1950_CYPRUS = 54;
    /** European 1950 Egypt */
    public static final int DATUM_EUROPEAN_1950_EGYPT = 55;
    /** European 1950 England Channel Islands ; Scotland ; Shetland Islands */
    public static final int DATUM_EUROPEAN_1950_ENGLAND_CHANNEL_ISLANDS_SCOTLAND_SHETLAND_ISLANDS = 56;
    /** European 1950 England ; Ireland ; Scotland ; Shetland Islands */
    public static final int DATUM_EUROPEAN_1950_ENGLAND_IRELAND_SCOTLAND_SHETLAND_ISLANDS = 57;
    /** European 1950 Finland ; Norway */
    public static final int DATUM_EUROPEAN_1950_FINLAND_NORWAY = 58;
    /** European 1950 Greece */
    public static final int DATUM_EUROPEAN_1950_GREECE = 59;
    /** European 1950 Iran */
    public static final int DATUM_EUROPEAN_1950_IRAN = 60;
    /** European 1950 Italy (Sardinia) */
    public static final int DATUM_EUROPEAN_1950_ITALY_SARDINIA = 61;
    /** European 1950 Italy (Sicily) */
    public static final int DATUM_EUROPEAN_1950_ITALY_SICILY = 62;
    /** European 1950 Malta */
    public static final int DATUM_EUROPEAN_1950_MALTA = 63;
    /** European 1950 Mean for Austria ; Belgium ; Denmark ; Finland ; France ; W. Germany; Gibraltar ; Greece ; Italy ; Luxembourg ; Netherlands ; Norway; Portugal ; Spain ; Sweden ;Switzerland */
    public static final int DATUM_EUROPEAN_1950_MEAN_FOR_AUSTRIA_BELGIUM_DENMARK_FINLAND_ETC = 64;
    /** European 1950 Mean for Austria ; Denmark ; France ; W. Germany ; Netherlands; Switzerland */
    public static final int DATUM_EUROPEAN_1950_MEAN_FOR_AUSTRIA_DENMARK_ETC = 65;
    /** European 1950 Mean for Iraq ; Israel ; Jordan ; Lebanon ; Kuwait ; Saudi Arabia ; Syria */
    public static final int DATUM_EUROPEAN_1950_MEAN_FOR_IRAQ_ISRAEL_JORDAN_ETC = 66;
    /** European 1950 Portugal ; Spain */
    public static final int DATUM_EUROPEAN_1950_PORTUGAL_SPAIN = 67;
    /** European 1950 Tunisia */
    public static final int DATUM_EUROPEAN_1950_TUNISIA = 68;
    /** European 1979 Switzerland */
    public static final int DATUM_EUROPEAN_1979_SWITZERLAND = 69;
    /** Fort Thomas 1955 Nevis ; St. Kitts (Leeward Islands) */
    public static final int DATUM_FORT_THOMAS_1955_NEVIS_ST_KITTS_LEEWARD_ISLANDS = 70;
    /** Gan 1970 Republic of Maldives */
    public static final int DATUM_GAN_1970_REPUBLIC_OF_MALDIVES = 71;
    /** Geod. Dat. '49 New Zealand */
    public static final int DATUM_GEOD_DAT_49_NEW_ZEALAND = 72;
    /** Graciosa SW '48 Azores (Faial ; Graciosa ; Pico ; S?o Jorge ; Terceira) */
    public static final int DATUM_GRACIOSA_SW_48_AZORES_FAIAL_GRACIOSA_PICO_SAO_JORGE_TERCEIRA = 73;
    /** Guam 1963 Guam */
    public static final int DATUM_GUAM_1963_GUAM = 74;
    /** Gunung Segara Indonesia (Kalimantan) */
    public static final int DATUM_GUNUNG_SEGARA_INDONESIA_KALIMANTAN = 75;
    /** GUX 1 Astro Guadalcanal Island */
    public static final int DATUM_GUX_1_ASTRO_GUADALCANAL_ISLAND = 76;
    /** Herat North Afghanistan */
    public static final int DATUM_HERAT_NORTH_AFGHANISTAN = 77;
    /** Hermannskogel Croatia-Serbia ; Bosnia-Herzegovina */
    public static final int DATUM_HERMANNSKOGEL_CROATIA_SERBIA_BOSNIA_HERZEGOVINA = 78;
    /** Hjorsey 1955 Iceland */
    public static final int DATUM_HJORSEY_1955_ICELAND = 79;
    /** Hong Kong 1963 Hong Kong */
    public static final int DATUM_HONG_KONG_1963_HONG_KONG = 80;
    /** Hu-Tzu-Shan Taiwan */
    public static final int DATUM_HU_TZU_SHAN_TAIWAN = 81;
    /** Indian Bangladesh */
    public static final int DATUM_INDIAN_BANGLADESH = 82;
    /** Indian India ; Nepal */
    public static final int DATUM_INDIAN_INDIA_NEPAL = 83;
    /** Indian Pakistan */
    public static final int DATUM_INDIAN_PAKISTAN = 84;
    /** Indian 1954 Thailand */
    public static final int DATUM_INDIAN_1954_THAILAND = 85;
    /** Indian 1960 Vietnam (Com Son Island) */
    public static final int DATUM_INDIAN_1960_VIETNAM_COM_SON_ISLAND = 86;
    /** Indian 1960 Vietnam (Near 16?N) */
    public static final int DATUM_INDIAN_1960_VIETNAM_NEAR_16_N = 87;
    /** Indian 1975 Thailand */
    public static final int DATUM_INDIAN_1975_THAILAND = 88;
    /** Indonesian 1974 Indonesia */
    public static final int DATUM_INDONESIAN_1974_INDONESIA = 89;
    /** Ireland 1965 Ireland */
    public static final int DATUM_IRELAND_1965_IRELAND = 90;
    /** ISTS 061 '68 SouthGeorgia Islands */
    public static final int DATUM_ISTS_061_68_SOUTHGEORGIA_ISLANDS = 91;
    /** ISTS 073 '69 Diego Garcia */
    public static final int DATUM_ISTS_073_69_DIEGO_GARCIA = 92;
    /** Johnston Isld '61 Johnston Island */
    public static final int DATUM_JOHNSTON_ISLD_61_JOHNSTON_ISLAND = 93;
    /** Kandawala Sri Lanka */
    public static final int DATUM_KANDAWALA_SRI_LANKA = 94;
    /** Kerguelen Isld '49 Kerguelen Island */
    public static final int DATUM_KERGUELEN_ISLD_49_KERGUELEN_ISLAND = 95;
    /** Kertau 1948 West Malaysia and Singapore */
    public static final int DATUM_KERTAU_1948_WEST_MALAYSIA_AND_SINGAPORE = 96;
    /** Kusaie Astro1951 Caroline Islands */
    public static final int DATUM_KUSAIE_ASTRO1951_CAROLINE_ISLANDS = 97;
    /** Korean System South Korea */
    public static final int DATUM_KOREAN_SYSTEM_SOUTH_KOREA = 98;
    /** L. C. 5 Astro 1961 Cayman Brac Island */
    public static final int DATUM_L_C_5_ASTRO_1961_CAYMAN_BRAC_ISLAND = 99;
    /** Leigon Ghana */
    public static final int DATUM_LEIGON_GHANA = 100;
    /** Liberia 1964 Liberia */
    public static final int DATUM_LIBERIA_1964_LIBERIA = 101;
    /** Luzon Philippines (Excluding Mindanao) */
    public static final int DATUM_LUZON_PHILIPPINES_EXCLUDING_MINDANAO = 102;
    /** Luzon Philippines (Mindanao) */
    public static final int DATUM_LUZON_PHILIPPINES_MINDANAO = 103;
    /** M'Poraloko Gabon */
    public static final int DATUM_M_PORALOKO_GABON = 104;
    /** Mahe 1971 Mahe Island */
    public static final int DATUM_MAHE_1971_MAHE_ISLAND = 105;
    /** Massawa Ethiopia (Eritrea) */
    public static final int DATUM_MASSAWA_ETHIOPIA_ERITREA = 106;
    /** Merchich Morocco */
    public static final int DATUM_MERCHICH_MOROCCO = 107;
    /** Midway Astro '61 Midway Islands */
    public static final int DATUM_MIDWAY_ASTRO_61_MIDWAY_ISLANDS = 108;
    /** Minna Cameroon */
    public static final int DATUM_MINNA_CAMEROON = 109;
    /** Minna Nigeria */
    public static final int DATUM_MINNA_NIGERIA = 110;
    /** Montserrat '58 Montserrat (Leeward Islands) */
    public static final int DATUM_MONTSERRAT_58_MONTSERRAT_LEEWARD_ISLANDS = 111;
    /** Nahrwan Oman (Masirah Island */
    public static final int DATUM_NAHRWAN_OMAN_MASIRAH_ISLAND = 112;
    /** Nahrwan Saudi Arabia */
    public static final int DATUM_NAHRWAN_SAUDI_ARABIA = 113;
    /** Nahrwan United Arab Emirates */
    public static final int DATUM_NAHRWAN_UNITED_ARAB_EMIRATES = 114;
    /** Naparima BWI Trinidad & Tobago */
    public static final int DATUM_NAPARIMA_BWI_TRINIDAD_TOBAGO = 115;
    /** NAD 1927 Alaska (Excluding Aleutian Ids) */
    public static final int DATUM_NAD_1927_ALASKA_EXCLUDING_ALEUTIAN_IDS = 116;
    /** NAD 1927 Alaska (Aleutian Ids East of 180?W) */
    public static final int DATUM_NAD_1927_ALASKA_ALEUTIAN_IDS_EAST_OF_180W = 117;
    /** NAD 1927 Alaska (Aleutian Ids West of 180?W) */
    public static final int DATUM_NAD_1927_ALASKA_ALEUTIAN_IDS_WEST_OF_180W = 118;
    /** NAD 1927 Bahamas (Except San Salvador Id) */
    public static final int DATUM_NAD_1927_BAHAMAS_EXCEPT_SAN_SALVADOR_ID = 119;
    /** NAD 1927 Bahamas (San Salvador Island) */
    public static final int DATUM_NAD_1927_BAHAMAS_SAN_SALVADOR_ISLAND = 120;
    /** NAD 1927 Canada (Alberta ; British Columbia) */
    public static final int DATUM_NAD_1927_CANADA_ALBERTA_BRITISH_COLUMBIA = 121;
    /** NAD 1927 Canada (Manitoba ; Ontario) */
    public static final int DATUM_NAD_1927_CANADA_MANITOBA_ONTARIO = 122;
    /** NAD 1927 Canada (New Brunswick ; Newfoundland ; Nova Scotia ; Quebec */
    public static final int DATUM_NAD_1927_CANADA_NEW_BRUNSWICK_NEWFOUNDLAND_NOVA_SCOTIA_QUEBEC = 123;
    /** NAD 1927 Canada (Northwest Territories ; Saskatchewan */
    public static final int DATUM_NAD_1927_CANADA_NORTHWEST_TERRITORIES_SASKATCHEWAN = 124;
    /** NAD 1927 Canada (Yukon) */
    public static final int DATUM_NAD_1927_CANADA_YUKON = 125;
    /** NAD 1927 Canal Zone */
    public static final int DATUM_NAD_1927_CANAL_ZONE = 126;
    /** NAD 1927 Cuba */
    public static final int DATUM_NAD_1927_CUBA = 127;
    /** NAD 1927 Greenland (Hayes Peninsula) */
    public static final int DATUM_NAD_1927_GREENLAND_HAYES_PENINSULA = 128;
    /** NAD 1927 Mean for Antigua ; Barbados ; Barbuda ; Caicos Islands ; Cuba ; Dominican Republic ; Grand Cayman ; Jamaica ; Turks Islands */
    public static final int DATUM_NAD_1927_MEAN_FOR_ANTIGUA_BARBADOS_BARBUDA_ETC = 129;
    /** NAD 1927 Mean for Belize ; Costa Rica; El Salvador ; Guatemala ; Honduras; */
    public static final int DATUM_NAD_1927_MEAN_FOR_BELIZE_COSTA_RICA_EL_SALVADOR_GUATEMALA_HONDURAS = 130;
    /** NAD 1927 Mean for Canada */
    public static final int DATUM_NAD_1927_MEAN_FOR_CANADA = 131;
    /** NAD 1927 Mean for CONUS */
    public static final int DATUM_NAD_1927_MEAN_FOR_CONUS = 132;
    /** NAD 1927 Mean for CONUS (East of Mississippi ; River Including Louisiana; Missouri ; Minnesota) */
    public static final int DATUM_NAD_1927_MEAN_FOR_CONUS_EAST_OF_MISSISSIPPI_RIVER_INCLUDING_LOUISIANA_MISSOURI_MINNESOTA = 133;
    /** NAD 1927 Mean for CONUS (West of Mississippi ; River Excluding Louisiana, Minnesota ; Missouri) */
    public static final int DATUM_NAD_1927_MEAN_FOR_CONUS_WEST_OF_MISSISSIPPI_RIVER_EXCLUDING_LOUISIANA_MINNESOTA_MISSOURI = 134;
    /** NAD'27 Mexico */
    public static final int DATUM_NAD_27_MEXICO = 135;
    /** NAD'83 Alaska (Excluding Aleutian Ids) */
    public static final int DATUM_NAD_83_ALASKA_EXCLUDING_ALEUTIAN_IDS = 136;
    /** NAD'83 Aleutian Ids */
    public static final int DATUM_NAD_83_ALEUTIAN_IDS = 137;
    /** NAD'83 Canada */
    public static final int DATUM_NAD_83_CANADA = 138;
    /** NAD'83 CONUS */
    public static final int DATUM_NAD_83_CONUS = 139;
    /** NAD'83 Hawaii */
    public static final int DATUM_NAD_83_HAWAII = 140;
    /** NAD'83 Mexico ; Central America */
    public static final int DATUM_NAD_83_MEXICO_CENTRAL_AMERICA = 141;
    /** North Sahara 1959 Algeria */
    public static final int DATUM_NORTH_SAHARA_1959_ALGERIA = 142;
    /** Obs. Met. 1939 Azores (Corvo & Flores Islands) */
    public static final int DATUM_OBS_MET_1939_AZORES_CORVO_FLORES_ISLANDS = 143;
    /** Old Egyptian 1907 Egypt */
    public static final int DATUM_OLD_EGYPTIAN_1907_EGYPT = 144;
    /** Old Hawaiian Hawaii */
    public static final int DATUM_OLD_HAWAIIAN_HAWAII = 145;
    /** Old Hawaiian Kauai */
    public static final int DATUM_OLD_HAWAIIAN_KAUAI = 146;
    /** Old Hawaiian Maui */
    public static final int DATUM_OLD_HAWAIIAN_MAUI = 147;
    /** Old Hawaiian Mean for Hawaii ; Kauai ; Maui ; Oahu */
    public static final int DATUM_OLD_HAWAIIAN_MEAN_FOR_HAWAII_KAUAI_MAUI_OAHU = 148;
    /** Old Hawaiian Oahu */
    public static final int DATUM_OLD_HAWAIIAN_OAHU = 149;
    /** Oman Oman */
    public static final int DATUM_OMAN_OMAN = 150;
    /** OS G. Britain '36 England */
    public static final int DATUM_OS_G_BRITAIN_36_ENGLAND = 151;
    /** OS G. Britain '36 England ; Isle of Man ; Wales */
    public static final int DATUM_OS_G_BRITAIN_36_ENGLAND_ISLE_OF_MAN_WALES = 152;
    /** OS G. Britain '36 Mean for England ; Isle of Man ; Scotland; Shetland Islands ; Wales */
    public static final int DATUM_OS_G_BRITAIN_36_MEAN_FOR_ENGLAND_ISLE_OF_MAN_ETC = 153;
    /** OS G. Britain '36 Scotland ; Shetland Islands */
    public static final int DATUM_OS_G_BRITAIN_36_SCOTLAND_SHETLAND_ISLANDS = 154;
    /** OS G. Britain '36 Wales */
    public static final int DATUM_OS_G_BRITAIN_36_WALES = 155;
    /** Pico de las Nieves Canary Islands */
    public static final int DATUM_PICO_DE_LAS_NIEVES_CANARY_ISLANDS = 156;
    /** Pitcairn Astro '67 Pitcairn Island */
    public static final int DATUM_PITCAIRN_ASTRO_67_PITCAIRN_ISLAND = 157;
    /** Point 58 Mean for Burkina Faso & Niger */
    public static final int DATUM_POINT_58_MEAN_FOR_BURKINA_FASO_NIGER = 158;
    /** Pointe Noire 1948 Congo */
    public static final int DATUM_POINTE_NOIRE_1948_CONGO = 159;
    /** Porto Santo 1936 Porto Santo ; Madeira Islands */
    public static final int DATUM_PORTO_SANTO_1936_PORTO_SANTO_MADEIRA_ISLANDS = 160;
    /** PSA1956 Bolivia */
    public static final int DATUM_PSA1956_BOLIVIA = 161;
    /** PSA1956 Chile (Northern ; Near 19?S) */
    public static final int DATUM_PSA1956_CHILE_NORTHERN_NEAR_19S = 162;
    /** PSA1956 Chile (Southern ; Near 43?S) */
    public static final int DATUM_PSA1956_CHILE_SOUTHERN_NEAR_43S = 163;
    /** PSA1956 Colombia */
    public static final int DATUM_PSA1956_COLOMBIA = 164;
    /** PSA1956 Ecuador */
    public static final int DATUM_PSA1956_ECUADOR = 165;
    /** PSA1956 Guyana */
    public static final int DATUM_PSA1956_GUYANA = 166;
    /** PSA1956 Mean for Bolivia ; Chile ; Colombia ; Ecuador ; Guyana ; Peru; Venezuela */
    public static final int DATUM_PSA1956_MEAN_FOR_BOLIVIA_CHILE_COLOMBIA_ETC = 167;
    /** PSA1956 Peru */
    public static final int DATUM_PSA1956_PERU = 168;
    /** PSA1956 Venezuela */
    public static final int DATUM_PSA1956_VENEZUELA = 169;
    /** PS Chilean 1963 Chile (Near 53?S(Hito XVIII) */
    public static final int DATUM_PS_CHILEAN_1963_CHILE_NEAR_53SHITO_XVIII = 170;
    /** Puerto Rico Puerto Rico ; Virgin Islands */
    public static final int DATUM_PUERTO_RICO_PUERTO_RICO_VIRGIN_ISLANDS = 171;
    /** Pulkovo 1942 Russia */
    public static final int DATUM_PULKOVO_1942_RUSSIA = 172;
    /** Qatar National Qatar */
    public static final int DATUM_QATAR_NATIONAL_QATAR = 173;
    /** Qornoq Greenland (South) */
    public static final int DATUM_QORNOQ_GREENLAND_SOUTH = 174;
    /** Reunion Mascarene Islands */
    public static final int DATUM_REUNION_MASCARENE_ISLANDS = 175;
    /** Rome 1940 Italy (Sardinia) */
    public static final int DATUM_ROME_1940_ITALY_SARDINIA = 176;
    /** S42 (Pulkovo '42) Hungary */
    public static final int DATUM_S42_PULKOVO_42_HUNGARY = 177;
    /** S42 (Pulkovo '42) Poland */
    public static final int DATUM_S42_PULKOVO_42_POLAND = 178;
    /** S42 (Pulkovo '42) Czechoslavakia */
    public static final int DATUM_S42_PULKOVO_42_CZECHOSLAVAKIA = 179;
    /** S42 (Pulkovo '42) Latvia */
    public static final int DATUM_S42_PULKOVO_42_LATVIA = 180;
    /** S42 (Pulkovo '42) Kazakhstan */
    public static final int DATUM_S42_PULKOVO_42_KAZAKHSTAN = 181;
    /** S42 (Pulkovo '42) Albania */
    public static final int DATUM_S42_PULKOVO_42_ALBANIA = 182;
    /** S42 (Pulkovo '42) Romania */
    public static final int DATUM_S42_PULKOVO_42_ROMANIA = 183;
    /** S-JTSK Czechoslavakia (Prior 1 Jan 1993) */
    public static final int DATUM_S_JTSK_CZECHOSLAVAKIA_PRIOR_1_JAN_1993 = 184;
    /** Santo (DOS1965 Espirito Santo Island */
    public static final int DATUM_SANTO_DOS1965_ESPIRITO_SANTO_ISLAND = 185;
    /** Sao Braz Azores (S?o Miguel ; Santa Maria Ids) */
    public static final int DATUM_SAO_BRAZ_AZORES_SAO_MIGUEL_SANTA_MARIA_IDS = 186;
    /** Sapper Hill 1943 East Falkland Island */
    public static final int DATUM_SAPPER_HILL_1943_EAST_FALKLAND_ISLAND = 187;
    /** Schwarzeck Namibia */
    public static final int DATUM_SCHWARZECK_NAMIBIA = 188;
    /** Selvagem G '38 Salvage Islands */
    public static final int DATUM_SELVAGEM_G_38_SALVAGE_ISLANDS = 189;
    /** Sierra Leone 1960 Sierra Leone */
    public static final int DATUM_SIERRA_LEONE_1960_SIERRA_LEONE = 190;
    /** SAD 1969 Argentina */
    public static final int DATUM_SAD_1969_ARGENTINA = 191;
    /** SAD 1969 Bolivia */
    public static final int DATUM_SAD_1969_BOLIVIA = 192;
    /** SAD 1969 Brazil */
    public static final int DATUM_SAD_1969_BRAZIL = 193;
    /** SAD 1969 Chile */
    public static final int DATUM_SAD_1969_CHILE = 194;
    /** SAD 1969 Colombia */
    public static final int DATUM_SAD_1969_COLOMBIA = 195;
    /** SAD 1969 Ecuador */
    public static final int DATUM_SAD_1969_ECUADOR = 196;
    /** SAD 1969 Ecuador (Baltra ; Galapagos) */
    public static final int DATUM_SAD_1969_ECUADOR_BALTRA_GALAPAGOS = 197;
    /** SAD 1969 Guyana */
    public static final int DATUM_SAD_1969_GUYANA = 198;
    /** SAD 1969 Mean Mean for Argentina ; Bolivia ; Brazil ; Chile ; Colombia ; Ecuador;Guyana ; Paraguay ; Peru ; Trinidad & Tobago ; Venezuela */
    public static final int DATUM_SAD_1969_MEAN_MEAN_FOR_ARGENTINA_BOLIVIA_BRAZIL_ETC = 199;
    /** SAD 1969 Paraguay */
    public static final int DATUM_SAD_1969_PARAGUAY = 200;
    /** SAD 1969 Peru */
    public static final int DATUM_SAD_1969_PERU = 201;
    /** SAD 1969 Trinidad & Tobago */
    public static final int DATUM_SAD_1969_TRINIDAD_TOBAGO = 202;
    /** SAD 1969 Venezuela */
    public static final int DATUM_SAD_1969_VENEZUELA = 203;
    /** South Asia Singapore */
    public static final int DATUM_SOUTH_ASIA_SINGAPORE = 204;
    /** Tananarive 1925 Madagascar */
    public static final int DATUM_TANANARIVE_1925_MADAGASCAR = 205;
    /** Timbalai 1948 Brunei ; E. Malaysia (Sabah Sarawak) */
    public static final int DATUM_TIMBALAI_1948_BRUNEI_E_MALAYSIA_SABAH_SARAWAK = 206;
    /** Tokyo Japan */
    public static final int DATUM_TOKYO_JAPAN = 207;
    /** Tokyo Mean for Japan ; South Korea ; Okinawa */
    public static final int DATUM_TOKYO_MEAN_FOR_JAPAN_SOUTH_KOREA_OKINAWA = 208;
    /** Tokyo Okinawa */
    public static final int DATUM_TOKYO_OKINAWA = 209;
    /** Tokyo South Korea */
    public static final int DATUM_TOKYO_SOUTH_KOREA = 210;
    /** Tristan Astro '68 Trist?o da Cunha */
    public static final int DATUM_TRISTAN_ASTRO_68_TRISTAO_DA_CUNHA = 211;
    /** Viti Levu 1916 Fiji (Viti Levu Island) */
    public static final int DATUM_VITI_LEVU_1916_FIJI_VITI_LEVU_ISLAND = 212;
    /** Voirol 1960 Algeria */
    public static final int DATUM_VOIROL_1960_ALGERIA = 213;
    /** Wake Isld '52 Wake Atoll */
    public static final int DATUM_WAKE_ISLD_52_WAKE_ATOLL = 214;
    /** W Eniwetok '60 Marshall Islands */
    public static final int DATUM_W_ENIWETOK_60_MARSHALL_ISLANDS = 215;
    /** WGS 1972 Global Definition */
    public static final int DATUM_WGS_1972_GLOBAL_DEFINITION = 216;
    /** WGS 1984 Global Definition */
    public static final int DATUM_WGS_1984_GLOBAL_DEFINITION = 217;
    /** Yacare Uruguay */
    public static final int DATUM_YACARE_URUGUAY = 218;
    /** Zanderij Suriname */
    public static final int DATUM_ZANDERIJ_SURINAME = 219;
    /** Amersfoort Netherlands */
    public static final int DATUM_AMERSFOORT_NETHERLANDS = 220;
    /** French NTF France ; Nouvelle Triangulation Francaise */
    public static final int DATUM_FRENCH_NTF_FRANCE_NOUVELLE_TRIANGULATION_FRANCAISE = 221;
    /** Potsdam Germany */
    public static final int DATUM_POTSDAM_GERMANY = 222;
    /** RT 90 Swedish */
    public static final int DATUM_RT_90_SWEDISH = 223;
    /** CH-1903 Swiss */
    public static final int DATUM_CH_1903_SWISS = 224;
    /** Austria Austria */
    public static final int DATUM_AUSTRIA_AUSTRIA = 225;
    /** European 1950 Belgium */
    public static final int DATUM_EUROPEAN_1950_BELGIUM = 226;
    /** Israeli Israeli */
    public static final int DATUM_ISRAELI_ISRAELI = 227;
    /** Rome 1940 Luxembourg */
    public static final int DATUM_ROME_1940_LUXEMBOURG = 228;
    /** Finland Hayford Finland */
    public static final int DATUM_FINLAND_HAYFORD_FINLAND = 229;
    /** Dionisos Greece */
    public static final int DATUM_DIONISOS_GREECE = 230;
    /** SAD 69 (IBGE) Brazil */
    public static final int DATUM_SAD_69_IBGE_BRAZIL = 231;
    /** Potsdam II Gemany */
    public static final int DATUM_POTSDAM_II_GEMANY = 232;
    /** Datum 73 Portugal */
    public static final int DATUM_DATUM_73_PORTUGAL = 233;
    /** WGS 1972 (GPS) Global Definition */
    public static final int DATUM_WGS_1972_GPS_GLOBAL_DEFINITION = 234;
    /** Adindan (GPS) Burkina Faso */
    public static final int DATUM_ADINDAN_GPS_BURKINA_FASO = 235;
    /** Ain el Abd 1970 (GPS) Bahrain */
    public static final int DATUM_AIN_EL_ABD_1970_GPS_BAHRAIN = 236;
    /** Arc 1960 (GPS Kenya and Taanzania */
    public static final int DATUM_ARC_1960_GPS_KENYA_AND_TAANZANIA = 237;
    /** Ascension Island 1958 (GPS) Ascension Island */
    public static final int DATUM_ASCENSION_ISLAND_1958_GPS_ASCENSION_ISLAND = 238;
    /** Belgium 1950 (GPS) Belgium */
    public static final int DATUM_BELGIUM_1950_GPS_BELGIUM = 239;
    /** Danish 1934 (GPS) Denmark */
    public static final int DATUM_DANISH_1934_GPS_DENMARK = 240;
    /** Hu-Tzu-Shan (GPS) Taiwan */
    public static final int DATUM_HU_TZU_SHAN_GPS_TAIWAN = 241;
    /** Indian Bangladesh (GPS) Bangladesh */
    public static final int DATUM_INDIAN_BANGLADESH_GPS_BANGLADESH = 242;
    /** Indian Maen (GPS) Mean for India */
    public static final int DATUM_INDIAN_MAEN_GPS_MEAN_FOR_INDIA = 243;
    /** Indian Thailand (GPS) Mean for Thailand */
    public static final int DATUM_INDIAN_THAILAND_GPS_MEAN_FOR_THAILAND = 244;
    /** Indonesian 1974 (GPS) Mean for Indonesia */
    public static final int DATUM_INDONESIAN_1974_GPS_MEAN_FOR_INDONESIA = 245;
    /** Johnston Isld 61 (GPS) Johnston Island */
    public static final int DATUM_JOHNSTON_ISLD_61_GPS_JOHNSTON_ISLAND = 246;
    /** Luzon Mean (GPS) Mean for Philippines */
    public static final int DATUM_LUZON_MEAN_GPS_MEAN_FOR_PHILIPPINES = 247;
    /** NAD27 Caribbean (GPS) Mean for Caribe */
    public static final int DATUM_NAD27_CARIBBEAN_GPS_MEAN_FOR_CARIBE = 248;
    /** Nahrwan Saudi Arabia (GPS) Mean for Saudi Arabia */
    public static final int DATUM_NAHRWAN_SAUDI_ARABIA_GPS_MEAN_FOR_SAUDI_ARABIA = 249;
    /** Naparima BWI (GPS) Trinidad and Tobago */
    public static final int DATUM_NAPARIMA_BWI_GPS_TRINIDAD_AND_TOBAGO = 250;
    /** Netherland Tri21 (GPS) Mean for Netherland */
    public static final int DATUM_NETHERLAND_TRI21_GPS_MEAN_FOR_NETHERLAND = 251;
    /** Nou Triag France (GPS) France */
    public static final int DATUM_NOU_TRIAG_FRANCE_GPS_FRANCE = 252;
    /** Nou Triag Luxemb (GPS) Luxemburg */
    public static final int DATUM_NOU_TRIAG_LUXEMB_GPS_LUXEMBURG = 253;
    /** Old Hawaiian Kauai (GPS) Kauai/Hawaii */
    public static final int DATUM_OLD_HAWAIIAN_KAUAI_GPS_KAUAI_HAWAII = 254;
    /** Old Hawaiian Maui (GPS) Maui/Hawaii */
    public static final int DATUM_OLD_HAWAIIAN_MAUI_GPS_MAUI_HAWAII = 255;
    /** Old Hawaiian Oahu (GPS) Oahu/Hawaii */
    public static final int DATUM_OLD_HAWAIIAN_OAHU_GPS_OAHU_HAWAII = 256;
    /** Portugal 73 (GPS) Portugal */
    public static final int DATUM_PORTUGAL_73_GPS_PORTUGAL = 257;
    /** RT 90 (GPS) Swedish */
    public static final int DATUM_RT_90_GPS_SWEDISH = 258;
    /** Sapper Hill 1943 (GPS) East Falkland Island */
    public static final int DATUM_SAPPER_HILL_1943_GPS_EAST_FALKLAND_ISLAND = 259;
    /** Timbalai 1948 (GPS) Mean for Brunei; E. Malaysia (Sabah Sarawak) */
    public static final int DATUM_TIMBALAI_1948_GPS_MEAN_FOR_BRUNEI_E_MALAYSIA_SABAH_SARAWAK = 260;
    /** Tokyo Mean (GPS) Mean for Japan; South Korea; Okinawa */
    public static final int DATUM_TOKYO_MEAN_GPS_MEAN_FOR_JAPAN_SOUTH_KOREA_OKINAWA = 261;
    /** Wake-Eniwetok 1960 (GPS) Marshall Islands */
    public static final int DATUM_WAKE_ENIWETOK_1960_GPS_MARSHALL_ISLANDS = 262;
    
    /**
     * Retorna o nome do datum em funcao do codigo.
     *
     * @author Alexandre Loss <alexandre.loss@gmail.com> <a href="mailto:">alexandre.loss@gmail.com</a>
     */
    public static String getDatumNameByCode(int code) {
        switch (code) {
            case DATUM_ADINDAN_BURKINA_FASO: return "DATUM ADINDAN BURKINA FASO";
            case DATUM_ADINDAN_CAMEROON: return "DATUM ADINDAN CAMEROON";
            case DATUM_ADINDAN_ETHIOPIA: return "DATUM ADINDAN ETHIOPIA";
            case DATUM_ADINDAN_MALI: return "DATUM ADINDAN MALI";
            case DATUM_ADINDAN_MEAN_FOR_ETHIOPIA_SUDAN: return "DATUM ADINDAN MEAN FOR ETHIOPIA SUDAN";
            case DATUM_ADINDAN_SENEGAL: return "DATUM ADINDAN SENEGAL";
            case DATUM_ADINDAN_SUDAN: return "DATUM ADINDAN SUDAN";
            case DATUM_AFGOOYE_SOMALIA: return "DATUM AFGOOYE SOMALIA";
            case DATUM_AIN_EL_ABD_70_BAHRAIN: return "DATUM AIN EL ABD 70 BAHRAIN";
            case DATUM_AIN_EL_ABD_70_SAUDI_ARABIA: return "DATUM AIN EL ABD 70 SAUDI ARABIA";
            case DATUM_AM_SAMOA_62_AMERICAN_SAMOA_ISLANDS: return "DATUM AM SAMOA 62 AMERICAN SAMOA ISLANDS";
            case DATUM_ANNA_1_A_65_COCOS_ISLANDS: return "DATUM ANNA 1 A 65 COCOS ISLANDS";
            case DATUM_ANTIGUA_ISD_43_ANTIGUA_LEEWARD_ISLANDS: return "DATUM ANTIGUA ISD 43 ANTIGUA LEEWARD ISLANDS";
            case DATUM_ARC_1950_BOTSWANA: return "DATUM ARC 1950 BOTSWANA";
            case DATUM_ARC_1950_BURUNDI: return "DATUM ARC 1950 BURUNDI";
            case DATUM_ARC_1950_LESOTHO: return "DATUM ARC 1950 LESOTHO";
            case DATUM_ARC_1950_MALAWI: return "DATUM ARC 1950 MALAWI";
            case DATUM_ARC_1950_MEAN_MEAN_FOR_ARC_1950: return "DATUM ARC 1950 MEAN MEAN FOR ARC 1950";
            case DATUM_ARC_1950_SWAZILAND: return "DATUM ARC 1950 SWAZILAND";
            case DATUM_ARC_1950_ZAIRE: return "DATUM ARC 1950 ZAIRE";
            case DATUM_ARC_1950_ZAMBIA: return "DATUM ARC 1950 ZAMBIA";
            case DATUM_ARC_1950_ZIMBABWE: return "DATUM ARC 1950 ZIMBABWE";
            case DATUM_ARC_1960_MEAN_FOR_KENYA_TANZANIA: return "DATUM ARC 1960 MEAN FOR KENYA TANZANIA";
            case DATUM_ARC_1960_KENYA: return "DATUM ARC 1960 KENYA";
            case DATUM_ARC_1960_TAANZANIA: return "DATUM ARC 1960 TAANZANIA";
            case DATUM_ASCENSION_ISD_58_ASCENSION_ISLAND: return "DATUM ASCENSION ISD 58 ASCENSION ISLAND";
            case DATUM_ASTRO_BEACON_E_45_IWO_JIMA: return "DATUM ASTRO BEACON E 45 IWO JIMA";
            case DATUM_ASTRO_DOS_71_4_ST_HELENA_ISLAND: return "DATUM ASTRO DOS 71 4 ST HELENA ISLAND";
            case DATUM_ASTRO_TERN_ISD_61_TERN_ISLAND: return "DATUM ASTRO TERN ISD 61 TERN ISLAND";
            case DATUM_ASTRON_STATION_52_MARCUS_ISLAND: return "DATUM ASTRON STATION 52 MARCUS ISLAND";
            case DATUM_AUSTRALIAN_G_66_AUSTRALIA_TASMANIA: return "DATUM AUSTRALIAN G 66 AUSTRALIA TASMANIA";
            case DATUM_AUSTRALIAN_G_84_AUSTRALIA_TASMANIA: return "DATUM AUSTRALIAN G 84 AUSTRALIA TASMANIA";
            case DATUM_AYABELLE_LIGHT_DJIBOUTI: return "DATUM AYABELLE LIGHT DJIBOUTI";
            case DATUM_BELLEVUE_IGN_EFATE_ERROMANGO_ISLANDS: return "DATUM BELLEVUE IGN EFATE ERROMANGO ISLANDS";
            case DATUM_BERMUDA_1957_BERMUDA: return "DATUM BERMUDA 1957 BERMUDA";
            case DATUM_BISSAU_GUINEA_BISSAU: return "DATUM BISSAU GUINEA BISSAU";
            case DATUM_BOGOTA_OBSTY_COLOMBIA: return "DATUM BOGOTA OBSTY COLOMBIA";
            case DATUM_BUKIT_RIMPAH_INDONESIA_BANGKA_AND_BELITUNG_IDS: return "DATUM BUKIT RIMPAH INDONESIA BANGKA AND BELITUNG IDS";
            case DATUM_CAMP_AREA_ASTRO_ANTARCTICA_MCMURDO_CAMP_AREA: return "DATUM CAMP AREA ASTRO ANTARCTICA MCMURDO CAMP AREA";
            case DATUM_CAMPO_INCHAUSPE_ARGENTINA: return "DATUM CAMPO INCHAUSPE ARGENTINA";
            case DATUM_CANTON_ASTRO_1966_PHOENIX_ISLANDS: return "DATUM CANTON ASTRO 1966 PHOENIX ISLANDS";
            case DATUM_CAPE_SOUTH_AFRICA: return "DATUM CAPE SOUTH AFRICA";
            case DATUM_CAPE_CANAVERAL_BAHAMAS_FLORIDA: return "DATUM CAPE CANAVERAL BAHAMAS FLORIDA";
            case DATUM_CARTHAGE_TUNISIA: return "DATUM CARTHAGE TUNISIA";
            case DATUM_CHATHAM_ISD_A_71_NEW_ZEALAND_CHATHAM_ISLAND: return "DATUM CHATHAM ISD A 71 NEW ZEALAND CHATHAM ISLAND";
            case DATUM_CHUA_ASTRO_PARAGUAY: return "DATUM CHUA ASTRO PARAGUAY";
            case DATUM_CORREGO_ALEGRE_BRAZIL: return "DATUM CORREGO ALEGRE BRAZIL";
            case DATUM_DABOLA_GUINEA: return "DATUM DABOLA GUINEA";
            case DATUM_DECEPTION_ISLAND_DECEPTION_ISLAND_ANTARCTIA: return "DATUM DECEPTION ISLAND DECEPTION ISLAND ANTARCTIA";
            case DATUM_DJAKARTA_BATAVIA_INDONESIA_SUMATRA: return "DATUM DJAKARTA BATAVIA INDONESIA SUMATRA";
            case DATUM_DOS_1968_NEW_GEORGIA_ISLANDS_GIZO_ISLAND: return "DATUM DOS 1968 NEW GEORGIA ISLANDS GIZO ISLAND";
            case DATUM_EASTER_ISLAND_1967_EASTER_ISLAND: return "DATUM EASTER ISLAND 1967 EASTER ISLAND";
            case DATUM_ESTONIA_SYSTEM_37_ESTONIA: return "DATUM ESTONIA SYSTEM 37 ESTONIA";
            case DATUM_EUROPEAN_1950_CYPRUS: return "DATUM EUROPEAN 1950 CYPRUS";
            case DATUM_EUROPEAN_1950_EGYPT: return "DATUM EUROPEAN 1950 EGYPT";
            case DATUM_EUROPEAN_1950_ENGLAND_CHANNEL_ISLANDS_SCOTLAND_SHETLAND_ISLANDS: return "DATUM EUROPEAN 1950 ENGLAND CHANNEL ISLANDS SCOTLAND SHETLAND ISLANDS";
            case DATUM_EUROPEAN_1950_ENGLAND_IRELAND_SCOTLAND_SHETLAND_ISLANDS: return "DATUM EUROPEAN 1950 ENGLAND IRELAND SCOTLAND SHETLAND ISLANDS";
            case DATUM_EUROPEAN_1950_FINLAND_NORWAY: return "DATUM EUROPEAN 1950 FINLAND NORWAY";
            case DATUM_EUROPEAN_1950_GREECE: return "DATUM EUROPEAN 1950 GREECE";
            case DATUM_EUROPEAN_1950_IRAN: return "DATUM EUROPEAN 1950 IRAN";
            case DATUM_EUROPEAN_1950_ITALY_SARDINIA: return "DATUM EUROPEAN 1950 ITALY SARDINIA";
            case DATUM_EUROPEAN_1950_ITALY_SICILY: return "DATUM EUROPEAN 1950 ITALY SICILY";
            case DATUM_EUROPEAN_1950_MALTA: return "DATUM EUROPEAN 1950 MALTA";
            case DATUM_EUROPEAN_1950_MEAN_FOR_AUSTRIA_BELGIUM_DENMARK_FINLAND_ETC: return "DATUM EUROPEAN 1950 MEAN FOR AUSTRIA BELGIUM DENMARK FINLAND ETC";
            case DATUM_EUROPEAN_1950_MEAN_FOR_AUSTRIA_DENMARK_ETC: return "DATUM EUROPEAN 1950 MEAN FOR AUSTRIA DENMARK ETC";
            case DATUM_EUROPEAN_1950_MEAN_FOR_IRAQ_ISRAEL_JORDAN_ETC: return "DATUM EUROPEAN 1950 MEAN FOR IRAQ ISRAEL JORDAN ETC";
            case DATUM_EUROPEAN_1950_PORTUGAL_SPAIN: return "DATUM EUROPEAN 1950 PORTUGAL SPAIN";
            case DATUM_EUROPEAN_1950_TUNISIA: return "DATUM EUROPEAN 1950 TUNISIA";
            case DATUM_EUROPEAN_1979_SWITZERLAND: return "DATUM EUROPEAN 1979 SWITZERLAND";
            case DATUM_FORT_THOMAS_1955_NEVIS_ST_KITTS_LEEWARD_ISLANDS: return "DATUM FORT THOMAS 1955 NEVIS ST KITTS LEEWARD ISLANDS";
            case DATUM_GAN_1970_REPUBLIC_OF_MALDIVES: return "DATUM GAN 1970 REPUBLIC OF MALDIVES";
            case DATUM_GEOD_DAT_49_NEW_ZEALAND: return "DATUM GEOD DAT 49 NEW ZEALAND";
            case DATUM_GRACIOSA_SW_48_AZORES_FAIAL_GRACIOSA_PICO_SAO_JORGE_TERCEIRA: return "DATUM GRACIOSA SW 48 AZORES FAIAL GRACIOSA PICO SC3O JORGE TERCEIRA";
            case DATUM_GUAM_1963_GUAM: return "DATUM GUAM 1963 GUAM";
            case DATUM_GUNUNG_SEGARA_INDONESIA_KALIMANTAN: return "DATUM GUNUNG SEGARA INDONESIA KALIMANTAN";
            case DATUM_GUX_1_ASTRO_GUADALCANAL_ISLAND: return "DATUM GUX 1 ASTRO GUADALCANAL ISLAND";
            case DATUM_HERAT_NORTH_AFGHANISTAN: return "DATUM HERAT NORTH AFGHANISTAN";
            case DATUM_HERMANNSKOGEL_CROATIA_SERBIA_BOSNIA_HERZEGOVINA: return "DATUM HERMANNSKOGEL CROATIA SERBIA BOSNIA HERZEGOVINA";
            case DATUM_HJORSEY_1955_ICELAND: return "DATUM HJORSEY 1955 ICELAND";
            case DATUM_HONG_KONG_1963_HONG_KONG: return "DATUM HONG KONG 1963 HONG KONG";
            case DATUM_HU_TZU_SHAN_TAIWAN: return "DATUM HU TZU SHAN TAIWAN";
            case DATUM_INDIAN_BANGLADESH: return "DATUM INDIAN BANGLADESH";
            case DATUM_INDIAN_INDIA_NEPAL: return "DATUM INDIAN INDIA NEPAL";
            case DATUM_INDIAN_PAKISTAN: return "DATUM INDIAN PAKISTAN";
            case DATUM_INDIAN_1954_THAILAND: return "DATUM INDIAN 1954 THAILAND";
            case DATUM_INDIAN_1960_VIETNAM_COM_SON_ISLAND: return "DATUM INDIAN 1960 VIETNAM COM SON ISLAND";
            case DATUM_INDIAN_1960_VIETNAM_NEAR_16_N: return "DATUM INDIAN 1960 VIETNAM NEAR 16D0N";
            case DATUM_INDIAN_1975_THAILAND: return "DATUM INDIAN 1975 THAILAND";
            case DATUM_INDONESIAN_1974_INDONESIA: return "DATUM INDONESIAN 1974 INDONESIA";
            case DATUM_IRELAND_1965_IRELAND: return "DATUM IRELAND 1965 IRELAND";
            case DATUM_ISTS_061_68_SOUTHGEORGIA_ISLANDS: return "DATUM ISTS 061 68 SOUTHGEORGIA ISLANDS";
            case DATUM_ISTS_073_69_DIEGO_GARCIA: return "DATUM ISTS 073 69 DIEGO GARCIA";
            case DATUM_JOHNSTON_ISLD_61_JOHNSTON_ISLAND: return "DATUM JOHNSTON ISLD 61 JOHNSTON ISLAND";
            case DATUM_KANDAWALA_SRI_LANKA: return "DATUM KANDAWALA SRI LANKA";
            case DATUM_KERGUELEN_ISLD_49_KERGUELEN_ISLAND: return "DATUM KERGUELEN ISLD 49 KERGUELEN ISLAND";
            case DATUM_KERTAU_1948_WEST_MALAYSIA_AND_SINGAPORE: return "DATUM KERTAU 1948 WEST MALAYSIA AND SINGAPORE";
            case DATUM_KUSAIE_ASTRO1951_CAROLINE_ISLANDS: return "DATUM KUSAIE ASTRO1951 CAROLINE ISLANDS";
            case DATUM_KOREAN_SYSTEM_SOUTH_KOREA: return "DATUM KOREAN SYSTEM SOUTH KOREA";
            case DATUM_L_C_5_ASTRO_1961_CAYMAN_BRAC_ISLAND: return "DATUM L C 5 ASTRO 1961 CAYMAN BRAC ISLAND";
            case DATUM_LEIGON_GHANA: return "DATUM LEIGON GHANA";
            case DATUM_LIBERIA_1964_LIBERIA: return "DATUM LIBERIA 1964 LIBERIA";
            case DATUM_LUZON_PHILIPPINES_EXCLUDING_MINDANAO: return "DATUM LUZON PHILIPPINES EXCLUDING MINDANAO";
            case DATUM_LUZON_PHILIPPINES_MINDANAO: return "DATUM LUZON PHILIPPINES MINDANAO";
            case DATUM_M_PORALOKO_GABON: return "DATUM M PORALOKO GABON";
            case DATUM_MAHE_1971_MAHE_ISLAND: return "DATUM MAHE 1971 MAHE ISLAND";
            case DATUM_MASSAWA_ETHIOPIA_ERITREA: return "DATUM MASSAWA ETHIOPIA ERITREA";
            case DATUM_MERCHICH_MOROCCO: return "DATUM MERCHICH MOROCCO";
            case DATUM_MIDWAY_ASTRO_61_MIDWAY_ISLANDS: return "DATUM MIDWAY ASTRO 61 MIDWAY ISLANDS";
            case DATUM_MINNA_CAMEROON: return "DATUM MINNA CAMEROON";
            case DATUM_MINNA_NIGERIA: return "DATUM MINNA NIGERIA";
            case DATUM_MONTSERRAT_58_MONTSERRAT_LEEWARD_ISLANDS: return "DATUM MONTSERRAT 58 MONTSERRAT LEEWARD ISLANDS";
            case DATUM_NAHRWAN_OMAN_MASIRAH_ISLAND: return "DATUM NAHRWAN OMAN MASIRAH ISLAND";
            case DATUM_NAHRWAN_SAUDI_ARABIA: return "DATUM NAHRWAN SAUDI ARABIA";
            case DATUM_NAHRWAN_UNITED_ARAB_EMIRATES: return "DATUM NAHRWAN UNITED ARAB EMIRATES";
            case DATUM_NAPARIMA_BWI_TRINIDAD_TOBAGO: return "DATUM NAPARIMA BWI TRINIDAD TOBAGO";
            case DATUM_NAD_1927_ALASKA_EXCLUDING_ALEUTIAN_IDS: return "DATUM NAD 1927 ALASKA EXCLUDING ALEUTIAN IDS";
            case DATUM_NAD_1927_ALASKA_ALEUTIAN_IDS_EAST_OF_180W: return "DATUM NAD 1927 ALASKA ALEUTIAN IDS EAST OF 180W";
            case DATUM_NAD_1927_ALASKA_ALEUTIAN_IDS_WEST_OF_180W: return "DATUM NAD 1927 ALASKA ALEUTIAN IDS WEST OF 180W";
            case DATUM_NAD_1927_BAHAMAS_EXCEPT_SAN_SALVADOR_ID: return "DATUM NAD 1927 BAHAMAS EXCEPT SAN SALVADOR ID";
            case DATUM_NAD_1927_BAHAMAS_SAN_SALVADOR_ISLAND: return "DATUM NAD 1927 BAHAMAS SAN SALVADOR ISLAND";
            case DATUM_NAD_1927_CANADA_ALBERTA_BRITISH_COLUMBIA: return "DATUM NAD 1927 CANADA ALBERTA BRITISH COLUMBIA";
            case DATUM_NAD_1927_CANADA_MANITOBA_ONTARIO: return "DATUM NAD 1927 CANADA MANITOBA ONTARIO";
            case DATUM_NAD_1927_CANADA_NEW_BRUNSWICK_NEWFOUNDLAND_NOVA_SCOTIA_QUEBEC: return "DATUM NAD 1927 CANADA NEW BRUNSWICK NEWFOUNDLAND NOVA SCOTIA QUEBEC";
            case DATUM_NAD_1927_CANADA_NORTHWEST_TERRITORIES_SASKATCHEWAN: return "DATUM NAD 1927 CANADA NORTHWEST TERRITORIES SASKATCHEWAN";
            case DATUM_NAD_1927_CANADA_YUKON: return "DATUM NAD 1927 CANADA YUKON";
            case DATUM_NAD_1927_CANAL_ZONE: return "DATUM NAD 1927 CANAL ZONE";
            case DATUM_NAD_1927_CUBA: return "DATUM NAD 1927 CUBA";
            case DATUM_NAD_1927_GREENLAND_HAYES_PENINSULA: return "DATUM NAD 1927 GREENLAND HAYES PENINSULA";
            case DATUM_NAD_1927_MEAN_FOR_ANTIGUA_BARBADOS_BARBUDA_ETC: return "DATUM NAD 1927 MEAN FOR ANTIGUA BARBADOS BARBUDA ETC";
            case DATUM_NAD_1927_MEAN_FOR_BELIZE_COSTA_RICA_EL_SALVADOR_GUATEMALA_HONDURAS: return "DATUM NAD 1927 MEAN FOR BELIZE COSTA RICA EL SALVADOR GUATEMALA HONDURAS";
            case DATUM_NAD_1927_MEAN_FOR_CANADA: return "DATUM NAD 1927 MEAN FOR CANADA";
            case DATUM_NAD_1927_MEAN_FOR_CONUS: return "DATUM NAD 1927 MEAN FOR CONUS";
            case DATUM_NAD_1927_MEAN_FOR_CONUS_EAST_OF_MISSISSIPPI_RIVER_INCLUDING_LOUISIANA_MISSOURI_MINNESOTA: return "DATUM NAD 1927 MEAN FOR CONUS EAST OF MISSISSIPPI RIVER INCLUDING LOUISIANA MISSOURI MINNESOTA";
            case DATUM_NAD_1927_MEAN_FOR_CONUS_WEST_OF_MISSISSIPPI_RIVER_EXCLUDING_LOUISIANA_MINNESOTA_MISSOURI: return "DATUM NAD 1927 MEAN FOR CONUS WEST OF MISSISSIPPI RIVER EXCLUDING LOUISIANA MINNESOTA MISSOURI";
            case DATUM_NAD_27_MEXICO: return "DATUM NAD 27 MEXICO";
            case DATUM_NAD_83_ALASKA_EXCLUDING_ALEUTIAN_IDS: return "DATUM NAD 83 ALASKA EXCLUDING ALEUTIAN IDS";
            case DATUM_NAD_83_ALEUTIAN_IDS: return "DATUM NAD 83 ALEUTIAN IDS";
            case DATUM_NAD_83_CANADA: return "DATUM NAD 83 CANADA";
            case DATUM_NAD_83_CONUS: return "DATUM NAD 83 CONUS";
            case DATUM_NAD_83_HAWAII: return "DATUM NAD 83 HAWAII";
            case DATUM_NAD_83_MEXICO_CENTRAL_AMERICA: return "DATUM NAD 83 MEXICO CENTRAL AMERICA";
            case DATUM_NORTH_SAHARA_1959_ALGERIA: return "DATUM NORTH SAHARA 1959 ALGERIA";
            case DATUM_OBS_MET_1939_AZORES_CORVO_FLORES_ISLANDS: return "DATUM OBS MET 1939 AZORES CORVO FLORES ISLANDS";
            case DATUM_OLD_EGYPTIAN_1907_EGYPT: return "DATUM OLD EGYPTIAN 1907 EGYPT";
            case DATUM_OLD_HAWAIIAN_HAWAII: return "DATUM OLD HAWAIIAN HAWAII";
            case DATUM_OLD_HAWAIIAN_KAUAI: return "DATUM OLD HAWAIIAN KAUAI";
            case DATUM_OLD_HAWAIIAN_MAUI: return "DATUM OLD HAWAIIAN MAUI";
            case DATUM_OLD_HAWAIIAN_MEAN_FOR_HAWAII_KAUAI_MAUI_OAHU: return "DATUM OLD HAWAIIAN MEAN FOR HAWAII KAUAI MAUI OAHU";
            case DATUM_OLD_HAWAIIAN_OAHU: return "DATUM OLD HAWAIIAN OAHU";
            case DATUM_OMAN_OMAN: return "DATUM OMAN OMAN";
            case DATUM_OS_G_BRITAIN_36_ENGLAND: return "DATUM OS G BRITAIN 36 ENGLAND";
            case DATUM_OS_G_BRITAIN_36_ENGLAND_ISLE_OF_MAN_WALES: return "DATUM OS G BRITAIN 36 ENGLAND ISLE OF MAN WALES";
            case DATUM_OS_G_BRITAIN_36_MEAN_FOR_ENGLAND_ISLE_OF_MAN_ETC: return "DATUM OS G BRITAIN 36 MEAN FOR ENGLAND ISLE OF MAN ETC";
            case DATUM_OS_G_BRITAIN_36_SCOTLAND_SHETLAND_ISLANDS: return "DATUM OS G BRITAIN 36 SCOTLAND SHETLAND ISLANDS";
            case DATUM_OS_G_BRITAIN_36_WALES: return "DATUM OS G BRITAIN 36 WALES";
            case DATUM_PICO_DE_LAS_NIEVES_CANARY_ISLANDS: return "DATUM PICO DE LAS NIEVES CANARY ISLANDS";
            case DATUM_PITCAIRN_ASTRO_67_PITCAIRN_ISLAND: return "DATUM PITCAIRN ASTRO 67 PITCAIRN ISLAND";
            case DATUM_POINT_58_MEAN_FOR_BURKINA_FASO_NIGER: return "DATUM POINT 58 MEAN FOR BURKINA FASO NIGER";
            case DATUM_POINTE_NOIRE_1948_CONGO: return "DATUM POINTE NOIRE 1948 CONGO";
            case DATUM_PORTO_SANTO_1936_PORTO_SANTO_MADEIRA_ISLANDS: return "DATUM PORTO SANTO 1936 PORTO SANTO MADEIRA ISLANDS";
            case DATUM_PSA1956_BOLIVIA: return "DATUM PSA1956 BOLIVIA";
            case DATUM_PSA1956_CHILE_NORTHERN_NEAR_19S: return "DATUM PSA1956 CHILE NORTHERN NEAR 19S";
            case DATUM_PSA1956_CHILE_SOUTHERN_NEAR_43S: return "DATUM PSA1956 CHILE SOUTHERN NEAR 43S";
            case DATUM_PSA1956_COLOMBIA: return "DATUM PSA1956 COLOMBIA";
            case DATUM_PSA1956_ECUADOR: return "DATUM PSA1956 ECUADOR";
            case DATUM_PSA1956_GUYANA: return "DATUM PSA1956 GUYANA";
            case DATUM_PSA1956_MEAN_FOR_BOLIVIA_CHILE_COLOMBIA_ETC: return "DATUM PSA1956 MEAN FOR BOLIVIA CHILE COLOMBIA ETC";
            case DATUM_PSA1956_PERU: return "DATUM PSA1956 PERU";
            case DATUM_PSA1956_VENEZUELA: return "DATUM PSA1956 VENEZUELA";
            case DATUM_PS_CHILEAN_1963_CHILE_NEAR_53SHITO_XVIII: return "DATUM PS CHILEAN 1963 CHILE NEAR 53SHITO XVIII";
            case DATUM_PUERTO_RICO_PUERTO_RICO_VIRGIN_ISLANDS: return "DATUM PUERTO RICO PUERTO RICO VIRGIN ISLANDS";
            case DATUM_PULKOVO_1942_RUSSIA: return "DATUM PULKOVO 1942 RUSSIA";
            case DATUM_QATAR_NATIONAL_QATAR: return "DATUM QATAR NATIONAL QATAR";
            case DATUM_QORNOQ_GREENLAND_SOUTH: return "DATUM QORNOQ GREENLAND SOUTH";
            case DATUM_REUNION_MASCARENE_ISLANDS: return "DATUM REUNION MASCARENE ISLANDS";
            case DATUM_ROME_1940_ITALY_SARDINIA: return "DATUM ROME 1940 ITALY SARDINIA";
            case DATUM_S42_PULKOVO_42_HUNGARY: return "DATUM S42 PULKOVO 42 HUNGARY";
            case DATUM_S42_PULKOVO_42_POLAND: return "DATUM S42 PULKOVO 42 POLAND";
            case DATUM_S42_PULKOVO_42_CZECHOSLAVAKIA: return "DATUM S42 PULKOVO 42 CZECHOSLAVAKIA";
            case DATUM_S42_PULKOVO_42_LATVIA: return "DATUM S42 PULKOVO 42 LATVIA";
            case DATUM_S42_PULKOVO_42_KAZAKHSTAN: return "DATUM S42 PULKOVO 42 KAZAKHSTAN";
            case DATUM_S42_PULKOVO_42_ALBANIA: return "DATUM S42 PULKOVO 42 ALBANIA";
            case DATUM_S42_PULKOVO_42_ROMANIA: return "DATUM S42 PULKOVO 42 ROMANIA";
            case DATUM_S_JTSK_CZECHOSLAVAKIA_PRIOR_1_JAN_1993: return "DATUM S JTSK CZECHOSLAVAKIA PRIOR 1 JAN 1993";
            case DATUM_SANTO_DOS1965_ESPIRITO_SANTO_ISLAND: return "DATUM SANTO DOS1965 ESPIRITO SANTO ISLAND";
            case DATUM_SAO_BRAZ_AZORES_SAO_MIGUEL_SANTA_MARIA_IDS: return "DATUM SAO BRAZ AZORES SC3O MIGUEL SANTA MARIA IDS";
            case DATUM_SAPPER_HILL_1943_EAST_FALKLAND_ISLAND: return "DATUM SAPPER HILL 1943 EAST FALKLAND ISLAND";
            case DATUM_SCHWARZECK_NAMIBIA: return "DATUM SCHWARZECK NAMIBIA";
            case DATUM_SELVAGEM_G_38_SALVAGE_ISLANDS: return "DATUM SELVAGEM G 38 SALVAGE ISLANDS";
            case DATUM_SIERRA_LEONE_1960_SIERRA_LEONE: return "DATUM SIERRA LEONE 1960 SIERRA LEONE";
            case DATUM_SAD_1969_ARGENTINA: return "DATUM SAD 1969 ARGENTINA";
            case DATUM_SAD_1969_BOLIVIA: return "DATUM SAD 1969 BOLIVIA";
            case DATUM_SAD_1969_BRAZIL: return "DATUM SAD 1969 BRAZIL";
            case DATUM_SAD_1969_CHILE: return "DATUM SAD 1969 CHILE";
            case DATUM_SAD_1969_COLOMBIA: return "DATUM SAD 1969 COLOMBIA";
            case DATUM_SAD_1969_ECUADOR: return "DATUM SAD 1969 ECUADOR";
            case DATUM_SAD_1969_ECUADOR_BALTRA_GALAPAGOS: return "DATUM SAD 1969 ECUADOR BALTRA GALAPAGOS";
            case DATUM_SAD_1969_GUYANA: return "DATUM SAD 1969 GUYANA";
            case DATUM_SAD_1969_MEAN_MEAN_FOR_ARGENTINA_BOLIVIA_BRAZIL_ETC: return "DATUM SAD 1969 MEAN MEAN FOR ARGENTINA BOLIVIA BRAZIL ETC";
            case DATUM_SAD_1969_PARAGUAY: return "DATUM SAD 1969 PARAGUAY";
            case DATUM_SAD_1969_PERU: return "DATUM SAD 1969 PERU";
            case DATUM_SAD_1969_TRINIDAD_TOBAGO: return "DATUM SAD 1969 TRINIDAD TOBAGO";
            case DATUM_SAD_1969_VENEZUELA: return "DATUM SAD 1969 VENEZUELA";
            case DATUM_SOUTH_ASIA_SINGAPORE: return "DATUM SOUTH ASIA SINGAPORE";
            case DATUM_TANANARIVE_1925_MADAGASCAR: return "DATUM TANANARIVE 1925 MADAGASCAR";
            case DATUM_TIMBALAI_1948_BRUNEI_E_MALAYSIA_SABAH_SARAWAK: return "DATUM TIMBALAI 1948 BRUNEI E MALAYSIA SABAH SARAWAK";
            case DATUM_TOKYO_JAPAN: return "DATUM TOKYO JAPAN";
            case DATUM_TOKYO_MEAN_FOR_JAPAN_SOUTH_KOREA_OKINAWA: return "DATUM TOKYO MEAN FOR JAPAN SOUTH KOREA OKINAWA";
            case DATUM_TOKYO_OKINAWA: return "DATUM TOKYO OKINAWA";
            case DATUM_TOKYO_SOUTH_KOREA: return "DATUM TOKYO SOUTH KOREA";
            case DATUM_TRISTAN_ASTRO_68_TRISTAO_DA_CUNHA: return "DATUM TRISTAN ASTRO 68 TRISTC3O DA CUNHA";
            case DATUM_VITI_LEVU_1916_FIJI_VITI_LEVU_ISLAND: return "DATUM VITI LEVU 1916 FIJI VITI LEVU ISLAND";
            case DATUM_VOIROL_1960_ALGERIA: return "DATUM VOIROL 1960 ALGERIA";
            case DATUM_WAKE_ISLD_52_WAKE_ATOLL: return "DATUM WAKE ISLD 52 WAKE ATOLL";
            case DATUM_W_ENIWETOK_60_MARSHALL_ISLANDS: return "DATUM W ENIWETOK 60 MARSHALL ISLANDS";
            case DATUM_WGS_1972_GLOBAL_DEFINITION: return "DATUM WGS 1972 GLOBAL DEFINITION";
            case DATUM_WGS_1984_GLOBAL_DEFINITION: return "DATUM WGS 1984 GLOBAL DEFINITION";
            case DATUM_YACARE_URUGUAY: return "DATUM YACARE URUGUAY";
            case DATUM_ZANDERIJ_SURINAME: return "DATUM ZANDERIJ SURINAME";
            case DATUM_AMERSFOORT_NETHERLANDS: return "DATUM AMERSFOORT NETHERLANDS";
            case DATUM_FRENCH_NTF_FRANCE_NOUVELLE_TRIANGULATION_FRANCAISE: return "DATUM FRENCH NTF FRANCE NOUVELLE TRIANGULATION FRANCAISE";
            case DATUM_POTSDAM_GERMANY: return "DATUM POTSDAM GERMANY";
            case DATUM_RT_90_SWEDISH: return "DATUM RT 90 SWEDISH";
            case DATUM_CH_1903_SWISS: return "DATUM CH 1903 SWISS";
            case DATUM_AUSTRIA_AUSTRIA: return "DATUM AUSTRIA AUSTRIA";
            case DATUM_EUROPEAN_1950_BELGIUM: return "DATUM EUROPEAN 1950 BELGIUM";
            case DATUM_ISRAELI_ISRAELI: return "DATUM ISRAELI ISRAELI";
            case DATUM_ROME_1940_LUXEMBOURG: return "DATUM ROME 1940 LUXEMBOURG";
            case DATUM_FINLAND_HAYFORD_FINLAND: return "DATUM FINLAND HAYFORD FINLAND";
            case DATUM_DIONISOS_GREECE: return "DATUM DIONISOS GREECE";
            case DATUM_SAD_69_IBGE_BRAZIL: return "DATUM SAD 69 IBGE BRAZIL";
            case DATUM_POTSDAM_II_GEMANY: return "DATUM POTSDAM II GEMANY";
            case DATUM_DATUM_73_PORTUGAL: return "DATUM DATUM 73 PORTUGAL";
            case DATUM_WGS_1972_GPS_GLOBAL_DEFINITION: return "DATUM WGS 1972 GPS GLOBAL DEFINITION";
            case DATUM_ADINDAN_GPS_BURKINA_FASO: return "DATUM ADINDAN GPS BURKINA FASO";
            case DATUM_AIN_EL_ABD_1970_GPS_BAHRAIN: return "DATUM AIN EL ABD 1970 GPS BAHRAIN";
            case DATUM_ARC_1960_GPS_KENYA_AND_TAANZANIA: return "DATUM ARC 1960 GPS KENYA AND TAANZANIA";
            case DATUM_ASCENSION_ISLAND_1958_GPS_ASCENSION_ISLAND: return "DATUM ASCENSION ISLAND 1958 GPS ASCENSION ISLAND";
            case DATUM_BELGIUM_1950_GPS_BELGIUM: return "DATUM BELGIUM 1950 GPS BELGIUM";
            case DATUM_DANISH_1934_GPS_DENMARK: return "DATUM DANISH 1934 GPS DENMARK";
            case DATUM_HU_TZU_SHAN_GPS_TAIWAN: return "DATUM HU TZU SHAN GPS TAIWAN";
            case DATUM_INDIAN_BANGLADESH_GPS_BANGLADESH: return "DATUM INDIAN BANGLADESH GPS BANGLADESH";
            case DATUM_INDIAN_MAEN_GPS_MEAN_FOR_INDIA: return "DATUM INDIAN MAEN GPS MEAN FOR INDIA";
            case DATUM_INDIAN_THAILAND_GPS_MEAN_FOR_THAILAND: return "DATUM INDIAN THAILAND GPS MEAN FOR THAILAND";
            case DATUM_INDONESIAN_1974_GPS_MEAN_FOR_INDONESIA: return "DATUM INDONESIAN 1974 GPS MEAN FOR INDONESIA";
            case DATUM_JOHNSTON_ISLD_61_GPS_JOHNSTON_ISLAND: return "DATUM JOHNSTON ISLD 61 GPS JOHNSTON ISLAND";
            case DATUM_LUZON_MEAN_GPS_MEAN_FOR_PHILIPPINES: return "DATUM LUZON MEAN GPS MEAN FOR PHILIPPINES";
            case DATUM_NAD27_CARIBBEAN_GPS_MEAN_FOR_CARIBE: return "DATUM NAD27 CARIBBEAN GPS MEAN FOR CARIBE";
            case DATUM_NAHRWAN_SAUDI_ARABIA_GPS_MEAN_FOR_SAUDI_ARABIA: return "DATUM NAHRWAN SAUDI ARABIA GPS MEAN FOR SAUDI ARABIA";
            case DATUM_NAPARIMA_BWI_GPS_TRINIDAD_AND_TOBAGO: return "DATUM NAPARIMA BWI GPS TRINIDAD AND TOBAGO";
            case DATUM_NETHERLAND_TRI21_GPS_MEAN_FOR_NETHERLAND: return "DATUM NETHERLAND TRI21 GPS MEAN FOR NETHERLAND";
            case DATUM_NOU_TRIAG_FRANCE_GPS_FRANCE: return "DATUM NOU TRIAG FRANCE GPS FRANCE";
            case DATUM_NOU_TRIAG_LUXEMB_GPS_LUXEMBURG: return "DATUM NOU TRIAG LUXEMB GPS LUXEMBURG";
            case DATUM_OLD_HAWAIIAN_KAUAI_GPS_KAUAI_HAWAII: return "DATUM OLD HAWAIIAN KAUAI GPS KAUAI HAWAII";
            case DATUM_OLD_HAWAIIAN_MAUI_GPS_MAUI_HAWAII: return "DATUM OLD HAWAIIAN MAUI GPS MAUI HAWAII";
            case DATUM_OLD_HAWAIIAN_OAHU_GPS_OAHU_HAWAII: return "DATUM OLD HAWAIIAN OAHU GPS OAHU HAWAII";
            case DATUM_PORTUGAL_73_GPS_PORTUGAL: return "DATUM PORTUGAL 73 GPS PORTUGAL";
            case DATUM_RT_90_GPS_SWEDISH: return "DATUM RT 90 GPS SWEDISH";
            case DATUM_SAPPER_HILL_1943_GPS_EAST_FALKLAND_ISLAND: return "DATUM SAPPER HILL 1943 GPS EAST FALKLAND ISLAND";
            case DATUM_TIMBALAI_1948_GPS_MEAN_FOR_BRUNEI_E_MALAYSIA_SABAH_SARAWAK: return "DATUM TIMBALAI 1948 GPS MEAN FOR BRUNEI E MALAYSIA SABAH SARAWAK";
            case DATUM_TOKYO_MEAN_GPS_MEAN_FOR_JAPAN_SOUTH_KOREA_OKINAWA: return "DATUM TOKYO MEAN GPS MEAN FOR JAPAN SOUTH KOREA OKINAWA";
            case DATUM_WAKE_ENIWETOK_1960_GPS_MARSHALL_ISLANDS: return "DATUM WAKE ENIWETOK 1960 GPS MARSHALL ISLANDS";
            default: return "<UNKNOWN DATUM> CODE = " + String.valueOf(code);
        }
    }

    public boolean isGeographic() {
        return false;
    }

    @Override
    public String toString(){
        return "DATUM";
    }
}
