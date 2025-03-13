/*
 * Constants.java
 *
 * Created on March 9, 2010, 12:55 PM
 *
 */

package br.org.tracksource.gtm211api;

/**
 * This class has some public static constants that are parameters of this API.
 *
 * @author ur5m
 */
public class Constants {
    
    /** Creates a new instance of Constants */
    public Constants() {
    }
 
    /**
     * Defines the minimum latitude/longitude difference that is considered actually different latitudes/longitudes.
     * For instance: 23.1234561111 and 23.123456222 are considered equal if LAT_LONG_RESOLUTION is set to 0.000001.
     * This is necessary due to the inherent inaccuracies of floating point operations.
     */
    public static final double LAT_LONG_RESOLUTION = 0.000001;

    /**
     * Defines de tolerance used to compute whether a point is on a line.  The point is almost never actually zero distance from the line,
     * thus the need for a tolerance.
     */
    public static final double POINT_ON_SEGMENT_TOLERANCE = 0.000001;
    
    /**
     * The charset name used to convert the byte-stream strings stored in GTM files to Java unicode String objects.
     */
    public static final String charsetNameGTM2Java = "ISO-8859-1";

    /**
     * The charset name used to convert the Java unicode String objects to byte-stream strings used to store text in GTM files.
     */
    public static final String charsetNameJava2GTM = "ISO-8859-1";

    /**
     * The charset name used to convert the single-byte character strings stored in TXT files to Java unicode String objects.
     */
    public static final String charsetNameTXT2Java = "ISO-8859-1";

    /**
     * The charset name used to convert the single-byte character strings in XML files to Java unicode String objects.
     */
    public static final String charsetNameXML2Java = "ISO-8859-1";

    /**
     * The PI constant divided by 180.  Can be used to convert from degress to radians.
     */
    public static final double PI_OVER_180 = Math.PI / 180.0d;

    /**
     * 180 divided by the PI constant.  Can be used to convert from radians to degress.
     */
    public static final double V180_OVER_PI = 180.0d / Math.PI;
}
