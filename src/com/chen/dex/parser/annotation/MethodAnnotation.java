package com.chen.dex.parser.annotation;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.field.FieldID;
import com.chen.dex.parser.method.MethodID;

public class MethodAnnotation extends DexItem {
    //该项索引
    public int index;

    //带注释的方法及其索引
    public int methodIndex;
    public MethodID methodIds;

    //注释列表的偏移量及内容
    public int annotationOffset;
    public AnnotationSetItem annotationSetItem;
}
