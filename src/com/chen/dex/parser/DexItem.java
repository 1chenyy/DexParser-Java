package com.chen.dex.parser;

import com.chen.dex.utils.BaseUtil;

public class DexItem {
    @Override
    public String toString() {
        return BaseUtil.objToFormatJson(this);
    }
}
