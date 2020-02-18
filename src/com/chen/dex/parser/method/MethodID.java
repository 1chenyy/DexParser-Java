package com.chen.dex.parser.method;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.string.StringID;
import com.chen.dex.parser.type.TypeID;
import com.chen.dex.parser.proto.ProtoID;

public class MethodID extends DexItem {
    //该method的索引
    public int index;

    //所在的类的类型及其索引
    public TypeID ownerIds;
    public int ownerIndex;

    //方法的proto及其索引
    public ProtoID protoIds;
    public int protoIndex;

    //名称及其索引
    public StringID nameIds;
    public int nameIndex;
}
