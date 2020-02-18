package com.chen.dex.parser.type;

import com.chen.dex.parser.DexHead;
import com.chen.dex.parser.string.StringIDParser;
import com.chen.dex.utils.BaseUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class TypeIDParser {

    private DexHead head;
    private ByteBuffer typeIdIn ;
    private StringIDParser stringIDParser;

    public TypeIDParser(ByteBuffer dexBuffer, DexHead head){
        this(dexBuffer,head,new StringIDParser(dexBuffer,head));
    }

    public TypeIDParser(ByteBuffer dexBuffer, DexHead head, StringIDParser stringIDParser){
        typeIdIn = BaseUtil.slice(dexBuffer, head.type_ids_off, head.type_ids_size * 4);
        this.head = head;
        this.stringIDParser = stringIDParser;
    }

    public ArrayList<TypeID> getAllTypeID() throws Exception {
        ArrayList<TypeID> ids = new ArrayList<>();

        for (int i = 0;i<head.type_ids_size;i++){
           ids.add(getTypeIDByIndex(i));
        }
        return ids;
    }

    public TypeID getTypeIDByIndex(int i) throws Exception {
        TypeID id = new TypeID();
        if (i == -1){
            id.index = -1;
            id.stringIndex = -1;
            id.data = stringIDParser.getStringIDByIndex(-1);
            return id;
        }

        if (i < 0 || i>head.type_ids_size){
            throw new Exception("Error index " + i);
        }


        id.index = i;

        int index = typeIdIn.getInt(i * 4);
        id.stringIndex = index;

        id.data = stringIDParser.getStringIDByIndex(index);

        return id;
    }
}
