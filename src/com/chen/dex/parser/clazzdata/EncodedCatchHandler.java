package com.chen.dex.parser.clazzdata;

import com.chen.dex.parser.DexItem;

public class EncodedCatchHandler extends DexItem {
    //size的原始数据
    public int sizeOrg;

    //此列表中捕获类型的数量
    public int size;

    public EncodedTypeAddrPair[] handlers;

    //“全部捕获”处理程序的字节码地址。只有当 size 为非正数时，此元素才会存在。
    public int catchAllAddr;
}
