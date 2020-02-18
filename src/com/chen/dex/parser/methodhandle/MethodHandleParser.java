package com.chen.dex.parser.methodhandle;

import com.chen.dex.parser.DexConstants;
import com.chen.dex.parser.DexHead;
import com.chen.dex.parser.annotation.MethodAnnotation;
import com.chen.dex.parser.field.FieldIDParser;
import com.chen.dex.parser.method.MethodIDParser;
import com.chen.dex.parser.proto.ProtoIDParser;
import com.chen.dex.parser.string.StringIDParser;
import com.chen.dex.parser.type.TypeIDParser;
import com.chen.dex.utils.BaseUtil;

import java.nio.ByteBuffer;

public class MethodHandleParser {
    private ByteBuffer methodHandleIdIn;
    private DexHead head;
    private FieldIDParser fieldIDParser;
    private MethodIDParser methodIDParser;

    public MethodHandleParser(ByteBuffer dexBuffer, DexHead head){
        this.methodHandleIdIn = BaseUtil.slice(dexBuffer,head.method_handle_ids_off,head.method_handle_ids_size*8);
        this.head = head;
        StringIDParser stringIDParser = new StringIDParser(dexBuffer,head);
        TypeIDParser typeIDParser = new TypeIDParser(dexBuffer,head,stringIDParser);
        ProtoIDParser protoIDParser = new ProtoIDParser(dexBuffer,head,stringIDParser,typeIDParser);
        fieldIDParser = new FieldIDParser(dexBuffer,head,stringIDParser,typeIDParser);
        methodIDParser = new MethodIDParser(dexBuffer,head,stringIDParser,typeIDParser,protoIDParser);
    }

    public MethodHandleParser(ByteBuffer dexBuffer, DexHead head, FieldIDParser fieldIDParser, MethodIDParser methodIDParser){
        this.methodHandleIdIn = BaseUtil.slice(dexBuffer,head.method_handle_ids_off,head.method_handle_ids_size*8);
        this.head = head;
        this.fieldIDParser = fieldIDParser;
        this.methodIDParser = methodIDParser;
    }

    public MethodHandle getMethodHandleByIndex(int index) throws Exception {
        if (head.method_handle_ids_off == 0 || head.method_handle_ids_size == 0){
            return null;
        }

        if (index < 0 || index > head.method_handle_ids_size){
            throw new Exception("Error index : " + index + " size = " + head.method_handle_ids_size);
        }

        methodHandleIdIn.position(index * 8);

        MethodHandle handle = new MethodHandle();

        handle.index = index;

        handle.type = methodHandleIdIn.getShort() & 0xFFFF;
        handle.unused1 = methodHandleIdIn.getShort() & 0xFFFF;
        handle.id = methodHandleIdIn.getShort() & 0xFFFF;
        handle.unused2 = methodHandleIdIn.getShort() & 0xFFFF;

        switch (handle.type){
            case DexConstants.INSTANCE_GET:
            case DexConstants.INSTANCE_PUT:
            case DexConstants.STATIC_GET:
            case DexConstants.STATIC_PUT:{
                handle.obj = fieldIDParser.getFieldByIndex(handle.id);
                return handle;
            }

            case DexConstants.INVOKE_INSTANCE:
            case DexConstants.INVOKE_STATIC:
            case DexConstants.INVOKE_CONSTRUCTOR:
            case DexConstants.INVOKE_DIRECT:
            case DexConstants.INVOKE_INTERFACE: {
                handle.obj = methodIDParser.getMethodByIndex(handle.id);
                return handle;
            }
            default:
                throw new RuntimeException();
        }
    }
}
