package com.chen.dex.parser.annotation;

import com.chen.dex.parser.DexItem;

public class AnnotationsDir extends DexItem {
    //字段的偏移量
    public int offset;

    //从文件开头到直接在该类上所做的注释的偏移量
    public int classAnnotationOffset;
    public AnnotationSetItem classAnnotation;

    //此项所注释的字段数量
    public int fieldAnnotationSize;

    //此项所注释的方法数量
    public int methodAnnotationSize;

    //此项所注释的方法参数列表的数量
    public int parameterAnnotationSize;

    public FieldAnnotation[] fieldAnnotations;

    public MethodAnnotation[] methodAnnotations;

    public ParameterAnnotation[] parameterAnnotations;
}
