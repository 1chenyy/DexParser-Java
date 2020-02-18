package com.chen.dex.parser.annotation;

import com.chen.dex.parser.DexItem;

public class AnnotationSetItem extends DexItem {
    //该内容的偏移量
    public int offset;

    //集合数量
    public int size;

    //集合内容
    public AnnotationOffItem[] entries;
}
