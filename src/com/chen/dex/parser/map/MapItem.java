package com.chen.dex.parser.map;

import com.chen.dex.parser.DexItem;

public class MapItem extends DexItem {
    public int index;

    //类型
    public int type;
    public int unused;

    //所指类型的数量
    public int size;
    //所指类型的偏移量
    public int offset;
}
