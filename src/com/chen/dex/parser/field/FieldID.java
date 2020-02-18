package com.chen.dex.parser.field;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.string.StringID;
import com.chen.dex.parser.type.TypeID;

public class FieldID extends DexItem {
    //该field的索引
    public int index;

    //字段的所有者类型及其索引
    public TypeID ownerIds;
    public int ownerIndex;

    //字段类型及其索引
    public TypeID typeIDs;
    public int typeIndex;

    //字段名称及其索引
    public StringID nameIDs;
    public int nameIndex;

}
