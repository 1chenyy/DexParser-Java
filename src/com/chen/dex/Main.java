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

//    public static void main(String[] args) {
//        try {
//            DexFileReader reader = new DexFileReader(new File("files/classes.dex"));
//            DexFileNode node = new DexFileNode();
//            DexFileWriter writer = new DexFileWriter();
//            reader.accept(node);
//
//            DexClassNode clazz = node.clzs.get(reader.getClassSize()-1);
//
//            node.accept(writer);
//            byte[] data = writer.toByteArray();
//            FileOutputStream out = new FileOutputStream(new File("files/out.dex"));
//            out.write(data);
//            out.flush();
//            out.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
