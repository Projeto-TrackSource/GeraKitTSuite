/*
 * ErrorVersionIsNot211.java
 *
 * Created in Dec 24th, 2008. 11:12
 *
 */

package br.org.tracksource.gtm211api.errors;

/**
 * Error thrown when a GTM file with a format different from 211 is loaded.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class ErrorVersionIsNot211 extends ErrorGTM{
    
    /** Default constructor. */
    public ErrorVersionIsNot211() {
        super("Attempt to set version different from 211.\nIf you're trying to open an existing GTM file, maybe it is corrupt.");
    }
    
    /** Constructor with a message. */
    public ErrorVersionIsNot211(String message) {
        super("Attempt to set version different from 211.\nIf you're trying to open an existing GTM file, maybe it is corrupt.\n" + message);
    }
    
}
