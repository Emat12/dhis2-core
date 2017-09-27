package org.hisp.dhis.scheduling;


import java.io.Serializable;
import java.util.HashMap;

/**
 * Interface for job specific parameters. Serializable so that we can store the object in the database.
 *
 * @author Henning Håkonsen
 */
public interface JobParameters
    extends Serializable
{
    JobId getJobId();
}
