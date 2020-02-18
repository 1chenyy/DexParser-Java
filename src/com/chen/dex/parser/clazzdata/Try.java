package com.chen.dex.parser.clazzdata;

import com.chen.dex.parser.DexItem;

public class Try extends DexItem {
    public int index;

    //起始地址
    public int startAddr;

    //覆盖的16位代码单元数量
    public int insnCount;

    //从关联的 encoded_catch_hander_list 开头部分到此条目的 encoded_catch_handler 的偏移量
    public int handlerOffset;
    public EncodedCatchHandler catchHandler;
}
