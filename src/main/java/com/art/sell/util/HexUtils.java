package com.art.sell.util;

public abstract class HexUtils {
    private final static String HEX_TABLE_STRING = "0123456789ABCDEF";

    private final static char[] HEX_DIGITS_CAPITAL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    private final static char[] HEX_DIGITS_SMALL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 将16进制的ASCII字符串转化为BCD的字节数组.
     *
     * @param hexString 16进制字符串
     * @return 字节数组
     */
    public static byte[] hexToByte(String hexString) {
        int len = hexString.length();
        byte[] numByte = hexString.toUpperCase().getBytes();
        byte[] bcdCode = new byte[len];
        for (int i = 0; i < len; i++) {
            bcdCode[i] = (byte) HEX_TABLE_STRING.indexOf(numByte[i]);
        }

        return compressByte(bcdCode);
    }

    /**
     * 压缩字节数组（BCD码转ASCII）
     *
     * @param srcByte 待压缩字节数组（BCD码）
     * @return 字节数组
     */
    public static byte[] compressByte(byte[] srcByte) {
        int len = srcByte.length / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] += (byte) ((srcByte[2 * i] << 4) + srcByte[2 * i + 1]);
        }

        return result;
    }

    /**
     * 异或
     * @param factor1 被异或值
     * @param factor2 异或值
     * @return 异或结果
     */
    public static byte[] xor(byte[] factor1, byte[] factor2) {
        int times = factor1.length;
        byte[] result = new byte[times];
        for (int i = 0; i < times; i++) {
            result[i] = (byte) (factor1[i] ^ factor2[i]);
        }

        return result;
    }

    /**
     * 将字节数组转换为16进制字符串
     *
     * @param byteArray 字节数组
     * @return 16进制字符串
     */
    public static String byteArrayToHex(byte[] byteArray) {
        return byteArrayToHex(byteArray, true);
    }

    /**
     * 将字节数组转换为16进制字符串
     * @param byteArray 字节数组
     * @param isCapital 是否输出为大写字母
     * @return 16进制字符串
     */
    public static String byteArrayToHex(byte[] byteArray, boolean isCapital) {
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        char[] hexDigits = isCapital ? HEX_DIGITS_CAPITAL : HEX_DIGITS_SMALL;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

    /**
     * 字节数组相加
     * @param first 第一个字节数组
     * @param second 第二个字节数组
     * @return 相加后结果
     */
    public static byte[] addByteArrays(byte first[], byte second[]) {
        byte result[] = new byte[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
