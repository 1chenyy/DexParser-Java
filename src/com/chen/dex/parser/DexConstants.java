package com.chen.dex.parser;

public class DexConstants {
    public static final int MAGIC_DEX = 0x6465780A ;
    public static final int ENDIAN_CONSTANT = 0x12345678;
    public static final int TYPE_CALL_SITE_ID_ITEM = 0x0007;
    public static final int TYPE_METHOD_HANDLE_ITEM = 0x0008;

    public static final int ACC_PUBLIC = 0x0001; // class, field, method
    public static final int ACC_PRIVATE = 0x0002; // class, field, method
    public static final int ACC_PROTECTED = 0x0004; // class, field, method
    public static final int ACC_STATIC = 0x0008; // field, method
    public static final int ACC_FINAL = 0x0010; // class, field, method
    // int ACC_SUPER = 0x0020; // class
    public static final int ACC_SYNCHRONIZED = 0x0020; // method
    public static final int ACC_VOLATILE = 0x0040; // field
    public static final int ACC_BRIDGE = 0x0040; // method
    public static final int ACC_VARARGS = 0x0080; // method
    public static final int ACC_TRANSIENT = 0x0080; // field
    public static final int ACC_NATIVE = 0x0100; // method
    public static final int ACC_INTERFACE = 0x0200; // class
    public static final int ACC_ABSTRACT = 0x0400; // class, method
    public static final int ACC_STRICT = 0x0800; // method
    public static final int ACC_SYNTHETIC = 0x1000; // class, field, method
    public static final int ACC_ANNOTATION = 0x2000; // class
    public static final int ACC_ENUM = 0x4000; // class(?) field inner
    public static final int ACC_CONSTRUCTOR = 0x10000;// constructor method (class or instance initializer)
    public static final int ACC_DECLARED_SYNCHRONIZED = 0x20000;

    public static final String ANNOTATION_DEFAULT_TYPE = "Ldalvik/annotation/AnnotationDefault;";
    public static final String ANNOTATION_SIGNATURE_TYPE = "Ldalvik/annotation/Signature;";
    public static final String ANNOTATION_THROWS_TYPE = "Ldalvik/annotation/Throws;";
    public static final String ANNOTATION_ENCLOSING_CLASS_TYPE = "Ldalvik/annotation/EnclosingClass;";
    public static final String ANNOTATION_ENCLOSING_METHOD_TYPE = "Ldalvik/annotation/EnclosingMethod;";
    public static final String ANNOTATION_INNER_CLASS_TYPE = "Ldalvik/annotation/InnerClass;";
    public static final String ANNOTATION_MEMBER_CLASSES_TYPE = "Ldalvik/annotation/MemberClasses;";

    public static final int DEX_035 = 0x00303335;
    public static final int DEX_036 = 0x00303336;
    public static final int DEX_037 = 0x00303337;
    public static final int DEX_038 = 0x00303338;

    public static final int VALUE_BYTE = 0x00;
    public static final int VALUE_SHORT = 0x02;
    public static final int VALUE_CHAR = 0x03;
    public static final int VALUE_INT = 0x04;
    public static final int VALUE_LONG = 0x06;
    public static final int VALUE_FLOAT = 0x10;
    public static final int VALUE_DOUBLE = 0x11;
    public static final int VALUE_METHOD_TYPE = 0x15;
    public static final int VALUE_METHOD_HANDLE = 0x16;
    public static final int VALUE_STRING = 0x17;
    public static final int VALUE_TYPE = 0x18;
    public static final int VALUE_FIELD = 0x19;
    public static final int VALUE_METHOD = 0x1a;
    public static final int VALUE_ENUM = 0x1b;
    public static final int VALUE_ARRAY = 0x1c;
    public static final int VALUE_ANNOTATION = 0x1d;
    public static final int VALUE_NULL = 0x1e;
    public static final int VALUE_BOOLEAN = 0x1f;

    public static final int STATIC_PUT = 0x00;
    public static final int STATIC_GET = 0x01;
    public static final int INSTANCE_PUT = 0x02;
    public static final int INSTANCE_GET = 0x03;
    public static final int INVOKE_STATIC = 0x04;
    public static final int INVOKE_INSTANCE = 0x05;
    public static final int INVOKE_CONSTRUCTOR = 0x06;
    public static final int INVOKE_DIRECT = 0x07;
    public static final int INVOKE_INTERFACE = 0x08;

    public static final int DBG_END_SEQUENCE = 0x00;
    public static final int DBG_ADVANCE_PC = 0x01;
    public static final int DBG_ADVANCE_LINE = 0x02;
    public static final int DBG_START_LOCAL = 0x03;
    public static final int DBG_START_LOCAL_EXTENDED = 0x04;
    public static final int DBG_END_LOCAL = 0x05;
    public static final int DBG_RESTART_LOCAL = 0x06;
    public static final int DBG_SET_PROLOGUE_END = 0x07;
    public static final int DBG_SET_EPILOGUE_BEGIN = 0x08;
    public static final int DBG_SET_FILE = 0x09;
    public static final int DBG_FIRST_SPECIAL = 0x0a;
    public static final int DBG_LINE_BASE = -4;
    public static final int DBG_LINE_RANGE = 15;
}
