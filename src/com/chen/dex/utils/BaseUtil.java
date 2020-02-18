package com.chen.dex.utils;

import com.chen.dex.parser.DexConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BaseUtil {
    public static String intToHexString(int i){
        return Integer.toHexString(i);
    }

    public static void skip(ByteBuffer in, int bytes) {
        in.position(in.position() + bytes);
    }

    public static ByteBuffer slice(ByteBuffer in, int offset, int length) {
        in.position(offset);
        ByteBuffer b = in.slice();
        b.limit(length);
        b.order(ByteOrder.LITTLE_ENDIAN);
        return b;
    }

    public static int readULeb128i(ByteBuffer in) {
        int value = 0;
        int count = 0;
        int b = in.get();
        while ((b & 0x80) != 0) {
            value |= (b & 0x7f) << count;
            count += 7;
            b = in.get();
        }
        value |= (b & 0x7f) << count;
        return value;
    }

    public static int readLeb128i(ByteBuffer in) {
        int bitpos = 0;
        int vln = 0;
        do {
            int inp = in.get();
            vln |= (inp & 0x7F) << bitpos;
            bitpos += 7;
            if ((inp & 0x80) == 0) {
                break;
            }
        } while (true);
        if (((1L << (bitpos - 1)) & vln) != 0) {
            vln -= (1L << bitpos);
        }
        return vln;
    }

    private static Gson normalGson = null;
    private static Gson formatGson = null;

    public static String objToJson(Object object){
        if (normalGson == null){
            normalGson = new Gson();
        }
        return normalGson.toJson(object);
    }

    public static<T> T jsonToObj(Class<T> t,String str){
        if (normalGson == null){
            normalGson = new GsonBuilder().
                    serializeSpecialFloatingPointValues().
                    create();
        }
        return normalGson.fromJson(str,t);
    }

    public static String objToFormatJson(Object object){
        if (formatGson == null){
            formatGson = new GsonBuilder().
                    setPrettyPrinting().
                    serializeSpecialFloatingPointValues().
                    create();
        }
        return formatGson.toJson(object);
    }


}
