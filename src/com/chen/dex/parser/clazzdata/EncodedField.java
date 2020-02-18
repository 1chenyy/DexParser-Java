package com.chen.dex.parser.clazzdata;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.field.FieldID;

public class EncodedField extends DexItem {
    public int index;

    public int fieldIndexDiff;
    public int fieldIndex;
    public FieldID field;

    public int accessFlag;
}
