package com.chen.dex.parser.string;

import com.chen.dex.parser.DexHead;
import com.chen.dex.parser.string.StringID;
import com.chen.dex.utils.BaseUtil;
import com.chen.dex.utils.Mutf8Util;

import java.io.UTFDataFormatException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class StringIDParser {

    private ByteBuffer stringIdBuffer;
    private ByteBuffer stringDataBuffer;
    private DexHead head;

    public StringIDParser(ByteBuffer dexBuffer, DexHead head){
        stringIdBuffer = BaseUtil.slice(dexBuffer,head.string_ids_off,head.string_ids_size*4);
        stringDataBuffer = dexBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        this.head = head;
    }


    public ArrayList<StringID> getAllStringID() throws Exception {
        ArrayList<StringID> ids = new ArrayList<>();

        for (int i = 0;i<head.string_ids_size;i++){
            ids.add(getStringIDByIndex(i));
        }

        return ids;
    }



    public StringID getStringIDByIndex(int index) throws Exception {
        StringID id = new StringID();
        if (index == -1){
            id.index = -1;
            id.length = 0;
            id.offset = 0;
            id.data = null;
            return id;
        }

        if (index < 0 || index > head.string_ids_size){
            throw new Exception("Error index " + index);
        }


        id.index = index;
        int offset = stringIdBuffer.getInt(index*4);
        id.offset = offset;

        stringDataBuffer.position(offset);

        id.length = BaseUtil.readULeb128i(stringDataBuffer);

        StringBuilder sb = new StringBuilder();
        id.data = Mutf8Util.decode(stringDataBuffer,sb);
        return id;
    }
}
