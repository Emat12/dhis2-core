package org.hisp.dhis.validation.scheduling;

/*
 * Copyright (c) 2004-2017, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.hisp.dhis.message.MessageService;
import org.hisp.dhis.scheduling.Job;
import org.hisp.dhis.scheduling.JobParameters;
import org.hisp.dhis.scheduling.JobType;
import org.hisp.dhis.scheduling.Parameters.MonitoringJobParameters;
import org.hisp.dhis.system.notification.Notifier;
import org.hisp.dhis.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hisp.dhis.system.notification.NotificationLevel.ERROR;
import static org.hisp.dhis.system.notification.NotificationLevel.INFO;

/**
 * @author Lars Helge Overland
 * @author Jim Grace
 */
public class MonitoringJob
    implements Job
{
    @Autowired
    private ValidationService validationService;

    @Autowired
    private Notifier notifier;

    @Autowired
    private MessageService messageService;

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public JobType getJobType()
    {
        return JobType.MONITORING;
    }

    @Override
    public void execute( JobParameters jobParameters )
    {
        MonitoringJobParameters jobConfig = (MonitoringJobParameters) jobParameters;

        notifier.clear( jobConfig.getJobId() ).notify( jobConfig.getJobId(), "Monitoring data" );

        try
        {
            validationService
                .startInteractiveValidationAnalysis( jobConfig.getPeriodStartDate(), jobConfig.getPeriodEndDate(),
                    jobConfig.getOrganisationUnits(), jobConfig.getAttributeOptionCombo(),
                    jobConfig.getValidationRuleGroups(), jobConfig.isSendNotifications(),
                    jobConfig.isPersistResults() );

            notifier.notify( jobConfig.getJobId(), INFO, "Monitoring process done", true );
        }
        catch ( RuntimeException ex )
        {
            notifier.notify( jobConfig.getJobId(), ERROR, "Process failed: " + ex.getMessage(), true );

            messageService.sendSystemErrorNotification( "Monitoring process failed", ex );

            throw ex;
        }
    }
}
