/*
 * ErrorCodeIsNotTrackmaker.java
 *
 * Criada em 24 de Dezembro de 2008, 11:25
 *
 */

package br.org.tracksource.gtm211api.errors;

/**
 * Error thrown when a GTM file with a header code property different from <b><code>"TrackMaker"</code></b> is loaded.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class ErrorCodeIsNotTrackMaker extends ErrorGTM{
    
    /** Default constructor. */
    public ErrorCodeIsNotTrackMaker() {
        super("Attempt to set code different from \"TrackMaker\".");
    }
    
    /** Constructor with a message. */
    public ErrorCodeIsNotTrackMaker(String message) {
        super("Attempt to set code different from \"TrackMaker\"." + message);
    }
}
