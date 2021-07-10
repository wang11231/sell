package com.art.sell.util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * 导出csv文件
 * Create by yuan on 2018/11/3
 */
public class exportCsvUtil {
    /**
     * @param title 标题
     * @param file  文件
     * @param lists 数据
     * @return
     */
    public static void exportCsv(List<String> title, File file, List<List<String>> lists, HttpServletResponse response) {
        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        OutputStream outputStream = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            for (String ti : title) {
                bw.append(ti).append(",");
            }
            bw.append("\n");
            if (lists != null && !lists.isEmpty()) {
                for (List<String> data : lists) {
                    for (String str : data) {
                        bw.append(str).append(",");
                    }
                    bw.append("\n");
                }
            }
            bw.flush();
            bw.close();
            out.flush();
            out.close();
            //写出csv文件
            outputStream = response.getOutputStream();
            setResponse(response,file.getName());
            FileInputStream inStream = new FileInputStream(file);
            byte[] buf = new byte[4096];
            int readLength;
            while (((readLength = inStream.read(buf)) != -1)) {
                outputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
                outputStream.write(buf, 0, readLength);
            }
            inStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void setResponse(HttpServletResponse response, String title) throws UnsupportedEncodingException {
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.setHeader("Content-Disposition", "attachment;filename="
                + new String(title.getBytes("GB2312"), "8859_1") + ".csv");
        response.addHeader("Cache-Control", "no-cache");
    }
}
