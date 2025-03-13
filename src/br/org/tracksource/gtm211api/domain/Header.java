/*
 * Header.java
 *
 * Criada em 9 de Dezembro de 2008, 20:04
 *
 */

package br.org.tracksource.gtm211api.domain;

import br.org.tracksource.gtm211api.errors.ErrorCodeIsNotTrackMaker;
import br.org.tracksource.gtm211api.errors.ErrorGTMRule;
import br.org.tracksource.gtm211api.errors.ErrorIsNotFalse;
import br.org.tracksource.gtm211api.errors.ErrorIsNotZero;
import br.org.tracksource.gtm211api.errors.ErrorObjectNotDefined;
import br.org.tracksource.gtm211api.errors.ErrorStringNotEmpty;
import br.org.tracksource.gtm211api.errors.ErrorValueOutOfRange;
import br.org.tracksource.gtm211api.errors.ErrorVersionIsNot211;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

/**
 * The Header class encompasses the map properties and other information about the map such as datum used, number of waypoints,
 * number of tracklogs, etc.  The header is the first record of a GTM file per the 211 GTM format specification and appears just once.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class Header extends Record{
    
    protected short version;
    protected String code;
    protected byte wli;
    protected byte vwt;
    protected byte gradnum;
    protected byte wptnum;
    protected byte usernum;
    protected byte coriml;
    protected byte ndatum;
    protected int gradcolor;
    protected int bcolor;
    protected int usercolor;
    protected float maxlon;
    protected float minlon;
    protected float maxlat;
    protected float minlat;
    protected float layers;
    protected float iconnum;
    protected boolean rectangular;
    protected boolean truegrid;
    protected int labelcolor;
    protected boolean usernegrit;
    protected boolean useritalic;
    protected boolean usersublin;
    protected boolean usertachado;
    protected boolean map;
    protected short labelsize;
    protected String gradfont;
    protected String labelfont;
    protected String userfont;
    protected String newdatum;
    
    
    /** Default constructor TODO = set default values to properties for this constructor.*/
    public Header(Map parent_map) throws ErrorObjectNotDefined {
        super(parent_map);
    }

    /**
     *Version of GTM format used in the GTM file. Must be <b><code>211</code></b> for 211 version.  Client applications
     * must use this property to check against prior format versions which can cause incorrect reading.
     */
    public short getVersion() {
        return version;
    }
    
    /**
     * See {@link #getVersion()}.  Throws {@link ErrorVersionIsNot211} upon attempt of setting a version different from <b><code>211</code></b>.
     */
    public void setVersion(short version) throws ErrorVersionIsNot211{
        if(version != 211) throw new ErrorVersionIsNot211();
        this.version = version;
    }

    /**
     *Must be <b><code>"TrackMaker"</code></b>.
     */
    public String getCode() {
        return code;
    }

    /**
     * See {@link #getCode()}.  Throws {@link ErrorCodeIsNotTrackMaker} upon attempt of setting a code different from <b><code>"TrackMaker"</code></b>.
     */
    public void setCode(String code) throws ErrorCodeIsNotTrackMaker {
        if(!code.equals("TrackMaker")) throw new ErrorCodeIsNotTrackMaker();
        this.code = code;
    }

    /**
     *Draws thick lines on maps.
     */
    public byte getWli() {
        return wli;
    }

    /**
     *See {@link #getWli()}.
     */
    public void setWli(byte wli) {
        this.wli = wli;
    }

    /**
     *Reserved for future use.  Must be <b><code>0</code></b>.
     */
    public byte getVwt() {
        return vwt;
    }

    /**
     *See {@link #getVwt()}.  Throws {@link ErrorIsNotZero} upon attempt of setting a value different from <b><code>0</code></b>.
     */
    public void setVwt(byte vwt) throws ErrorIsNotZero  {
        if(vwt != (byte)(0)) throw new ErrorIsNotZero();
        this.vwt = vwt;
    }

    /**
     *Font size of the Grid Lines. Defauts to <b><code>8</code></b>.
     */
    public byte getGradnum() {
        return gradnum;
    }

    /**
     *See {@link #getGradnum()}.
     */
    public void setGradnum(byte gradnum) {
        this.gradnum = gradnum;
    }

    /**
     *Reserved for future use.  Must be <b><code>0</code></b>.
     */
    public byte getWptnum() {
        return wptnum;
    }

    /**
     *See {@link #getWptnum()}. The specification says it must be <b><code>0</code></b>, but some applications are known to
     * use this field to store information.
     */
    public void setWptnum(byte wptnum) {
        this.wptnum = wptnum;
    }

    /**
     *Reserved for future use.  It is supposed to be <b><code>0</code></b>.
     */
    public byte getUsernum() {
        return usernum;
    }

    /**
     *See {@link #getUsernum()}.
     */
    public void setUsernum(byte usernum){
        this.usernum = usernum;
    }

    /**
     *Icon color:<br>
     *0 - black (Default) : used with clear background colors<br>
     *1 - white : used with dark background colors
     */
    public byte getCoriml() {
        return coriml;
    }

    /**
     *See {@link #getCoriml()}. Throws {@link ErrorValueOutOfRange} upon attempt of setting an invalid value.
     */
    public void setCoriml(byte coriml) throws ErrorValueOutOfRange {
        if(coriml == (byte)0 || coriml == (byte)1)
            this.coriml = coriml;
        else
            throw new ErrorValueOutOfRange("0 or 1", String.valueOf(coriml));
    }

    /**
     *Reserved for future use.  Must be <b><code>0</code></b>.
     */
    public byte getNdatum() {
        return ndatum;
    }

    /**
     *See {@link #getNdatum()}.   Throws {@link ErrorIsNotZero} upon attempt of setting a value different from <b><code>0</code></b>.
     */
    public void setNdatum(byte ndatum) throws ErrorIsNotZero {
        if(ndatum != (byte)(0)) throw new ErrorIsNotZero();
        this.ndatum = ndatum;
    }

    /**
     *Grid color. RGB. Default = 0 (Black).  Actually it is BGR, for instance red is 255.
     */
    public int getGradcolor() {
        return gradcolor;
    }

    /**
     *See {@link #getGradcolor()}.
     */
    public void setGradcolor(int gradcolor) {
        this.gradcolor = gradcolor;
    }

    /**
     *Background color. RGB. Default = 16777215 (White). Actually it is BGR, for instance red is 255.
     */
    public int getBcolor() {
        return bcolor;
    }

    /**
     *See {@link #getBcolor()}.
     */
    public void setBcolor(int bcolor) {
        this.bcolor = bcolor;
    }

    /**
     *Number of Waypoint Styles.  Defines the number of {@link Stylefont1} objects in the {@link Map#colStylefont1} colletion.
     *It is at least 4, even if there is less than four styles.
     */
    public int getNwptstyles() {
        if(this.parentMapReference.getStylefont1Count() < 4)
            return 4;
        else
            return this.parentMapReference.getStylefont1Count();
    }

    /**
     *Text Color of default Waypoint. RGB. Default = 0 (Black).  Actually it is BGR, for instance red is 255.
     */
    public int getUsercolor() {
        return usercolor;
    }

    /**
     *See {@link #getUsercolor()}.
     */
    public void setUsercolor(int usercolor) {
        this.usercolor = usercolor;
    }

    /**
     *Number of waypoints. Returns the number {@link Wpts1} objects in the {@link Map#colWpts1} collection.
     */
    public int getNwpts() {
        return this.parentMapReference.getWpts1Count();
    }

    /**
     *Number of Trackpoints. Returns the sum of counts of {@link Trcks1} objects in each {@link Trknome1#colTrcks1} collections.<br>
     *NOTE: the Ntrcks field cannot be set, although present as data in the GTM file, because it must vary with the
     *total count of trackpoints of each tracklog according to the various changes during map processing.<br>
     *<b>ATTENTION:</b> Caution when storing the value returned of this method in a variable for later usage, since the
     *number of track points may change over time due to map processing/editing. It is always advisable to call this method instead of
     *storing its return value unless you make sure there will be no trackpoint removal or addition.
     */
    public int getNtrcks() {
        int res = 0;
        Iterator<Trknome1> itracklog = this.parentMapReference.getTracklogHeaderIterator();
        while(itracklog.hasNext())
            res += itracklog.next().getTrcks1Count();
        return res;
    }

    /**
     *Number of RoutePoints. Returns the number of {@link Rtes1} objects in the {@link Map#colRtes1} collection.
     */
    public int getNrtes() {
        return this.parentMapReference.getRtes1Count();
    }

    /**
     *Bounding Box that stores the actual extent of the data in the file. Represents the minimum-bounding
     *rectangle orthogonal to the X and Y axes that contains all shapes. Basically used by the <i>Catalog of
     *Images</i>.
     *@see #getMinlon()
     *@see #getMaxlat()
     *@see #getMinlat()
     */
    public float getMaxlon() {
        return maxlon;
    }

    /**
     *See {@link #getMaxlon()}.
     */
    public void setMaxlon(float maxlon) {
        this.maxlon = maxlon;
    }

    /**
     *Bounding Box that stores the actual extent of the data in the file. Represents the minimum-bounding
     *rectangle orthogonal to the X and Y axes that contains all shapes. Basically used by the <i>Catalog of
     *Images</i>.
     *@see #getMaxlon()
     *@see #getMaxlat()
     *@see #getMinlat()
     */
    public float getMinlon() {
        return minlon;
    }

    /**
     *See {@link #getMinlon()}.
     */
    public void setMinlon(float minlon) {
        this.minlon = minlon;
    }

    /**
     *Bounding Box that stores the actual extent of the data in the file. Represents the minimum-bounding
     *rectangle orthogonal to the X and Y axes that contains all shapes. Basically used by the <i>Catalog of
     *Images</i>.
     *@see #getMaxlon()
     *@see #getMinlon()
     *@see #getMinlat()
     */
    public float getMaxlat() {
        return maxlat;
    }

    /**
     *See {@link #getMaxlat()}.
     */
    public void setMaxlat(float maxlat) {
        this.maxlat = maxlat;
    }

    /**
     *Bounding Box that stores the actual extent of the data in the file. Represents the minimum-bounding
     *rectangle orthogonal to the X and Y axes that contains all shapes. Basically used by the <i>Catalog of
     *Images</i>.
     *@see #getMaxlon()
     *@see #getMinlon()
     *@see #getMaxlat()
     */
    public float getMinlat() {
        return minlat;
    }

    /**
     *See {@link #getMinlat()}.
     */
    public void setMinlat(float minlat) {
        this.minlat = minlat;
    }

    /**
     *Number of Map images.
     *Returns the number {@link Images1} objects in the {@link Map#colImages1} collection.
     *Also, returns the number of images saved in the trailing end of the GTM file, each image is saved
     * as byte strings corresponding to their original bytes retrieved from the external image files.
    */
    public int getN_maps() {
        return this.parentMapReference.getImages1Count();
    }

    /**
     *Number of Tracklog Styles (tracklog headers). Defines the number of {@link Trknome1} objects in the {@link Map#colTrknome1} collection.
     *A tracklog style is the individual headers of each tracklog in the map.
     */
    public int getN_tk() {
        return this.parentMapReference.getTrknome1Count();
    }

    /**
     *Number of layers. Reserved for future use. Must be zero.
     */
    public float getLayers() {
        return layers;
    }

    /**
     *See {@link #getLayers()}.
     */
    public void setLayers(float layers){
        this.layers = layers;
    }

    /**
     *Number of User icon images. Reserved for future use. Must be zero.
     */
    public float getIconnum() {
        return iconnum;
    }

    /**
     *See {@link #getIconnum()}.
     */
    public void setIconnum(float iconnum) throws ErrorIsNotZero {
        this.iconnum = iconnum;
    }

    /**
     *Activates rectangular coordinates when GTM file is opened.  Default = false.
     */
    public boolean isRectangular() {
        return rectangular;
    }

    /**
     *See {@link #isRectangular()}.
     */
    public void setRectangular(boolean rectangular) {
        this.rectangular = rectangular;
    }

    /**
     *Activates <i>True Grid Mode</i> when GTM file is opened. Default = false.
     */
    public boolean isTruegrid() {
        return truegrid;
    }

    /**
     *See {@link #isTruegrid()}.
     */
    public void setTruegrid(boolean truegrid) {
        this.truegrid = truegrid;
    }

    /**
     *Text Color of Tracklog Labels.
     */
    public int getLabelcolor() {
        return labelcolor;
    }

    /**
     *See {@link #getLabelcolor()}.
     */
    public void setLabelcolor(int labelcolor) {
        this.labelcolor = labelcolor;
    }

    /**
     *Reserved for future use.  Must be false.
     */
    public boolean isUsernegrit() {
        return usernegrit;
    }

    /**
     *See {@link #isUsernegrit()}. Throws {@link ErrorIsNotFalse} upon attempt of setting a value different from <b><code>false</code></b>.
     */
    public void setUsernegrit(boolean usernegrit) throws ErrorIsNotFalse {
        if(usernegrit) throw new ErrorIsNotFalse();
        this.usernegrit = usernegrit;
    }

    /**
     *Reserved for future use.  Must be false.
     */
    public boolean isUseritalic() {
        return useritalic;
    }

    /**
     *See {@link #isUseritalic()}. Throws {@link ErrorIsNotFalse} upon attempt of setting a value different from <b><code>false</code></b>.
     */
    public void setUseritalic(boolean useritalic) throws ErrorIsNotFalse {
        if(useritalic) throw new ErrorIsNotFalse();
        this.useritalic = useritalic;
    }

    /**
     *Reserved for future use.  Must be false.
     */
    public boolean isUsersublin() {
        return usersublin;
    }

    /**
     *See {@link #isUsersublin()}. The specification says it must be always <b><code>false</code></b>, but some applications
     * have seen to be using this field as a flag.
     */
    public void setUsersublin(boolean usersublin) throws ErrorIsNotFalse {
        //if(usersublin) throw new ErrorIsNotFalse();
        this.usersublin = usersublin;
    }

    /**
     *Reserved for future use.  Must be false.
     */
    public boolean isUsertachado() {
        return usertachado;
    }

    /**
     *See {@link #isUsertachado()}. The specification says it must be always <b><code>false</code></b>, but some applications
     * have seen to be using this field as a flag.
     */
    public void setUsertachado(boolean usertachado) {
        this.usertachado = usertachado;
    }

    /**
     *Set to <b><code>true</code></b> if there are maps (images).
     */
    public boolean isMap() {
        return map;
    }

    /**
     *See {@link #isMap()}.
     */
    public void setMap(boolean map) {
        this.map = map;
    }

    /**
     *Font size of tracklog labels.
     */
    public short getLabelsize() {
        return labelsize;
    }

    /**
     *See {@link #getLabelsize()}.
     */
    public void setLabelsize(short labelsize) {
        this.labelsize = labelsize;
    }

    /**
     *Font name of the grid text. Default = <i>Times New Roman</i>.
     */
    public String getGradfont() {
        return gradfont;
    }

    /**
     *See {@link #getGradfont()}.
     */
    public void setGradfont(String gradfont) {
        this.gradfont = gradfont;
    }

    /**
     *Font name of tracklogs labels. Default = <i>Times New Roman</i>.
     */
    public String getLabelfont() {
        return labelfont;
    }

    /**
     *See {@link #getLabelfont()}.
     */
    public void setLabelfont(String labelfont) {
        this.labelfont = labelfont;
    }

    /**
     *Reserved for future use. Should be empty per the GTM 211 file specification, but it seems
     * to be used to store the file name, excluding the extension .GTM.
     */
    public String getUserfont() {
        return userfont;
    }

    /**
     *See {@link #getUserfont()}.
     */
    public void setUserfont(String userfont) throws ErrorStringNotEmpty{
        this.userfont = userfont;
    }

    /**
     *Reserved for future use.  Must be empty string.
     */
    public String getNewdatum() {
        return newdatum;
    }

    /**
     *See {@link #getNewdatum()}. Throws {@link ErrorStringNotEmpty} upon attempt of setting a value different from empty string.
     */
    public void setNewdatum(String newdatum) throws ErrorStringNotEmpty {
        if(!newdatum.equals("")) throw new ErrorStringNotEmpty();
        this.newdatum = newdatum;
    }

    public void writeToStream(OutputStream stream) throws IOException {
        stream.write(this.convertShortTo2bytes(this.version));
        stream.write(this.convertStringToByteArray(this.code, false));
        stream.write(this.wli);
        stream.write(this.vwt);
        stream.write(this.gradnum);
        stream.write(this.wptnum);
        stream.write(this.usernum);
        stream.write(this.coriml);
        stream.write(this.ndatum);
        stream.write(this.convertIntTo4bytes(this.gradcolor));
        stream.write(this.convertIntTo4bytes(this.bcolor));
        stream.write(this.convertIntTo4bytes(this.getNwptstyles()));
        stream.write(this.convertIntTo4bytes(this.usercolor));
        stream.write(this.convertIntTo4bytes(this.getNwpts()));
        stream.write(this.convertIntTo4bytes(this.getNtrcks()));
        stream.write(this.convertIntTo4bytes(this.getNrtes()));
        stream.write(this.convertFloatTo4bytes(this.maxlon));
        stream.write(this.convertFloatTo4bytes(this.minlon));
        stream.write(this.convertFloatTo4bytes(this.maxlat));
        stream.write(this.convertFloatTo4bytes(this.minlat));
        stream.write(this.convertIntTo4bytes(this.getN_maps()));
        stream.write(this.convertIntTo4bytes(this.getN_tk()));
        stream.write(this.convertFloatTo4bytes(this.layers));
        stream.write(this.convertFloatTo4bytes(this.iconnum));
        stream.write(this.convertBooleanTo2bytes(this.rectangular));
        stream.write(this.convertBooleanTo2bytes(this.truegrid));
        stream.write(this.convertIntTo4bytes(this.labelcolor));
        stream.write(this.convertBooleanTo2bytes(this.usernegrit));
        stream.write(this.convertBooleanTo2bytes(this.useritalic));
        stream.write(this.convertBooleanTo2bytes(this.usersublin));
        stream.write(this.convertBooleanTo2bytes(this.usertachado));
        stream.write(this.convertBooleanTo2bytes(this.map));
        stream.write(this.convertShortTo2bytes(this.labelsize));
        stream.write(this.convertStringToByteArray(this.gradfont, true));
        stream.write(this.convertStringToByteArray(this.labelfont, true));
        stream.write(this.convertStringToByteArray(this.userfont, true));
        stream.write(this.convertStringToByteArray(this.newdatum, true));
    }
    
    /**
     *Returns a new Header object with the same properties of this Header object.
     *@param parent_map_reference A {@link Map} object reference that must be different from the
     *map reference of this Header, otherwise an exception will be thrown.
     */
    public Header makeCopy(Map parent_map_reference) throws ErrorGTMRule, ErrorObjectNotDefined{
        if(parent_map_reference == this.parentMapReference)
            throw new ErrorGTMRule("Two headers cannot have the same Map reference.");
        Header result = new Header(parent_map_reference);
        try{
        result.setVersion(this.version);
        result.setCode(this.code);
        result.setWli(this.wli);
        result.setVwt(this.vwt);
        result.setGradnum(this.gradnum);
        result.setWptnum(this.wptnum);
        result.setUsernum(this.usernum);
        result.setCoriml(this.coriml);
        result.setNdatum(this.ndatum);
        result.setGradcolor(this.gradcolor);
        result.setBcolor(this.bcolor);
        result.setUsercolor(this.usercolor);
        result.setMaxlon(this.maxlon);
        result.setMinlon(this.minlon);
        result.setMaxlat(this.maxlat);
        result.setMinlat(this.minlat);
        result.setLayers(this.layers);
        result.setIconnum(this.iconnum);
        result.setRectangular(this.rectangular);
        result.setTruegrid(this.truegrid);
        result.setLabelcolor(this.labelcolor);
        result.setUsernegrit(this.usernegrit);
        result.setUseritalic(this.useritalic);
        result.setUsersublin(this.usersublin);
        result.setUsertachado(this.usertachado);
        result.setMap(this.map);
        result.setLabelsize(this.labelsize);
        result.setGradfont(this.gradfont);
        result.setLabelfont(this.labelfont);
        result.setUserfont(this.userfont);
        result.setNewdatum(this.newdatum);
        }catch(Exception ex){} //assume all parameters above are valid
        return result;
    }

    public boolean isGeographic() {
        return false;
    }

    @Override
    public String toString(){
        return "HEADER";
    }
}
