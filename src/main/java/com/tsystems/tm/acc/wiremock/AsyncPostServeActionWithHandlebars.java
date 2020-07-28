package com.tsystems.tm.acc.wiremock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public abstract class AsyncPostServeActionWithHandlebars extends PostServeActionWithHandlebars {
    protected final ScheduledExecutorService scheduler;

    public AsyncPostServeActionWithHandlebars() {
        super();
        scheduler = Executors.newScheduledThreadPool(10);
    }
}
