package com.chen.dex.parser.clazzdata;

import com.chen.dex.parser.DexItem;

public class Code extends DexItem {
    public int offset;

    //寄存器数量
    public int registersSize;

    //传入参数的字数
    public int insSize;

    //出参空间的字数
    public int outsSize;

    //try_item 数量,如果此值为非零值，则这些项会显示为 insns 数组
    public int triesSize;

    //调试信息偏移量
    public int debugInfoOffset;
    public DebugInfo debugInfo;

    //指令列表大小
    public int insnsSize;
    public byte[] insns;

    //使 tries 实现四字节对齐的两字节填充
    //只有 tries_size 为非零值且 insns_size 是奇数时，此元素才会存在
    public short padding;

    public Try[] tries;

    public EncodedCatchHandlerList handlers;



}
