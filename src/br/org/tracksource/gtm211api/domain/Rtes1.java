/*
 * Rtes1.java
 *
 * Criada em 28 de Dezembro de 2008, 11:18
 *
 */

package br.org.tracksource.gtm211api.domain;

import br.org.tracksource.gtm211api.errors.ErrorObjectNotDefined;
import br.org.tracksource.gtm211api.errors.ErrorStringIncorrectLength;
import br.org.tracksource.gtm211api.errors.ErrorValueOutOfRange;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The Rtes1 class represents a waypoint of a route, which is a sequence of waypoints.  NOTE: route waypoints have nothing to do with the waypoints stored as <i>wpts1</i> records.
 * The route points records begin following the tracklog description records of a GTM file per the 211 GTM format specification and appear
 * as many times as specified by the <code>nrtes</code> field of the <i>header</i> record.<br>
 * <br>
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class Rtes1 extends PointRecord{
    
    /**
     *Routepoint name.
     */
    protected String wname;
    
    /**
     *Routepoint comments.
     */
    protected String wcomment;
    
    /**
     *Route name.  This value must be repeated in all route points belonging to the same route.
     */
    protected String rname;
    
    /**
     *See {@link Wpts1#ico}.
     */
    protected short ico;
    
    /**
     *See {@link Wpts1#dspl}.
     */
    protected byte dspl;
    
    /**
     *Route flag : 1 = Start a new route / 0 = continue the route.
     *It has the same logic of {@link Trcks1#tnum} field.
     */
    protected byte tnum;
    
    /**
     *Reserved for future use.  Should be zero.
     */
    protected short wrot;
    
    /**
     *Reserved for future use.  Should be zero.
     */
    protected short wlayer;
    
    
    /** Constructor. */
    public Rtes1(Map parent_map) throws ErrorObjectNotDefined {
        super(parent_map);
    }

    public String getWname() {
        return wname;
    }

    public void setWname(String wname) throws ErrorStringIncorrectLength {
        if(wname.length() != 10) throw new ErrorStringIncorrectLength(10, wname.length());
        this.wname = wname;
    }

    public String getWcomment() {
        return wcomment;
    }

    public void setWcomment(String wcomment) {
        this.wcomment = wcomment;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
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

    public byte getTnum() {
        return tnum;
    }

    public void setTnum(byte tnum) throws ErrorValueOutOfRange{
        if(tnum != 0 && tnum != 1) throw new ErrorValueOutOfRange("0 or 1", String.valueOf(tnum));
        this.tnum = tnum;
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

    public void setWlayer(short wlayer) {
        this.wlayer = wlayer;
    }

    public void writeToStream(OutputStream stream) throws IOException {
        stream.write(this.convertDoubleTo8bytes(this.latitude));
        stream.write(this.convertDoubleTo8bytes(this.longitude));
        stream.write(this.convertStringToByteArray(this.wname, false));
        stream.write(this.convertStringToByteArray(this.wcomment, true));
        stream.write(this.convertStringToByteArray(this.rname, true));
        stream.write(this.convertShortTo2bytes(this.ico));
        stream.write(this.dspl);
        stream.write(this.tnum);
        stream.write(this.convertIntTo4bytes(this.date));
        stream.write(this.convertShortTo2bytes(this.wrot));
        stream.write(this.convertFloatTo4bytes(this.alt));
        stream.write(this.convertShortTo2bytes(this.wlayer));
    }
    
}
