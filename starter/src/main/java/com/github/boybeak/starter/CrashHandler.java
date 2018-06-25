package com.github.boybeak.starter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gaoyunfei on 16/8/28.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss", Locale.getDefault());

    public static final String ACTION_CRASH_CAUGHT = "com.nulldreams.action.ACTION_CRASH_CAUGHT";
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private static CrashHandler sHandler = null;

    public static synchronized CrashHandler getInstance() {
        if (sHandler == null) {
            sHandler = new CrashHandler();
        }
        return sHandler;
    }

//    private Context mContext;

    private File dir;
    private String versionName;

    private CrashHandler() {
    }

    public void install (Context context) {
        dir = context.getExternalCacheDir();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        throwable.printStackTrace();

        File crashLogFile = new File(dir,
                "crash" + File.separator + DATE_FORMAT.format(new Date()) + ".crash");
        if (!crashLogFile.getParentFile().exists()) {
            crashLogFile.getParentFile().mkdirs();
        }

        try {
            FileWriter fileWriter = new FileWriter(crashLogFile);
            fileWriter.append("app_version:" + versionName + "\n");
            fileWriter.append("model:" + Build.MODEL + "\n");
            fileWriter.append("manufacturer:" + Build.MANUFACTURER + "\n");
            fileWriter.append("sdk_int:" + Build.VERSION.SDK_INT + "\n");
            fileWriter.append("code_name:" + Build.VERSION.CODENAME + "\n");
            fileWriter.append("incremental:" + Build.VERSION.INCREMENTAL + "\n");
            fileWriter.append("crash_time:" + FORMAT.format(new Date()) + "\n");
            PrintWriter writer = new PrintWriter(fileWriter);

            throwable.printStackTrace(writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.exit(1);
        }

        /*Intent it = new Intent();
        it.setAction(ACTION_CRASH_CAUGHT);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(it);*/
    }
}
