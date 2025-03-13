/*
 * PointRecord.java
 *
 * Criada em 27 de Dezembro de 2008, 12:04
 *
 */

package br.org.tracksource.gtm211api.domain;

import br.org.tracksource.gtm211api.Constants;
import br.org.tracksource.gtm211api.errors.ErrorObjectNotDefined;

/**
 * Base class of all classes representing records for point map entities such as waypoints and tracklog points
 * in the GTM file.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public abstract class PointRecord extends Record{
     /**
     *Latitude in degrees with 13 decimal places.
     */
    protected double latitude;
    
    /**
     *Longitude in degrees with 13 decimal places.
     */
    protected double longitude;

    /**
     *Altitude in meters.
     */
    protected float alt;
    
    /**
     *Point date - Number of seconds since 31-Dec-1989. Example: 284202876 means 1-February-1999 9:14:36 AM.
     *Client applications are responsible for correct conversion between these integer values and actual date and time data types.
     */
    protected int date;
    
    /**
     * This value is not stored in GTM files. Instead, it is useful in algorithms.
     */
    protected boolean visited;
   
    /** Constructor. */
    public PointRecord(Map parent_map) throws ErrorObjectNotDefined {
        super(parent_map);
    }

    /** Construtor com valor de latitude e longitude. */
    public PointRecord(Map parent_map, double latitude, double longitude) throws ErrorObjectNotDefined {
        this(parent_map);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getAlt() {
        return alt;
    }

    public void setAlt(float alt) {
        this.alt = alt;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
    
    /**
     * Tells whether this point entity has the same coordinates of
     * the given point according to Constants#LAT_LONG_RESOLUTION setting.
     * @see #areTooClose(PointRecord, double)
     */
    public boolean coincidesWith(PointRecord pr){
        return (Math.abs(pr.getLatitude() -this.getLatitude())  < Constants.LAT_LONG_RESOLUTION) && 
               (Math.abs(pr.getLongitude()-this.getLongitude()) < Constants.LAT_LONG_RESOLUTION);
    }

    /**
     * Tells wether this point entity is too close to the given point according
     * to the given tolerance.  This method does like #coincidesWith(PointRecord)
     * but with a specific tolerance rather than the standard API constant Constants#LAT_LONG_RESOLUTION that
     * defines geographical coincidence.
     */
    public boolean areTooClose(PointRecord pr, double tolerance){
        return (Math.abs(pr.getLatitude() -this.getLatitude())  < tolerance) &&
               (Math.abs(pr.getLongitude()-this.getLongitude()) < tolerance);
    }
    
    public boolean isGeographic(){
        return true;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
