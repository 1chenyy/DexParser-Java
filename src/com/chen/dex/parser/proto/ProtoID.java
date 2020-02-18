package com.chen.dex.parser.proto;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.string.StringID;
import com.chen.dex.parser.type.TypeID;
import com.chen.dex.parser.type.TypeList;

public class ProtoID extends DexItem {
    //第几个proto
    public int index;

    //shorty所指向的String
    public StringID shortyID;
    //shorty所指向d额String索引
    public int shortyIndex;

    //return所指向的type
    public TypeID returnTypeID;
    //return所指向的Type索引
    public int returnTypeIndex;

    //parameters对象
    public TypeList parameters;
    //parameters的偏移量
    public int parametersOffset;


}
