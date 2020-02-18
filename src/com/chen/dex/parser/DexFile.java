package com.chen.dex.parser;

import com.chen.dex.parser.clazz.ClassID;
import com.chen.dex.parser.clazz.ClassIDParser;
import com.chen.dex.parser.field.FieldIDParser;
import com.chen.dex.parser.method.MethodIDParser;
import com.chen.dex.parser.proto.ProtoIDParser;
import com.chen.dex.parser.string.StringIDParser;
import com.chen.dex.parser.type.TypeIDParser;
import com.chen.dex.utils.BaseUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;

public class DexFile{

    private ByteBuffer dexBuffer;
    private DexHead head;
    private StringIDParser stringIDParser;
    private TypeIDParser typeIDParser;
    private ProtoIDParser protoIDParser;
    private FieldIDParser fieldIDParser;
    private MethodIDParser methodIDParser;
    private ClassIDParser classIDParser;

    public DexFile(File dexFile) throws Exception {
        dexBuffer = ByteBuffer.wrap(Files.readAllBytes(dexFile.toPath()));
        head = new DexHead(dexBuffer);
        classIDParser = new ClassIDParser(dexBuffer,head);
        
    }




}
