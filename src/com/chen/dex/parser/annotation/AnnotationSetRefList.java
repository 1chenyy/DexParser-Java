package com.chen.dex.parser.annotation;

import com.chen.dex.parser.DexItem;

public class AnnotationSetRefList extends DexItem {
    //所在位置的偏移量
    public int offset;

    //集合数量
    public int size;
    public AnnotationSetRefItem[] list;

}
