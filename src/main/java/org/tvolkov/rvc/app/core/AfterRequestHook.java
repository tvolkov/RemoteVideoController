package org.tvolkov.rvc.app.core;

import android.os.Bundle;

public interface AfterRequestHook {
    public void afterRequest(final int requestId, final int result, final Bundle data);
}
