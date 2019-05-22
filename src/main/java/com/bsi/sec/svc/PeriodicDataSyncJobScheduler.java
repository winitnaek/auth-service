/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.exception.ProcessingException;
import static com.bsi.sec.util.AppConstants.SCHED_CRON_EXPR;
import com.bsi.sec.util.LogUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.scheduler.SchedulerFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author igorV
 */
@Component
public class PeriodicDataSyncJobScheduler {

    private static final Logger log = LoggerFactory.getLogger(PeriodicDataSyncJobScheduler.class);

    @Autowired
    private AsyncPeriodicDataSyncJob perDataSyncJob;

    @Autowired
    private Ignite ignite;

    /**
     * "Periodic Sync" will be scheduled as per configuration.
     *
     * @throws Exception
     */
    public void schedule() throws Exception {
        SchedulerFuture<?> fut = null;

        try {
            fut = ignite.scheduler().scheduleLocal(new Runnable() {
                @Override
                public void run() {
                    try {
                        perDataSyncJob.run();
                    } catch (Exception ex) {
                        String errMsg = LogUtils.jsonize("Scheduled Periodic"
                                + " Job error!", "exception", ex);

                        if (log.isErrorEnabled()) {
                            log.error(errMsg);
                        }
                    }
                }
            }, SCHED_CRON_EXPR);

            if (log.isDebugEnabled()) {
                if (!fut.isCancelled() && fut.isDone()) {
                    log.debug(LogUtils.jsonize("Average Exec Time", "time",
                            fut.averageIdleTime()));
                }
            }
        } catch (Exception ex) {
            if (fut != null && !fut.isCancelled()) {
                fut.cancel();
            }

            String errMsg = LogUtils.jsonize("Scheduled Periodic"
                    + " Job error!", "exception", ex);

            if (log.isErrorEnabled()) {
                log.error(errMsg);
            }

            throw new ProcessingException(errMsg);
        }
    }
}
