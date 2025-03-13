/*
 * Images1.java
 *
 * Criada em 25 de Dezembro de 2008, 12:26
 *
 */

package br.org.tracksource.gtm211api.domain;

import br.org.tracksource.gtm211api.errors.ErrorObjectNotDefined;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The Images1 class represents a raster image pasted in the map to be used as background image.
 * The image records begin following the datum record of a GTM file per the 211 GTM format specification and appear
 * as many times as specified by the <code>n_maps</code> field of the <i>header</i> record.<br><br>
 * Note: The actual image file data bytes are appended to the end of the GTM file without any modification as they were read from the original image file.
 * It is up to the client application or subclass to decode and/or uncompress the image data.<br><br>
 * A rendering application must display the image according to the following:<br>
 * <img src="doc-files/example_map_image.jpg"/>
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class Images1 extends Record {
    /**
     *Image name.
     */
    protected String name_map;
    
    /**
     *Image comments.
     */
    protected String comments;
    
    /**
     *upper-left corner in degrees
     */
    protected float gposx;
    
    /**
     *upper-left corner in degrees
     */
    protected float gposy;
    
    /**
     *Image width in degrees
     */
    protected float gwidth;
    
    /**
     *Image Height in degrees
     */
    protected float gheight;
    
    /**
     *Number of bytes of the original file image data, which was appended to the end of the GTM file.
     */
    protected int imagelen;
    
    /**
     *Reserved for future use.  Must be 0.
     */
    protected float metax;
    
    /**
     *Reserved for future use.  Must be 0.
     */
    protected float metay;
    
    /**
     *Reserved for future use.  Must be 0.
     */
    protected byte metamapa;
    
    /**
     *Reserved for future use.  Must be 1.
     */
    protected byte tnum;

    /** Constructor. */
    public Images1(Map parent_map) throws ErrorObjectNotDefined {
        super(parent_map);
    }

    public String getName_map() {
        return name_map;
    }

    public void setName_map(String name_map) {
        this.name_map = name_map;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public float getGposx() {
        return gposx;
    }

    public void setGposx(float gposx) {
        this.gposx = gposx;
    }

    public float getGposy() {
        return gposy;
    }

    public void setGposy(float gposy) {
        this.gposy = gposy;
    }

    public float getGwidth() {
        return gwidth;
    }

    public void setGwidth(float gwidth) {
        this.gwidth = gwidth;
    }

    public float getGheight() {
        return gheight;
    }

    public void setGheight(float gheight) {
        this.gheight = gheight;
    }

    public int getImagelen() {
        return imagelen;
    }

    public void setImagelen(int imagelen) {
        this.imagelen = imagelen;
    }

    public float getMetax() {
        return metax;
    }

    public void setMetax(float metax) {
        this.metax = metax;
    }

    public float getMetay() {
        return metay;
    }

    public void setMetay(float metay) {
        this.metay = metay;
    }

    public byte getMetamapa() {
        return metamapa;
    }

    public void setMetamapa(byte metamapa) {
        this.metamapa = metamapa;
    }

    public byte getTnum() {
        return tnum;
    }

    public void setTnum(byte tnum) {
        this.tnum = tnum;
    }

    public void writeToStream(OutputStream stream) throws IOException {
        stream.write(this.convertStringToByteArray(this.name_map, true));
        stream.write(this.convertStringToByteArray(this.comments, true));
        stream.write(this.convertFloatTo4bytes(this.gposx));
        stream.write(this.convertFloatTo4bytes(this.gposy));
        stream.write(this.convertFloatTo4bytes(this.gwidth));
        stream.write(this.convertFloatTo4bytes(this.gheight));
        stream.write(this.convertIntTo4bytes(this.imagelen));
        stream.write(this.convertFloatTo4bytes(this.metax));
        stream.write(this.convertFloatTo4bytes(this.metay));
        stream.write(this.metamapa);
        stream.write(this.tnum);
    }

    public boolean isGeographic() {
        return true;
    }
    
}
