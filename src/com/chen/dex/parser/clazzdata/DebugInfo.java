package com.chen.dex.parser.clazzdata;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.string.StringID;

import java.util.List;


public class DebugInfo extends DexItem {
    public int offset;

    //状态机的 line 寄存器的初始值
    public int lineStart;

    //已编码的参数名称的数量
    public int parametersSize;

    //方法参数名称的字符串索引
    public int[] parameterNameIndexs;
    public StringID[] parameterNames;

    public List<Byte> opcodes;
}