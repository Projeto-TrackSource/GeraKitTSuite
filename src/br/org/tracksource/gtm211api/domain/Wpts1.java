/*
 * Wpts1.java
 *
 * Criada em 25 de Dezembro de 2008, 15:13
 *
 */

package br.org.tracksource.gtm211api.domain;

import br.org.tracksource.gtm211api.Constants;
import br.org.tracksource.gtm211api.errors.ErrorIsNotZero;
import br.org.tracksource.gtm211api.errors.ErrorObjectNotDefined;
import br.org.tracksource.gtm211api.errors.ErrorStringIncorrectLength;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Comparator;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * The Wpts1 class represents a waypoint of the map.
 * The waypoint records begin following the image records of a GTM file per the 211 GTM format specification and appear
 * as many times as specified by the <code>nwpts</code> field of the <i>header</i> record.<br><br>
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class Wpts1 extends PointRecord{
    
    /**
     *Waypoint name.  It doesn't need to be unique.
     */
    protected String name;
    
    /**
     *Waypoint comments.
     */
    protected String wname;
    
    /**
     *Icon number. See <code>ICON_*</code> constants defined in this class. Default = 48.
     */
    protected short ico;
    
    /**
     *Display style. See <code>WPT_STYLE_*</code> constants for the predefined styles.
     *Values greater than 3 mean user defined styles available for GPS Trackmaker Pro (r) users
     * and defined by <i>stylefont1</i> records in the GTM file.
     */
    protected byte dspl;
    
    /**
     *Angle of rotation x 10. Example: 1800 = 180 degrees.
     */
    protected short wrot;
    
    /**
     *Reserved for future use. Must be zero.
     */
    protected short wlayer;

    /** Static array of the waypoint icons.
     *  This array is populated lasily, by demand of #getIcon() calls in the instances.
     */
    private static ImageIcon icons[] = new ImageIcon[274];
    
    /** Constructor. */
    public Wpts1(Map parent_map) throws ErrorObjectNotDefined {
        super(parent_map);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws ErrorStringIncorrectLength {
        if(name.length() != 10) throw new ErrorStringIncorrectLength(10, name.length());
        this.name = name;
    }

    public String getWname() {
        return wname;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }

    public short getIco() {
        return ico;
    }

    public void setIco(short ico) {
        this.ico = ico;
    }

    public byte getDspl() {
        return dspl;
    }

    public void setDspl(byte dspl) {
        this.dspl = dspl;
    }

    public short getWrot() {
        return wrot;
    }

    public void setWrot(short wrot) {
        this.wrot = wrot;
    }

    public short getWlayer() {
        return wlayer;
    }

    public void setWlayer(short wlayer) throws ErrorIsNotZero{
        if(wlayer != 0) throw new ErrorIsNotZero();
        this.wlayer = wlayer;
    }

    public void writeToStream(OutputStream stream) throws IOException {
        stream.write(this.convertDoubleTo8bytes(this.latitude));
        stream.write(this.convertDoubleTo8bytes(this.longitude));
        stream.write(this.convertStringToByteArray(this.name, false));
        stream.write(this.convertStringToByteArray(this.wname, true));
        stream.write(this.convertShortTo2bytes(this.ico));
        stream.write(this.dspl);
        stream.write(this.convertIntTo4bytes(this.date));
        stream.write(this.convertShortTo2bytes(this.wrot));
        stream.write(this.convertFloatTo4bytes(this.alt));
        stream.write(this.convertShortTo2bytes(this.wlayer));
    }

    /**
     * Returns the graphic icon used to represent this waypoint on maps and dialogs.
     * This is a convenience method wrapping a call to the #getIconGraphi(int) class method.
     */
    public Icon getIconGraphic(){
        return Wpts1.getIconGraphic(this.getIco());
    }

    /**
     * Returns the icon Image object used to represent this waypoint on maps.
     * This is a convenience method wrapping a call to the #getIconGraphi(int) class method.
     */
    public ImageIcon getIconImage(){
        return Wpts1.getIconGraphic(this.getIco());
    }
    
    /**
     * Returns the graphic icon used to represent the waypoint whose ico field falue is passed as parameter.
     * The stored ImageIcon objects has its Description property set to P+ico_field_value, such as W21, meaning
     * a waypoint icon whose code is 21.
     */
    public static ImageIcon getIconGraphic(int ico_field_value){
        //lasy loading of the waypoint icons
        if(Wpts1.icons[ico_field_value] == null){
            URL imgURL = Wpts1.class.getResource("waypoint_icons/" + String.valueOf(ico_field_value) + ".png");
            if(imgURL == null)
                imgURL = Wpts1.class.getResource("waypoint_icons/unknown.png");
            Wpts1.icons[ico_field_value] = new ImageIcon(imgURL);
            Wpts1.icons[ico_field_value].setDescription("W" + String.valueOf(ico_field_value));
        }
        return Wpts1.icons[ico_field_value];
    }

    @Override
    public String toString(){
        return String.format("POI:(%2.6f,%2.6f)-%s", this.getLatitude(), this.getLongitude(), this.getWname());
    }

    /**
     * True if this waypoint represents a city.
     */
    public boolean isCity(){
        return this.getIco() == Wpts1.ICON_CITY_LARGE  ||
               this.getIco() == Wpts1.ICON_CITY_MEDIUM ||
               this.getIco() == Wpts1.ICON_CITY_SMALL  ||
               this.getIco() == Wpts1.ICON_CITY_CAPITOL ;
    }

    public static ComparadorWpts1ByLatitude getComparadorWpts1ByLatitude(){
        return new ComparadorWpts1ByLatitude();
    }

    public static ComparadorWpts1ByWname getComparadorWpts1ByWname(){
        return new ComparadorWpts1ByWname();
    }
    
    /**Name with symbol (Icon). */
    public static final int WPT_STYLE_NAME_WITH_SYMBOL = 1;
    /** Symbol only */
    public static final int WPT_STYLE_SYMBOL_ONLY = 2;
    /** Comment with Symbol (Default) */
    public static final int WPT_STYLE_COMMENT_WITH_SYMBOL = 3;
    /** Comments in a Text Box (without symbol) */
    public static final int WPT_STYLE_COMMENTS_IN_A_TEXT_BOX = 4;
    
    /** Airport */
    public static final int ICON_AIRPORT = 1 ;
    /** Ball Park */
    public static final int ICON_BALL_PARK = 2 ;
    /** Bank */
    public static final int ICON_BANK = 3 ;
    /** Bar */
    public static final int ICON_BAR = 4 ;
    /** Boat Ramp */
    public static final int ICON_BOAT_RAMP = 5 ;
    /** Campground */
    public static final int ICON_CAMPGROUND = 6 ;
    /** Car */
    public static final int ICON_CAR = 7 ;
    /** City Large */
    public static final int ICON_CITY_LARGE = 8 ;
    /** City Medium */
    public static final int ICON_CITY_MEDIUM = 9 ;
    /** City small */
    public static final int ICON_CITY_SMALL = 10 ;
    /** Dam */
    public static final int ICON_DAM = 11 ;
    /** Danger Area */
    public static final int ICON_DANGER_AREA = 12 ;
    /** Drinking Water */
    public static final int ICON_DRINKING_WATER = 13 ;
    /** Fishing Area */
    public static final int ICON_FISHING_AREA = 14 ;
    /** Gas Station */
    public static final int ICON_GAS_STATION = 15 ;
    /** Glider Area */
    public static final int ICON_GLIDER_AREA = 16 ;
    /** Golf Course */
    public static final int ICON_GOLF_COURSE = 17 ;
    /** Heliport */
    public static final int ICON_HELIPORT = 18 ;
    /** Hotel */
    public static final int ICON_HOTEL = 19 ;
    /** Animals */
    public static final int ICON_ANIMALS = 20 ;
    /** Information */
    public static final int ICON_INFORMATION = 21 ;
    /** Man Overboard */
    public static final int ICON_MAN_OVERBOARD = 22 ;
    /** Marina */
    public static final int ICON_MARINA = 23 ;
    /** Mine */
    public static final int ICON_MINE = 24 ;
    /** Medical Facility */
    public static final int ICON_MEDICAL_FACILITY = 25 ;
    /** Parachute Area */
    public static final int ICON_PARACHUTE_AREA = 26 ;
    /** Park */
    public static final int ICON_PARK = 27 ;
    /** Parking Area */
    public static final int ICON_PARKING_AREA = 28 ;
    /** Picnic Area */
    public static final int ICON_PICNIC_AREA = 29 ;
    /** Private Field */
    public static final int ICON_PRIVATE_FIELD = 30 ;
    /** Residence */
    public static final int ICON_RESIDENCE = 31 ;
    /** Restaurant */
    public static final int ICON_RESTAURANT = 32 ;
    /** Restroom */
    public static final int ICON_RESTROOM = 33 ;
    /** Scenic Area */
    public static final int ICON_SCENIC_AREA = 34 ;
    /** School */
    public static final int ICON_BELL = 35 ;
    /** Seaplane Base */
    public static final int ICON_SEAPLANE_BASE = 36 ;
    /** Shipwreck */
    public static final int ICON_SHIPWRECK = 37 ;
    /** Shopping Center */
    public static final int ICON_SHOPPING_CENTER = 38 ;
    /** Short Tower */
    public static final int ICON_SHORT_TOWER = 39 ;
    /** Police */
    public static final int ICON_POLICE = 40 ;
    /** Skiing Area */
    public static final int ICON_SKIING_AREA = 41 ;
    /** Soft Field */
    public static final int ICON_SOFT_FIELD = 42 ;
    /** Swimming Area */
    public static final int ICON_SWIMMING_AREA = 43 ;
    /** Tall Tower */
    public static final int ICON_TALL_TOWER = 44 ;
    /** Telephone */
    public static final int ICON_TELEPHONE = 45 ;
    /** Tracback Point */
    public static final int ICON_TRACBACK_POINT = 46 ;
    /** Ultralight Area */
    public static final int ICON_ULTRALIGHT_AREA = 47 ;
    /** Waypoint */
    public static final int ICON_WAYPOINT = 48 ;
    /** Boat */
    public static final int ICON_BOAT = 49 ;
    /** Exit */
    public static final int ICON_EXIT = 50 ;
    /** Flag */
    public static final int ICON_FLAG = 51 ;
    /** Duck */
    public static final int ICON_DUCK = 52 ;
    /** Buoy */
    public static final int ICON_BUOY = 53 ;
    /** Back Track */
    public static final int ICON_BACK_TRACK = 54 ;
    /** Beach */
    public static final int ICON_BEACH = 55 ;
    /** Bridge */
    public static final int ICON_BRIDGE = 56 ;
    /** Building */
    public static final int ICON_BUILDING = 57 ;
    /** Car Repair */
    public static final int ICON_CAR_REPAIR = 58 ;
    /** Cemetary */
    public static final int ICON_CEMETARY = 59 ;
    /** Church */
    public static final int ICON_CHURCH = 60 ;
    /** Civil */
    public static final int ICON_CIVIL = 61 ;
    /** Convenience */
    public static final int ICON_CONVENIENCE = 62 ;
    /** Crossing */
    public static final int ICON_CROSSING = 63 ;
    /** Fast Food */
    public static final int ICON_FAST_FOOD = 64 ;
    /** Forest */
    public static final int ICON_FOREST = 65 ;
    /** Ghost Town */
    public static final int ICON_GHOST_TOWN = 66 ;
    /** Levee */
    public static final int ICON_LEVEE = 67 ;
    /** Military */
    public static final int ICON_MILITARY = 68 ;
    /** Oil Field */
    public static final int ICON_OIL_FIELD = 69 ;
    /** Post Office */
    public static final int ICON_POST_OFFICE = 70 ;
    /** Rv Park */
    public static final int ICON_RV_PARK = 71 ;
    /** Scales */
    public static final int ICON_SCALES = 72 ;
    /** Summit */
    public static final int ICON_SUMMIT = 73 ;
    /** Toll Booth */
    public static final int ICON_TOLL_BOOTH = 74 ;
    /** Trail Head */
    public static final int ICON_TRAIL_HEAD = 75 ;
    /** Truck Stop */
    public static final int ICON_TRUCK_STOP = 76 ;
    /** Tunnel */
    public static final int ICON_TUNNEL = 77 ;
    /** Highway */
    public static final int ICON_HIGHWAY = 78 ;
    /** Gate */
    public static final int ICON_GATE = 79 ;
    /** Fall */
    public static final int ICON_FALL = 80 ;
    /** Fence */
    public static final int ICON_FENCE = 81 ;
    /** Mata-Burro */
    public static final int ICON_MATA_BURRO = 82 ;
    /** Fitness Center */
    public static final int ICON_FITNESS_CENTER = 83 ;
    /** Movie Theater */
    public static final int ICON_MOVIE_THEATER = 84 ;
    /** Live Theater */
    public static final int ICON_LIVE_THEATER = 85 ;
    /** Zoo */
    public static final int ICON_ZOO = 86 ;
    /** Horn */
    public static final int ICON_HORN = 87 ;
    /** Bowling */
    public static final int ICON_BOWLING = 88 ;
    /** Car Rental */
    public static final int ICON_CAR_RENTAL = 89 ;
    /** City Capitol */
    public static final int ICON_CITY_CAPITOL = 90 ;
    /** Controlled Area */
    public static final int ICON_CONTROLLED_AREA = 91 ;
    /** Stadium */
    public static final int ICON_STADIUM = 92 ;
    /** Museum */
    public static final int ICON_MUSEUM = 93 ;
    /** Amusement Park */
    public static final int ICON_AMUSEMENT_PARK = 94 ;
    /** Skull */
    public static final int ICON_SKULL = 95 ;
    /** Department Store */
    public static final int ICON_DEPARTMENT_STORE = 96 ;
    /** Pharmacy */
    public static final int ICON_PHARMACY = 97 ;
    /** Pizza */
    public static final int ICON_PIZZA = 98 ;
    /** Diver Down */
    public static final int ICON_DIVER_DOWN = 99 ;
    /** Light */
    public static final int ICON_LIGHT = 100 ;
    /** Pin */
    public static final int ICON_PIN = 101 ;
    /** (Blank) */
    public static final int ICON_BLANK = 102 ;
    /** Pigsty */
    public static final int ICON_PIGSTY = 103 ;
    /** Tree */
    public static final int ICON_TREE = 104 ;
    /** Bamboo */
    public static final int ICON_BAMBOO = 105 ;
    /** Banana Plant */
    public static final int ICON_BANANA_PLANT = 106 ;
    /** Arrow-Down */
    public static final int ICON_ARROW_DOWN = 107 ;
    /** Bifurcation */
    public static final int ICON_BIFURCATION = 108 ;
    /** Cavern */
    public static final int ICON_CAVERN = 109 ;
    /** River */
    public static final int ICON_RIVER = 110 ;
    /** Rock */
    public static final int ICON_ROCK = 111 ;
    /** Arrow-up */
    public static final int ICON_ARROW_UP = 112 ;
    /** Trunk */
    public static final int ICON_TRUNK = 113 ;
    /** Soccer Field */
    public static final int ICON_SOCCER_FIELD = 114 ;
    /** Sporting Court */
    public static final int ICON_SPORTING_COURT = 115 ;
    /** Flag-Green */
    public static final int ICON_FLAG_GREEN = 116 ;
    /** Trench */
    public static final int ICON_TRENCH = 117 ;
    /** Ship-Yellow */
    public static final int ICON_SHIP_YELLOW = 118 ;
    /** Green Sign */
    public static final int ICON_GREEN_SIGN = 119 ;
    /** Swamp */
    public static final int ICON_SWAMP = 120 ;
    /** Lake */
    public static final int ICON_LAKE = 121 ;
    /** Stop! */
    public static final int ICON_STOP = 122 ;
    /** Fishing Area */
    public static final int ICON_FISHING_AREA_2 = 123 ;
    /** Speed Reducer */
    public static final int ICON_SPEED_REDUCER = 124 ;
    /** Stairway */
    public static final int ICON_STAIRWAY = 125 ;
    /** Cactus */
    public static final int ICON_CACTUS = 126 ;
    /** Ship-Red */
    public static final int ICON_SHIP_RED = 127 ;
    /** Letter - S */
    public static final int ICON_LETTER_S = 128 ;
    /** Letter - D */
    public static final int ICON_LETTER_D = 129 ;
    /** Letter - N */
    public static final int ICON_LETTER_N = 130 ;
    /** Crossing */
    public static final int ICON_CROSSING_2 = 131 ;
    /** Cross */
    public static final int ICON_CROSS = 132 ;
    /** Flag-Red */
    public static final int ICON_FLAG_RED = 133 ;
    /** Curve1 */
    public static final int ICON_CURVE1 = 134 ;
    /** Curve2 */
    public static final int ICON_CURVE2 = 135 ;
    /** Curve3 */
    public static final int ICON_CURVE3 = 136 ;
    /** Curve4 */
    public static final int ICON_CURVE4 = 137 ;
    /** Letter - W */
    public static final int ICON_LETTER_W = 138 ;
    /** Letter - L */
    public static final int ICON_LETTER_L = 139 ;
    /** Letter - R */
    public static final int ICON_LETTER_R = 140 ;
    /** Radio Beacon */
    public static final int ICON_RADIO_BEACON = 141 ;
    /** Road Sign */
    public static final int ICON_ROAD_SIGN = 142 ;
    /** Geocache */
    public static final int ICON_GEOCACHE = 143 ;
    /** Geocache Found */
    public static final int ICON_GEOCACHE_FOUND = 144 ;
    /** Traffic Light */
    public static final int ICON_TRAFFIC_LIGHT = 145 ;
    /** Bus Station */
    public static final int ICON_BUS_STATION = 146 ;
    /** Train Station */
    public static final int ICON_TRAIN_STATION = 147 ;
    /** School */
    public static final int ICON_SCHOOL = 148 ;
    /** Mile Marker */
    public static final int ICON_MILE_MARKER = 149 ;
    /** Conservation Area */
    public static final int ICON_CONSERVATION_AREA = 150 ;
    /** Magellan Waypoint */
    public static final int ICON_MAGELLAN_WAYPOINT = 151 ;
    /** Box */
    public static final int ICON_BOX = 152 ;
    /** Aerial */
    public static final int ICON_AERIAL = 153 ;
    /** Auto Repair */
    public static final int ICON_AUTO_REPAIR = 154 ;
    /** Boat (1) */
    public static final int ICON_BOAT_1 = 155 ;
    /** Exit Ramp */
    public static final int ICON_EXIT_RAMP = 156 ;
    /** Fixed Nav Aid */
    public static final int ICON_FIXED_NAV_AID = 157 ;
    /** Floating Buoy */
    public static final int ICON_FLOATING_BUOY = 158 ;
    /** Garden */
    public static final int ICON_GARDEN = 159 ;
    /** Fish Farm */
    public static final int ICON_FISH_FARM = 160 ;
    /** Lighthouse */
    public static final int ICON_LIGHTHOUSE = 161 ;
    /** Truck Service */
    public static final int ICON_TRUCK_SERVICE = 162 ;
    /** Resort */
    public static final int ICON_RESORT = 163 ;
    /** Scuba */
    public static final int ICON_SCUBA = 164 ;
    /** Shooting */
    public static final int ICON_SHOOTING = 165 ;
    /** Sight Seeing */
    public static final int ICON_SIGHT_SEEING = 166 ;
    /** Sounding */
    public static final int ICON_SOUNDING = 167 ;
    /** Winery */
    public static final int ICON_WINERY = 168 ;
    /** Navaid (Amber) */
    public static final int ICON_NAVAID_AMBER = 169 ;
    /** Navaid (Black) */
    public static final int ICON_NAVAID_BLACK = 170 ;
    /** Navaid (Blue) */
    public static final int ICON_NAVAID_BLUE = 171 ;
    /** Navaid (Green) */
    public static final int ICON_NAVAID_GREEN = 172 ;
    /** Navaid (Green;Red) */
    public static final int ICON_NAVAID_GREEN_RED = 173 ;
    /** Navaid (Green;White) */
    public static final int ICON_NAVAID_GREEN_WHITE = 174 ;
    /** Navaid (Orange) */
    public static final int ICON_NAVAID_ORANGE = 175 ;
    /** Navaid (Red) */
    public static final int ICON_NAVAID_RED = 176 ;
    /** Navaid (Red;Green) */
    public static final int ICON_NAVAID_RED_GREEN = 177 ;
    /** Navaid (Red;White) */
    public static final int ICON_NAVAID_RED_WHITE = 178 ;
    /** Navaid (Violet) */
    public static final int ICON_NAVAID_VIOLET = 179 ;
    /** Navaid (White) */
    public static final int ICON_NAVAID_WHITE = 180 ;
    /** Navaid (White;Green) */
    public static final int ICON_NAVAID_WHITE_GREEN = 181 ;
    /** Navaid (White;Red) */
    public static final int ICON_NAVAID_WHITE_RED = 182 ;
    /** Buoy (White) */
    public static final int ICON_BUOY_WHITE = 183 ;
    /** Dot (White) */
    public static final int ICON_DOT_WHITE = 184 ;
    /** Red Square */
    public static final int ICON_RED_SQUARE = 185 ;
    /** Red Diamond */
    public static final int ICON_RED_DIAMOND = 186 ;
    /** Green Square */
    public static final int ICON_GREEN_SQUARE = 187 ;
    /** Green Diamond */
    public static final int ICON_GREEN_DIAMOND = 188 ;
    /** Restricted Area */
    public static final int ICON_RESTRICTED_AREA = 189 ;
    /** Navaid (unlit) */
    public static final int ICON_NAVAID_UNLIT = 190 ;
    /** Dot (Small) */
    public static final int ICON_DOT_SMALL = 191 ;
    /** Libraries */
    public static final int ICON_LIBRARIES = 192 ;
    /** (Lowrance WPT) */
    public static final int ICON_LOWRANCE_WPT = 193 ;
    /** Lowrance WPT1 */
    public static final int ICON_LOWRANCE_WPT1 = 194 ;
    /** Lowrance WPT2 */
    public static final int ICON_LOWRANCE_WPT2 = 195 ;
    /** Mark (1) */
    public static final int ICON_MARK_1 = 196 ;
    /** Mark (2) */
    public static final int ICON_MARK_2 = 197 ;
    /** Mark (3) */
    public static final int ICON_MARK_3 = 198 ;
    /** Cross(Red) */
    public static final int ICON_CROSSRED = 199 ;
    /** Store */
    public static final int ICON_STORE = 200 ;
    /** Exclamation */
    public static final int ICON_EXCLAMATION = 201 ;
    /** Flag (EUA) */
    public static final int ICON_FLAG_EUA = 202 ;
    /** Flag (CAN) */
    public static final int ICON_FLAG_CAN = 203 ;
    /** Flag (BRA) */
    public static final int ICON_FLAG_BRA = 204 ;
    /** Man */
    public static final int ICON_MAN = 205 ;
    /** Animals (1) */
    public static final int ICON_ANIMALS_1 = 206 ;
    /** Deer Tracks */
    public static final int ICON_DEER_TRACKS = 207 ;
    /** Tree Stand */
    public static final int ICON_TREE_STAND = 208 ;
    /** Bridge(1) */
    public static final int ICON_BRIDGE1 = 209 ;
    /** Fence(1) */
    public static final int ICON_FENCE1 = 210 ;
    /** Intersection */
    public static final int ICON_INTERSECTION = 211 ;
    /** Non-Direct Beacon */
    public static final int ICON_NON_DIRECT_BEACON = 212 ;
    /** VHF Omni-Range */
    public static final int ICON_VHF_OMNI_RANGE = 213 ;
    /** Vor/Tacan */
    public static final int ICON_VOR_TACAN = 214 ;
    /** Vor-Dme */
    public static final int ICON_VOR_DME = 215 ;
    /** 1st Approach Fix */
    public static final int ICON_1ST_APPROACH_FIX = 216 ;
    /** Localizer Outer */
    public static final int ICON_LOCALIZER_OUTER = 217 ;
    /** Missed Appr. Pt */
    public static final int ICON_MISSED_APPR_PT = 218 ;
    /** Tacan */
    public static final int ICON_TACAN = 219 ;
    /** CheckPoint */
    public static final int ICON_CHECKPOINT = 220 ;
    /** Delegacy */
    public static final int ICON_DELEGACY = 232 ;
    /** Diver(1) */
    public static final int ICON_DIVER1 = 235 ;
    /** Blue Diamond */
    public static final int ICON_BLUE_DIAMOND = 259 ;

    public static final int ICON_FLAG_BLUE_2 = 221;
    public static final int ICON_FLAG_GREEN_2 = 222;
    public static final int ICON_FLAG_RED_2 = 223;
    public static final int ICON_INDICATOR_BLUE = 224;
    public static final int ICON_INDICATOR_GREEN = 225;
    public static final int ICON_INDICATOR_RED = 226;
    public static final int ICON_BOX_BLUE = 227;
    public static final int ICON_BOX_GREEN = 228;
    public static final int ICON_BOX_PINK = 229;
    public static final int ICON_SKIING_RESORT = 230;
    public static final int ICON_BIKE = 231;
    public static final int ICON_BULLDOZER = 233;
    public static final int ICON_ICE_SKATING = 234;
    public static final int ICON_RESTRICTED_AREA_2 = 236;
    public static final int ICON_CONTACT_AFRO = 237;
    public static final int ICON_CONTACT_ALIEN = 238;
    public static final int ICON_CONTACT_CAPITAIN = 239;
    public static final int ICON_CONTACT_BIG_EAR = 240;
    public static final int ICON_CONTACT_BIKER = 241;
    public static final int ICON_CONTACT_BUG = 242;
    public static final int ICON_CONTACT_CAT = 243;
    public static final int ICON_CONTACT_DOG = 244;
    public static final int ICON_CONTACT_UGLY = 245;
    public static final int ICON_CONTACT_WOMAN_1 = 246;
    public static final int ICON_CONTACT_WOMAN_2 = 247;
    public static final int ICON_CONTACT_WOMAN_3 = 248;
    public static final int ICON_CONTACT_MUSTACHE = 249;
    public static final int ICON_CONTACT_KUNG_FU = 250;
    public static final int ICON_CONTACT_PIRATE = 251;
    public static final int ICON_CONTACT_COP = 252;
    public static final int ICON_CONTACT_SMILEY = 253;
    public static final int ICON_CONTACT_SMART = 254;
    public static final int ICON_CONTACT_SUMO = 255;
    public static final int ICON_HYDRANT = 256;
    public static final int ICON_ANCHORING_PROHIBITED = 257;
    public static final int ICON_CIRCLE_WITH_X = 258;
    public static final int ICON_BEACON = 260;
    public static final int ICON_COASTAL_GUARD = 261;
    public static final int ICON_REEF = 262;
    public static final int ICON_AQUATIC_PLANT = 263;
    public static final int ICON_SCARP = 264;
    public static final int ICON_SHIPYARD = 265;
    public static final int ICON_HORN_1 = 266;
    public static final int ICON_HORN_2 = 267;
    public static final int ICON_HORN_3 = 268;
    public static final int ICON_HORN_4 = 269;
    public static final int ICON_HORN_5 = 270;
    public static final int ICON_HORN_6 = 271;
    public static final int ICON_HORN_7 = 272;
    public static final int ICON_HORN_8 = 273;


    /**
     * Wrapper of #getIconNamePTBR(int) for backward compatibility.
     * New code is encouraged to make calls to either #getIconNamePTBR(int) or
     * #getIconNameEN(int) or any other language-specific versions of this method
     * according to the user locale.
     */
    public static String getIconName(int iconCode){
        return Wpts1.getIconNamePTBR(iconCode);
    }

    /**
     * Returns the english icon name given its code according to the GTM file format specification.
     */
    public static String getIconNameEN(int iconCode){
        switch(iconCode){
            case ICON_AIRPORT:
                return "Airport";
            case ICON_BALL_PARK:
                return "Ball Park";
            case ICON_BANK:
                return "Bank";
            case ICON_BAR:
                return "Bar";
            case ICON_BOAT_RAMP:
                return "Boat Ramp";
            case ICON_CAMPGROUND:
                return "Campground";
            case ICON_CAR:
                return "Car";
            case ICON_CITY_LARGE:
                return "City Large";
            case ICON_CITY_MEDIUM:
                return "City Medium";
            case ICON_CITY_SMALL:
                return "City small";
            case ICON_DAM:
                return "Dam";
            case ICON_DANGER_AREA:
                return "Danger Area";
            case ICON_DRINKING_WATER:
                return "Drinking Water";
            case ICON_FISHING_AREA:
                return "Fishing Area";
            case ICON_GAS_STATION:
                return "Gas Station";
            case ICON_GLIDER_AREA:
                return "Glider Area";
            case ICON_GOLF_COURSE:
                return "Golf Course";
            case ICON_HELIPORT:
                return "Heliport";
            case ICON_HOTEL:
                return "Hotel";
            case ICON_ANIMALS:
                return "Animals";
            case ICON_INFORMATION:
                return "Information";
            case ICON_MAN_OVERBOARD:
                return "Man Overboard";
            case ICON_MARINA:
                return "Marina";
            case ICON_MINE:
                return "Mine";
            case ICON_MEDICAL_FACILITY:
                return "Medical Facility";
            case ICON_PARACHUTE_AREA:
                return "Parachute Area";
            case ICON_PARK:
                return "Park";
            case ICON_PARKING_AREA:
                return "Parking Area";
            case ICON_PICNIC_AREA:
                return "Picnic Area";
            case ICON_PRIVATE_FIELD:
                return "Private Field";
            case ICON_RESIDENCE:
                return "Residence";
            case ICON_RESTAURANT:
                return "Restaurant";
            case ICON_RESTROOM:
                return "Restroom";
            case ICON_SCENIC_AREA:
                return "Scenic Area";
            case ICON_BELL:
                return "Bell";
            case ICON_SEAPLANE_BASE:
                return "Seaplane Base";
            case ICON_SHIPWRECK:
                return "Shipwreck";
            case ICON_SHOPPING_CENTER:
                return "Shopping Center";
            case ICON_SHORT_TOWER:
                return "Short Tower";
            case ICON_POLICE:
                return "Police";
            case ICON_SKIING_AREA:
                return "Skiing Area";
            case ICON_SOFT_FIELD:
                return "Soft Field";
            case ICON_SWIMMING_AREA:
                return "Swimming Area";
            case ICON_TALL_TOWER:
                return "Tall Tower";
            case ICON_TELEPHONE:
                return "Telephone";
            case ICON_TRACBACK_POINT:
                return "Tracback Point";
            case ICON_ULTRALIGHT_AREA:
                return "Ultralight Area";
            case ICON_WAYPOINT:
                return "Waypoint";
            case ICON_BOAT:
                return "Boat";
            case ICON_EXIT:
                return "Exit";
            case ICON_FLAG:
                return "Flag";
            case ICON_DUCK:
                return "Duck";
            case ICON_BUOY:
                return "Buoy";
            case ICON_BACK_TRACK:
                return "Back Track";
            case ICON_BEACH:
                return "Beach";
            case ICON_BRIDGE:
                return "Bridge";
            case ICON_BUILDING:
                return "Building";
            case ICON_CAR_REPAIR:
                return "Car Repair";
            case ICON_CEMETARY:
                return "Cemetary";
            case ICON_CHURCH:
                return "Church";
            case ICON_CIVIL:
                return "Civil";
            case ICON_CONVENIENCE:
                return "Convenience";
            case ICON_CROSSING:
                return "Crossing";
            case ICON_FAST_FOOD:
                return "Fast Food";
            case ICON_FOREST:
                return "Forest";
            case ICON_GHOST_TOWN:
                return "Ghost Town";
            case ICON_LEVEE:
                return "Levee";
            case ICON_MILITARY:
                return "Military";
            case ICON_OIL_FIELD:
                return "Oil Field";
            case ICON_POST_OFFICE:
                return "Post Office";
            case ICON_RV_PARK:
                return "Rv Park";
            case ICON_SCALES:
                return "Scales";
            case ICON_SUMMIT:
                return "Summit";
            case ICON_TOLL_BOOTH:
                return "Toll Booth";
            case ICON_TRAIL_HEAD:
                return "Trail Head";
            case ICON_TRUCK_STOP:
                return "Truck Stop";
            case ICON_TUNNEL:
                return "Tunnel";
            case ICON_HIGHWAY:
                return "Highway";
            case ICON_GATE:
                return "Gate";
            case ICON_FALL:
                return "Fall";
            case ICON_FENCE:
                return "Fence";
            case ICON_MATA_BURRO:
                return "Mata-Burro";
            case ICON_FITNESS_CENTER:
                return "Fitness Center";
            case ICON_MOVIE_THEATER:
                return "Movie Theater";
            case ICON_LIVE_THEATER:
                return "Live Theater";
            case ICON_ZOO:
                return "Zoo";
            case ICON_HORN:
                return "Horn";
            case ICON_BOWLING:
                return "Bowling";
            case ICON_CAR_RENTAL:
                return "Car Rental";
            case ICON_CITY_CAPITOL:
                return "City Capitol";
            case ICON_CONTROLLED_AREA:
                return "Controlled Area";
            case ICON_STADIUM:
                return "Stadium";
            case ICON_MUSEUM:
                return "Museum";
            case ICON_AMUSEMENT_PARK:
                return "Amusement Park";
            case ICON_SKULL:
                return "Skull";
            case ICON_DEPARTMENT_STORE:
                return "Department Store";
            case ICON_PHARMACY:
                return "Pharmacy";
            case ICON_PIZZA:
                return "Pizza";
            case ICON_DIVER_DOWN:
                return "Diver Down";
            case ICON_LIGHT:
                return "Light";
            case ICON_PIN:
                return "Pin";
            case ICON_BLANK:
                return "Blank";
            case ICON_PIGSTY:
                return "Pigsty";
            case ICON_TREE:
                return "Tree";
            case ICON_BAMBOO:
                return "Bamboo";
            case ICON_BANANA_PLANT:
                return "Banana Plant";
            case ICON_ARROW_DOWN:
                return "Arrow-Down";
            case ICON_BIFURCATION:
                return "Bifurcation";
            case ICON_CAVERN:
                return "Cavern";
            case ICON_RIVER:
                return "River";
            case ICON_ROCK:
                return "Rock";
            case ICON_ARROW_UP:
                return "Arrow-up";
            case ICON_TRUNK:
                return "Trunk";
            case ICON_SOCCER_FIELD:
                return "Soccer Field";
            case ICON_SPORTING_COURT:
                return "Sporting Court";
            case ICON_FLAG_GREEN:
                return "Flag-Green";
            case ICON_TRENCH:
                return "Trench";
            case ICON_SHIP_YELLOW:
                return "Ship-Yellow";
            case ICON_GREEN_SIGN:
                return "Green Sign";
            case ICON_SWAMP:
                return "Swamp";
            case ICON_LAKE:
                return "Lake";
            case ICON_STOP:
                return "Stop!";
            case ICON_FISHING_AREA_2:
                return "Fishing Area";
            case ICON_SPEED_REDUCER:
                return "Speed Reducer";
            case ICON_STAIRWAY:
                return "Stairway";
            case ICON_CACTUS:
                return "Cactus";
            case ICON_SHIP_RED:
                return "Ship-Red";
            case ICON_LETTER_S:
                return "Letter - S";
            case ICON_LETTER_D:
                return "Letter - D";
            case ICON_LETTER_N:
                return "Letter - N";
            case ICON_CROSSING_2:
                return "Crossing";
            case ICON_CROSS:
                return "Cross";
            case ICON_FLAG_RED:
                return "Flag-Red";
            case ICON_CURVE1:
                return "Curve1";
            case ICON_CURVE2:
                return "Curve2";
            case ICON_CURVE3:
                return "Curve3";
            case ICON_CURVE4:
                return "Curve4";
            case ICON_LETTER_W:
                return "Letter - W";
            case ICON_LETTER_L:
                return "Letter - L";
            case ICON_LETTER_R:
                return "Letter - R";
            case ICON_RADIO_BEACON:
                return "Radio Beacon";
            case ICON_ROAD_SIGN:
                return "Road Sign";
            case ICON_GEOCACHE:
                return "Geocache";
            case ICON_GEOCACHE_FOUND:
                return "Geocache Found";
            case ICON_TRAFFIC_LIGHT:
                return "Traffic Light";
            case ICON_BUS_STATION:
                return "Bus Station";
            case ICON_TRAIN_STATION:
                return "Train Station";
            case ICON_SCHOOL:
                return "School";
            case ICON_MILE_MARKER:
                return "Mile Marker";
            case ICON_CONSERVATION_AREA:
                return "Conservation Area";
            case ICON_MAGELLAN_WAYPOINT:
                return "Magellan Waypoint";
            case ICON_BOX:
                return "Box";
            case ICON_AERIAL:
                return "Aerial";
            case ICON_AUTO_REPAIR:
                return "Auto Repair";
            case ICON_BOAT_1:
                return "Boat 1:";
            case ICON_EXIT_RAMP:
                return "Exit Ramp";
            case ICON_FIXED_NAV_AID:
                return "Fixed Nav Aid";
            case ICON_FLOATING_BUOY:
                return "Floating Buoy";
            case ICON_GARDEN:
                return "Garden";
            case ICON_FISH_FARM:
                return "Fish Farm";
            case ICON_LIGHTHOUSE:
                return "Lighthouse";
            case ICON_TRUCK_SERVICE:
                return "Truck Service";
            case ICON_RESORT:
                return "Resort";
            case ICON_SCUBA:
                return "Scuba";
            case ICON_SHOOTING:
                return "Shooting";
            case ICON_SIGHT_SEEING:
                return "Sight Seeing";
            case ICON_SOUNDING:
                return "Sounding";
            case ICON_WINERY:
                return "Winery";
            case ICON_NAVAID_AMBER:
                return "Navaid Amber";
            case ICON_NAVAID_BLACK:
                return "Navaid Black";
            case ICON_NAVAID_BLUE:
                return "Navaid Blue";
            case ICON_NAVAID_GREEN:
                return "Navaid Green";
            case ICON_NAVAID_GREEN_RED:
                return "Navaid Green Red";
            case ICON_NAVAID_GREEN_WHITE:
                return "Navaid Green White";
            case ICON_NAVAID_ORANGE:
                return "Navaid Orange";
            case ICON_NAVAID_RED:
                return "Navaid Red";
            case ICON_NAVAID_RED_GREEN:
                return "Navaid Red Green";
            case ICON_NAVAID_RED_WHITE:
                return "Navaid Red White";
            case ICON_NAVAID_VIOLET:
                return "Navaid Violet";
            case ICON_NAVAID_WHITE:
                return "Navaid White";
            case ICON_NAVAID_WHITE_GREEN:
                return "Navaid White Green";
            case ICON_NAVAID_WHITE_RED:
                return "Navaid White Red";
            case ICON_BUOY_WHITE:
                return "Buoy White";
            case ICON_DOT_WHITE:
                return "Dot White";
            case ICON_RED_SQUARE:
                return "Red Square";
            case ICON_RED_DIAMOND:
                return "Red Diamond";
            case ICON_GREEN_SQUARE:
                return "Green Square";
            case ICON_GREEN_DIAMOND:
                return "Green Diamond";
            case ICON_RESTRICTED_AREA:
                return "Restricted Area";
            case ICON_NAVAID_UNLIT:
                return "Navaid unlit";
            case ICON_DOT_SMALL:
                return "Dot Small";
            case ICON_LIBRARIES:
                return "Libraries";
            case ICON_LOWRANCE_WPT:
                return "Lowrance WPT";
            case ICON_LOWRANCE_WPT1:
                return "Lowrance WPT1";
            case ICON_LOWRANCE_WPT2:
                return "Lowrance WPT2";
            case ICON_MARK_1:
                return "Mark 1";
            case ICON_MARK_2:
                return "Mark 2";
            case ICON_MARK_3:
                return "Mark 3";
            case ICON_CROSSRED:
                return "Cross Red";
            case ICON_STORE:
                return "Store";
            case ICON_EXCLAMATION:
                return "Exclamation";
            case ICON_FLAG_EUA:
                return "Flag EUA";
            case ICON_FLAG_CAN:
                return "Flag CAN";
            case ICON_FLAG_BRA:
                return "Flag BRA";
            case ICON_MAN:
                return "Man";
            case ICON_ANIMALS_1:
                return "Animals 1";
            case ICON_DEER_TRACKS:
                return "Deer Tracks";
            case ICON_TREE_STAND:
                return "Tree Stand";
            case ICON_BRIDGE1:
                return "Bridge 1";
            case ICON_FENCE1:
                return "Fence 1";
            case ICON_INTERSECTION:
                return "Intersection";
            case ICON_NON_DIRECT_BEACON:
                return "Non-Direct Beacon";
            case ICON_VHF_OMNI_RANGE:
                return "VHF Omni-Range";
            case ICON_VOR_TACAN:
                return "Vor/Tacan";
            case ICON_VOR_DME:
                return "Vor-Dme";
            case ICON_1ST_APPROACH_FIX:
                return "1st Approach Fix";
            case ICON_LOCALIZER_OUTER:
                return "Localizer Outer";
            case ICON_MISSED_APPR_PT:
                return "Missed Appr. Pt";
            case ICON_TACAN:
                return "Tacan";
            case ICON_CHECKPOINT:
                return "CheckPoint";
            case ICON_DELEGACY:
                return "Delegacy";
            case ICON_DIVER1:
                return "Diver 1";
            case ICON_BLUE_DIAMOND:
                return "Blue Diamond";
            default:
                return "ICON CODE = " + String.valueOf(iconCode);
        }
    }

    /**
     * Returns the brazilian portuguese icon name given its code according to the GTM file format specification.
     */
    public static String getIconNamePTBR(int iconCode){
        switch(iconCode){
            case ICON_AIRPORT:
                return "Aeroporto";
            case ICON_BALL_PARK:
                return "Praça de Esportes";
            case ICON_BANK:
                return "Banco";
            case ICON_BAR:
                return "Bar";
            case ICON_BOAT_RAMP:
                return "Rampa de Barco";
            case ICON_CAMPGROUND:
                return "Acampamento";
            case ICON_CAR:
                return "Carro";
            case ICON_CITY_LARGE:
                return "Cidade Grande";
            case ICON_CITY_MEDIUM:
                return "Cidade Média";
            case ICON_CITY_SMALL:
                return "Cidade Pequena";
            case ICON_DAM:
                return "Barragem";
            case ICON_DANGER_AREA:
                return "Área de Perigo";
            case ICON_DRINKING_WATER:
                return "Água";
            case ICON_FISHING_AREA:
                return "Área de Pesca";
            case ICON_GAS_STATION:
                return "Combustível";
            case ICON_GLIDER_AREA:
                return "Rampa de Vôo";
            case ICON_GOLF_COURSE:
                return "Campo de Golf";
            case ICON_HELIPORT:
                return "Heliporto";
            case ICON_HOTEL:
                return "Hotel";
            case ICON_ANIMALS:
                return "Animais";
            case ICON_INFORMATION:
                return "Informações";
            case ICON_MAN_OVERBOARD:
                return "Mergulhador";
            case ICON_MARINA:
                return "Marina";
            case ICON_MINE:
                return "Mina";
            case ICON_MEDICAL_FACILITY:
                return "Hospital";
            case ICON_PARACHUTE_AREA:
                return "Área de Pouso";
            case ICON_PARK:
                return "Parque";
            case ICON_PARKING_AREA:
                return "Estacionamento";
            case ICON_PICNIC_AREA:
                return "Área de Picnic";
            case ICON_PRIVATE_FIELD:
                return "Área Particular";
            case ICON_RESIDENCE:
                return "Residência";
            case ICON_RESTAURANT:
                return "Restaurante";
            case ICON_RESTROOM:
                return "Banheiros";
            case ICON_SCENIC_AREA:
                return "Paisagem";
            case ICON_BELL:
                return "Sino";
            case ICON_SEAPLANE_BASE:
                return "Ancoradouro";
            case ICON_SHIPWRECK:
                return "Naufrágio";
            case ICON_SHOPPING_CENTER:
                return "Shopping Center";
            case ICON_SHORT_TOWER:
                return "Torre Baixa";
            case ICON_POLICE:
                return "Polícia";
            case ICON_SKIING_AREA:
                return "Área de Esqui";
            case ICON_SOFT_FIELD:
                return "Terreno Arenoso";
            case ICON_SWIMMING_AREA:
                return "Área de Banho";
            case ICON_TALL_TOWER:
                return "Torre Alta";
            case ICON_TELEPHONE:
                return "Telefone";
            case ICON_TRACBACK_POINT:
                return "Caminho de Volta";
            case ICON_ULTRALIGHT_AREA:
                return "Ultralight Area";
            case ICON_WAYPOINT:
                return "Waypoint";
            case ICON_BOAT:
                return "Barco";
            case ICON_EXIT:
                return "Saída";
            case ICON_FLAG:
                return "Bandeira";
            case ICON_DUCK:
                return "Ponto Principal";
            case ICON_BUOY:
                return "Bóia";
            case ICON_BACK_TRACK:
                return "Trilha de Volta";
            case ICON_BEACH:
                return "Praia";
            case ICON_BRIDGE:
                return "Ponte";
            case ICON_BUILDING:
                return "Prédio";
            case ICON_CAR_REPAIR:
                return "Oficina";
            case ICON_CEMETARY:
                return "Cemitério";
            case ICON_CHURCH:
                return "Igreja";
            case ICON_CIVIL:
                return "Civil";
            case ICON_CONVENIENCE:
                return "Conveniência";
            case ICON_CROSSING:
                return "Cruzamento";
            case ICON_FAST_FOOD:
                return "Fast Food";
            case ICON_FOREST:
                return "Floresta";
            case ICON_GHOST_TOWN:
                return "Cidade Fantasma";
            case ICON_LEVEE:
                return "Nível";
            case ICON_MILITARY:
                return "Área Militar";
            case ICON_OIL_FIELD:
                return "Campo de Óleo";
            case ICON_POST_OFFICE:
                return "Correios";
            case ICON_RV_PARK:
                return "Estac. Trailler";
            case ICON_SCALES:
                return "Fórum";
            case ICON_SUMMIT:
                return "Cume";
            case ICON_TOLL_BOOTH:
                return "Pedágio";
            case ICON_TRAIL_HEAD:
                return "Trilha a Frente";
            case ICON_TRUCK_STOP:
                return "Caminhões";
            case ICON_TUNNEL:
                return "Túnel";
            case ICON_HIGHWAY:
                return "Rodovia";
            case ICON_GATE:
                return "Porteira";
            case ICON_FALL:
                return "Cachoeira";
            case ICON_FENCE:
                return "Cerca";
            case ICON_MATA_BURRO:
                return "Mata-Burro";
            case ICON_FITNESS_CENTER:
                return "Academia";
            case ICON_MOVIE_THEATER:
                return "Cinema";
            case ICON_LIVE_THEATER:
                return "Teatro";
            case ICON_ZOO:
                return "Zoológico";
            case ICON_HORN:
                return "Alto-Falante";
            case ICON_BOWLING:
                return "Boliche";
            case ICON_CAR_RENTAL:
                return "Locadora";
            case ICON_CITY_CAPITOL:
                return "Cidade (Capital)";
            case ICON_CONTROLLED_AREA:
                return "Área Restrita";
            case ICON_STADIUM:
                return "Estádio";
            case ICON_MUSEUM:
                return "Museu";
            case ICON_AMUSEMENT_PARK:
                return "Pq Diversão";
            case ICON_SKULL:
                return "Cuidado!";
            case ICON_DEPARTMENT_STORE:
                return "Loja Depart.";
            case ICON_PHARMACY:
                return "Farmácia";
            case ICON_PIZZA:
                return "Pizzaria";
            case ICON_DIVER_DOWN:
                return "Mergulho";
            case ICON_LIGHT:
                return "Farol";
            case ICON_PIN:
                return "Alfinete";
            case ICON_BLANK:
                return "Sem Ícone";
            case ICON_PIGSTY:
                return "Chiqueiro";
            case ICON_TREE:
                return "Árvore";
            case ICON_BAMBOO:
                return "Bambuzal";
            case ICON_BANANA_PLANT:
                return "Bananal";
            case ICON_ARROW_DOWN:
                return "Seta-Desce";
            case ICON_BIFURCATION:
                return "Bifurcação";
            case ICON_CAVERN:
                return "Gruta";
            case ICON_RIVER:
                return "Rio";
            case ICON_ROCK:
                return "Rocha";
            case ICON_ARROW_UP:
                return "Seta-Sobe";
            case ICON_TRUNK:
                return "Tronco";
            case ICON_SOCCER_FIELD:
                return "Campo Futebol";
            case ICON_SPORTING_COURT:
                return "Quadra";
            case ICON_FLAG_GREEN:
                return "Bandeira Verde";
            case ICON_TRENCH:
                return "Vala";
            case ICON_SHIP_YELLOW:
                return "Nave Amarela";
            case ICON_GREEN_SIGN:
                return "Sinal Verde";
            case ICON_SWAMP:
                return "Brejo";
            case ICON_LAKE:
                return "Lagoa";
            case ICON_STOP:
                return "Pare!";
            case ICON_FISHING_AREA_2:
                return "Área de Pesca";
            case ICON_SPEED_REDUCER:
                return "Lombada";
            case ICON_STAIRWAY:
                return "Escada";
            case ICON_CACTUS:
                return "Cactus";
            case ICON_SHIP_RED:
                return "Nave Vermelha";
            case ICON_LETTER_S:
                return "Letra S";
            case ICON_LETTER_D:
                return "Letra D";
            case ICON_LETTER_N:
                return "Letra N";
            case ICON_CROSSING_2:
                return "Cruzamento";
            case ICON_CROSS:
                return "Cruz";
            case ICON_FLAG_RED:
                return "Bandeira (Verm)";
            case ICON_CURVE1:
                return "Curva1";
            case ICON_CURVE2:
                return "Curva2";
            case ICON_CURVE3:
                return "Curva3";
            case ICON_CURVE4:
                return "Curva4";
            case ICON_LETTER_W:
                return "Letra W";
            case ICON_LETTER_L:
                return "Letra L";
            case ICON_LETTER_R:
                return "Letra R";
            case ICON_RADIO_BEACON:
                return "Sinalizador";
            case ICON_ROAD_SIGN:
                return "Placa";
            case ICON_GEOCACHE:
                return "Geocache";
            case ICON_GEOCACHE_FOUND:
                return "Geocache Achado";
            case ICON_TRAFFIC_LIGHT:
                return "Semáforo";
            case ICON_BUS_STATION:
                return "Rodoviária";
            case ICON_TRAIN_STATION:
                return "Estação Ferroviária";
            case ICON_SCHOOL:
                return "Escola";
            case ICON_MILE_MARKER:
                return "Placa Km";
            case ICON_CONSERVATION_AREA:
                return "Área de Proteção";
            case ICON_MAGELLAN_WAYPOINT:
                return "WPT Magellan";
            case ICON_BOX:
                return "Caixa";
            case ICON_AERIAL:
                return "Aéreo";
            case ICON_AUTO_REPAIR:
                return "Oficina(1)";
            case ICON_BOAT_1:
                return "Barco(1)";
            case ICON_EXIT_RAMP:
                return "Rampa de Saída";
            case ICON_FIXED_NAV_AID:
                return "Auxílio Náut. Fixo";
            case ICON_FLOATING_BUOY:
                return "Bóia(1)";
            case ICON_GARDEN:
                return "Jardim";
            case ICON_FISH_FARM:
                return "Viveiro de Pesca";
            case ICON_LIGHTHOUSE:
                return "Farol(1)";
            case ICON_TRUCK_SERVICE:
                return "Reboque";
            case ICON_RESORT:
                return "Resort";
            case ICON_SCUBA:
                return "Mergulho(MGL)";
            case ICON_SHOOTING:
                return "Área de Tiro";
            case ICON_SIGHT_SEEING:
                return "Passeio Turístico";
            case ICON_SOUNDING:
                return "Som";
            case ICON_WINERY:
                return "Adegas";
            case ICON_NAVAID_AMBER:
                return "Navaid (Amarelo)";
            case ICON_NAVAID_BLACK:
                return "Navaid (Preto)";
            case ICON_NAVAID_BLUE:
                return "Navaid (Azul)";
            case ICON_NAVAID_GREEN:
                return "Navaid (Verde)";
            case ICON_NAVAID_GREEN_RED:
                return "Navaid (Verd-Verm)";
            case ICON_NAVAID_GREEN_WHITE:
                return "Navaid (Verd-Bran)";
            case ICON_NAVAID_ORANGE:
                return "Navaid (Laranja)";
            case ICON_NAVAID_RED:
                return "Navaid (Vermelho)";
            case ICON_NAVAID_RED_GREEN:
                return "Navaid (Verm-Verd)";
            case ICON_NAVAID_RED_WHITE:
                return "Navaid (Verm-Bran)";
            case ICON_NAVAID_VIOLET:
                return "Navaid (Violeta)";
            case ICON_NAVAID_WHITE:
                return "Navaid (Branco)";
            case ICON_NAVAID_WHITE_GREEN:
                return "Navaid (Bran-Verd)";
            case ICON_NAVAID_WHITE_RED:
                return "Navaid (Bran-Verm)";
            case ICON_BUOY_WHITE:
                return "Bóia (Branca)";
            case ICON_DOT_WHITE:
                return "Ponto Branco";
            case ICON_RED_SQUARE:
                return "Bandeira (Verm)";
            case ICON_RED_DIAMOND:
                return "Diamante Vermelho";
            case ICON_GREEN_SQUARE:
                return "Bandeira (Verde)";
            case ICON_GREEN_DIAMOND:
                return "Diamante Verde";
            case ICON_RESTRICTED_AREA:
                return "Área Restrita";
            case ICON_NAVAID_UNLIT:
                return "Navaid Apagado";
            case ICON_DOT_SMALL:
                return "Ponto Pequeno";
            case ICON_LIBRARIES:
                return "Biblioteca";
            case ICON_LOWRANCE_WPT:
                return "WPT Lowrance";
            case ICON_LOWRANCE_WPT1:
                return "WPT Lowrance1";
            case ICON_LOWRANCE_WPT2:
                return "WPT Lowrance2";
            case ICON_MARK_1:
                return "Marcação(1)";
            case ICON_MARK_2:
                return "Marcação(2)";
            case ICON_MARK_3:
                return "Marcação(3)";
            case ICON_CROSSRED:
                return "Cruz(1)";
            case ICON_STORE:
                return "Loja";
            case ICON_EXCLAMATION:
                return "Exclamação";
            case ICON_FLAG_EUA:
                return "Bandeira(EUA)";
            case ICON_FLAG_CAN:
                return "Bandeira(CAN)";
            case ICON_FLAG_BRA:
                return "Bandeira(BRA)";
            case ICON_MAN:
                return "Homens";
            case ICON_ANIMALS_1:
                return "Animais(1)";
            case ICON_DEER_TRACKS:
                return "Trilha de Cervo";
            case ICON_TREE_STAND:
                return "Piso sobre Árvore";
            case ICON_BRIDGE1:
                return "Ponte(1)";
            case ICON_FENCE1:
                return "Cerca(1)";
            case ICON_INTERSECTION:
                return "Interseção";
            case ICON_NON_DIRECT_BEACON:
                return "Beacon Difuso";
            case ICON_VHF_OMNI_RANGE:
                return "VHF Omni-Range";
            case ICON_VOR_TACAN:
                return "Vor/Tacan";
            case ICON_VOR_DME:
                return "Vor-Dme";
            case ICON_1ST_APPROACH_FIX:
                return "Fixo Aproximação";
            case ICON_LOCALIZER_OUTER:
                return "Marcador Externo";
            case ICON_MISSED_APPR_PT:
                return "Ponto de Arremesso";
            case ICON_TACAN:
                return "Tacan";
            case ICON_CHECKPOINT:
                return "Checado";
            case ICON_DELEGACY:
                return "Delegacia";
            case ICON_DIVER1:
                return "Mergulhador(1)";
            case ICON_BLUE_DIAMOND:
                return "Diamante Azul";
            case ICON_FLAG_BLUE_2: return "Bandeira (Azul)";
            case ICON_FLAG_GREEN_2: return "Bandeira (Verde)";
            case ICON_FLAG_RED_2: return "Bandeira (Vermelha)";
            case ICON_INDICATOR_BLUE: return "Indicador (Azul)";
            case ICON_INDICATOR_GREEN: return "Indicador (Verde)";
            case ICON_INDICATOR_RED: return "Indicador (Vermelho)";
            case ICON_BOX_BLUE: return "Caixa (Azul)";
            case ICON_BOX_GREEN: return "Caixa (Verde)";
            case ICON_BOX_PINK: return "Caixa (Rosa)";
            case ICON_SKIING_RESORT: return "Resort de esqui";
            case ICON_BIKE: return "Bicicleta";
            case ICON_BULLDOZER: return "Trator";
            case ICON_ICE_SKATING: return "Patinacao";
            case ICON_RESTRICTED_AREA_2: return "Area restrita 2";
            case ICON_CONTACT_AFRO: return "Contato afro";
            case ICON_CONTACT_ALIEN: return "Contato alien";
            case ICON_CONTACT_CAPITAIN: return "Contato capitao";
            case ICON_CONTACT_BIG_EAR: return "Contato orelhudo";
            case ICON_CONTACT_BIKER: return "Contato biker";
            case ICON_CONTACT_BUG: return "Contato bug";
            case ICON_CONTACT_CAT: return "Contato gato";
            case ICON_CONTACT_DOG: return "Contato cao";
            case ICON_CONTACT_UGLY: return "Contato feio";
            case ICON_CONTACT_WOMAN_1: return "Contato mulher 1";
            case ICON_CONTACT_WOMAN_2: return "Contato mulher 2";
            case ICON_CONTACT_WOMAN_3: return "Contato mulher 3";
            case ICON_CONTACT_MUSTACHE: return "Contato cavanhaque";
            case ICON_CONTACT_KUNG_FU: return "Contato kung-fu";
            case ICON_CONTACT_PIRATE: return "Contato pirata";
            case ICON_CONTACT_COP: return "Contato policial";
            case ICON_CONTACT_SMILEY: return "Contato risonho";
            case ICON_CONTACT_SMART: return "Contato esperto";
            case ICON_CONTACT_SUMO: return "Contato sumo";
            case ICON_HYDRANT: return "Hidrante";
            case ICON_ANCHORING_PROHIBITED: return "Proibido ancorar";
            case ICON_CIRCLE_WITH_X: return "Circulo com X";
            case ICON_BEACON: return "Beacon";
            case ICON_COASTAL_GUARD: return "Guarda costeira";
            case ICON_REEF: return "Arrecife";
            case ICON_AQUATIC_PLANT: return "Planta aquatica";
            case ICON_SCARP: return "Escarpa";
            case ICON_SHIPYARD: return "Estaleiro";
            case ICON_HORN_1: return "Buzzer 1";
            case ICON_HORN_2: return "Buzzer 2";
            case ICON_HORN_3: return "Buzzer 3";
            case ICON_HORN_4: return "Buzzer 4";
            case ICON_HORN_5: return "Buzzer 5";
            case ICON_HORN_6: return "Buzzer 6";
            case ICON_HORN_7: return "Buzzer 7";
            case ICON_HORN_8: return "Buzzer 8";
            default:
                return "ICON CODE = " + String.valueOf(iconCode);
        }
    }

}

/**
 * This class is a [code]Comparator[/code] of two waypoints by their latitudes.
 */
class ComparadorWpts1ByLatitude implements Comparator<Wpts1>{
    public int compare(Wpts1 wpa, Wpts1 wpb) {
        if(wpa.getLatitude() < wpb.getLatitude())
            return -1;
        else if(wpa.getLatitude() > wpb.getLatitude())
            return 1;
        else
            return 0;
    }
}

/**
 * This class is a [code]Comparator[/code] of two waypoints by their comments (wname).
 */
class ComparadorWpts1ByWname implements Comparator<Wpts1>{
    public int compare(Wpts1 wpa, Wpts1 wpb) {
        return wpa.getWname().compareTo(wpb.getWname());
    }
}
