package com.chen.dex.parser.annotation;

import com.chen.dex.parser.DexItem;

public class AnnotationItem extends DexItem {
    public int offset;

    public int visibility;
    public EncodedAnnotation annotation;
}
