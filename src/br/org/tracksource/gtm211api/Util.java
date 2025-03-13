/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.tracksource.gtm211api;

/**
 *
 * @author ur5m
 */
public class Util {

    /**
     * Determines de counter-clockwise angle from the horizontal versor (1, 0) to the vector passed as parameter.
     *
     * @param xV1 The x coordinate of the origin of the vector.
     * @param yV1 The y coordinate of the origin of the vector.
     * @param xV2 The x coordinate of the distal of the vector.
     * @param yV2 The y coordinate of the distal of the vector.
     * @return Angle in radians.
     */
    public static double getCounterClockwiseAngle(double xV1, double yV1, double xV2, double yV2){
        double theta = Math.acos( (xV2 - xV1) / Math.sqrt((xV2-xV1)*(xV2-xV1) + (yV2-yV1)*(yV2-yV1)) );
        if((yV2-yV1) > 0)
            return theta;
        else
            return 2 * Math.PI - theta;
    }

    /**
     * Determines de clockwise angle from the horizontal versor (1, 0) to the vector passed as parameter.
     *
     * @param xV1 The x coordinate of the origin of the vector.
     * @param yV1 The y coordinate of the origin of the vector.
     * @param xV2 The x coordinate of the distal of the vector.
     * @param yV2 The y coordinate of the distal of the vector.
     * @return Angle in radians.
     */
    public static double getClockwiseAngle(double xV1, double yV1, double xV2, double yV2){
        double theta = Math.acos( (xV2 - xV1) / Math.sqrt((xV2-xV1)*(xV2-xV1) + (yV2-yV1)*(yV2-yV1)) );
        if((yV2-yV1) > 0)
            return 2 * Math.PI - theta;
        else
            return theta;
    }

    /**
     * Determina o angulo orientado no sentido anti-horario do vetor 1 ao vetor 2, ambos com origem em (x0, y0).
     * O valor do angulo eh em radianos.
     *
     * Determines de counter-clockwise angle from vector 1 to vector 2, both with common origin at (x0, y0).
     *
     * @param x0 x coordinate of the common origin.
     * @param y0 y coordinate of the common origin.
     * @param xV1 x coordinate of distal of vector 1.
     * @param yV1 y coordinate of distal of vector 1.
     * @param xV2 x coordinate of distal of vector 2.
     * @param yV2 y coordinate of distal of vector 2.
     * @return
     */
    public static double getCounterClockwiseAngleBetweenVectors(double x0, double y0, double xV1, double yV1, double xV2, double yV2){
        double theta1 = Util.getClockwiseAngle(x0, y0, xV1, yV1); //get the clockwise angle from versor (1, 0) to V1
        double theta2 = Util.getCounterClockwiseAngle(x0, y0, xV2, yV2); //get the counter-clockwise angle from versor (1, 0) to V2
        double dt = theta2 + theta1; //calculate total counter-clockwise angle
        if(dt >= 2 * Math.PI) //subtract 360 if needed
            dt -= 2 * Math.PI;
        return dt;
    }

    /**
     * Calculates the length, in meters, of a line between the two points of the globe
     * whose coordinates are the four parameters according to this formula:
     * length(m) = 6378000 * acos(sen(Lat1) * sen(Lat2) + cos(Lat1) * cos(Lat2) * cos(|Lon2 - Lon1|)).
     * The ellipsoid is a sphere with a radius of 6,378km for simplicity and speed, which yields lengths accurate enough
     * for general purpose GPS applications given we are not interested in comparing positions.
     * Source: Observatório Nacional http://obsn3.on.br/~jlkm/geopath/
     */
    public static double calculateGeodeticLengthSphere_m(double lat1, double lon1, double lat2, double lon2){
        double lat_no1_rad = lat1 * Constants.PI_OVER_180;
        double lat_no2_rad = lat2 * Constants.PI_OVER_180;
        double lon_no1_rad = lon1 * Constants.PI_OVER_180;
        double lon_no2_rad = lon2 * Constants.PI_OVER_180;
        double tetha = Math.sin(lat_no1_rad)*Math.sin(lat_no2_rad) + Math.cos(lat_no1_rad)*Math.cos(lat_no2_rad)*Math.cos(Math.abs(lon_no1_rad-lon_no2_rad));
        if(tetha > 1d)
            return 0.0d;
        return 6378000d * Math.acos(tetha);
    }


    /**
     * Determines de clockwise angle from the vertical versor (0, 1) to the vector passed as parameter.
     *
     * @param xV1 The x coordinate of the origin of the vector.
     * @param yV1 The y coordinate of the origin of the vector.
     * @param xV2 The x coordinate of the distal of the vector.
     * @param yV2 The y coordinate of the distal of the vector.
     * @return azimuth in degrees.
     */
    public static float getAzimuthDegrees( double xV1, double yV1, double xV2, double yV2 ){
        double x = xV2 - xV1;
        double y = yV2 - yV1;
        float z;
        if( Math.abs(y) < Constants.LAT_LONG_RESOLUTION ) //x é considerado zero, evitar divisão por zero
            if( x >= 0.0d )
                return 90.0f; //leste
            else
                return 270.0f; //oeste
        else{
            z = (float) ( Math.atan( -x / y ) * Constants.V180_OVER_PI );
            if ( y < 0.0d )
                z += 180.0d;
            if( z < 0)
                z = -z;
            else
                z = 360.0f - z;
            return z;
        }
    }

}
