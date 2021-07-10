package com.art.sell.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将excel数据装换成数据模型
 *
 * @author Administrator
 */
@Slf4j
public class FileUtil {
    private static final int HEADER = 0;
    private static final int START = 1;
    private Workbook book;
    /**
     * key:excel对应标题 ,value:对象属性
     */
    private Map<String, String> associations;
    /**
     * 装换失败的数据信息，记录行数
     */
    private StringBuffer error = new StringBuffer(0);
    private Map<Integer, String> header;
    /**
     * 默认的日期格式
     */
    private String date_format = "yyyy-MM-dd";
    private SimpleDateFormat format;

    /**
     * 初始化工作簿
     *
     * @param filePath
     */
    public void init(String filePath) {
        log.info("filePath==" + filePath);
        try {
            URL url = null;
            try {
                url = new URL(filePath);
            } catch (MalformedURLException e) {
                log.error("Url:" + filePath + "======>urlErr:" + e.getMessage());
                //this.error.append("找不到文件路径，请联系客服");
            }
            //TODO 生产用代码
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			//设置是否要从 URL 连接读取数据,默认为true
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            //TODO 本地测试用代码
            /*File file = new File(filePath);
            FileInputStream inputStream = new FileInputStream(file);*/
            String type = filePath.substring(filePath.lastIndexOf(".")+1);
            if (type.equals("xls")) {
                book = new HSSFWorkbook(inputStream);
            }
            if (type.equals("xlsx")) {
                book = new XSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            log.error("ioErr:" + e.getMessage());
            this.error.append("读取excel失败，请联系客服");
        } catch (Exception e) {
            log.error("ioErr:" + e.getMessage());
            this.error.append("读取excel失败，请联系客服");
        }
    }

    public FileUtil(Map<String, String> associations, String filePath) {
        this.associations = associations;
        this.init(filePath);
        format = new SimpleDateFormat(date_format);
    }



    /**
     * @return true 存在错误，false 不存在错误
     */
    public boolean hasError() {
        return error.capacity() > 0;
    }

    public StringBuffer getError() {
        return error;
    }

    /**
     * 获取第一行标题栏数据
     *
     * @param sheet
     * @return map key：标题栏列下标（0开始） value 标题栏值
     */
    private void loadHeader(Sheet sheet) {
        this.header = new HashMap<>();
        Row row = sheet.getRow(HEADER);
        int columns = row.getLastCellNum();
        for (int i = 0; i < columns; i++) {
            log.info("加载标题栏:" + row.getCell(i).getStringCellValue());
            String value = row.getCell(i).getStringCellValue();
            if (null == value) {
                //throw new RuntimeException("标题栏不能为空！");
                continue;
            }
            header.put(i, value);
        }
        log.info("<<<<<<<<<<<<标题栏加载完毕>>>>>>>>>>>");
    }

    /**
     * @param clazz
     * @param required 是否每个属性都是必须的
     * @return
     */
    public <T> List<T> bindToModels(Class clazz, boolean required) {

        Sheet sheet = this.book.getSheetAt(0);
		// 获取行数
        int rowNum = sheet.getLastRowNum();
        if (rowNum < 1) {
            return new ArrayList<T>();
        }
        // 加载标题栏数据
        this.loadHeader(sheet);
        List<T> result = new ArrayList<T>();
        for (int i = START; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            int cellNum = row.getLastCellNum();
            T instance = null;
            try {
                instance = (T) clazz.newInstance();
            } catch (InstantiationException e) {
                log.error("class初始化失败：" + e.getMessage());
                error.append("系统错误，请联系客服");
            } catch (IllegalAccessException e) {
                log.error("class初始化失败安全权限异常：" + e.getMessage());
                this.error.append("系统错误，请联系客服");
            }
            for (int columns = 0; columns < cellNum; columns++) {
                Cell cell = row.getCell(columns);
                // 判断单元格的数据类型
                String value = regexLength(loadCellType(cell));
                // 获取单元格的值
                String key = header.get(columns);
                if (StringUtils.isEmpty(value)) {
                    // 如果为必填的则将错误信息记录
                    if (required) {
                        instance = null;
                        this.error.append(
                                "第" + (i + 1) + "行，" + header.get(columns)
                                        + "字段，数据为空！").append("\n");

                        continue;
                    }
                    if ("转运/清关公司".equals(key)) {
                        instance = null;
                        break;
                    }
                } else {
                    // 加载实际值
                    try {
                        this.loadValue(clazz, instance, this.associations.get(key),
                                value);
                    } catch (Exception e){
                        log.error(this.associations.get(key) + "====>数据装载失败：" + e.getMessage());
                    }
                }
            }
            if (instance != null) {
                result.add(instance);
            }
        }
        log.info("<<<<<装换完成" + (this.hasError() ? "有错误信息" : "") + "，共有对象:"
                + result.size() + "个" + ">>>>>>");
        return result;
    }

    /**
     * 将单元格数据转换成string类型
     *
     * @param
     * @param cell
     * @return
     */
    private String loadCellType(Cell cell) {
        String value = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    // 判断当前的cell是否为Date
                    try {
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            value = this.formateDate(cell.getDateCellValue());
                        } else {
                            value = String.valueOf(cell.getNumericCellValue());
                        }
                    }catch (Exception e) {
                        this.error.append("时间格式问题，你可以用excel中的‘短日期’功能设置单元格");
                    }finally {
                        break;
                    }
                case HSSFCell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    value ="";
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    try {
                        value = String.valueOf(cell.getNumericCellValue());
                    } catch (IllegalStateException e) {
                        value = String.valueOf(cell.getRichStringCellValue());
                    }
                    this.error.append("表格中不能有公式");
                    log.info("不支持函数！");
                    break;
            }
        }
        return replaceBlank(value);
    }

    /**
     * 注入属性值
     *
     * @param instance
     * @param pro      属性对象
     * @param value    属性值
     */
    @SuppressWarnings("unchecked")
    private <T> void loadValue(Class clazz, T instance, String pro, String value)//clazz OrderExcelDto.class | instance Object
            throws InvocationTargetException, ParseException, IllegalAccessException, NoSuchMethodException {
        String getMethod = this.initGetMethod(pro);
        Class type = clazz.getDeclaredMethod(getMethod, null).getReturnType();
        Method method = clazz.getMethod(this.initSetMethod(pro), type);
        if (type == String.class) {
            method.invoke(instance, value);
        } else if (type == int.class || type == Integer.class) {
            method.invoke(instance, Integer.parseInt(value));
        } else if (type == long.class || type == Long.class) {
            method.invoke(instance, Long.parseLong(value));
        } else if (type == float.class || type == Float.class) {
            method.invoke(instance, Float.parseFloat(value));
        } else if (type == double.class || type == Double.class) {
            method.invoke(instance, Double.parseDouble(value));
        } else if (type == Date.class) {
            method.invoke(instance, this.parseDate(value));
        }
    }

    private Date parseDate(String value) throws ParseException {
        return format.parse(value);
    }

    private String formateDate(Date date) {
        return format.format(date);
    }

    public String initSetMethod(String field) {
        return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    public String initGetMethod(String field) {
        return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    public String getDate_format() {
        return date_format;
    }

    public void setDate_format(String date_format) {
        this.date_format = date_format;
    }

    /**
     * 去除数据的空格、回车、换行符、制表符
     */
    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
    public String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            //空格\t、回车\n、换行符\r、制表符\t

            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public String regexLength(String value) {
        if (value.length() > 255) {
            return value.substring(0, 255);
        }else {
            return value;
        }
    }

    public static String upload(MultipartFile file, String path) throws Exception{
        if(file == null){
            throw new RuntimeException("上传文件为空");
        }
        if(org.apache.commons.lang3.StringUtils.isBlank(path)){
            throw new RuntimeException("上传路径为空");
        }
        String format = DateFormatUtils.format(new Date(), "yyyyMMdd");
        File f1 = new File(path, format);
        if (!f1.exists()) {
            f1.mkdir();
        }
        String parentPath = f1.getPath();
        if (parentPath == null) {
            parentPath = f1.getAbsolutePath();

        }
        log.info("文件上传的路径为 ：" + parentPath);
        String originalFilename = file.getOriginalFilename();
        //修正原文件的名字
        String newFileName = UploadFileUtil.convertFileName(originalFilename);
        File storedFile = new File(parentPath + File.separator + newFileName);
        FileUtils.writeByteArrayToFile(storedFile, file.getBytes());
        log.info("文件上传成功...");
        //返回相对路径
        String resPath = storedFile.getPath();
        if (resPath == null) {
            resPath = storedFile.getCanonicalPath();
        }
        log.info("返回文件的路径为 ：" + resPath);
        return resPath;
    }
}