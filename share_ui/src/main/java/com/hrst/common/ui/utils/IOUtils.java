package com.hrst.common.ui.utils;


import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO流工具类
 * @author BD
 * 
 */
public class IOUtils {
	
	public static String copyBigData (String pathFileName, Context context) {
		try {
			String[] data = pathFileName.split("/");
			//分割
			String fileName = data[data.length - 1];
			String path = pathFileName.substring(0, pathFileName.indexOf(fileName));
			File fs = new File(path, fileName);
			
//			if (!fs.exists()) {
				InputStream myInput;
				OutputStream myOutPut = new FileOutputStream(fs);
				myInput = context.getAssets().open(fileName);
				byte[] buffer = new byte[1024];
				int length = myInput.read(buffer);
				
				while (length > 0) {
					myOutPut.write(buffer, 0, length);
					length = myInput.read(buffer);
				}
				
				myOutPut.flush();
				myInput.close();
				myOutPut.close();
//			}
			return fs.getAbsolutePath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取文件的数据
	 * @param file : 文件路径
	 * @return
	 * @throws IOException
	 */
	public static String readFile(File file) throws IOException {
		if(file.exists()) su(file.getAbsolutePath());
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		String len = null;
		while ((len = br.readLine()) != null) {
			sb.append(len+"\r\n");
		}
		br.close();
		return sb.toString();
	}

	/**
	 * 向指定的文件追加数据
	 * @param file : 文件
	 * @param data : 数据
	 * @throws IOException
	 */
	public static void writeData(File file, String data) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(file, true);
		data = data + "\r\n";
		outputStream.write(data.getBytes());
		outputStream.close();
		su(file.getAbsolutePath());
	}
	
	/**
	 * 将输入流写入指定的路径
	 * @param inputStream
	 * @param file
	 * @throws IOException
	 */
	public static void writeByte(InputStream inputStream,File file) throws IOException{
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			int len = 0;
			byte[] buffer = new byte[1024];
			
			while((len = inputStream.read(buffer)) != -1){
				fileOutputStream.write(buffer, 0, len);
			}
		} catch (IOException e) {
			throw new IOException();
		}finally{
			fileOutputStream.close();
			inputStream.close();
			IOUtils.su(file.getAbsolutePath());
		}
	}
	
	
	/**
	 * 获取文件权限
	 */
	public static void su(String path) {
		try {
			Process localProcess = Runtime.getRuntime().exec("/system/bin/su");
//			String str = command + "\n" + "exit\n";
			String str = "chmod 777 " + path + "\n" + "exit\n";
			localProcess.getOutputStream().write(str.getBytes());
		} catch (Exception e) {
		}
	}
	public static boolean exeCommand(String command){
		String str = command + "\n" + "exit\n";
		try {
			Process localProcess = Runtime.getRuntime().exec("/system/bin/su");
			localProcess.getOutputStream().write(command.getBytes());
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
}
