/*
 * Trcks1.java
 *
 * Criada em 27 de Dezembro de 2008, 12:00
 *
 */

package br.org.tracksource.gtm211api.domain;

import br.org.tracksource.gtm211api.errors.ErrorObjectNotDefined;
import br.org.tracksource.gtm211api.errors.ErrorValueOutOfRange;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The Trcks1 class represents a tracklog point.
 * The tracklog point records begin after the waypoint style records of a GTM file, following the 211 GTM format specification, and appear
 * as many times as specified by the <code>ntrcks</code> field of the <i>header</i> record.<br>
 * <br>
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class Trcks1 extends PointRecord{

    /**
     *Tracklog flag : 1 = this point starts a new Tracklog / 0 = this point continues a tracklog.
     *Client code does not need to consider this flag since it is automatically set upon file save.<br>
     *<br>
     */
    protected byte tnum;
    
    /** Constructor. */
    public Trcks1(Map parent_map) throws ErrorObjectNotDefined {
        super(parent_map);
    }

    public byte getTnum() {
        return tnum;
    }

    public void setTnum(byte tnum) throws ErrorValueOutOfRange{
        if(tnum != 0 && tnum != 1) throw new ErrorValueOutOfRange("0 or 1", String.valueOf(tnum));
        this.tnum = tnum;
    }

    /**
     * Return a copy of Trcks1 except
     */
    public Trcks1 copy () throws ErrorObjectNotDefined, ErrorValueOutOfRange {
        Trcks1 tk_new = new Trcks1(this.getParentMapReference());
        tk_new.setTnum(tnum);
        tk_new.setAlt(alt);
        tk_new.setDate(date);
        tk_new.setLatitude(latitude);
        tk_new.setLongitude(longitude);
        tk_new.setVisited(visited);
        return (tk_new);
    }

    public void writeToStream(OutputStream stream) throws IOException {
        stream.write(this.convertDoubleTo8bytes(this.latitude));
        stream.write(this.convertDoubleTo8bytes(this.longitude));
        stream.write(this.convertIntTo4bytes(this.date));
        stream.write(this.tnum);
        stream.write(this.convertFloatTo4bytes(this.alt));
    }
    
    @Override
    public String toString(){
        return String.format("TRACKPOINT:(%2.6f;%2.6f)",this.getLatitude(),this.getLongitude());
    }
}
