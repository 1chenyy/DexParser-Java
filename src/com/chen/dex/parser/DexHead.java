package com.chen.dex.parser;

import com.chen.dex.parser.map.MapItem;
import com.chen.dex.utils.BaseUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DexHead extends DexItem{


    public int dex_version;

    public int checkSum;
    public String checkSumStr;

    public byte[] sign;
    public int fileSize;
    public int headLength;

    public int map_off;
    public int map_size = 0;
    public MapItem[] mapItems;

    public int link_size;
    public int link_off;

    public int string_ids_size;
    public int string_ids_off;

    public int type_ids_size;
    public int type_ids_off;

    public int proto_ids_size;
    public int proto_ids_off;

    public int field_ids_size;
    public int field_ids_off;

    public int method_ids_size;
    public int method_ids_off;

    public int class_defs_size;
    public int class_defs_off;

    public int data_size;
    public int data_off;

    public int call_site_ids_off = 0;
    public int call_site_ids_size = 0;
    public int method_handle_ids_off = 0;
    public int method_handle_ids_size = 0;

    public DexHead(ByteBuffer dexBuffer) throws Exception {
        dexBuffer.position(0);

        //读取Magic
        dexBuffer = dexBuffer.asReadOnlyBuffer().order(ByteOrder.BIG_ENDIAN);
        int magic = dexBuffer.getInt() ;
        if (!(magic == DexConstants.MAGIC_DEX)){
            throw new Exception("Error magic : "+ BaseUtil.intToHexString(magic));
        }

        //读取版本
        int version = dexBuffer.getInt() >> 8;
        if (version < DexConstants.DEX_035){
            throw new Exception("Error version : " + BaseUtil.intToHexString(version));
        }
        dex_version = version;

        dexBuffer.order(ByteOrder.LITTLE_ENDIAN);

        // 校验和 签名 文件大小 头部长度
        //BaseUtil.skip(dexBuffer,4+20+4+4);
        checkSum = dexBuffer.getInt();
        checkSumStr = BaseUtil.intToHexString(checkSum);

        sign = new byte[20];
        for (int i = 0 ;i < 20 ;i++){
            sign[i] = dexBuffer.get();
        }

        fileSize = dexBuffer.getInt();
        headLength = dexBuffer.getInt();

        // 读取端序标志
        int endian_tag = dexBuffer.getInt();
        if (endian_tag != DexConstants.ENDIAN_CONSTANT){
            throw new Exception("Error endian tag : " + BaseUtil.intToHexString(endian_tag));
        }

        link_size = dexBuffer.getInt();
        link_off = dexBuffer.getInt();

        map_off = dexBuffer.getInt();

        string_ids_size = dexBuffer.getInt();
        string_ids_off = dexBuffer.getInt();

        type_ids_size = dexBuffer.getInt();
        type_ids_off = dexBuffer.getInt();

        proto_ids_size = dexBuffer.getInt();
        proto_ids_off = dexBuffer.getInt();

        field_ids_size = dexBuffer.getInt();
        field_ids_off = dexBuffer.getInt();

        method_ids_size = dexBuffer.getInt();
        method_ids_off = dexBuffer.getInt();

        class_defs_size = dexBuffer.getInt();
        class_defs_off = dexBuffer.getInt();

        data_size = dexBuffer.getInt();
        data_off = dexBuffer.getInt();

        dexBuffer.position(map_off);
        map_size = dexBuffer.getInt();
        mapItems = new MapItem[map_size];
        for (int i = 0;i<map_size;i++){
            MapItem item = new MapItem();

            item.index = i;
            item.type = dexBuffer.getShort() & 0xFFFF;
            item.unused = dexBuffer.getShort() & 0xFFFF;
            item.size = dexBuffer.getInt();
            item.offset = dexBuffer.getInt();

            switch (item.type){
                case DexConstants.TYPE_CALL_SITE_ID_ITEM:
                    call_site_ids_off = item.offset;
                    call_site_ids_size = item.size;
                    break;
                case DexConstants.TYPE_METHOD_HANDLE_ITEM:
                    method_handle_ids_off = item.offset;
                    method_handle_ids_size = item.size;
                    break;
                default:
                    break;
            }

            mapItems[i]=item;
        }

        if (dex_version > DexConstants.DEX_037){

        }
    }

    @Override
    public String toString() {
        return BaseUtil.objToFormatJson(this);
    }
}
