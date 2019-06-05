package hrst.sczd.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;

/**
 * @author 沈月美
 * @describe 本类是文件操作类主要是生成查询文件,在服务器上下载文件时候用到
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class FileUtils {
	private String SDPATH;
	private FileOutputStream output = null; // 文件输出流对象
	private static FileUtils intance;
	private long len = 0;

	public String getSDPATH() {
		return SDPATH;
	}

	/**
	 * 单例
	 * 
	 * @return FileUtils
	 */
	public static FileUtils getIntance() {
		if (intance == null) {
			intance = new FileUtils();
		}
		return intance;
	}

	/**
	 * 构造方法
	 * 
	 */
	public FileUtils() {
		// 得到当前外部存储设备的目录
		// /SDCARD
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 判断网络
	 * 
	 * @param con
	 *            上下文
	 * @return string 获取网络的形式
	 */
	public static String checkNetworkInfo(Context con) {

		String typeState = "";
		try {
			typeState = "NO";
			ConnectivityManager conMan = (ConnectivityManager) con
					.getSystemService(Context.CONNECTIVITY_SERVICE); // mobile
																		// 3G
			android.net.NetworkInfo.State mobile = conMan.getNetworkInfo(
					ConnectivityManager.TYPE_MOBILE).getState();
			if (mobile.toString().equals("CONNECTED")) {
				typeState = "OK,M";
			}
			android.net.NetworkInfo.State wifi = conMan.getNetworkInfo(
					ConnectivityManager.TYPE_WIFI).getState();
			if (wifi.toString().equals("CONNECTED")) {
				typeState = "OK,W";
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return typeState;
	}

	/**
	 * 在报错信息中判断找到Caused by的位置，截取字符
	 * 
	 * @param string
	 *            内容
	 * @return String 截取后的内容
	 */
	public static String initException(String string) {

		int index = string.indexOf("Caused by");
		if (index < 0) {
			return "没有找到Caused by！";
		}
		if (string.substring(index).length() > 200) {
			return string.substring(index, index + 200);
		} else {
			return string.substring(index);
		}
	}

	/**
	 * 从信息中找到=，截取时间
	 * 
	 * @param string
	 *            内容
	 * @return String 截取到的时间
	 */
	public static String initTime(String string) {
		int index = string.indexOf("=");
		string = string.substring(index + 1, index + 14);
		return string;
	}

	/**
	 * 读取文件
	 * 
	 * @param path
	 *            路径
	 * @return String 内容
	 */
	public static String readFile(String path) {
		String contentString = "";
		try {
			InputStream inStream = new FileInputStream(path);
			if (inStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(
						inStream, "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					contentString += line + "\n";
				}
				inStream.close();
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return contentString;
	}

	public long getLen() {
		return len;
	}

	public void setLen(long len) {
		this.len = len;
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 *            文件路径与名称
	 * @return File 创建的文件
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	/**
	 * 删除SD卡上的文件
	 * 
	 */
	public void deleteFile(File file) {
		// 判断文件是否存在
		if (file.exists()) {
			// 如果是文件
			if (file.isFile()) {
				file.delete();
			}
			// 如果是目录
			else if (file.isDirectory()) {
				// 获取目录下所有文件
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
		}
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 *            目录路径与名称
	 * @return File 创建的目录
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 * 
	 * @param fileName
	 *            文件夹路径
	 * @return boolean 是否存在
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @param path
	 *            路径
	 * @param fileName
	 *            文件名
	 * @param input
	 */

	public synchronized File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);

			byte buffer[] = new byte[1024];
			int temp = 0;

			while ((temp = input.read(buffer)) != -1) {
				output.write(buffer, 0, temp);
				len = len + temp;
			}

			output.flush();
			return file;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 写入文件内容
	 * 
	 * @param path
	 *            地址
	 * @param fileName
	 *            文件名
	 * @param string
	 *            内容
	 * @return File 写入的文件
	 */
	public File write2SDFromString2(String path, String fileName, String string) {
		File file = null;
		byte[] bytes = string.getBytes();
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);

			output = new FileOutputStream(file, true);
			output.write(bytes);
			output.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return file;
	}

	/**
	 * 写入文件内容（会覆盖）
	 * 
	 * @param path
	 *            地址
	 * @param fileName
	 *            文件名
	 * @param string
	 *            内容
	 * @return File 写入的文件
	 */
	public File write2SDFromString(String path, String fileName, String string) {
		File file = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);

			FileWriter fw = new FileWriter(file);
			BufferedWriter buffw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(buffw);

			pw.println(string);

			pw.close();
			buffw.close();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return file;
	}

	/**
	 * 写入文件内容（会覆盖）
	 * 
	 * @param path
	 *            地址
	 * @param fileName
	 *            文件名
	 * @param string
	 *            内容
	 * @return File 写入的文件
	 */
	public File write2SDFromString3(String path, String fileName, String string) {
		File file = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);
//			FileWriter fw = new FileWriter(file);
//			BufferedWriter buffw
			Writer out= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"GBK"));
			PrintWriter pw = new PrintWriter(out);
			
			pw.println(string);

			pw.close();
			out.close();
//			buffw.close();
//			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return file;
	}
	
	/**
	 * 读取assets下的文件
	 * 
	 * @param fileName
	 *            文件路径与名称
	 * @param context
	 *            上下文
	 * @return String 内容
	 */
	public String readAssetsFile(String fileName, Context context) {
		String fileStr = "";
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			in.close();
			fileStr = new String(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileStr;
	}

	/**
	 * 遍历内存卡下的文件
	 * 
	 * @param file
	 *            文件夹路径
	 * @param key
	 *            类型文件 如.txt
	 * @return
	 */
	public List<File> search(String path, String key) {
		List<File> listFiles = new ArrayList<File>();
		File file = new File(path);
		try {
			File[] files = file.listFiles();
			if (files == null) {
				return null;
			}
			if (files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					if (!files[i].isDirectory()) {
						if (files[i].getName().indexOf(key) != -1) {
							listFiles.add(files[i]);
						}
					}
				}
			}
			return listFiles;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listFiles;
	}
	/**
	 * 遍历内存卡下的文件
	 * 
	 * @param file
	 *            文件夹路径
	 * @param key
	 *            类型文件 如.txt
	 * @return
	 */
	public String[] searchDir(String path) {
		File file = new File(path);
		String[] files = file.list();
		try {
			
			if (files == null) {
				return null;
			}
			if (files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					if (new File(files[i]).isFile()) {
						System.out.println("文件夹下的文件");
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return files;
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public boolean DeleteFolder(String sPath) {
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return false;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String sPath) {
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			return true;
		}
		return false;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				
				if (!deleteFile(files[i].getAbsolutePath()))
					break;
			} // 删除子目录
			else {
				if (!deleteDirectory(files[i].getAbsolutePath()))
					break;
			}
		}
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
}