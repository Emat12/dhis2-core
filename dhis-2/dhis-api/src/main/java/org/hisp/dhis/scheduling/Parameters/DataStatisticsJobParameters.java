package org.hisp.dhis.scheduling.Parameters;

import org.hisp.dhis.scheduling.JobParameters;
import org.hisp.dhis.scheduling.JobId;

/**
 * @author Henning Håkonsen
 */
public class DataStatisticsJobParameters
    implements JobParameters
{
    private static final long serialVersionUID = 2L;

    private JobId jobId;

    public DataStatisticsJobParameters()
    {}

    public DataStatisticsJobParameters( JobId jobId )
    {
        this.jobId = jobId;
    }

    public JobId getJobId()
    {
        return jobId;
    }

    public void setJobId( JobId jobId )
    {
        this.jobId = jobId;
    }
}
