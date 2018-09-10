package com.github.boybeak.loader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Downloader {
	
	public static Downloader newInstance() {
		return new Downloader();
	}
	
	public static long getFileSize(String url) {
		try {
			return getFileSize(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static long getFileSize(URL url) {
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
	
	private URL from;
	private long size;
	private File to, toTmp;
	private Callback callback;
	private boolean autoContinueOnBreakPointIfPossible = false;
	
	private Downloader() {
		
	}
	
	public Downloader from(URL url) {
		from = url;
		return this;
	}
	
	public Downloader from(String url) {
		try {
			return from(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("can not make a URL by {" + url + "}");
	}
	
	public Downloader to(File file) {
		to = file;
		if (!to.getParentFile().exists()) {
			to.getParentFile().mkdirs();
		}
		toTmp = new File(to.getParentFile(), "." + to.getName() + ".tmp");
		return this;
	}
	
	public Downloader to(String path) {
		return to(new File(path));
	}
	
	public Downloader autoContinueOnBreakPointIfPossible(boolean auto) {
		autoContinueOnBreakPointIfPossible = auto;
		return this;
	}
	
	public Downloader listenBy(Callback callback) {
		this.callback = callback;
		return this;
	}
	
	public Downloader start() {
		size = getFileSize(from);
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
			throw new IllegalStateException("The download url {" + from.toString() + "} seems doesn't exist");
		}
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
			outStream = new FileOutputStream(to, autoContinueOnBreakPointIfPossible);
		    byte dataBuffer[] = new byte[1024 * 8];
		    int bytesRead;
		    long bytesCount = toTmp.length();
		    int notifyLoopIndex = 0;
		    inStream.skip(bytesCount);
		    
		    while ((bytesRead = inStream.read(dataBuffer, 0, dataBuffer.length)) != -1) {
		    	bytesCount += bytesRead;
		    	outStream.write(dataBuffer, 0, bytesRead);
		        if (notifyLoopIndex == 4) {
		        	/*if (bytesCount >= size/2) {
		        		System.exit(2);
		        	}*/
		        	if (callback != null) {
		        		callback.onProgress(size, bytesCount, from);
		        	}
		        	notifyLoopIndex = 0;
		        }
		        notifyLoopIndex++;
		    }
		    toTmp.renameTo(to);
		    if (callback != null) {
		    	callback.onEnd(size, to, from);
		    }
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
	
	/*private void tempTo() {
		return new File()
	}*/
	
	public interface Callback {
		void onStart(long fileSize, URL url);
		void onProgress(long fileSize, long bytesSize, URL url);
		void onEnd(long fileSize, File file, URL url);
		void onError(Exception e);
	}
	
}
