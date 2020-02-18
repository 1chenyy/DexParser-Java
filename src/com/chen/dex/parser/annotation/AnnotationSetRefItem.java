package com.chen.dex.parser.annotation;

import com.chen.dex.parser.DexItem;

public class AnnotationSetRefItem extends DexItem {
    //所在索引
    public int index;

    //所指向的注释集合偏移量及内容
    public int offset;
    public AnnotationSetItem annotationSetItem;
}
