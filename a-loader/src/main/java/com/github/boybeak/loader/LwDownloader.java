package com.github.boybeak.loader;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class LwDownloader extends AsyncTask<Void, Float, Void> {

    private static final String TAG = LwDownloader.class.getSimpleName();

    public static LwDownloader newInstance() {
        return new LwDownloader();
    }

    public static int getFileSize(String url) {
        try {
            return getFileSize(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public static int getFileSize(URL url) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).disconnect();
            }
        }
    }

    private LwDownloader() {

    }

    private URL from;
    private int size;
    private File to, toTmp;
    private Callback callback;

    public LwDownloader from(URL url) {
        from = url;
        return this;
    }

    public LwDownloader from(String url) {
        try {
            return from(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("can not make a URL by {" + url + "}");
    }

    public LwDownloader to(File file) {
        to = file;
        if (!to.getParentFile().exists()) {
            to.getParentFile().mkdirs();
        }
        toTmp = new File(to.getParentFile(), "." + to.getName() + ".tmp");
        return this;
    }

    public LwDownloader listenBy(Callback callback) {
        this.callback = callback;
        return this;
    }

    public LwDownloader to(String path) {
        return to(new File(path));
    }

    public LwDownloader start() {
        execute();
        return this;
    }

    private void doDownload() {
        if (to.isFile() && to.exists()) {
            if (callback != null) {
                callback.onEnd(size, to, from);
            }
            return;
        }
        BufferedInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new BufferedInputStream(from.openStream());
            outStream = new FileOutputStream(to, true);
            byte dataBuffer[] = new byte[1024 * 8];
            int bytesRead;
            int bytesCount = (int)toTmp.length();
            int notifyLoopIndex = 0;
            inStream.skip(bytesCount);
            final int updateLoopSize = (int)Math.ceil(size * 5f / 100 / dataBuffer.length);
            Log.v(TAG, "doDownload updateLoopSize=" + updateLoopSize);
            int updateProgressCount = 0;
            while ((bytesRead = inStream.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                bytesCount += bytesRead;
                outStream.write(dataBuffer, 0, bytesRead);

                if (notifyLoopIndex == updateLoopSize) {
                    if (callback != null) {
                        callback.onProgress(size, bytesCount, from);
                    }
                    publishProgress(bytesCount * 100f / size);
                    updateProgressCount++;
                    notifyLoopIndex = 0;
                } else {
                    notifyLoopIndex++;
                }
            }
            toTmp.renameTo(to);
            if (callback != null) {
                callback.onEnd(size, to, from);
            }
            Log.v(TAG, "doDownload updateProgressCount=" + updateProgressCount);
        } catch (IOException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onError(e);
            }
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected final Void doInBackground(Void... voids) {
        Log.v(TAG, "doInBackground thread=" + Thread.currentThread().getName());
        try {
            size = getFileSize(from);
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onError(e);
            }
            return null;
        }
        if (toTmp.exists() && toTmp.length() > size) {
            toTmp.delete();
			/*if (autoContinueOnBreakPointIfPossible) {

			} else {
				throw new IllegalStateException("Already download file larger than total file");
			}*/
        }
        if (size > 0) {
            if (callback != null) {
                callback.onStart(size, from);
            }
            doDownload();
        } else {
            Exception e = new IllegalStateException("The download url {" + from.toString() + "} seems doesn't exist");
            if (callback != null) {
                callback.onError(e);
            }
        }
        return null;
    }

    @Override
    protected final void onPreExecute() {
        Log.v(TAG, "onPreExecute thread=" + Thread.currentThread().getName());
    }

    @Override
    protected final void onProgressUpdate(Float... values) {
        Log.v(TAG, "onProgressUpdate thread=" + Thread.currentThread().getName() + " progress=" + values[0]);
    }

    @Override
    protected final void onPostExecute(Void aVoid) {
        Log.v(TAG, "onPostExecute thread=" + Thread.currentThread().getName());
    }

    @Override
    protected void onCancelled() {
        Log.v(TAG, "onCancelled thread=" + Thread.currentThread().getName());
    }

    public interface Callback {
        void onStart(int fileSize, URL url);
        void onProgress(int fileSize, int bytesSize, URL url);
        void onEnd(int fileSize, File file, URL url);
        void onError(Exception e);
    }

}
