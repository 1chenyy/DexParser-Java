package com.chen.dex.parser.clazzdata;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.method.MethodID;

public class EncodedMethod extends DexItem {
    public int index;

    public int methodIndexDiff;
    public int methodIndex;
    public MethodID method;

    public int accessFlag;

    public int codeOffset;
    public Code code;
}
