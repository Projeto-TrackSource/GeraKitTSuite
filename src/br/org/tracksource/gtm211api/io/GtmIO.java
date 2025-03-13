/*
 * GtmIO.java
 *
 * Created on October 29th, 2008. 9:32PM.
 *
 */

package br.org.tracksource.gtm211api.io;

import br.org.tracksource.gtm211api.Constants;
import br.org.tracksource.gtm211api.domain.Datum;
import br.org.tracksource.gtm211api.domain.Header;
import br.org.tracksource.gtm211api.domain.Images1;
import br.org.tracksource.gtm211api.domain.Map;
import br.org.tracksource.gtm211api.domain.Rtes1;
import br.org.tracksource.gtm211api.domain.Stylefont1;
import br.org.tracksource.gtm211api.domain.Trcks1;
import br.org.tracksource.gtm211api.domain.Trknome1;
import br.org.tracksource.gtm211api.domain.Wpts1;
import br.org.tracksource.gtm211api.errors.ErrorCodeIsNotTrackMaker;
import br.org.tracksource.gtm211api.errors.ErrorGTMRule;
import br.org.tracksource.gtm211api.errors.ErrorIsNotFalse;
import br.org.tracksource.gtm211api.errors.ErrorIsNotZero;
import br.org.tracksource.gtm211api.errors.ErrorObjectNotDefined;
import br.org.tracksource.gtm211api.errors.ErrorStringIncorrectLength;
import br.org.tracksource.gtm211api.errors.ErrorStringNotEmpty;
import br.org.tracksource.gtm211api.errors.ErrorValueOutOfRange;
import br.org.tracksource.gtm211api.errors.ErrorVersionIsNot211;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

/**
 * This class features static methods to load and save GTM files.  It is the bridge between internal data structures
 * and the files in the GTM format.
 *
 * @author Projeto Tracksource.
 */
public class GtmIO {
    
    /** Default constructor.  Currently useless. */
    public GtmIO() {
    }
 
    /**
     *Saves a GTM file using the {@link Map} object data.<br><br>
     *NOTE: this function will always save an uncompressed GTM file regardless
     *of the input file used to generate the {@link Map} object.
     */
    public static void SaveGTMFile(Map map, String file_path) throws FileNotFoundException, IOException, ErrorGTMRule{
        //BufferedOutputStream(FileOutputStream("nome", false)) --> first call to clear the file off old content
        //BufferedOutputStream(FileOutputStream("nome", true)) --> further calls to the same file to append bytes
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file_path, false));
        map.writeToStream(stream);
        stream.flush();
        stream.close();
    }
    
    /**
     *Loads a GTM file and return a {@link Map} object containing all map elements retrieved from the file.
     *The function accepts files that have been compressed with the GZip format, a feature present in GPS Trackmaker (r),
     * in this case the file name extension must be .GZ or .GTZ, otherwise the function will attempt to load
     * it as uncompressed GTM file leading to load failure.<br><br>
     *NOTE: the {@link #SaveGTMFile(Map, String)} function will always save an uncompressed GTM file regardless
     *of the input file used to generate the {@link Map} object.
     */
    public static Map LoadGTMFile(String file_path) throws FileNotFoundException, IOException, ErrorVersionIsNot211, ErrorCodeIsNotTrackMaker, ErrorIsNotZero, ErrorValueOutOfRange, ErrorIsNotFalse, ErrorStringNotEmpty, ErrorObjectNotDefined, ErrorGTMRule, ErrorStringIncorrectLength{
        int data_size;
        Rtes1 rtes1;
        int nrtes;
        Trknome1 trknome1;
        int n_tk;
        Trcks1 trcks1;
        int ntrcks;
        Stylefont1 stylefont1;
        Wpts1 wpts1;
        int nwpts;
        Images1 images1;
        Datum datum;
        int text_size;
        byte raw_data[];
        byte file_data[]; //renamed from buf[]
        File f;
        InputStream in;
        int len = 0; //number of bytes read so far
        int pos; //current position in the file data buffer
        int n_maps;
        int nwptstyles;
        Header header;

        
        /////////////////////LOADS THE FILE BYTES INTO THE FILE DATA MEMORY BUFFER////////////////////////
        f = new File(file_path);
        if(file_path.toUpperCase().endsWith(".GTZ") || file_path.toUpperCase().endsWith(".GZ")){
            GZIPInputStream zin;
            zin = new GZIPInputStream(new FileInputStream(file_path));
            byte chunk[] = new byte[8192];
            ArrayList<byte[]> abytes = new ArrayList<byte[]>();
            byte bytes[];
            int bytes_read = 0;
            while ((len = zin.read(chunk, 0, 8192)) != -1){
                bytes_read += len;
                bytes = new byte[len];
                System.arraycopy(chunk, 0, bytes, 0, len);
                abytes.add(bytes);
            }
            zin.close();
            file_data = new byte[bytes_read];
            Iterator<byte[]> iab = abytes.iterator();
            pos = 0;
            while(iab.hasNext()){
                bytes = iab.next();
                System.arraycopy(bytes, 0, file_data, pos, bytes.length);
                pos += bytes.length;
            }
            abytes.clear();
        }
        else{
            file_data = new byte[(int)f.length()];
            in = new BufferedInputStream(new FileInputStream(file_path));
            while(len < f.length())
                len += in.read(file_data, len, (int)f.length()-len);
            in.close();
        }
        
        //construct the map object to be returned to the caller
        Map map = new Map();
        
        ////////////////////////TRAVERSES THE HEADER RECORD BYTES TO CONSTRUCT A HEADER OBJECT///////////////////////

        //create a header object
        header = new Header(map);

        //initialize the reference values used to traverse the file data buffer
        pos = 0;

        //read the two first bytes corresponding to the version field
        header.setVersion(GtmIO.convert2bytesToShort(file_data, 0)); 
        pos += 2;

        //read the next 10 bytes correspondig to the code field
        raw_data = new byte[10];
        System.arraycopy(file_data, pos, raw_data, 0, 10);
        header.setCode(new String(raw_data, Constants.charsetNameGTM2Java));
        pos += 10;
        
        //read the next byte corresponding to the wli field
        header.setWli(file_data[pos++]);
        
        //read the next byte corresponding to the vwt field
        header.setVwt(file_data[pos++]);
        
        //read the next byte corresponding to the gradnum field
        header.setGradnum(file_data[pos++]);

        //read the next byte corresponding to the wptnum field
        header.setWptnum(file_data[pos++]);

        //read the next byte corresponding to the usernum field
        header.setUsernum(file_data[pos++]);
        
        //read the next byte corresponding to the coriml field
        header.setCoriml(file_data[pos++]);

        //read the next byte corresponding to the ndatum field
        header.setNdatum(file_data[pos++]);
        
        //read the next four bytes corresponding to the gradcolor field
        header.setGradcolor(GtmIO.convert4bytesToInt(file_data, pos)); 
        pos += 4;
        
        //read the next four bytes corresponding to the bcolor field
        header.setBcolor(GtmIO.convert4bytesToInt(file_data, pos)); 
        pos += 4;

        //read the next four bytes corresponding to the nwptstyles field
        nwptstyles = GtmIO.convert4bytesToInt(file_data, pos); 
        pos += 4;

        //read the next four bytes corresponding to the usercolor field
        header.setUsercolor(GtmIO.convert4bytesToInt(file_data, pos)); 
        pos += 4;
        
        //read the next four bytes corresponding to the nwpts field
        nwpts = GtmIO.convert4bytesToInt(file_data, pos); 
        pos += 4;
        
        //read the next four bytes corresponding to the ntrcks field
        //NOTE: the ntrcks field is only relevant during load time and cannot be set in the Header object
        //because the total number of trackpoints is a function of the total trackpoints in each individual
        //tracklog which can vary during map processing.
        ntrcks = GtmIO.convert4bytesToInt(file_data, pos); 
        pos += 4;
        
        //read the next four bytes corresponding to the nrtes field
        nrtes = GtmIO.convert4bytesToInt(file_data, pos); 
        pos += 4;
        
        //read the next four bytes corresponding to the maxlon field
        header.setMaxlon(GtmIO.convert4bytesToFloat(file_data, pos)); 
        pos += 4;
        
        //read the next four bytes corresponding to the minlon field
        header.setMinlon(GtmIO.convert4bytesToFloat(file_data, pos)); 
        pos += 4;
        
        //read the next four bytes corresponding to the maxlat field
        header.setMaxlat(GtmIO.convert4bytesToFloat(file_data, pos)); 
        pos += 4;

        //read the next four bytes corresponding to the minlat field
        header.setMinlat(GtmIO.convert4bytesToFloat(file_data, pos)); 
        pos += 4;
        
        //read the next four bytes corresponding to the n_maps field
        n_maps = GtmIO.convert4bytesToInt(file_data, pos); 
        pos += 4;
        
        //read the next four bytes corresponding to the n_tk field
        n_tk = GtmIO.convert4bytesToInt(file_data, pos); 
        pos += 4;
        
        //read the next four bytes corresponding to the layers field
        header.setLayers(GtmIO.convert4bytesToFloat(file_data, pos)); 
        pos += 4;
        
        //read the next four bytes corresponding to the iconnum field
        header.setIconnum(GtmIO.convert4bytesToFloat(file_data, pos)); 
        pos += 4;
        
        //read the next two bytes corresponding to the rectangular field
        header.setRectangular(0 != GtmIO.convert2bytesToShort(file_data, pos)); 
        pos += 2;
        
        //read the next two bytes corresponding to the truegrid field
        header.setTruegrid(0 != GtmIO.convert2bytesToShort(file_data, pos)); 
        pos += 2;

        //read the next four bytes corresponding to the labelcolor field
        header.setLabelcolor(GtmIO.convert4bytesToInt(file_data, pos)); 
        pos += 4;
        
        //read the next two bytes corresponding to the usernegrit field
        header.setUsernegrit(0 != GtmIO.convert2bytesToShort(file_data, pos)); 
        pos += 2;
        
        //read the next two bytes corresponding to the useritalic field
        header.setUseritalic(0 != GtmIO.convert2bytesToShort(file_data, pos)); 
        pos += 2;
        
        //read the next two bytes corresponding to the usersublin field
        header.setUsersublin(0 != GtmIO.convert2bytesToShort(file_data, pos)); 
        pos += 2;
        
        //read the next two bytes corresponding to the usertachado field
        header.setUsertachado(0 != GtmIO.convert2bytesToShort(file_data, pos)); 
        pos += 2;

        //read the next two bytes corresponding to the map field
        header.setMap(0 != GtmIO.convert2bytesToShort(file_data, pos)); 
        pos += 2;
        
        //read the next two bytes corresponding to the labelsize field
        header.setLabelsize(GtmIO.convert2bytesToShort(file_data, pos)); 
        pos += 2;
        
        //read the next two bytes corresponding to the gradfont field size
        text_size = GtmIO.convert2bytesToShort(file_data, pos); 
        pos += 2;
        //read the gradfont field content
        raw_data = new byte[text_size];
        System.arraycopy(file_data, pos, raw_data, 0, text_size);
        header.setGradfont(new String(raw_data, Constants.charsetNameGTM2Java));
        pos += text_size;
        
        //read the next two bytes corresponding to the labelfont field size
        text_size = GtmIO.convert2bytesToShort(file_data, pos); 
        pos += 2;
        //read the labelfont field content
        raw_data = new byte[text_size];
        System.arraycopy(file_data, pos, raw_data, 0, text_size);
        header.setLabelfont(new String(raw_data, Constants.charsetNameGTM2Java));
        pos += text_size;
        
        //read the next two bytes corresponding to the userfont field size
        text_size = GtmIO.convert2bytesToShort(file_data, pos); 
        pos += 2;
        //read the userfont field content
        raw_data = new byte[text_size];
        System.arraycopy(file_data, pos, raw_data, 0, text_size);
        header.setUserfont(new String(raw_data, Constants.charsetNameGTM2Java));
        pos += text_size;

        //read the next two bytes corresponding to the newdatum field size
        text_size = GtmIO.convert2bytesToShort(file_data, pos); 
        pos += 2;
        //read the newdatum field content
        raw_data = new byte[text_size];
        System.arraycopy(file_data, pos, raw_data, 0, text_size);
        header.setNewdatum(new String(raw_data, Constants.charsetNameGTM2Java));
        pos += text_size;

        //finally add the completed header object to the map object.
        map.setHeader(header);
        
        ////////////////////////TRAVERSES THE DATUM RECORD BYTES TO CONSTRUCT A DATUM OBJECT///////////////////////

        //create a datum object
        datum = new Datum(map);

        //read the next two bytes corresponding to the ngrid field
        datum.setNgrid(GtmIO.convert2bytesToShort(file_data, pos));
        pos += 2;

        //read the next eight bytes corresponding to the origin field
        datum.setOrigin(GtmIO.convert8bytesToDouble(file_data, pos));
        pos += 8;

        //read the next eight bytes corresponding to the falseeast field
        datum.setFalseeast(GtmIO.convert8bytesToDouble(file_data, pos));
        pos += 8;

        //read the next eight bytes corresponding to the scale1 field
        datum.setScale1(GtmIO.convert8bytesToDouble(file_data, pos));
        pos += 8;

        //read the next eight bytes corresponding to the falsenorthing field
        datum.setFalsenorthing(GtmIO.convert8bytesToDouble(file_data, pos));
        pos += 8;
        
        //read the next two bytes corresponding to the ndatum field
        datum.setNdatum(GtmIO.convert2bytesToShort(file_data, pos));
        pos += 2;
        
        //read the next eight bytes corresponding to the axis field
        datum.setAxis(GtmIO.convert8bytesToDouble(file_data, pos));
        pos += 8;
        
        //read the next eight bytes corresponding to the flattenig field
        datum.setFlattenig(GtmIO.convert8bytesToDouble(file_data, pos));
        pos += 8;
        
        //read the next two bytes corresponding to the dx field
        datum.setDx(GtmIO.convert2bytesToShort(file_data, pos));
        pos += 2;
        
        //read the next two bytes corresponding to the dy field
        datum.setDy(GtmIO.convert2bytesToShort(file_data, pos));
        pos += 2;
        
        //read the next two bytes corresponding to the dy field
        datum.setDz(GtmIO.convert2bytesToShort(file_data, pos));
        pos += 2;

        //finally add the completed datum object to the map object.
        map.setDatum(datum);
        
        ////////////////////////TRAVERSES THE IMAGES1 RECORDS (IF ANY) BYTES TO CONSTRUCT IMAGES1 OBJECTS///////////////////////
        
        for(int i = 1; i <= n_maps; i++){
            //create a images1 object
            images1 = new Images1(map);
            
            //read the next two bytes corresponding to the name_map field size
            text_size = GtmIO.convert2bytesToShort(file_data, pos);
            pos += 2;
            //read the name_map field content
            raw_data = new byte[text_size];
            System.arraycopy(file_data, pos, raw_data, 0, text_size);
            images1.setName_map(new String(raw_data, Constants.charsetNameGTM2Java));
            pos += text_size;
            
            //read the next two bytes corresponding to the comments field size
            text_size = GtmIO.convert2bytesToShort(file_data, pos);
            pos += 2;
            //read the comments field content
            raw_data = new byte[text_size];
            System.arraycopy(file_data, pos, raw_data, 0, text_size);
            images1.setComments(new String(raw_data, Constants.charsetNameGTM2Java));
            pos += text_size;
            
            //read the next four bytes corresponding to the gposx field
            images1.setGposx(GtmIO.convert4bytesToFloat(file_data, pos));
            pos += 4;
            
            //read the next four bytes corresponding to the gposy field
            images1.setGposy(GtmIO.convert4bytesToFloat(file_data, pos));
            pos += 4;
            
            //read the next four bytes corresponding to the gwidth field
            images1.setGwidth(GtmIO.convert4bytesToFloat(file_data, pos));
            pos += 4;
            
            //read the next four bytes corresponding to the gheight field
            images1.setGheight(GtmIO.convert4bytesToFloat(file_data, pos));
            pos += 4;
            
            //read the next four bytes corresponding to the imagelen field
            images1.setImagelen(GtmIO.convert4bytesToInt(file_data, pos));
            pos += 4;
            
            //read the next four bytes corresponding to the metax field
            images1.setMetax(GtmIO.convert4bytesToFloat(file_data, pos));
            pos += 4;
            
            //read the next four bytes corresponding to the metay field
            images1.setMetay(GtmIO.convert4bytesToFloat(file_data, pos));
            pos += 4;
            
            //read the next byte corresponding to the metamapa field
            images1.setMetamapa(file_data[pos++]);
            
            //read the next byte corresponding to the Tnum field
            images1.setTnum(file_data[pos++]);
            
            //add the completed images1 object to the map object
            map.addImages1(images1);
        }

        ////////////////////////TRAVERSES THE WPTS1 RECORDS (IF ANY) BYTES TO CONSTRUCT WPTS1 OBJECTS///////////////////////
        for(int i = 1; i <= nwpts; i++){
            //create a wpts1 object
            wpts1 = new Wpts1(map);

            //read the next eight bytes corresponding to the latitude field
            wpts1.setLatitude(GtmIO.convert8bytesToDouble(file_data, pos));
            pos += 8;
            
            //read the next eight bytes corresponding to the longitude field
            wpts1.setLongitude(GtmIO.convert8bytesToDouble(file_data, pos));
            pos += 8;

            //read the next 10 bytes correspondig to the name field
            raw_data = new byte[10];
            System.arraycopy(file_data, pos, raw_data, 0, 10);
            wpts1.setName(new String(raw_data, Constants.charsetNameGTM2Java));
            pos += 10;
            
            //read the next two bytes corresponding to the wname field size
            text_size = GtmIO.convert2bytesToShort(file_data, pos);
            pos += 2;
            //read the wname field content
            raw_data = new byte[text_size];
            System.arraycopy(file_data, pos, raw_data, 0, text_size);
            wpts1.setWname(new String(raw_data, Constants.charsetNameGTM2Java));
            pos += text_size;
            
            //read the next two bytes corresponding to the ico field
            wpts1.setIco((short)GtmIO.convert2bytesToShort(file_data, pos));
            pos += 2;
            
            //read the next byte corresponding to the dspl field
            wpts1.setDspl(file_data[pos++]);
            
            //read the next four bytes corresponding to the wdate field
            wpts1.setDate(GtmIO.convert4bytesToInt(file_data, pos));
            pos += 4;
            
            //read the next two bytes corresponding to the wrot field
            wpts1.setWrot(GtmIO.convert2bytesToShort(file_data, pos));
            pos += 2;
            
            //read the next four bytes corresponding to the walt field
            wpts1.setAlt(GtmIO.convert4bytesToFloat(file_data, pos));
            pos += 4;
            
            //read the next two bytes corresponding to the wlayer field
            wpts1.setWlayer(GtmIO.convert2bytesToShort(file_data, pos));
            pos += 2;
            
            //add the completed wpts1 object to the map object
            map.addWpts1(wpts1);
        }
        
        ////////////////////////TRAVERSES THE STYLEFONT1 RECORDS (IF ANY) BYTES TO CONSTRUCT STYLEFONT1 OBJECTS///////////////////////
        for(int i = 1; i <= nwptstyles && map.getHeader().getNwpts() > 0; i++){
            //create a Stylefonr1 object
            stylefont1 = new Stylefont1(map);

            //read the next four bytes corresponding to the Height field
            stylefont1.setHeight(GtmIO.convert4bytesToInt(file_data, pos));
            pos += 4;
            
            //read the next two bytes corresponding to the FaceName field size
            text_size = GtmIO.convert2bytesToShort(file_data, pos);
            pos += 2;
            //read the FaceName field content
            raw_data = new byte[text_size];
            System.arraycopy(file_data, pos, raw_data, 0, text_size);
            stylefont1.setFaceName(new String(raw_data, Constants.charsetNameGTM2Java));
            pos += text_size;

            //read the next byte corresponding to the dspl field
            stylefont1.setDspl(file_data[pos++]);
            
            //read the next four bytes corresponding to the color field
            stylefont1.setColor(GtmIO.convert4bytesToInt(file_data, pos));
            pos += 4;

            //read the next four bytes corresponding to the Weight field
            stylefont1.setWeight(GtmIO.convert4bytesToInt(file_data, pos));
            pos += 4;
            
            //read the next four bytes corresponding to the scale1 field
            stylefont1.setScale1(GtmIO.convert4bytesToFloat(file_data, pos));
            pos += 4;
            
            //read the next byte corresponding to the border field
            stylefont1.setBorder(file_data[pos++]);
            
            //read the next two bytes corresponding to the background field
            stylefont1.setBackground(0 != GtmIO.convert2bytesToShort(file_data, pos));
            pos += 2;
            
            //read the next four bytes corresponding to the backcolor field
            stylefont1.setBackcolor(GtmIO.convert4bytesToInt(file_data, pos));
            pos += 4;

            //read the next byte corresponding to the Italic field
            stylefont1.setItalic(file_data[pos++]);
            
            //read the next byte corresponding to the Underline field
            stylefont1.setUnderline(file_data[pos++]);
            
            //read the next byte corresponding to the StrikeOut field
            stylefont1.setStrikeOut(file_data[pos++]);
            
            //read the next byte corresponding to the alignment field
            stylefont1.setAlignment(file_data[pos++]);
            
            //add the completed stylefont1 object to the map object
            map.addStylefont1(stylefont1);
        }
        
        ////////////////////////TRAVERSES THE TRCKS1 RECORDS (IF ANY) BYTES TO CONSTRUCT TRCKS1 OBJECTS///////////////////////
        //the trackpoints will be stored locally and will be assigned to their respective
        //tracklog headers in the tracklog style records processing.
        ArrayList<Trcks1> localTrackpointCollection = new ArrayList<Trcks1>();
        for(int i = 1; i <= ntrcks; i++){
            //create a trcks1 object
            trcks1 = new Trcks1(map);

            //read the next eight bytes corresponding to the latitude field
            trcks1.setLatitude(GtmIO.convert8bytesToDouble(file_data, pos));
            pos += 8;
            
            //read the next eight bytes corresponding to the longitude field
            trcks1.setLongitude(GtmIO.convert8bytesToDouble(file_data, pos));
            pos += 8;
            
            //read the next four bytes corresponding to the wdate field
            trcks1.setDate(GtmIO.convert4bytesToInt(file_data, pos));
            pos += 4;
            
            //read the next byte corresponding to the tnum field
            trcks1.setTnum(file_data[pos++]);
            
            //read the next four bytes corresponding to the walt field
            trcks1.setAlt(GtmIO.convert4bytesToFloat(file_data, pos));
            pos += 4;
            
            //add the completed trcks1 object to the local trackpoint storage
            localTrackpointCollection.add(trcks1);
        }
        
        ////////////////////////TRAVERSES THE TRKNOME1 RECORDS (IF ANY) BYTES TO CONSTRUCT TRKNOME1 OBJECTS///////////////////////
        Iterator<Trcks1> iTrackPoints = localTrackpointCollection.iterator();
        Trcks1 nextTrackPoint = null;
        if(iTrackPoints.hasNext()) //meybe there is no tracklogs in the map
            nextTrackPoint = iTrackPoints.next();
        for(int i = 1; i <= n_tk; i++){
            //create a trknome1 object
            trknome1 = new Trknome1(map);
            
            //read the next two bytes corresponding to the tname field size
            text_size = GtmIO.convert2bytesToShort(file_data, pos);
            pos += 2;
            //read the tname field content
            raw_data = new byte[text_size];

            System.arraycopy(file_data, pos, raw_data, 0, text_size);
            trknome1.setTname(new String(raw_data, Constants.charsetNameGTM2Java));
            pos += text_size;

            //read the next byte corresponding to the ttype field
            trknome1.setTtype(file_data[pos++]);

            //read the next four bytes corresponding to the tcolor field
            trknome1.setTcolor(GtmIO.convert4bytesToInt(file_data, pos));
            pos += 4;

            //read the next four bytes corresponding to the tscale field
            trknome1.setTscale(GtmIO.convert4bytesToFloat(file_data, pos));
            pos += 4;
            
            //read the next byte corresponding to the tlabel field
            trknome1.setTlabel(file_data[pos++]);
            
            //read the next two bytes corresponding to the tlayer field
            trknome1.setTlayer(GtmIO.convert2bytesToShort(file_data, pos));
            pos += 2;
            
            //adds the tracklog's points to its collection from the local trackpoint collection
            if (nextTrackPoint != null)
                trknome1.addTrcks1(nextTrackPoint);
            else
                throw new ErrorObjectNotDefined("Erro ao carregar o GTM. Trackpoints inexsitentes (nextTrackPoint==null)");
            while(iTrackPoints.hasNext()){
                nextTrackPoint = iTrackPoints.next();
                if(nextTrackPoint.getTnum() == 0)
                    trknome1.addTrcks1(nextTrackPoint);
                else
                    break;
            }

            
            //add the completed trknome1 object to the map object
            map.addTrknome1(trknome1);
        }
        //clear the local trackpoint storage to free memory for the next processes
        localTrackpointCollection.clear();

        ////////////////////////TRAVERSES THE RTES1 RECORDS (IF ANY) BYTES TO CONSTRUCT RTES1 OBJECTS///////////////////////
        for(int i = 1; i <= nrtes; i++){
            //create a rtes1 object
            rtes1 = new Rtes1(map);
            
            //read the next eight bytes corresponding to the latitude field
            rtes1.setLatitude(GtmIO.convert8bytesToDouble(file_data, pos));
            pos += 8;
            
            //read the next eight bytes corresponding to the longitude field
            rtes1.setLongitude(GtmIO.convert8bytesToDouble(file_data, pos));
            pos += 8;

            //read the next 10 bytes correspondig to the wname field
            raw_data = new byte[10];
            System.arraycopy(file_data, pos, raw_data, 0, 10);
            rtes1.setWname(new String(raw_data, Constants.charsetNameGTM2Java));
            pos += 10;
            
            //read the next two bytes corresponding to the wcomment field size
            text_size = GtmIO.convert2bytesToShort(file_data, pos);
            pos += 2;
            //read the wcomment field content
            raw_data = new byte[text_size];
            System.arraycopy(file_data, pos, raw_data, 0, text_size);
            rtes1.setWcomment(new String(raw_data, Constants.charsetNameGTM2Java));
            pos += text_size;
            
            //read the next two bytes corresponding to the rname field size
            text_size = GtmIO.convert2bytesToShort(file_data, pos);
            pos += 2;
            //read the rname field content
            raw_data = new byte[text_size];
            System.arraycopy(file_data, pos, raw_data, 0, text_size);
            rtes1.setRname(new String(raw_data, Constants.charsetNameGTM2Java));
            pos += text_size;
            
            //read the next two bytes corresponding to the ico field
            rtes1.setIco(GtmIO.convert2bytesToShort(file_data, pos));
            pos += 2;
            
            //read the next byte corresponding to the dspl field
            rtes1.setDspl(file_data[pos++]);
            
            //read the next byte corresponding to the tnum field
            rtes1.setTnum(file_data[pos++]);
            
            //read the next four bytes corresponding to the tdate field
            rtes1.setDate(GtmIO.convert4bytesToInt(file_data, pos));
            pos += 4;

            //read the next two bytes corresponding to the wrot field
            rtes1.setWrot(GtmIO.convert2bytesToShort(file_data, pos));
            pos += 2;
            
            //read the next four bytes corresponding to the walt field
            rtes1.setAlt(GtmIO.convert4bytesToFloat(file_data, pos));
            pos += 4;

            //read the next two bytes corresponding to the wlayer field
            rtes1.setWlayer(GtmIO.convert2bytesToShort(file_data, pos));
            pos += 2;
            
            //add the completed rtes1 object to the map object
            map.addRtes1(rtes1);
        }
        
        /////////// THE USER_ICON RECORDS ARE NOT SUPPORTED IN 211 VERSION OF THE GTM FORMAT//////////
        if(header.getIconnum() != 0)
            throw new ErrorIsNotZero("The iconnum field of the header is found to be non-zero. user_icon records are not supported in 211 version of GTM file format.");

        /////////// THE LAYERS RECORDS ARE NOT SUPPORTED IN 211 VERSION OF THE GTM FORMAT//////////
        if(header.getLayers() != 0)
            throw new ErrorIsNotZero("The layers field of the header is found to be non-zero. layers records are not supported in 211 version of GTM file format.");

        ////////////////////////FINALLY TRAVERSES THE IMAGE FILE DATA BYTES (IF ANY) TO RECOVER MAP IMAGE DATA///////////////////////
        for(int i = 1; i <= header.getN_maps(); i++){
            
            //get the corresponding Images1 object, which contains information
            //about the length of the data.
            images1 = map.getImages1(i-1);

            //get the image file data length
            data_size = images1.getImagelen();
            
            //create a byte array with the same size as specified in the corresponding images1 record (represented by an Images1 object)
            raw_data = new byte[data_size];
            
            //read the data bytes from the GTM file
            System.arraycopy(file_data, pos, raw_data, 0, data_size);
            pos += data_size;

            //add the byte array to the map object
            map.addImageData(raw_data);
        }
        
        //Returns the completed Map object to the caller.
        return map;
    }

    /**
     *Converts a sequence of two bytes in the given array of bytes into a 16-bit integer value.
     *The first byte is the least significant one, for example, 255 is represented as the sequence FF 00.
     *Bytes are read starting at the position specified by <code>offset</code> parameter towards the end of the array,
     *meaning that if offset is 5, the second byte has index 6 (7th byte in the array).
     *@param bytes The byte array containg the data to be converted.
     *@param offset The position in the array corresponding to the first byte of the 2-byte sequence.
     */
     public static short convert2bytesToShort(byte bytes[], int offset){
        return (short)((bytes[offset] & 0x000000FF) | ((bytes[offset+1] & 0x000000FF) << 8));
    }

    /**
     *Converts a sequence of four bytes in the given array of bytes into a 32-bit integer value.
     *The first byte is the least significant one, for example, 255 is represented as the sequence FF 00 00 00.
     *Bytes are read starting at the position specified by <code>offset</code> parameter towards the end of the array,
     *meaning that if offset is 5, the second byte has index 6 (7th byte in the array), the third one is 7 and the last byte is 8.
     *@param bytes The byte array containg the data to be converted.
     *@param offset The position in the array corresponding to the first byte of the 4-byte sequence.
     */
    public static int convert4bytesToInt(byte bytes[], int offset){
        return (bytes[offset] & 0x000000FF) | ((bytes[offset+1] & 0x000000FF) << 8) | ((bytes[offset+2] & 0x000000FF) << 16) | ((bytes[offset+3] & 0x000000FF) << 24);
    }

    /**
     *Converts a sequence of four bytes in the given array of bytes into a 32-bit IEEE float-point value.
     *The first byte is the least significant one.
     *Bytes are read starting at the position specified by <code>offset</code> parameter towards the end of the array,
     *meaning that if offset is 5, the second byte has index 6 (7th byte in the array), the third one is 7 and the last byte is 8.
     *@param bytes The byte array containg the data to be converted.
     *@param offset The position in the array corresponding to the first byte of the 4-byte sequence.
     */
    public static float convert4bytesToFloat(byte bytes[], int offset){
        return Float.intBitsToFloat(GtmIO.convert4bytesToInt(bytes, offset));
    }

    /**
     *Converts a sequence of eight bytes in the given array of bytes into a 64-bit integer value.
     *The first byte is the least significant one.
     *Bytes are read starting at the position specified by <code>offset</code> parameter towards the end of the array,
     *meaning that if offset is 5, the second byte has index 6 (7th byte in the array), the third one is 7 and so on.
     *@param bytes The byte array containg the data to be converted.
     *@param offset The position in the array corresponding to the first byte of the 8-byte sequence.
     */
    public static long convert8bytesToLong(byte bytes[], int offset){
        return (GtmIO.convert4bytesToInt(bytes, offset) & 0x00000000FFFFFFFFL) | ((GtmIO.convert4bytesToInt(bytes, offset+4) & 0x00000000FFFFFFFFL)<<32) ;
    }

    /**
     *Converts a sequence of eight bytes in the given array of bytes into a 64-bit IEEE float-point value.
     *The first byte is the least significant one.
     *Bytes are read starting at the position specified by <code>offset</code> parameter towards the end of the array,
     *meaning that if offset is 5, the second byte has index 6 (7th byte in the array), the third one is 7 and so on.
     *@param bytes The byte array containg the data to be converted.
     *@param offset The position in the array corresponding to the first byte of the 8-byte sequence.
     */
    public static double convert8bytesToDouble(byte bytes[], int offset){
        return Double.longBitsToDouble(GtmIO.convert8bytesToLong(bytes, offset));
    }
}
