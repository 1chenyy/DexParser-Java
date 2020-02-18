package com.chen.dex;

import com.chen.dex.parser.DexFile;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        DexFile dexFile = null;
        try {
            dexFile = new DexFile(new File("files/classes.dex"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
