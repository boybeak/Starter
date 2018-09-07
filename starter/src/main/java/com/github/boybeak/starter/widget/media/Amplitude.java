package com.github.boybeak.starter.widget.media;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyunfei on 2017/5/5.
 */

public class Amplitude {

    private static final String TAG = Amplitude.class.getSimpleName();

    public static Amplitude fromFile (String path) {
        return fromFile(new File(path));
    }

    public static Amplitude fromFile (File file) {
        try {
            return fromStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Amplitude fromStream (InputStream stream) {

        try {
            byte[] amp17v = new byte[6];
            stream.read(amp17v);

            byte[] periodBa = new byte[4];
            stream.read(periodBa);
            int period = byteArrayToInt(periodBa);

            byte[] arraySizeBa = new byte[4];
            stream.read(arraySizeBa);
            int arraySize = byteArrayToInt(arraySizeBa);

            byte[] dataB = new byte[arraySize * 4];
            stream.read(dataB);

            int[] data = byteArrayToIntArray(dataB);

            return new Amplitude(period, data);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] intToByteArray (int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    private static int byteArrayToInt (byte[] array) {
        return ByteBuffer.wrap(array).getInt();
    }

    private static byte[] floatToByteArray (float value) {
        return ByteBuffer.allocate(4).putFloat(value).array();
    }

    private static float byteArrayToFloat (byte[] array) {
        return ByteBuffer.wrap(array).getFloat();
    }

    private static byte[] longToByteArray (long value) {
        return ByteBuffer.allocate(8).putLong(value).array();
    }

    private static long byteArrayToLong (byte[] array) {
        return ByteBuffer.wrap(array).getLong();
    }

    private static byte[] intArrayToByteArray (int[] array) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(array.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(array);
        return byteBuffer.array();
    }

    private static int[] byteArrayToIntArray (byte[] array) {
        int[] dst = new int[array.length / 4];
        ByteBuffer.wrap(array).asIntBuffer().get(dst);
        return dst;
    }

    private int period;
    private int[] amplitudeArray;

    public Amplitude(int period, int[] amplitudeArray) {
        this.period = period;
        this.amplitudeArray = amplitudeArray;
    }

    public Amplitude (int period, List<Integer> amplitudeList) {
        final int length = amplitudeList.size();
        int[] data = new int[length];
        for (int i  = 0; i < length; i++) {
            data[i] = amplitudeList.get(i);
        }
        this.period = period;
        this.amplitudeArray = data;
    }

    public int getPeriod() {
        return period;
    }

    public int[] getAmplitudeArray() {
        return amplitudeArray;
    }

    public List<Integer> getAmplitudeList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < amplitudeArray.length; i++) {
            list.add(amplitudeArray[i]);
        }
        return list;
    }

    public boolean flushTo (File to) {
        if (!to.getParentFile().exists()) {
            if (!to.getParentFile().mkdirs()) {
                return false;
            }
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(to);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (outputStream != null) {
            byte[] amp17v = {0x61, 0x6d, 0x70, 0x31, 0x37, 0x76};

            byte[] period = intToByteArray(this.period);
            byte[] arrayLength = intToByteArray(this.amplitudeArray.length);
            byte[] dataB = intArrayToByteArray(this.amplitudeArray);
            Log.v(TAG, "flushTo dataB.length=" + dataB);

            try {
                outputStream.write(amp17v);

                outputStream.write(period);
                outputStream.write(arrayLength);
                outputStream.write(dataB);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean flushTo (String to) {
        return flushTo(new File(to));
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("period", period);
            jsonObject.put("amplitude_length", amplitudeArray.length);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
