package com.chen.dex.parser.clazzdata;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.type.TypeID;

public class EncodedTypeAddrPair extends DexItem {
    public int index;

    public int typeIndex;
    public TypeID type;

    //关联异常处理程序的字节码地址
    public int addr;
}
