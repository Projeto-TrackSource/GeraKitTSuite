/*
 * Record.java
 *
 * Criada em 25 de Dezembro de 2008, 09:46
 *
 */

package br.org.tracksource.gtm211api.domain;

import br.org.tracksource.gtm211api.Constants;
import br.org.tracksource.gtm211api.errors.ErrorObjectNotDefined;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Base class of all classes representing records in the GTM file.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public abstract class Record {
    /**
     *The reference to the {@link Map} object composed by this object.
     */
    protected Map parentMapReference;
    
    /**
     *The number used to tell whether an object is selected or not for removal or by the user via mouse, for examples.
     *If the number equals {@link Map#selectionNumber} then this object is selected.
     */
    protected int selectionNumber = 0;
    
    /** Constructor.
     *@param parent_map The {@link Map} object reference representing the map specified by the GTM file
     *from which the record was read.
     */
    public Record(Map parent_map) throws ErrorObjectNotDefined  {
        if(parent_map == null)
            throw new ErrorObjectNotDefined("Parent map reference cannot be null.");
        this.parentMapReference = parent_map;
    }

    public Map getParentMapReference() {
        return parentMapReference;
    }

    public void setParentMapReference(Map parentMapReference) throws ErrorObjectNotDefined {
        if(parentMapReference == null)
            throw new ErrorObjectNotDefined("Parent map reference cannot be null.");
        this.parentMapReference = parentMapReference;
    }
    
    /**
     *Writes the bytes corresponding to a GTM file record to the specified stream to typically, but not limited to, a disk file.
     */
    public abstract void writeToStream(OutputStream stream) throws IOException;
    
    
    /**
     *Converts a 16-bit integer value into an array of two bytes.  The first byte, with index zero,
     *is the least significant byte.  This method is called internally by {@link #writeToStream(OutputStream)} implementations
     * in concrete subclasses.
     */
    protected byte[] convertShortTo2bytes(short value){
        byte[] r = new byte[2];
        r[0] = (byte)( value & 0x000000FF);
        r[1] = (byte)((value & 0x0000FF00) >> 8);
        return r;
    }

    /**
     *Converts a 32-bit integer value into an array of four bytes.  The first byte, with index zero,
     *is the least significant byte.  This method is called internally by {@link #writeToStream(OutputStream)} implementations
     * in concrete subclasses.
     */
    protected byte[] convertIntTo4bytes(int value){
        byte[] r = new byte[4];
        r[0] = (byte)( value & 0x000000FF);
        r[1] = (byte)((value & 0x0000FF00) >> 8);
        r[2] = (byte)((value & 0x00FF0000) >> 16);
        r[3] = (byte)((value & 0xFF000000) >> 24);
        return r;
    }

    /**
     *Converts a 32-bit IEEE floating point value into an array of four bytes.  The first byte, with index zero,
     *is the rightmost byte in byte string float point binary representation.  This method is called
     * internally by {@link #writeToStream(OutputStream)} implementations in concrete subclasses.
     */
    protected byte[] convertFloatTo4bytes(float value){
        int ivalue = Float.floatToIntBits(value);
        byte[] r = new byte[4];
        r[0] = (byte)( ivalue & 0x000000FF);
        r[1] = (byte)((ivalue & 0x0000FF00) >> 8);
        r[2] = (byte)((ivalue & 0x00FF0000) >> 16);
        r[3] = (byte)((ivalue & 0xFF000000) >> 24);
        return r;
    }

    
    /**
     *Converts a 64-bit integer value into an array of eight bytes.  The first byte, with index zero,
     *is the least significant byte.  This method is called internally by {@link #writeToStream(OutputStream)} implementations
     * in concrete subclasses.
     */
    protected byte[] convertLongTo8bytes(long value){
        byte[] r = new byte[8];
        r[0] = (byte)( value & 0x00000000000000FFl);
        r[1] = (byte)((value & 0x000000000000FF00l) >> 8);
        r[2] = (byte)((value & 0x0000000000FF0000l) >> 16);
        r[3] = (byte)((value & 0x00000000FF000000l) >> 24);
        r[4] = (byte)((value & 0x000000FF00000000l) >> 32);
        r[5] = (byte)((value & 0x0000FF0000000000l) >> 40);
        r[6] = (byte)((value & 0x00FF000000000000l) >> 48);
        r[7] = (byte)((value & 0xFF00000000000000l) >> 56);
        return r;
    }

    /**
     *Converts a 64-bit IEEE floating point value into an array of eight bytes.  The first byte, with index zero,
     *is the rightmost byte in byte string float point binary representation.  This method is called
     * internally by {@link #writeToStream(OutputStream)} implementations in concrete subclasses.
     */
    protected byte[] convertDoubleTo8bytes(double value){
        long lvalue = Double.doubleToLongBits(value);
        byte[] r = new byte[8];
        r[0] = (byte)( lvalue & 0x00000000000000FFl);
        r[1] = (byte)((lvalue & 0x000000000000FF00l) >> 8);
        r[2] = (byte)((lvalue & 0x0000000000FF0000l) >> 16);
        r[3] = (byte)((lvalue & 0x00000000FF000000l) >> 24);
        r[4] = (byte)((lvalue & 0x000000FF00000000l) >> 32);
        r[5] = (byte)((lvalue & 0x0000FF0000000000l) >> 40);
        r[6] = (byte)((lvalue & 0x00FF000000000000l) >> 48);
        r[7] = (byte)((lvalue & 0xFF00000000000000l) >> 56);
        return r;
    }
    
    /**
     *Converts a text string into a sequence of bytes.  The first byte, with index zero, is the first character
     *of the text.
     *@param string_is_variable_length If true, the method includes two heading bytes informing the string size (thus limiting the text length to 64kB),
     * a requirement for the way Visual Basic stores variable length strings in files.
     */
    protected byte[] convertStringToByteArray(String text, boolean string_is_variable_length) throws UnsupportedEncodingException{
        /*
        Available 8-bit charsets per 1.4.2 JSE specification.  Later versions may support further charsets. 
        US-ASCII
        ISO-8859-1
        UTF-8
         */
        byte[] string_data = text.getBytes(Constants.charsetNameJava2GTM);
        if(string_is_variable_length){
            byte[] bytes_string_length = this.convertShortTo2bytes((short)text.length());
            byte[] final_bytes = new byte[2 + string_data.length];
            System.arraycopy(bytes_string_length, 0, final_bytes, 0, 2);
            System.arraycopy(string_data, 0, final_bytes, 2, string_data.length);
            return final_bytes;
        }else
            return string_data;
    }
    
    /**
     *Converts a boolean value into a sequence of two bytes. False is 00 00 and true is FF FF.
     */
    protected byte[] convertBooleanTo2bytes(boolean value){
        byte[] r = new byte[2];
        if(value)
            r[0] = r[1] = (byte)0x000000FF;
        else
            r[0] = r[1] = 0;
        return r;
    }
    
    /**
     *Sets the {@link #selectionNumber} field the same value as {@link Map#selectionNumber} field of the
     *{@link #parentMapReference} object, causing this object to be selected.
     */
    public void setSelected(){
        this.selectionNumber = this.parentMapReference.getSelectionNumber();
    }

    /**
     *Sets the {@link #selectionNumber} field to zero causing this object to be unselected.
     *@see Map#clearSelection()
     */
    public void setUnselected(){
        this.selectionNumber = 0;
    }

    /**
     *Returns whether this object is selected or not.
     */
    public boolean isSelected(){
        return this.selectionNumber == this.parentMapReference.getSelectionNumber();
    }
    
    /**
     * Tells whether this object is a geographic entity or not.
     */
    public abstract boolean isGeographic();
}
