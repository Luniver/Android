package hrst.sczd.utils;
/**
 * 
 * 获取制定目录下所有制定的文件
 */
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class SelectFilesManager {
private Context context;
private static SelectFilesManager model;

private SelectFilesManager(Context context) {
	this.context = context;

}

public static SelectFilesManager getInstance(Context context) {
	if (model == null) {
		model = new SelectFilesManager(context);
	}
	return model;
}

public String[] getFileArray(String url,String key) { 
    List<File> listFiles= new ArrayList<File>();
  	 File[] files= new File(url).listFiles(); 
      
    if (files==null) {
		return null;
	}
    if (files.length>0) {
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory()) {
				if (files[i].getName().indexOf(key)!=-1) {
					listFiles.add(files[i]);
				}
			}
		}
	} 
      int listSize = listFiles.size();  
      if (listSize == 0) {  
          return null;  
      }  
      String[] res = new String[listSize];    //  将mp3文件路径赋给字符串数组，然后返回它    
      for (int i = 0; i < listSize; i++) {  
          res[i] = url + listFiles.get(i).getName();  
      }  
      return res;  
  }  
}
