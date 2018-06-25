package com.nulldreams.picker;

public class Thumb {
    public String path;


    @Override
    public boolean equals(Object obj) {
        return obj instanceof Thumb && ((Thumb) obj).path.equals(path);
    }
}
