package com.chen.dex.parser.annotation;

import com.chen.dex.parser.DexItem;

public class AnnotationOffItem extends DexItem {
    //第几项
    public int index;

    //偏移量及所指向的内容
    public int offset;
    public AnnotationItem annotation;
}
