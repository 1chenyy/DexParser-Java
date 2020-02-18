package com.chen.dex.parser.proto;

import com.chen.dex.parser.*;
import com.chen.dex.parser.string.StringIDParser;
import com.chen.dex.parser.type.TypeID;
import com.chen.dex.parser.type.TypeIDParser;
import com.chen.dex.parser.type.TypeList;
import com.chen.dex.utils.BaseUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ProtoIDParser {

    private ByteBuffer protoIdIn ;
    private ByteBuffer typeListIn ;
    private DexHead head;
    private StringIDParser stringIDParser;
    private TypeIDParser typeIDParser;

    public ProtoIDParser(ByteBuffer dexBuffer, DexHead head){
        protoIdIn = BaseUtil.slice(dexBuffer,head.proto_ids_off,head.proto_ids_size*12);
        typeListIn = dexBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        this.head = head;
        this.stringIDParser = new StringIDParser(dexBuffer,head);
        this.typeIDParser = new TypeIDParser(dexBuffer,head, stringIDParser);
    }

    public ProtoIDParser(ByteBuffer dexBuffer, DexHead head, StringIDParser stringIDParser, TypeIDParser typeIDParser){
        protoIdIn = BaseUtil.slice(dexBuffer,head.proto_ids_off,head.proto_ids_size*12);
        typeListIn = dexBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        this.head = head;
        this.stringIDParser = stringIDParser;
        this.typeIDParser = typeIDParser;
    }


    public ArrayList<ProtoID> getAllProtoID() throws Exception {
        ArrayList<ProtoID> list = new ArrayList<>();
        for (int i = 0;i<head.proto_ids_size;i++){
            list.add(getProtoIDByIndex(i));
        }
        return list;
    }

    public ProtoID getProtoIDByIndex(int index) throws Exception {
        if (index < 0 || index>head.proto_ids_size){
            throw new Exception("Error index " +index);
        }

        ProtoID protoID = new ProtoID();

        protoIdIn.position(index*12);

        protoID.index = index;

        protoID.shortyIndex = protoIdIn.getInt();
        protoID.shortyID = stringIDParser.getStringIDByIndex(protoID.shortyIndex);

        protoID.returnTypeIndex = protoIdIn.getInt();
        protoID.returnTypeID = typeIDParser.getTypeIDByIndex(protoID.returnTypeIndex);

        protoID.parametersOffset = protoIdIn.getInt();
        protoID.parameters= getTypeList(protoID.parametersOffset);

        return protoID;
    }

    public TypeList getTypeList(int offset) throws Exception {
        TypeList typeList = new TypeList();
        typeList.offset = offset;
        if (offset != 0){
            typeListIn.position(offset);

            typeList.size = typeListIn.getInt();

            typeList.typeIndexs = new int[typeList.size];

            typeList.typeList = new TypeID[typeList.size];

            for (int i = 0;i<typeList.size;i++){
                typeList.typeIndexs[i] = 0xFFFF &typeListIn.getShort();
                typeList.typeList[i] = typeIDParser.getTypeIDByIndex(typeList.typeIndexs[i]);
            }
        }
        return typeList;
    }
}
