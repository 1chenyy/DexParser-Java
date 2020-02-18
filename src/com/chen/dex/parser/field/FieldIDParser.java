package com.chen.dex.parser.field;

import com.chen.dex.parser.DexHead;
import com.chen.dex.parser.string.StringIDParser;
import com.chen.dex.parser.type.TypeIDParser;
import com.chen.dex.utils.BaseUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class FieldIDParser {

    private ByteBuffer fieldIdIn ;
    private DexHead head;
    private TypeIDParser typeIDParser;
    private StringIDParser stringIDParser;

    public FieldIDParser(ByteBuffer dexBuffer, DexHead head){
        fieldIdIn = BaseUtil.slice(dexBuffer, head.field_ids_off, head.field_ids_size * 8);
        this.head = head;
        stringIDParser = new StringIDParser(dexBuffer,head);
        typeIDParser = new TypeIDParser(dexBuffer,head, stringIDParser);
    }

    public FieldIDParser(ByteBuffer dexBuffer, DexHead head, StringIDParser stringIDParser, TypeIDParser typeIDParser){
        fieldIdIn = BaseUtil.slice(dexBuffer, head.field_ids_off, head.field_ids_size * 8);
        this.head = head;
        this.typeIDParser = typeIDParser;
        this.stringIDParser = stringIDParser;
    }


    public ArrayList<FieldID> getAllField() throws Exception {
        ArrayList<FieldID> list = new ArrayList<>();
        for (int i = 0;i<head.field_ids_size;i++){
            list.add(getFieldByIndex(i));
        }
        return list;
    }

    public FieldID getFieldByIndex(int index) throws Exception {
        if (index < 0 || index > head.field_ids_size){
            throw new Exception("Error index : " + index);
        }

        fieldIdIn.position(index * 8);



        FieldID fieldID = new FieldID();
        fieldID.index = index;

        fieldID.ownerIndex = 0xFFFF &fieldIdIn.getShort();
        fieldID.ownerIds = typeIDParser.getTypeIDByIndex(fieldID.ownerIndex);

        fieldID.typeIndex = 0xFFFF &fieldIdIn.getShort();
        fieldID.typeIDs = typeIDParser.getTypeIDByIndex(fieldID.typeIndex);

        fieldID.nameIndex = fieldIdIn.getInt();
        fieldID.nameIDs = stringIDParser.getStringIDByIndex(fieldID.nameIndex);

        return fieldID;
    }
}
