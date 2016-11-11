package com.koch.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


/**
 * 工具类 - 文件处理
 */

public class FileUtil {
	public static void delete(String path){
		if(path != null && !path.equals("")){
			File file = new File(path);
			if(file.exists()){
				file.delete();
			}
		}
	}
}