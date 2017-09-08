package org.hisp.dhis.scheduling.Parameters;

import com.cronutils.model.field.CronField;
import com.cronutils.model.field.CronFieldName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hisp.dhis.feedback.ErrorReport;
import org.hisp.dhis.scheduling.JobParameters;
import org.hisp.dhis.scheduling.TaskId;

import java.util.Map;

/**
 * @author Henning Håkonsen
 */
public class MonitoringJobParameters
    implements JobParameters
{
    private TaskId taskId;

    public MonitoringJobParameters()
    {}

    public MonitoringJobParameters( TaskId taskId )
    {
        this.taskId = taskId;
    }

    public TaskId getTaskId()
    {
        return taskId;
    }

    @Override
    public ErrorReport validate( Map<CronFieldName, CronField> cronFieldNameCronFieldMap )
    {
        return null;
    }
}
