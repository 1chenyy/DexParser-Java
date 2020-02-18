package com.chen.dex.parser.clazz;

import com.chen.dex.parser.DexItem;
import com.chen.dex.parser.annotation.AnnotationsDir;
import com.chen.dex.parser.annotation.EncodedArray;
import com.chen.dex.parser.annotation.EncodedArrayItem;
import com.chen.dex.parser.clazzdata.ClassData;
import com.chen.dex.parser.string.StringID;
import com.chen.dex.parser.type.TypeID;
import com.chen.dex.parser.type.TypeList;

public class ClassID extends DexItem {
    //该class的索引
    public int index;

    //该类的type及索引
    public TypeID classID;
    public int classIndex;

    //访问标志
    public int accessFlag;

    //父类的type及索引
    public TypeID superClassID;
    public int superClassIndex;

    //接口数据偏移量及接口列表
    public int interfaceOffset;
    public TypeList interfaces;

    //源文件对应的Stringid及索引
    public int srcFileStringIndex;
    public StringID srcFileStringIds;

    //注解信息的偏移量及对象
    public int annotationOffset;
    public AnnotationsDir annotationsDir;

    //类数据的偏移量
    public int classDataOffset;
    public ClassData classData;

    //静态变量初始值列表偏移量
    public int staticValueOffset;
    public EncodedArrayItem staticValue;
}
