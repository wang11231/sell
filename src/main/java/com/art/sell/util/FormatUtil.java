package com.art.sell.util;

import java.text.SimpleDateFormat;

public class FormatUtil {

    public static final ThreadLocal<SimpleDateFormat> FORMAT_YMDHMS = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));
    public static final ThreadLocal<SimpleDateFormat> FORMAT_YMD = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));

}
