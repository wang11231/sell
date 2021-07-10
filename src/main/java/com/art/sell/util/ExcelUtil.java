package com.art.sell.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理excel的工具类
 * @author Administrator
 */
public class ExcelUtil {
	protected static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	public Workbook book;

	public Sheet sheet;

	protected Sheet readExcelFile(String path){

		InputStream in = null;
		if(path == null){
			return null;
		}
		try{

			in = new FileInputStream(new File(path));

			String suffix = path.substring(path.lastIndexOf(".")+1);
			if("xls".equals(suffix)){
				book = new HSSFWorkbook(in);
			}else if("xlsx".equals(suffix)){
				book = new XSSFWorkbook(in);
			}

			//获取到第一个sheet
			Sheet uploadSheet = book.getSheetAt(0);

			//将解析到的sheet赋值给uploadSheet
			sheet = uploadSheet;
			return uploadSheet;

		}catch (Exception e){

			e.printStackTrace();
			return null;
		}

	}


    public List<String> header;
	public int idStartIndex;
    public int idEndIndex;
    public List<Long> ids;
	/**
	 * 获取到excel的第一行头信息
	 * @return
	 */
	protected List<String> getHeader() {
		Row row = sheet.getRow(0);
		short lastCellNum = row.getLastCellNum();
		short firstCellNum = row.getFirstCellNum();

		//切割产品信息，如176_产品名1
		for (int i = firstCellNum; i < lastCellNum; i++) {
			Cell cell = row.getCell(i);
			if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
				String value = cell.getStringCellValue();
				if(StringUtils.isNotBlank(value) && value.indexOf("_") > -1){
					cell.setCellValue(Double.parseDouble(value.split("_")[0]));
				}
			}
		}

		List excelHeader = new ArrayList();
		//得到第一个产品id的索引
		for (int i = firstCellNum; i < lastCellNum; i++) {
			Cell cell = row.getCell(i);
			int type = cell.getCellType();
			if(type == HSSFCell.CELL_TYPE_NUMERIC){
				idStartIndex = i;
				break;
			}
		}
		//得到最后一个产品id的索引
		for (int i = lastCellNum-1; i >= firstCellNum; i--) {
			Cell cell = row.getCell(i);
			int type = cell.getCellType();
			if(type == HSSFCell.CELL_TYPE_NUMERIC){
				idEndIndex = i;
				break;
			}
		}
		for (int i = firstCellNum; i < lastCellNum; i++) {
			Cell cell = row.getCell(i);
			String value = getCellValue(cell);
			excelHeader.add(value);
		}
        List<Long> idsLong = new ArrayList<>();
        for (int i = idStartIndex; i < idEndIndex + 1; i++) {
            Cell cell = row.getCell(i);
            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                Double value = cell.getNumericCellValue();
                idsLong.add(value.longValue());
            }
        }
        ids = idsLong;
		header = excelHeader;
		return excelHeader;
	}

	protected static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取excel单元格内容:结果为String或者数值
	 * @param cell
	 * @return
	 */
	private String getCellValue(Cell cell){
		if(cell == null){
			return null;
		}

		String value = null;
		int type = cell.getCellType();

		switch (type) {
			// 数字
			case HSSFCell.CELL_TYPE_NUMERIC:
				//如果为时间格式的内容
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					//注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd HH:mm:ss
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
					value=sdf.format(HSSFDateUtil.getJavaDate(cell.
							getNumericCellValue())).toString();
					break;
				} else {
					value = new DecimalFormat("0").format(cell.getNumericCellValue());
                    //value = cell.getNumericCellValue()+"";
				}
				break;
			// 字符串
			case HSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			// Boolean
			case HSSFCell.CELL_TYPE_BOOLEAN:
				value = cell.getBooleanCellValue()+"";
				break;
			// 公式
			case HSSFCell.CELL_TYPE_FORMULA:
				value = cell.getCellFormula()+"";
				break;
			// 空值
			case HSSFCell.CELL_TYPE_BLANK:
				//value = "";
				break;
			// 故障
			case HSSFCell.CELL_TYPE_ERROR:
				//value = "未知类型";
				break;
			default:
				//value = "未知类型";
				break;
		}

		return value;
	}

	public List<Map<String,String>> excelData;
	/**
	 * 解析excel
	 */
	protected List<Map<String,String>> parseExcel(){
		getHeader();
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();



		List<Map<String,String>> data  = new ArrayList<>();

		for(int i=firstRowNum+1;i<=lastRowNum;i++){
			Row row = sheet.getRow(i);
			short firstCellNum = row.getFirstCellNum();
			short lastCellNum = row.getLastCellNum();

			Map<String,String> map = new LinkedHashMap<>();
			for(int j=firstCellNum;j<lastCellNum;j++){

				Cell cell = row.getCell(j);
				String value = getCellValue(cell);
				map.put(header.get(j),value);
			}
			data.add(map);
		}

		excelData = data;
		return excelData;
	}

	/*public void createExcel(OutputStream os) throws WriteException, IOException {
		//创建工作薄
		WritableWorkbook workbook = Workbook.createWorkbook(os);
		//创建新的一页
		WritableSheet sheet = workbook.createSheet("First Sheet", 0);
		//创建要显示的具体内容
		Label formate = new Label(0,0,"数据格式");
		sheet.addCell(formate);
		Label floats = new Label(1,0,"浮点型");
		sheet.addCell(floats);
		Label integers = new Label(2,0,"整型");
		sheet.addCell(integers);
		Label booleans = new Label(3,0,"布尔型");
		sheet.addCell(booleans);
		Label dates = new Label(4,0,"日期格式");
		sheet.addCell(dates);

		Label example = new Label(0,1,"数据示例");
		sheet.addCell(example);
		//浮点数据
		jxl.write.Number number = new jxl.write.Number(1,1,3.1415926535);
		sheet.addCell(number);
		//整形数据
		jxl.write.Number ints = new jxl.write.Number(2,1,15042699);
		sheet.addCell(ints);
		jxl.write.Boolean bools = new jxl.write.Boolean(3,1,true);
		sheet.addCell(bools);
		//日期型数据
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		WritableCellFormat cf1 = new WritableCellFormat(DateFormats.FORMAT1);
		DateTime dt = new DateTime(4,1,date,cf1);
		sheet.addCell(dt);
		//把创建的内容写入到输出流中，并关闭输出流
		workbook.write();
		workbook.close();
		os.close();

	}*/
}
