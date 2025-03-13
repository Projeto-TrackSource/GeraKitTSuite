/*
 * Map.java
 *
 * Created on October 29th, 2008. 9:36PM.
 *
 */

package br.org.tracksource.gtm211api.domain;

import br.org.tracksource.gtm211api.Constants;
import br.org.tracksource.gtm211api.MapStatisticsStruct;
import br.org.tracksource.gtm211api.errors.ErrorGTMRule;
import br.org.tracksource.gtm211api.errors.ErrorObjectNotDefined;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * This is the root class of the entire structure of cartographic elements such as waypoints and tracklogs
 * represented by the various classes in this package.
 *
 * @author Projeto Tracksource
 */
public class Map {
    /**The header.  General map properties. */
    protected Header header;
    /**Information about the datum used in this map.*/
    protected Datum datum;
    /** some statistics informations about this map */
    protected MapStatisticsStruct statistics;
    /**Collection of image description of images pasted as background images in this map.
     * @see #colImageData*/
    protected ArrayList<Images1> colImages1;
    /**Collection of map waypoints. */
    protected ArrayList<Wpts1> colWpts1;
    /**Collection of waypoint display styles. */
    protected ArrayList<Stylefont1> colStylefont1;
    /**Collection of tracklog headers. */
    protected ArrayList<Trknome1> colTrknome1;
    /**Collection of route points. Different from tracklogs, routes do not have a descriptive header, they are
     * simply a sequence of points.*/
    protected ArrayList<Rtes1> colRtes1;
    /**Collection of image file data byte arrays.
     * @see #colImages1*/
    protected ArrayList<byte[]> colImageData;

    /**The number used to determine whether objects are selected or not for some operation (removal, transformation, etc.).
     * If the {@link Record#selectionNumber} field of the objects equals this number then they are selected.
     */
    protected int selectionNumber = 1;

    /** Default constructor. */
    public Map() {
        statistics = new MapStatisticsStruct();
        colImages1 = new ArrayList<Images1>();
        colImageData = new ArrayList<byte[]>();
        colWpts1 = new ArrayList<Wpts1>();
        colStylefont1 = new ArrayList<Stylefont1>();
        colTrknome1 = new ArrayList<Trknome1>();
        colRtes1 = new ArrayList<Rtes1>();
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Datum getDatum() {
        return datum;
    }

    public void setDatum(Datum datum) {
        this.datum = datum;
    }

    /**
     *Adds an {@link Images1} object to the {@link #colImages1} collection.<br>
     *<br>
     *<b>IMPORTANT:</b> It is up to the client application or class to load the corresponding actual image file data
     * bytes from the GTM file or image file and add them to the {@link #colImageData} collection.
     */
    public void addImages1(Images1 images1){
        this.colImages1.add(images1);
        this.expandMBR(images1);
    }

    /**
     *Adds a byte array corresponding to the image file content read from the GTM file or image file to
     * the {@link #colImageData} collection.<br>
     *<br>
     *<b>IMPORTANT:</b> It is up to the client application to ensure the correct correspondence
     *between the {@link Images1} objects stored in the {@link #colImages1} collection and the byte arrays
     *stored in the <code>colImageData</code> collection.  For example: if mapxpto.jpg is the second map image
     *then its data bytes must be the second byte array in the <code>colImageData</code> collection.
     */
    public void addImageData(byte[] data){
        this.colImageData.add(data);
    }

    /**
     *Adds a {@link Wpts1} object to the {@link #colWpts1} collection.<br>
     *<br>
     */
    public void addWpts1(Wpts1 wpts1){
        this.colWpts1.add(wpts1);
        this.expandMBR(wpts1);
    }
    
    /**
     *Removes a {@link Wpts1} object from the {@link #colWpts1} collection
     * given a waypoint.<br>
     *<br>
     */
    public void removeWpts1(Wpts1 wpts1){
        int index = this.colWpts1.indexOf(wpts1);
        if (index > -1) this.colWpts1.remove(index);
    }
    
    /**
     *Removes a {@link Wpts1} object from the {@link #colWpts1} collection
     * given its index.<br>
     *<br>
     */
    public void removeWpts1(int index){
        this.colWpts1.remove(index);
    }

    /**
     *Adds a {@link Stylefont1} object to the {@link #colStylefont1} collection.<br>
     *<br>
     */
    public void addStylefont1(Stylefont1 stylefont1){
        this.colStylefont1.add(stylefont1);
    }


    /**
     *Adds a {@link Trknome1} object to the {@link #colTrknome1} collection.<br>
     *<br>
     */
    public void addTrknome1(Trknome1 trknome1){
        this.colTrknome1.add(trknome1);
        this.statistics.add(trknome1);
        this.expandMBR(trknome1);
    }

    /**
     *Adds a {@link Trknome1} object to the {@link #colTrknome1} collection in such a way
     * that it becomes the last object in the drawing order.<br>
     *<br>
     */
    public void addTrknome1ToBack(Trknome1 trknome1){
        this.colTrknome1.add(0, trknome1);
        this.statistics.add(trknome1);
        this.expandMBR(trknome1);
    }

    /**
     *Removes a {@link Trknome1} object from the {@link #colTrknome1} collection
     * given its index.<br>
     * TODO: It does not update the MBR boundaries.
     *<br>
     */
    public void removeTrknome1(int index){
        this.statistics.remove(this.colTrknome1.get(index));
        this.colTrknome1.remove(index);
        //this.expandMBR(trknome1);
    }

    /**
     *Removes a {@link Trknome1} object from the {@link #colTrknome1} collection
     * given a track.<br>
     * TODO: It does not update the MBR boundaries.
     *<br>
     */
    public void removeTrknome1(Trknome1 trknome1){
        int index = this.colTrknome1.indexOf(trknome1);
        if (index > -1) {
            this.statistics.remove(trknome1);
            this.colTrknome1.remove(index);
        }
        //this.expandMBR(trknome1);
    }

    /**
     *Adds a {@link Rtes1} object to the {@link #colRtes1} collection.<br>
     *<br>
     */
    public void addRtes1(Rtes1 rtes1){
        this.colRtes1.add(rtes1);
        this.expandMBR(rtes1);
    }

    /**
     *Retrieves an {@link Images1} object from the {@link #colImages1} collection by its index.
     * The object index reflects the position in which the corresponding <i>images1</i> record was written in the GTM file<br>
     *IMPORTANT: The first object has index zero.
     */
    public Images1 getImages1(int index){
        return this.colImages1.get(index);
    }

    /**
     *Writes all map objects as file records according to the GTM file format specification.
     */
    public void writeToStream(OutputStream stream) throws IOException, ErrorGTMRule{

        //header record
        this.header.writeToStream(stream);

        //coordinate system and datum record
        this.datum.writeToStream(stream);

        //image information records
        Iterator<Images1> iimages1 = this.colImages1.iterator();
        while(iimages1.hasNext())
            iimages1.next().writeToStream(stream);

        //waypoint records
        Iterator<Wpts1> iwpts1 = this.colWpts1.iterator();
        while(iwpts1.hasNext())
            iwpts1.next().writeToStream(stream);

        //waypoint style records
        if(this.colWpts1.size() > 0){
            //if there is less than the four default styles
            if(this.colStylefont1.size() < 4)
                //ensure at least the default four styles
                this.ensureTheFourDefaultStylefonts();
            Iterator<Stylefont1> istylefont1 = this.colStylefont1.iterator();
            while(istylefont1.hasNext())
                istylefont1.next().writeToStream(stream);
        }

        //tracklog point records
        Iterator<Trknome1> itrknome1 = this.colTrknome1.iterator();
        Trknome1 trknome1;
        Trcks1 trackpoint;
        int contador;
        while(itrknome1.hasNext()){
            trknome1 = itrknome1.next();
            Iterator<Trcks1> itrcks1 = trknome1.getTrackPointIterator();
            if(trknome1.getTrcks1Count() < 2)
                throw new ErrorGTMRule("Any tracklog must have at least two track points.");
            contador = 0;
            while(itrcks1.hasNext()){
                trackpoint = itrcks1.next();
                //make sure the trackpoints have the tracklog start marker correctly set
                try{
                if(contador == 0)
                    trackpoint.setTnum((byte)1); //the first trackpoint of a tracklog has this flag set to 1
                else
                    trackpoint.setTnum((byte)0); //the rest are 0
                }catch(Exception ex){}
                trackpoint.writeToStream(stream);
                contador++;
            }
        }

        //tracklog header records
        itrknome1 = this.colTrknome1.iterator();
        while(itrknome1.hasNext())
            itrknome1.next().writeToStream(stream);

        //route records
        Iterator<Rtes1> irtes1 = this.colRtes1.iterator();
        while(irtes1.hasNext())
            irtes1.next().writeToStream(stream);

        //imaga data records
        Iterator<byte[]> iimagedata = this.colImageData.iterator();
        while(iimagedata.hasNext())
            stream.write(iimagedata.next());
    }


    public Iterator<Trknome1> getTracklogHeaderIterator(){
        return this.colTrknome1.iterator();
    }

    public Iterator<Wpts1> getWaypointIterator(){
        return this.colWpts1.iterator();
    }

    /**
     *Increments the {@link #selectionNumber} field causing all current selected objects to
     *be unselected.
     */
    public void clearSelection(){
        this.selectionNumber++;
    }

    public int getSelectionNumber() {
        return selectionNumber;
    }

    /**
     *Deletes all selected objects in the collections. It does not affect
     *single instance objects (header and datum) regardless of they have been selected or not.<BR>
     *IMPORTANT: When a {@link Trknome1} is selected and subsequently deleted, all its {@link Trcks1} child objects
     * are deleted along, whether they have been individually selected or not because the track points cannot exist
     * without a parent tracklog header.
     *@see Record#selectionNumber
     */
    public void deleteSelectedObjects(){

        ArrayList<Images1> ncolImages1;
        ArrayList<Wpts1> ncolWpts1;
        ArrayList<Stylefont1> ncolStylefont1;
        ArrayList<Trcks1> ncolTrcks1;
        ArrayList<Trknome1> ncolTrknome1;
        ArrayList<Rtes1> ncolRtes1;
        ArrayList<byte[]> ncolImageData;
        ncolImages1 = new ArrayList<Images1>();
        ncolImageData = new ArrayList<byte[]>();
        ncolWpts1 = new ArrayList<Wpts1>();
        ncolStylefont1 = new ArrayList<Stylefont1>();
        ncolTrcks1 = new ArrayList<Trcks1>();
        ncolTrknome1 = new ArrayList<Trknome1>();
        ncolRtes1 = new ArrayList<Rtes1>();


        Iterator<Images1> iimages1 = this.colImages1.iterator();
        Iterator<byte[]> iimagedata = this.colImageData.iterator();
        Images1 images1;
        while(iimages1.hasNext()){
            iimagedata.next();
            if(!(images1 = iimages1.next()).isSelected()){
                ncolImages1.add(images1);
                ncolImageData.add(iimagedata.next());
            }
        }

        Iterator<Wpts1> iwpts1 = this.colWpts1.iterator();
        Wpts1 wpts1;
        while(iwpts1.hasNext())
            if(!(wpts1 = iwpts1.next()).isSelected())
                ncolWpts1.add(wpts1);

        Iterator<Stylefont1> istylefont1 = this.colStylefont1.iterator();
        Stylefont1 stylefont1;
        while(istylefont1.hasNext())
            if(!(stylefont1 = istylefont1.next()).isSelected())
                ncolStylefont1.add(stylefont1);

        //the Trcks1 objects are automatically deleted when their parent
        //Trknome1 objects are deleted.

        Iterator<Trknome1> itrknome1 = this.colTrknome1.iterator();
        Trknome1 trknome1;
        while(itrknome1.hasNext())
            if(!(trknome1 = itrknome1.next()).isSelected())
                ncolTrknome1.add(trknome1);

        Iterator<Rtes1> irtes1 = this.colRtes1.iterator();
        Rtes1 rtes1;
        while(irtes1.hasNext())
            if(!(rtes1 = irtes1.next()).isSelected())
                ncolRtes1.add(rtes1);

        this.colImages1.clear();
        this.colWpts1.clear();
        this.colStylefont1.clear();
        this.colTrknome1.clear();
        this.colRtes1.clear();

        this.colImages1 = ncolImages1;
        this.colWpts1 = ncolWpts1;
        this.colStylefont1 = ncolStylefont1;
        this.colTrknome1 = ncolTrknome1;
        this.colRtes1 = ncolRtes1;
    }

    /**
     *Creates a new Map object using the same properties of this map except the following:<br>
     *1) there are no waypoints;<br>
     *2) there are no tracklogs;<br>
     *3) there are no routes;<br>
     *4) there are no images;<br>
     *5) there are only the predefined four waypoint styles according to the GTM file format specification.<br>
     * @deprecated Use #createMap() instead, which is a better way to create new GTM maps.
     */
    @Deprecated
    public Map deriveNewEmptyMap() throws ErrorGTMRule, ErrorObjectNotDefined{
        Map result = new Map();
        result.setHeader(this.header.makeCopy(result));
        result.setDatum(this.datum.makeCopy(result));
        Iterator<Stylefont1> ist = this.colStylefont1.iterator();
        int counter = 0;
        while(ist.hasNext() && counter < 4){ //copy only the first four styles (the predefined ones)
            result.addStylefont1(ist.next().makeCopy(result));
            counter++;
        }
        return result;
    }

    /**
     * Creates a new empty Map object using the WGS-84 datum with MBR set to a point located at (0.0, 0.0).
     */
    public static Map createMap() {
        try{
            Map map = new Map();

            Header header = new Header(map);
            header.setVersion((short)211);
            header.setCode("TrackMaker");
            header.setWli((byte)0);
            header.setVwt((byte)0);
            header.setGradnum((byte)8); //font size for grid labels
            header.setWptnum((byte)0);
            header.setUsernum((byte)0);
            header.setCoriml((byte)0); //0 = icons with black background; 1 = icons with white background
            header.setNdatum((byte)0);
            header.setGradcolor(0); //grid color RGB (black)
            header.setBcolor(16777215); //background color RGB (white)
            //nwptstyles 4-byte integer (skipped, determined automatically)
            header.setUsercolor(0); //default text color for waypoints RGB (black)
            //nwpts 4-byte integer (skipped, determined automatically)
            //ntrcks 4-byte integer (skipped, determined automatically)
            //nrtes 4-byte integer (skipped, determined automatically)
            header.setMaxlat(0.0F); //map MBR
            header.setMaxlon(0.0F); //map MBR
            header.setMinlat(0.0F); //map MBR
            header.setMaxlon(0.0F); //map MBR
            //n_maps 4-byte integer (skipped, determined automatically)
            //n_tk 4-byte integer (skipped, determined automatically)
            header.setLayers(0.0F);
            header.setIconnum(0.0F);
            header.setRectangular(false); //activates rectangular coordinate system when GTM is opened
            header.setTruegrid(false); //activates true grid mode when GTM is opened
            header.setLabelcolor(0); //label text color for tracklogs RGB (black)
            header.setUsernegrit(false);
            header.setUseritalic(false);
            header.setUsersublin(false);
            header.setUsertachado(false);
            header.setMap(false); //set to true if there are images
            header.setLabelsize((short)12); //font size for tracklog labels
            header.setGradfont("Arial"); //grid text font
            header.setLabelfont("Arial"); //tracklog labels font
            header.setUserfont("");
            header.setNewdatum("");
            map.setHeader(header);

            Datum datum = new Datum(map);
            datum.setNgrid((short)0); //set to 0 because header.rectangular == false, 1 == user grid, 2+ == some standard rectangular grid
            datum.setOrigin(0.0); //user grid parameter
            datum.setFalseeast(0.0); //user grid paramater
            datum.setScale1(0.0); //user grid parameter
            datum.setFalsenorthing(0.0); //user grid parameter
            datum.setNdatum((short)217); //WGS-84
            datum.setAxis(0.0); //user datum parameter (ellipsoid major semi-axis)
            datum.setFlattenig(0.0); //user datum parameter (ellipsoid flattening)
            datum.setDx((short)0); //user datum parameter (ellipsoid center X-offset)
            datum.setDy((short)0); //user datum parameter (ellipsoid center Y-offset)
            datum.setDz((short)0); //user datum parameter (ellipsoid center Z-offset)
            map.setDatum(datum);

            return map;
        }catch(Exception ex){
            return null;
        }
    }

    /**
     *Returns the total number of waypoint styles in the {@link Map#colStylefont1} collection
     */
    public int getStylefont1Count(){
        return this.colStylefont1.size();
    }

    /**
     *Returns the total number of waypoints in the {@link Map#colWpts1} collection
     */
    public int getWpts1Count(){
        return this.colWpts1.size();
    }

    /**
     *Returns the total number of route points in the {@link Map#colRtes1} collection
     */
    public int getRtes1Count(){
        return this.colRtes1.size();
    }

    /**
     *Returns the total number of images in the {@link Map#colImages1} collection
     */
    public int getImages1Count(){
        return this.colImages1.size();
    }

    /**
     *Returns the total number of tracklogs in the {@link Map#colTrknome1} collection
     */
    public int getTrknome1Count(){
        return this.colTrknome1.size();
    }

    /**
     * Sorts the waypoints by their latitudes.
     */
    public void sortWaypointsByLatitudes(){
        Collections.sort(this.colWpts1, Wpts1.getComparadorWpts1ByLatitude());
    }

    /**
     * Returns the waypoint corresponding to the index in the #colWpts1 collection.
     */
    public Wpts1 getWaypoint(int i){
        return this.colWpts1.get(i);
    }

    /**
     * Returns the tracklog corresponding to the index in the #colTrknome1 collection.
     */
    public Trknome1 getTracklog(int i){
        return this.colTrknome1.get(i);
    }

    /**
     * Iterates over this Map's tracklog collection and puts tracklogs that are polygons
     * in the first list passed as parameter and tracklogs that are lines in the other list.
     * These lists should not be null.
     *
     * @param polygonList The list to receive the polygons.
     * @param lineList The list to receive the lines.
     */
    public void getTracklogGroups(ArrayList<Trknome1> polygonList, ArrayList<Trknome1> lineList){
        Iterator<Trknome1> it = this.colTrknome1.iterator();
        Trknome1 t;
        while(it.hasNext()){
            t = it.next();
            if(t.isFilledPolygon())
                polygonList.add(t);
            else
                lineList.add(t);
        }
    }

    /**
     * If the newly added PointRecord object relies outside de current
     * MBR (defined by the four [min|max][lat|lon] header fields) its
     * is updated accordingly.
     */
    private void expandMBR(PointRecord p){
        if(this.colImages1.size() + this.colRtes1.size() + this.colTrknome1.size() + this.colWpts1.size() == 0){
            this.getHeader().setMaxlat((float) p.getLatitude());
            this.getHeader().setMinlat((float) p.getLatitude());
            this.getHeader().setMaxlon((float) p.getLongitude());
            this.getHeader().setMinlon((float) p.getLongitude());
        }else{
            if (p.getLatitude() > this.getHeader().getMaxlat()) {
                this.getHeader().setMaxlat((float) p.getLatitude());
            }
            if (p.getLatitude() < this.getHeader().getMinlat()) {
                this.getHeader().setMinlat((float) p.getLatitude());
            }
            if (p.getLongitude() > this.getHeader().getMaxlon()) {
                this.getHeader().setMaxlon((float) p.getLongitude());
            }
            if (p.getLongitude() < this.getHeader().getMinlon()) {
                this.getHeader().setMinlon((float) p.getLongitude());
            }
        }
    }

    /**
     * If the newly added Tracklog object relies (partly or completely) outside de current
     * MBR (defined by the four [min|max][lat|lon] header fields) its
     * is updated accordingly.
     */
    private void expandMBR(Trknome1 t){
        if(this.colImages1.size() + this.colRtes1.size() + this.colTrknome1.size() + this.colWpts1.size() == 0){
            this.getHeader().setMaxlat((float) t.getMBRMaxLat());
            this.getHeader().setMinlat((float) t.getMBRMinLat());
            this.getHeader().setMaxlon((float) t.getMBRMaxLon());
            this.getHeader().setMinlon((float) t.getMBRMinLon());
        }else{
            if (t.getMBRMaxLat() > this.getHeader().getMaxlat()) {
                this.getHeader().setMaxlat((float) t.getMBRMaxLat());
            }
            if (t.getMBRMinLat() < this.getHeader().getMinlat()) {
                this.getHeader().setMinlat((float) t.getMBRMinLat());
            }
            if (t.getMBRMaxLon() > this.getHeader().getMaxlon()) {
                this.getHeader().setMaxlon((float) t.getMBRMaxLon());
            }
            if (t.getMBRMinLon() < this.getHeader().getMinlon()) {
                this.getHeader().setMinlon((float) t.getMBRMinLon());
            }
        }
    }

    /**
     * If the newly added image object relies outside (partly ou completely) de current
     * MBR (defined by the four [min|max][lat|lon] header fields) its
     * is updated accordingly.
     */
    private void expandMBR(Images1 i){
        if(this.colImages1.size() + this.colRtes1.size() + this.colTrknome1.size() + this.colWpts1.size() == 0){
            this.getHeader().setMinlon(i.getGposx());
            this.getHeader().setMinlat(i.getGposy() - i.getGheight());
            this.getHeader().setMaxlon(i.getGposx() + i.getGwidth());
            this.getHeader().setMaxlat(i.getGposy());
        }else{
            if(i.getGposx() < this.getHeader().getMinlon())
                this.getHeader().setMinlon(i.getGposx());
            if(i.getGposy() - i.getGheight() < this.getHeader().getMinlat())
                this.getHeader().setMinlat(i.getGposy() - i.getGheight());
            if(i.getGposx() + i.getGwidth() > this.getHeader().getMaxlon())
                this.getHeader().setMaxlon(i.getGposx() + i.getGwidth());
            if(i.getGposy() > this.getHeader().getMaxlat())
                this.getHeader().setMaxlat(i.getGposy());
        }
    }

    /**
     * Shortcut methods to get the MBR values from the Header field.
     */
    public float getMBRMinLat(){
        return this.getHeader().getMinlat();
    }
    /**
     * Shortcut methods to get the MBR values from the Header field.
     */
    public float getMBRMaxLat(){
        return this.getHeader().getMaxlat();
    }
    /**
     * Shortcut methods to get the MBR values from the Header field.
     */
    public float getMBRMinLon(){
        return this.getHeader().getMinlon();
    }
    /**
     * Shortcut methods to get the MBR values from the Header field.
     */
    public float getMBRMaxLon(){
        return this.getHeader().getMaxlon();
    }

    /**
     * Removes all images from this map.
     */
    public void removeAllImages(){
        this.colImageData.clear();
        this.colImages1.clear();
        this.header.setMap(false);
    }

    /**
     * Create the four default stylefonts required for Trackmaker
     * proper operation.  Deletes any custom styles, if any.
     * Be assured that waypoints don't use custom styles (dspl property) after
     * a call to this method otherwise the GTM map may run into problems
     * during load in GPS Trackmaker.
     */
    public void ensureTheFourDefaultStylefonts(){
        try{
            this.colStylefont1.clear();
            Stylefont1 sf = new Stylefont1(this);
            sf.setHeight(-11);
            sf.setFaceName("Arial");
            sf.setDspl((byte)0);
            sf.setColor(0);
            sf.setWeight(400);
            sf.setScale1(0.0F);
            sf.setBorder((byte)0);
            sf.setBackground(false);
            sf.setBackcolor(0);
            sf.setItalic((byte)0);
            sf.setUnderline((byte)0);
            sf.setStrikeOut((byte)0);
            sf.setAlignment((byte)0);
            this.colStylefont1.add(sf);

            sf = new Stylefont1(this);
            sf.setHeight(-11);
            sf.setFaceName("Arial");
            sf.setDspl((byte)1);
            sf.setColor(0);
            sf.setWeight(400);
            sf.setScale1(0.0F);
            sf.setBorder((byte)0);
            sf.setBackground(false);
            sf.setBackcolor(0);
            sf.setItalic((byte)0);
            sf.setUnderline((byte)0);
            sf.setStrikeOut((byte)0);
            sf.setAlignment((byte)0);
            this.colStylefont1.add(sf);

            sf = new Stylefont1(this);
            sf.setHeight(-11);
            sf.setFaceName("Arial");
            sf.setDspl((byte)2);
            sf.setColor(0);
            sf.setWeight(400);
            sf.setScale1(0.0F);
            sf.setBorder((byte)0);
            sf.setBackground(false);
            sf.setBackcolor(0);
            sf.setItalic((byte)0);
            sf.setUnderline((byte)0);
            sf.setStrikeOut((byte)0);
            sf.setAlignment((byte)0);
            this.colStylefont1.add(sf);

            sf = new Stylefont1(this);
            sf.setHeight(-11);
            sf.setFaceName("Arial");
            sf.setDspl((byte)3);
            sf.setColor(0);
            sf.setWeight(400);
            sf.setScale1(0.0F);
            sf.setBorder((byte)139);
            sf.setBackground(true);
            sf.setBackcolor(65535); //yellow
            sf.setItalic((byte)0);
            sf.setUnderline((byte)0);
            sf.setStrikeOut((byte)0);
            sf.setAlignment((byte)1);
            this.colStylefont1.add(sf);
        }catch(Exception ex){}
    }

    /**
     * Tries to find a waypoint based on the passed coordinate.
     * The coordinate must be within Constants#LAT_LONG_RESOLUTION from
     * the waypoint for a match.  Also, the #sortWaypointsByLatitudes
     * method must be called at least once before calling this method
     * because this method uses binary search for optimized searches.
     */
    public Wpts1 findWaypoint(double latitude, double longitude){

        Wpts1 key = null;
        try{key = new Wpts1(this);} catch(Exception ex){}

        key.setLongitude(longitude);
        key.setLatitude(latitude);

        int i = Collections.binarySearch(colWpts1, key, Wpts1.getComparadorWpts1ByLatitude());

        //if the returned index is negative (no exact match), then decode it to find
        //the index of waypoint immediatley after the coordinate
        //considering the ordering criteria.
        if( i < 0){
            i = i + 1;
            i = -i;
        }

        Wpts1 wp_candidato;
        int j;

        //procuro pelos waypoints que estao a leste do waypoint ateh que a longitude difira de Constants.LAT_LON_RESOLUTION
        for(j = i; j < this.getWpts1Count(); j++){
            wp_candidato = this.getWaypoint(j);
            if(Math.abs(wp_candidato.getLatitude()-key.getLatitude()) < Constants.LAT_LONG_RESOLUTION){ //se a diferenca de latitudes for menor que a resolucao
                if(wp_candidato.coincidesWith(key)){ //se hah coincidencia geografica entre dois waypoints proximos
                    return wp_candidato;
                }
            }else{ //too far, can abort the inner loop
                break;
            }
        }

        //procuro pelos waypoints que estao a oeste do waypoint ateh que a longitude difira de Constants.LAT_LON_RESOLUTION
        for(j = i - 1; j >=0; j--){
            wp_candidato = this.getWaypoint(j);
            if(Math.abs(wp_candidato.getLatitude()-key.getLatitude()) < Constants.LAT_LONG_RESOLUTION){ //se a diferenca de latitudes for menor que a resolucao
                if(wp_candidato.coincidesWith(key)){ //se hah coincidencia geografica entre dois waypoints proximos
                    return wp_candidato;
                }
            }else{ //too far, can abort the inner loop
                break;
            }
        }

        return null;
    }

    /**
     * Returns the index of the first tracklog whose tname field (track name)
     * matches the search string.  This is a table scan (worst case = O(n)) search and not
     * an optimized, indexed search.  Never use this method inside intensive looping.
     * If no tracklog is found or start_of_search is greater than the total number
     * of tracklog minus 1, then -1 is returned.
     *
     * @param pattern Search criterium.  Can be a regular expression.
     * @param start_of_search Index from which the search will be performed.
     */
    public int findTracklogByName( String pattern, int start_of_search ){
        Pattern regex = Pattern.compile(pattern);
        for( int i = start_of_search; i < this.colTrknome1.size(); i++ ){
            Trknome1 track = this.colTrknome1.get(i);
            if( regex.matcher(track.getTname()).matches() )
                return i;
        }
        return -1;
    }
    
    public MapStatisticsStruct getStatistics(){
        return this.statistics;
    }
}