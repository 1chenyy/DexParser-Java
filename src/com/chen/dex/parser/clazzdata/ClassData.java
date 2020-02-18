package com.chen.dex.parser.clazzdata;

import com.chen.dex.parser.DexItem;

public class ClassData extends DexItem {
    //该数据的偏移量
    public int offset;

    public int staticFieldsSize;

    public int instaceFieldsSize;

    public int directMethodsSize;

    public int virtualMethodsSize;

    public EncodedField[] staticFields;

    public EncodedField[] instanceFields;

    public EncodedMethod[] directMethods;

    public EncodedMethod[] virtualMethods;
}
