package com.chen.dex.parser.annotation;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.string.StringID;

public class AnnotationElement extends DexItem {
    public int index;

    public int nameIndex;
    public StringID name;

    public Object value;
}
