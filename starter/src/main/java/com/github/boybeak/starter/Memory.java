package com.github.boybeak.starter;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by gaoyunfei on 2018/3/8.
 */

public class Memory {

    private static final String TAG = Memory.class.getSimpleName();

    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }

    public static String getAvailableInternalMemorySize(Context context) {
        return formatSize(context, getAvailableInternalMemorySize());
    }

    public static String getTotalInternalMemorySize(Context context) {
        return formatSize(context, getTotalInternalMemorySize());
    }

    public static long getTotalInternalMemorySize () {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks * blockSize;
    }

    public static String getAvailableExternalMemorySize(Context context) {
        return formatSize(context, getAvailableExternalMemorySize());
    }

    public static long getAvailableExternalMemorySize () {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return availableBlocks * blockSize;
        }
        return 0;
    }

    public static String getTotalExternalMemorySize(Context context) {
        return formatSize(context, getTotalExternalMemorySize());
    }

    public static long getTotalExternalMemorySize () {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return totalBlocks * blockSize;
        }
        return 0;
    }

    public static long getUsedExternalMemorySize () {
        return getTotalExternalMemorySize() - getAvailableExternalMemorySize();
    }

    public static float getExternalMemoryUsagePercent () {
        long totalSize = getTotalExternalMemorySize();
        if (totalSize > 0) {
            return (int) (getUsedExternalMemorySize() * 100f / totalSize);
        }
        return 0;
    }

    public static String getUsedExternalMemorySize (Context context) {
        return formatSize(context, getUsedExternalMemorySize());
    }

    public static String formatSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
        /*String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();*/
    }

    public static long getRamSize (Context context) {
        ActivityManager actManager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;
        return totalMemory;
    }

    public static long sizeOfFolder(File dir) {

        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            if (fileList == null) {
                return 0;
            }
            for(int i = 0; i < fileList.length; i++) {
                // Recursive call if it's a directory
                if(fileList[i].isDirectory()) {
                    result += sizeOfFolder(fileList [i]);
                } else {
                    // Sum the file size in bytes
                    result += fileList[i].length();
                }
            }
            return result; // return the file size
        }
        return 0;
    }

    public static boolean isDirNoMedia (File dir) {
        return new File(dir, ".nomedia").exists();
    }

    public static boolean makeFolderNoMedia (File folder) {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File nomedia = new File(folder, ".nomedia");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(nomedia);
            fos.write("this is a no meidia file".getBytes());
            fos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
