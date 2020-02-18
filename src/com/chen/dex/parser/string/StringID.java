package com.chen.dex.parser.string;

import com.chen.dex.parser.DexItem;
import com.chen.dex.utils.BaseUtil;

public class StringID extends DexItem {
    //第几个String
    public int index;

    //内容偏移量
    public int offset;
    //长度
    public int length;
    //字符串内容
    public String data;
}
