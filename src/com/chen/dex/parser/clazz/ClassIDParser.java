package com.chen.dex.parser.clazz;

import com.chen.dex.parser.DexConstants;
import com.chen.dex.parser.DexHead;
import com.chen.dex.parser.annotation.*;
import com.chen.dex.parser.clazzdata.*;
import com.chen.dex.parser.field.FieldIDParser;
import com.chen.dex.parser.method.MethodIDParser;
import com.chen.dex.parser.methodhandle.MethodHandleParser;
import com.chen.dex.parser.proto.ProtoIDParser;
import com.chen.dex.parser.string.StringID;
import com.chen.dex.parser.string.StringIDParser;
import com.chen.dex.parser.type.TypeIDParser;
import com.chen.dex.utils.BaseUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ClassIDParser {

    private ByteBuffer classDefIn ;
    private ByteBuffer annotationsDirectoryItemIn;
    private ByteBuffer annotationSetItemIn;
    private ByteBuffer annotationSetRefListIn;
    private ByteBuffer classDataIn;
    private ByteBuffer codeItemIn;
    private ByteBuffer debugInfoIn;
    private ByteBuffer encodedArrayItemIn;
    private StringIDParser stringIDParser;
    private TypeIDParser typeIDParser;
    private ProtoIDParser protoIDParser;
    private MethodIDParser methodIDParser;
    private FieldIDParser fieldIDParser;
    private MethodHandleParser methodHandleParser;

    private DexHead head;

    public ClassIDParser(ByteBuffer dexBuffer, DexHead head){
        this.head = head;

        classDefIn = BaseUtil.slice(dexBuffer, head.class_defs_off, head.class_defs_size * 32);
        annotationsDirectoryItemIn = dexBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        annotationSetItemIn = dexBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        annotationSetRefListIn = dexBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        classDataIn = dexBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        codeItemIn = dexBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        debugInfoIn = dexBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        encodedArrayItemIn = dexBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);

        stringIDParser = new StringIDParser(dexBuffer,head);
        typeIDParser = new TypeIDParser(dexBuffer,head, stringIDParser);
        protoIDParser = new ProtoIDParser(dexBuffer,head, stringIDParser, typeIDParser);
        methodIDParser = new MethodIDParser(dexBuffer,head, stringIDParser, typeIDParser, protoIDParser);
        fieldIDParser = new FieldIDParser(dexBuffer,head, stringIDParser, typeIDParser);
        methodHandleParser = new MethodHandleParser(dexBuffer,head,fieldIDParser,methodIDParser);
    }



    public ArrayList<ClassID> getAllClass() throws Exception {
        ArrayList<ClassID> list = new ArrayList<>();
        for(int i = 0;i<head.class_defs_size;i++){
            list.add(getClassByIndex(i));
        }
        return list;
    }

    public ClassID getClassByIndex(int index) throws Exception {
        if (index < 0 || index > head.class_defs_size){
            throw new Exception("Error index : " + index);
        }

        classDefIn.position(index * 32);



        ClassID classID = new ClassID();

        classID.index = index;

        classID.classIndex = classDefIn.getInt();
        classID.classID = typeIDParser.getTypeIDByIndex(classID.classIndex);

        classID.accessFlag = classDefIn.getInt();

        classID.superClassIndex = classDefIn.getInt();
        classID.superClassID = typeIDParser.getTypeIDByIndex(classID.superClassIndex);

        classID.interfaceOffset = classDefIn.getInt();
        classID.interfaces = protoIDParser.getTypeList(classID.interfaceOffset);

        classID.srcFileStringIndex = classDefIn.getInt();
        classID.srcFileStringIds = stringIDParser.getStringIDByIndex(classID.srcFileStringIndex);

        classID.annotationOffset = classDefIn.getInt();
        classID.annotationsDir = parserAnnotation(classID.annotationOffset);

        classID.classDataOffset = classDefIn.getInt();
        classID.classData = parserClassData(classID.classDataOffset);

        classID.staticValueOffset = classDefIn.getInt();
        classID.staticValue = readEncodedArrayItem(classID.staticValueOffset);

        return classID;
    }

    public AnnotationsDir parserAnnotation(int offset) throws Exception {
        AnnotationsDir annotationsDir = new AnnotationsDir();
        annotationsDir.offset = offset;
        if (offset == 0){
            return annotationsDir;
        }

        annotationsDirectoryItemIn.position(offset);

        annotationsDir.classAnnotationOffset = annotationsDirectoryItemIn.getInt();
        annotationsDir.classAnnotation = readAnnotationSetItem(annotationsDir.classAnnotationOffset);

        annotationsDir.fieldAnnotationSize = annotationsDirectoryItemIn.getInt();
        annotationsDir.methodAnnotationSize = annotationsDirectoryItemIn.getInt();
        annotationsDir.parameterAnnotationSize = annotationsDirectoryItemIn.getInt();

        annotationsDir.fieldAnnotations = new FieldAnnotation[annotationsDir.fieldAnnotationSize];
        for (int i = 0; i < annotationsDir.fieldAnnotationSize; i++){
            FieldAnnotation fieldAnnotation = new FieldAnnotation();
            fieldAnnotation.index = i;
            fieldAnnotation.filedIndex = annotationsDirectoryItemIn.getInt();
            fieldAnnotation.filedIds = fieldIDParser.getFieldByIndex(fieldAnnotation.filedIndex);

            fieldAnnotation.annotationOffset = annotationsDirectoryItemIn.getInt();
            fieldAnnotation.annotationSetItem = readAnnotationSetItem(fieldAnnotation.annotationOffset);
            annotationsDir.fieldAnnotations[i] = fieldAnnotation;
        }

        annotationsDir.methodAnnotations = new MethodAnnotation[annotationsDir.methodAnnotationSize];
        for (int i = 0; i < annotationsDir.methodAnnotationSize; i++){
            MethodAnnotation methodAnnotation = new MethodAnnotation();
            methodAnnotation.index = i;
            methodAnnotation.methodIndex = annotationsDirectoryItemIn.getInt();
            methodAnnotation.methodIds = methodIDParser.getMethodByIndex(methodAnnotation.methodIndex);

            methodAnnotation.annotationOffset = annotationsDirectoryItemIn.getInt();
            methodAnnotation.annotationSetItem = readAnnotationSetItem(methodAnnotation.annotationOffset);
            annotationsDir.methodAnnotations[i] = methodAnnotation;
        }

        annotationsDir.parameterAnnotations = new ParameterAnnotation[annotationsDir.parameterAnnotationSize];
        for (int i = 0; i < annotationsDir.parameterAnnotationSize; i++){
            ParameterAnnotation parameterAnnotation = new ParameterAnnotation();
            parameterAnnotation.index = i;
            parameterAnnotation.methodIndex = annotationsDirectoryItemIn.getInt();
            parameterAnnotation.methodIds = methodIDParser.getMethodByIndex(parameterAnnotation.methodIndex);

            parameterAnnotation.annotationOffset = annotationsDirectoryItemIn.getInt();
            parameterAnnotation.annotationSetRefList = readAnnotationSetRefList(parameterAnnotation.annotationOffset);

            annotationsDir.parameterAnnotations[i] = parameterAnnotation;
        }

        return annotationsDir;
    }

    private AnnotationSetItem readAnnotationSetItem(int offset) throws Exception {
        AnnotationSetItem annotationSetItem = new AnnotationSetItem();

        annotationSetItem.offset = offset;

        if (offset == 0){
            return annotationSetItem;
        }
        annotationSetItemIn.position(offset);

        annotationSetItem.size = annotationSetItemIn.getInt();
        annotationSetItem.entries = readAnnotationItem(annotationSetItem.size,annotationSetItemIn);

        return annotationSetItem;
    }

    private AnnotationSetRefList readAnnotationSetRefList(int offset) throws Exception {
        AnnotationSetRefList annotationSetRefList = new AnnotationSetRefList();
        annotationSetRefList.offset = offset;

        if (offset == 0){
            return annotationSetRefList;
        }

        annotationSetRefListIn.position(offset);

        annotationSetRefList.size = annotationSetRefListIn.getInt();
        annotationSetRefList.list = readAnnotationSetRefItem(annotationSetRefList.size,annotationSetRefListIn);

        return annotationSetRefList;
    }

    private AnnotationOffItem[] readAnnotationItem(int size,ByteBuffer in) throws Exception {
        AnnotationOffItem[] annotationOffItems = new AnnotationOffItem[size];
        for (int i = 0;i<size;i++){
            AnnotationOffItem annotationOffItem = new AnnotationOffItem();
            annotationOffItems[i] = annotationOffItem;
            annotationOffItem.index = i;
            annotationOffItem.offset = in.getInt();
        }
        for (int i = 0;i<size;i++){
            AnnotationOffItem annotationOffItem = annotationOffItems[i];
            in.position(annotationOffItem.offset);

            AnnotationItem annotationItem = new AnnotationItem();
            annotationOffItem.annotation = annotationItem;

            annotationItem.offset = annotationOffItem.offset;
            annotationItem.visibility = 0xFF & annotationSetItemIn.get();
            annotationItem.annotation = readEncodedAnnotation(in);


        }
        return annotationOffItems;
    }

    private AnnotationSetRefItem[] readAnnotationSetRefItem(int size,ByteBuffer in) throws Exception {
        AnnotationSetRefItem[] annotationSetRefItems = new AnnotationSetRefItem[size];
        for (int i = 0;i<size;i++){
            AnnotationSetRefItem item = new AnnotationSetRefItem();
            item.index = i;
            item.offset = in.getInt();
            item.annotationSetItem = readAnnotationSetItem(item.offset);
            annotationSetRefItems[i] = item;
        }
        return annotationSetRefItems;
    }

    private EncodedAnnotation readEncodedAnnotation(ByteBuffer in) throws Exception {
        EncodedAnnotation encodedAnnotation = new EncodedAnnotation();

        encodedAnnotation.typeIdx = BaseUtil.readULeb128i(in);
        encodedAnnotation.typeID = typeIDParser.getTypeIDByIndex(encodedAnnotation.typeIdx);

        encodedAnnotation.size = BaseUtil.readULeb128i(in);

        encodedAnnotation.elements = new AnnotationElement[encodedAnnotation.size];
        for (int i = 0;i<encodedAnnotation.size;i++){
            AnnotationElement element = new AnnotationElement();
            encodedAnnotation.elements[i] = element;
            element.nameIndex = BaseUtil.readULeb128i(in);
            element.name = stringIDParser.getStringIDByIndex(element.nameIndex);
            element.value = readEncodedValue(in);
        }

        return encodedAnnotation;
    }

    private Object readEncodedValue(ByteBuffer in) throws Exception {
        int b = 0xFF & in.get();
        int type = b & 0x1f;
        switch (type) {
            case DexConstants.VALUE_BYTE:
                return new Byte((byte) readIntBits(in, b));

            case DexConstants.VALUE_SHORT:
                return new Short((short) readIntBits(in, b));

            case DexConstants.VALUE_CHAR:
                return new Character((char) readUIntBits(in, b));

            case DexConstants.VALUE_INT:
                return new Integer((int) readIntBits(in, b));

            case DexConstants.VALUE_LONG:
                return new Long(readIntBits(in, b));

            case DexConstants.VALUE_FLOAT:
                return Float.intBitsToFloat((int) (readFloatBits(in, b) >> 32));

            case DexConstants.VALUE_DOUBLE:
                return Double.longBitsToDouble(readFloatBits(in, b));
            case DexConstants.VALUE_METHOD_TYPE:
                return protoIDParser.getProtoIDByIndex((int) readUIntBits(in, b));
            case DexConstants.VALUE_METHOD_HANDLE:
                return methodHandleParser.getMethodHandleByIndex((int) readUIntBits(in, b));

            case DexConstants.VALUE_STRING:
                return stringIDParser.getStringIDByIndex((int) readUIntBits(in, b));

            case DexConstants.VALUE_TYPE: {
                int type_id = (int) readUIntBits(in, b);
                return typeIDParser.getTypeIDByIndex(type_id);
            }
            case DexConstants.VALUE_FIELD: {
                int field_id = (int) readUIntBits(in, b);
                return fieldIDParser.getFieldByIndex(field_id);
            }
            case DexConstants.VALUE_METHOD: {
                int method_id = (int) readUIntBits(in, b);
                return methodIDParser.getMethodByIndex(method_id);

            }
            case DexConstants.VALUE_ENUM: {
                return fieldIDParser.getFieldByIndex((int) readUIntBits(in, b));
            }
            case DexConstants.VALUE_ARRAY: {
                return readEncodedArray(in);
            }
            case DexConstants.VALUE_ANNOTATION: {
                return readEncodedAnnotation(in);
            }
            case DexConstants.VALUE_NULL:
                return null;
            case DexConstants.VALUE_BOOLEAN: {
                return new Boolean(((b >> 5) & 0x3) != 0);
            }
            default:
                throw new Exception("Not support yet.");
        }
    }

    private EncodedArray readEncodedArray(ByteBuffer in) throws Exception {
        EncodedArray encodedArray = new EncodedArray();
        encodedArray.size = BaseUtil.readULeb128i(in);
        encodedArray.values = new Object[encodedArray.size];
        for (int i = 0;i<encodedArray.size;i++){
            encodedArray.values[i] = readEncodedValue(in);
        }
        return encodedArray;
    }

    private static long readIntBits(ByteBuffer in, int before) {
        int length = ((before >> 5) & 0x7) + 1;
        long value = 0;
        for (int j = 0; j < length; j++) {
            value |= ((long) (0xFF & in.get())) << (j * 8);
        }
        int shift = (8 - length) * 8;
        return value << shift >> shift;
    }

    private static long readUIntBits(ByteBuffer in, int before) {
        int length = ((before >> 5) & 0x7) + 1;
        long value = 0;
        for (int j = 0; j < length; j++) {
            value |= ((long) (0xFF & in.get())) << (j * 8);
        }
        return value;
    }

    private static long readFloatBits(ByteBuffer in, int before) {
        int bytes = ((before >> 5) & 0x7) + 1;
        long result = 0L;
        for (int i = 0; i < bytes; ++i) {
            result |= ((long) (0xFF & in.get())) << (i * 8);
        }
        result <<= (8 - bytes) * 8;
        return result;
    }

    private ClassData parserClassData(int offset) throws Exception {
        ClassData classData = new ClassData();
        classData.offset = offset;

        if (offset == 0){
            return classData;
        }

        classDataIn.position(offset);

        classData.staticFieldsSize = BaseUtil.readULeb128i(classDataIn);
        classData.instaceFieldsSize = BaseUtil.readULeb128i(classDataIn);
        classData.directMethodsSize = BaseUtil.readULeb128i(classDataIn);
        classData.virtualMethodsSize = BaseUtil.readULeb128i(classDataIn);

        classData.staticFields = readEncodedField(classData.staticFieldsSize,classDataIn);
        classData.instanceFields = readEncodedField(classData.instaceFieldsSize,classDataIn);
        classData.directMethods = readEncodedMethod(classData.directMethodsSize,classDataIn);
        classData.virtualMethods = readEncodedMethod(classData.virtualMethodsSize,classDataIn);

        return classData;
    }

    private EncodedField[] readEncodedField(int size,ByteBuffer in) throws Exception {
        EncodedField[] encodedFields = new EncodedField[size];
        int lastIndex = 0;
        for (int i = 0;i<size;i++){
            EncodedField encodedField = new EncodedField();

            encodedField.index = i;

            encodedField.fieldIndexDiff = BaseUtil.readULeb128i(in);
            encodedField.fieldIndex = lastIndex+encodedField.fieldIndexDiff;
            lastIndex = encodedField.fieldIndex;
            encodedField.field = fieldIDParser.getFieldByIndex(encodedField.fieldIndex);

            encodedField.accessFlag = BaseUtil.readULeb128i(in);

            encodedFields[i] = encodedField;
        }
        return encodedFields;
    }



    private EncodedMethod[] readEncodedMethod(int size,ByteBuffer in) throws Exception {
        EncodedMethod[] encodedMethods = new EncodedMethod[size];
        int lastIndex = 0;
        for (int i = 0;i < size ; i++){
            EncodedMethod encodedMethod = new EncodedMethod();

            encodedMethod.index = i;
            encodedMethod.methodIndexDiff =  BaseUtil.readULeb128i(in);
            encodedMethod.methodIndex = lastIndex + encodedMethod.methodIndexDiff;
            lastIndex = encodedMethod.methodIndex;
            encodedMethod.method = methodIDParser.getMethodByIndex(encodedMethod.methodIndex);

            encodedMethod.accessFlag = BaseUtil.readULeb128i(in);

            encodedMethod.codeOffset = BaseUtil.readULeb128i(in);
            encodedMethod.code = readCode(encodedMethod.codeOffset);

            encodedMethods[i] = encodedMethod;
        }

        return encodedMethods;
    }

    private Code readCode(int offset) throws Exception {
        Code code = new Code();
        code.offset = offset;

        if (offset == 0){
            return code;
        }

        codeItemIn.position(offset);

        code.registersSize = 0xFFFF & codeItemIn.getShort();

        code.insSize = 0xFFFF & codeItemIn.getShort();
        code.outsSize = 0xFFFF & codeItemIn.getShort();
        code.triesSize = 0xFFFF & codeItemIn.getShort();
        code.debugInfoOffset = codeItemIn.getInt();
        code.debugInfo = readDebugInfo(code.debugInfoOffset);
        code.insnsSize = codeItemIn.getInt();
        code.insns = new byte[code.insnsSize*2];
        codeItemIn.get(code.insns);

        if (code.triesSize > 0){
            if (code.insnsSize % 2 == 1){
                code.padding = codeItemIn.getShort();
            }
            int encoded_catch_handler_list = codeItemIn.position() + code.triesSize * 8;
            ByteBuffer handlerIn = codeItemIn.duplicate().order(ByteOrder.LITTLE_ENDIAN);
            code.tries = readTry(code.triesSize,codeItemIn,handlerIn,encoded_catch_handler_list);
            code.handlers = readHandlerList(codeItemIn);
        }

        return code;
    }

    private DebugInfo readDebugInfo(int offset) throws Exception {
        DebugInfo debugInfo = new DebugInfo();
        debugInfo.offset = offset;

        if (offset == 0){
            return debugInfo;
        }

        debugInfoIn.position(offset);

        debugInfo.lineStart = BaseUtil.readULeb128i(debugInfoIn);
        debugInfo.parametersSize = BaseUtil.readULeb128i(debugInfoIn);
        debugInfo.parameterNameIndexs = new int[debugInfo.parametersSize];
        debugInfo.parameterNames = new StringID[debugInfo.parametersSize];

        for(int i = 0; i<debugInfo.parametersSize; i++){
            int nameIdx = BaseUtil.readULeb128i(debugInfoIn) - 1;
            debugInfo.parameterNameIndexs[i] = nameIdx;
            debugInfo.parameterNames[i] = stringIDParser.getStringIDByIndex(nameIdx);
        }

        debugInfo.opcodes = new ArrayList<>();

        while (true){
            byte opcode = debugInfoIn.get();
            debugInfo.opcodes.add(opcode);
            if ((opcode & 0xff) == DexConstants.DBG_END_SEQUENCE ){
                break;
            }
        }

        return debugInfo;
    }

    private Try[] readTry(int size, ByteBuffer in, ByteBuffer handlerIn, int encoded_catch_handler_list) throws Exception {
        Try[] tries = new Try[size];

        for (int i = 0;i<size;i++){
            Try t = new Try();
            tries[i] = t;
            t.index = i;
            t.startAddr = in.getInt();
            t.insnCount = 0xFFFF & in.getShort();
            t.handlerOffset = 0xFFFF & in.getShort();

            handlerIn.position(t.handlerOffset + encoded_catch_handler_list);
            EncodedCatchHandler catchHandler = new EncodedCatchHandler();
            t.catchHandler = catchHandler;
            catchHandler.sizeOrg = BaseUtil.readLeb128i(handlerIn);
            boolean catchAll = false;
            if (catchHandler.sizeOrg <= 0){
                catchHandler.size = -catchHandler.sizeOrg;
                catchAll = true;
            }else{
                catchHandler.size = catchHandler.sizeOrg;
            }
            EncodedTypeAddrPair[] pairs = new EncodedTypeAddrPair[catchHandler.size];
            for (int j = 0;j<catchHandler.size;j++){
                EncodedTypeAddrPair pair = new EncodedTypeAddrPair();
                pair.index = j;
                pair.typeIndex = BaseUtil.readULeb128i(handlerIn);
                pair.type = typeIDParser.getTypeIDByIndex(pair.typeIndex);
                pair.addr = BaseUtil.readULeb128i(handlerIn);
                pairs[j] = pair;
            }
            catchHandler.handlers = pairs;
            if (catchAll){
                catchHandler.catchAllAddr = BaseUtil.readULeb128i(handlerIn);
            }

        }

        return tries;
    }

    public EncodedCatchHandlerList readHandlerList(ByteBuffer in) throws Exception {
        EncodedCatchHandlerList list = new EncodedCatchHandlerList();

        list.size = BaseUtil.readULeb128i(in);

        EncodedCatchHandler[] catchHandlers = new EncodedCatchHandler[list.size];
        list.list = catchHandlers;

        for (int i = 0;i<list.size;i++){
            EncodedCatchHandler catchHandler = new EncodedCatchHandler();
            catchHandler.sizeOrg = BaseUtil.readLeb128i(in);
            boolean catchAll = false;
            if (catchHandler.sizeOrg <= 0){
                catchHandler.size = -catchHandler.sizeOrg;
                catchAll = true;
            }else{
                catchHandler.size = catchHandler.sizeOrg;
            }
            EncodedTypeAddrPair[] pairs = new EncodedTypeAddrPair[catchHandler.size];
            for (int j = 0;j<catchHandler.size;j++){
                EncodedTypeAddrPair pair = new EncodedTypeAddrPair();
                pair.index = j;
                pair.typeIndex = BaseUtil.readULeb128i(in);
                pair.type = typeIDParser.getTypeIDByIndex(pair.typeIndex);
                pair.addr = BaseUtil.readULeb128i(in);
                pairs[j] = pair;
            }
            catchHandler.handlers = pairs;
            if (catchAll){
                catchHandler.catchAllAddr = BaseUtil.readULeb128i(in);
            }

            catchHandlers[i] = catchHandler;
        }


        return list;
    }

    public EncodedArrayItem readEncodedArrayItem(int offset) throws Exception {
        EncodedArrayItem encodedArrayItem = new EncodedArrayItem();
        encodedArrayItem.offset = offset;

        if (offset == 0){
            return encodedArrayItem;
        }

        encodedArrayItemIn.position(offset);
        encodedArrayItem.value = readEncodedArray(encodedArrayItemIn);

        return encodedArrayItem;
    }
}
