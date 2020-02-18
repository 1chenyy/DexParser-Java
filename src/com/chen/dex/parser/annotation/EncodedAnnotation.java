package com.chen.dex.parser.annotation;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.type.TypeID;

public class EncodedAnnotation extends DexItem {
    //注释的类型及索引
    public int typeIdx;
    public TypeID typeID;

    //注释中 name-value 映射的数量
    public int size;

    public AnnotationElement[] elements;
}
