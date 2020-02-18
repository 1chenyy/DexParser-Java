package com.chen.dex.parser.annotation;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.field.FieldID;

public class FieldAnnotation extends DexItem {
    //该项索引
    public int index;

    //带注释的字段及其索引
    public int filedIndex;
    public FieldID filedIds;

    //注释列表的偏移量及内容
    public int annotationOffset;
    public AnnotationSetItem annotationSetItem;
}
