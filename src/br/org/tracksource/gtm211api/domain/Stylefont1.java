/*
 * Stylefont1.java
 *
 * Criada em 25 de Dezembro de 2008, 19:51
 *
 */

package br.org.tracksource.gtm211api.domain;

import br.org.tracksource.gtm211api.errors.ErrorGTMRule;
import br.org.tracksource.gtm211api.errors.ErrorObjectNotDefined;
import br.org.tracksource.gtm211api.errors.ErrorValueOutOfRange;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The Stylefont1 class represents a waypoint display style, example.
 * The waypoint style records begin following the waypoint records of a GTM file per the 211 GTM format specification and appear
 * as many times as specified by the <code>nwptstyles</code> field of the <i>header</i> record.<br>
 * <br>
 * IMPORTANT: There are four predefined styles, so there must be necessarily at least four <i>stylefont1</i> records in the GTM file,
 * except for when there are no waypoints in the map, in this case, there will be no <i>stylefont1</i> record in the file,
 * regardless of the value returned by {@link Header#getNwptstyles()}.  If applicable, the four required waypoint style records are:<br>
 * <img src="doc-files/the_four_predefined_wpt_styles.jpg"/>
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class Stylefont1 extends Record{

    /**
     *Font size multiplied by -1.333333. Example: If font size is 8, height will be -11.
     */
    protected int Height;

    /**
     *Font name. Default = <i>Times New Roman</i>.
     */
    protected String FaceName;

    /**
     *Waypoint display style.  See the <code>WPT_STYLE_*</code> constants defined in the {@link Wpts1} class for the predefined styles.
     *Values greater than 3 refer to custom styles defined by users of GPS Trackmaker Pro (r).
     */
    protected byte dspl;

    /**
     *Text color (RGB). Actually BGR, for example red is 255.
     */
    protected int color;

    /**
     *Text weight. Normal = 400 / Bold = 700.  The value 0 is often found and defaults to normal weight, although it is not documented
     * in the GTM 211 file format specification.
     */
    protected int Weight;

    /**
     *Maximum scale in kilometers that the Waypoint appears on screen (Default = 0).
     */
    protected float scale1;

    /**
     *Border flags of the background box:<br><img src="doc-files/border_flags.jpg"/>.
     */
    protected byte border;

    /**
     *Presence background box. Used by the Text Box predefined tyle ({@link Wpts1#WPT_STYLE_COMMENTS_IN_A_TEXT_BOX}).
     */
    protected boolean background;

    /**
     *Color of the Background Box (RGB). Actually BGR, for example red is 255.
     */
    protected int backcolor;

    /**
     *Normal = 0 / Italic = 1.
     */
    protected byte Italic;

    /**
     *Normal = 0 / Underline = 1.
     */
    protected byte Underline;

    /**
     *Normal = 0 / Strikeout = 1.
     */
    protected byte StrikeOut;

    /**
     *Text alignment in Text Box: 0 = left; 1 = center; 2 = right.
     */
    protected byte alignment;
    
    
    
    /** Constructor. */
    public Stylefont1(Map parent_map) throws ErrorObjectNotDefined {
        super(parent_map);
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int Height) {
        this.Height = Height;
    }

    public String getFaceName() {
        return FaceName;
    }

    public void setFaceName(String FaceName) {
        this.FaceName = FaceName;
    }

    public byte getDspl() {
        return dspl;
    }

    public void setDspl(byte dspl) {
        this.dspl = dspl;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int Weight) throws ErrorValueOutOfRange {
        if(Weight != 0 && Weight != 400 && Weight != 700)
            throw new ErrorValueOutOfRange("0, 400 or 700", String.valueOf(Weight));
        this.Weight = Weight;
    }

    public float getScale1() {
        return scale1;
    }

    public void setScale1(float scale1) {
        this.scale1 = scale1;
    }

    public byte getBorder() {
        return border;
    }

    public void setBorder(byte border) {
        this.border = border;
    }

    public boolean isBackground() {
        return background;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }

    public int getBackcolor() {
        return backcolor;
    }

    public void setBackcolor(int backcolor) {
        this.backcolor = backcolor;
    }

    public byte getItalic() {
        return Italic;
    }

    public void setItalic(byte Italic) throws ErrorValueOutOfRange  {
        if(Italic != 0 && Italic != 1)
            throw new ErrorValueOutOfRange("0 or 1", String.valueOf(Italic));
        this.Italic = Italic;
    }

    public byte getUnderline() {
        return Underline;
    }

    public void setUnderline(byte Underline) throws ErrorValueOutOfRange {
        if(Underline != 0 && Underline != 1)
            throw new ErrorValueOutOfRange("0 or 1", String.valueOf(Underline));
        this.Underline = Underline;
    }

    public byte getStrikeOut() {
        return StrikeOut;
    }

    public void setStrikeOut(byte StrikeOut) throws ErrorValueOutOfRange {
        if(StrikeOut != 0 && StrikeOut != 1)
            throw new ErrorValueOutOfRange("0 or 1", String.valueOf(StrikeOut));
        this.StrikeOut = StrikeOut;
    }

    public byte getAlignment() {
        return alignment;
    }

    public void setAlignment(byte alignment) throws ErrorValueOutOfRange {
        if(alignment != 0 && alignment != 1 && alignment != 2)
            throw new ErrorValueOutOfRange("0, 1 or 2", String.valueOf(alignment));
        this.alignment = alignment;
    }

    public void writeToStream(OutputStream stream) throws IOException {
        stream.write(this.convertIntTo4bytes(this.Height));
        stream.write(this.convertStringToByteArray(this.FaceName, true));
        stream.write(this.dspl);
        stream.write(this.convertIntTo4bytes(this.color));
        stream.write(this.convertIntTo4bytes(this.Weight));
        stream.write(this.convertFloatTo4bytes(this.scale1));
        stream.write(this.border);
        stream.write(this.convertBooleanTo2bytes(this.background));
        stream.write(this.convertIntTo4bytes(this.backcolor));
        stream.write(this.Italic);
        stream.write(this.Underline);
        stream.write(this.StrikeOut);
        stream.write(this.alignment);
    }
    
    /**
     *Returns a new Stylefont1 object using the same properties of this Stylefont1 object.
     *@param parent_map_reference A {@link Map} object reference that can be same of this object.
     */
    public Stylefont1 makeCopy(Map parent_map_reference) throws ErrorObjectNotDefined{
        Stylefont1 result = new Stylefont1(parent_map_reference);
        try{
            result.setHeight(this.Height);
            result.setFaceName(this.FaceName);
            result.setDspl(this.dspl);
            result.setColor(this.color);
            result.setWeight(this.Weight);
            result.setScale1(this.scale1);
            result.setBorder(this.border);
            result.setBackground(this.background);
            result.setBackcolor(this.backcolor);
            result.setItalic(this.Italic);
            result.setUnderline(this.Underline);
            result.setStrikeOut(this.StrikeOut);
            result.setAlignment(this.alignment);
        }catch(Exception ex){} //assume all parameters above are valid
        return result;
    }

    public boolean isGeographic() {
        return false;
    }
}
