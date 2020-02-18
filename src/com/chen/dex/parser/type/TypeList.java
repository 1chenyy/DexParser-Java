package com.chen.dex.parser.type;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.type.TypeID;

public class TypeList extends DexItem {
    //parameters数据的偏移量
    public int offset;

    //有几个parameter
    public int size;
    //type集合
    public TypeID[] typeList;
    //type索引集合
    public int[] typeIndexs;

}
