package com.github.boybeak.loader;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URL;

public class downLoader {

    public static downLoader newInstance() {
        return new downLoader();
    }

    static DownloaderHandler handler = null;
    private Downloader downloader = null;

    private downLoader() {
        downloader = Downloader.newInstance();
    }

    public void download(String from, File to, Downloader.Callback callback) {
        handler = new DownloaderHandler(callback);
        downloader.from(from).to(to).autoContinueOnBreakPointIfPossible(true)
                .listenBy(new Downloader.Callback() {
                    @Override
                    public void onStart(long fileSize, URL url) {
                        handler.sendStartMsg(fileSize, url);
                    }

                    @Override
                    public void onProgress(long fileSize, long bytesSize, URL url) {
                        handler.sendProgressMsg(fileSize, bytesSize, url);
                    }

                    @Override
                    public void onEnd(long fileSize, File file, URL url) {
                        handler.sendEndMsg(fileSize, file, url);
                    }

                    @Override
                    public void onError(Exception e) {
                        handler.sendErrorMsg(e);
                    }
                })
                .start();
    }

    private static class DownloaderHandler extends Handler {

        private static final int ACTION_START = 1, ACTION_PROGRESS = 2, ACTION_END = 3, ACTION_ERROR = 4;

        private WeakReference<Downloader.Callback> callbackWeakRef = null;

        public DownloaderHandler (Downloader.Callback callback) {
            callbackWeakRef = new WeakReference<>(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            Downloader.Callback callback = callbackWeakRef.get();
            if (callback == null) {
                return;
            }
            switch (msg.what) {
                case ACTION_START: {
                    callback.onStart(msg.getData().getLong("size"), (URL)msg.obj);
                }
                case ACTION_PROGRESS:
                    callback.onProgress(
                            msg.getData().getLong("size"),
                            msg.getData().getLong("bytes_size"),
                            (URL)msg.obj);
                    break;
                case ACTION_END:
                    callback.onEnd(
                            msg.getData().getLong("size"),
                            (File) msg.getData().getSerializable("file"),
                            (URL)msg.obj);
                    break;
                case ACTION_ERROR:
                    callback.onError((Exception) msg.obj);
                    break;
            }
        }

        public void sendStartMsg(long fileSize, URL url) {
            Message msg = obtainMessage();
            msg.what = ACTION_START;
            msg.getData().putLong("size", fileSize);
            msg.obj = url;
            sendMessage(msg);
        }

        public void sendProgressMsg(long fileSize, long bytesSize, URL url) {
            Message msg = obtainMessage();
            msg.what = ACTION_PROGRESS;
            msg.getData().putLong("size", fileSize);
            msg.getData().putLong("bytes_size", bytesSize);
            msg.obj = url;
            sendMessage(msg);
        }

        public void sendEndMsg(long fileSize, File file, URL url) {
            Message msg = obtainMessage();
            msg.what = ACTION_END;
            msg.getData().putLong("size", fileSize);
            msg.getData().putSerializable("file", file);
            msg.obj = url;
            sendMessage(msg);
        }

        public void sendErrorMsg(Exception e) {
            Message msg = obtainMessage();
            msg.what = ACTION_ERROR;
            msg.obj = e;
            sendMessage(msg);
        }

    }

}
