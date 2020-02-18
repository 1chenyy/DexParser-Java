package com.chen.dex.parser.method;

import com.chen.dex.parser.DexHead;
import com.chen.dex.parser.string.StringIDParser;
import com.chen.dex.parser.type.TypeIDParser;
import com.chen.dex.parser.proto.ProtoIDParser;
import com.chen.dex.utils.BaseUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MethodIDParser {

    private ByteBuffer methoIdIn ;
    private DexHead head;
    private TypeIDParser typeIDParser;
    private StringIDParser stringIDParser;
    private ProtoIDParser protoIDParser;

    public MethodIDParser(ByteBuffer dexBuffer, DexHead head){
        methoIdIn = BaseUtil.slice(dexBuffer, head.method_ids_off, head.method_ids_size * 8);
        this.head = head;
        stringIDParser = new StringIDParser(dexBuffer,head);
        typeIDParser = new TypeIDParser(dexBuffer,head, stringIDParser);
        protoIDParser = new ProtoIDParser(dexBuffer,head, stringIDParser, typeIDParser);
    }

    public MethodIDParser(ByteBuffer dexBuffer, DexHead head, StringIDParser stringIDParser, TypeIDParser typeIDParser, ProtoIDParser protoIDParser){
        methoIdIn = BaseUtil.slice(dexBuffer, head.method_ids_off, head.method_ids_size * 8);
        this.head = head;
        this.typeIDParser = typeIDParser;
        this.stringIDParser = stringIDParser;
        this.protoIDParser = protoIDParser;
    }


    public ArrayList<MethodID> getAllMethod() throws Exception {
        ArrayList<MethodID> list = new ArrayList<>();
        for(int i = 0;i<head.method_ids_size;i++){
            list.add(getMethodByIndex(i));
        }
        return list;
    }

    public MethodID getMethodByIndex(int index) throws Exception {
        if (index < 0 || index > head.method_ids_size){
            throw new Exception("Error index : " + index);
        }

        methoIdIn.position(index * 8);

        MethodID methodID = new MethodID();

        methodID.index = index;

        methodID.ownerIndex = 0xFFFF & methoIdIn.getShort();
        methodID.ownerIds = typeIDParser.getTypeIDByIndex(methodID.ownerIndex);

        methodID.protoIndex = 0xFFFF & methoIdIn.getShort();
        methodID.protoIds = protoIDParser.getProtoIDByIndex(methodID.protoIndex);

        methodID.nameIndex = methoIdIn.getInt();
        methodID.nameIds = stringIDParser.getStringIDByIndex(methodID.nameIndex);

        return methodID;
    }
}
