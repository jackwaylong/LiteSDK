package com.longway.framework.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.concurrent.Future;

/**
 * Created by longway on 15/11/29.
 */
public class Utils {
    public static void cancelAsyncTask(AsyncTask asyncTask, boolean mayInterruptIfRunning) {
        if (asyncTask != null && !asyncTask.isCancelled() && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            asyncTask.cancel(mayInterruptIfRunning);
        }
    }

    public static void cancelFuture(Future future, boolean mayInterruptIfRunning) {
        if (future != null && !future.isCancelled() && !future.isDone()) {
            future.cancel(mayInterruptIfRunning);
        }
    }

    public static void gc() {
        System.gc();
        System.runFinalization();

    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static boolean contextIsValidate(Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    public static boolean fragmentIsValidate(Fragment fragment){
        if (fragment == null || fragment.isDetached() || fragment.isRemoving()) {
            return false;
        }
        return true;
    }

    public static Context convertContext(Context context) {
        if (context == null) {
            throw new NullPointerException("context must be not null");
        }
        if (!(context instanceof Application)) {
            return context.getApplicationContext();
        }
        return context;
    }

    public static boolean viewIsValidate(View view) {
        if (view == null) {
            return false;
        }
        return true;
    }
}
