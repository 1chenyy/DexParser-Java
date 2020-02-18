package com.chen.dex.parser.type;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.string.StringID;

public class TypeID extends DexItem {
    //第几个type
    public int index;

    //所指向的String索引
    public int stringIndex;
    //所指向的String内容
    public StringID data;

}
