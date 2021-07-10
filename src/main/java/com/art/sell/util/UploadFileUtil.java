package com.art.sell.util;

import com.art.sell.pojo.Msg;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件上传工具类
 * @author Administrator
 */
public class UploadFileUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadFileUtil.class);

	public  static Msg failureMsg = new Msg(Msg.FAILURE_CODE,"文件上传失败",new Date());

	/**
	 *
	 * @param file 服务器接收的文件
	 * @param uploadPath 上传文件的路径
	 * @return
	 */
	public static Msg uploadFile(MultipartFile file,String uploadPath) throws IOException {

		if(file == null){
			return failureMsg;
		}
		if(StringUtils.isBlank(uploadPath)){
			return failureMsg;
		}
		String originalFilename = file.getOriginalFilename();
		if(StringUtils.isBlank(originalFilename)){
			return failureMsg;
		}

		/*if(!isExcelFile(originalFilename)){
			return failureMsg;
		}*/

		//String newFileName = convertFileName(originalFilename);

		File out = new File(uploadPath, originalFilename);
		FileUtils.writeByteArrayToFile(out,file.getBytes());

		Msg msg = new Msg(Msg.SUCCESS_CODE,"文件上传成功",new Date());
		return msg;


	}


	/**
	 * 文件名字添加时间戳的方法
	 * @param fileName
	 * @return
	 */
	public static String convertFileName(String fileName){
		int index = fileName.lastIndexOf(".");
		String prefixName = fileName.substring(0,index);
		String suffixName = fileName.substring(index);

		long timeMillis = System.currentTimeMillis();
		String newName = prefixName+"_"+timeMillis+suffixName;
		return newName;
	}


	static Pattern pattern = Pattern.compile("^(?:\\w+\\.xlsx|\\w+\\.xls)$");
	/**
	 * 判断文件的名字是否是excel
	 * @param fileName
	 * @return
	 */
	private static boolean isExcelFile(String fileName){

		Matcher matcher = pattern.matcher(fileName);
		return matcher.matches();
	}


	public static String upload(MultipartFile file, String path) throws IOException{
		String format = DateFormatUtils.format(new Date(), "yyyyMMdd");
		File f1 = new File(path, format);
		if (!f1.exists()) {
			f1.mkdirs();
		}

		String parentPath = f1.getPath();
		if (parentPath == null) {
			parentPath = f1.getAbsolutePath();

		}
		LOGGER.debug("文件上传的路径为 ：" + parentPath);
		String originalFilename = file.getOriginalFilename().replaceAll(" ","");
		//修正原文件的名字
		String newFileName = UploadFileUtil.convertFileName(originalFilename);

		File storedFile = new File(parentPath + File.separator + newFileName);
		FileUtils.writeByteArrayToFile(storedFile, file.getBytes());
		LOGGER.debug("文件上传成功...");

		//返回相对路径
		String resPath = storedFile.getPath();
		if (resPath == null) {
			resPath = storedFile.getCanonicalPath();
		}
		LOGGER.debug("返回文件的路径为 ：" + resPath);
		return resPath;
	}

}
