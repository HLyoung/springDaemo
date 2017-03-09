

package com.neo.utils;
import java.io.*;

public class fileHandler{
	public static String getVersionFromFile(String file) throws IOException{
		System.out.println(file);
		FileReader fr = new FileReader(file);
		
		BufferedReader  br = new BufferedReader(fr);
		String line = null;
		String [] array = null;
		while((line = br.readLine()) != null){
			array = line.split(" ");
			if(array[0].compareTo("Version:") == 0 || array[0].compareTo("version:") == 0)
				break;
		}
		br.close();
		fr.close();
		System.out.println(String.valueOf(array.length));
		return array.length>=2?array[1]:null;
	}
	
	public static boolean createFile(String sfile ) throws IOException{
		if(sfile.endsWith(File.separator))
			return false;
		File file = new File(sfile); 
		if(file.exists())
			return true;
		if(!file.getParentFile().exists())
			if(!file.getParentFile().mkdirs())
				return false;
		try{
			if(file.createNewFile())
				return true;
			else
				return false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	public static boolean createDir(String destDirName){
		File dir = new File(destDirName);
		if(dir.exists())
			return true;
		if(!destDirName.endsWith(File.separator))
			destDirName = destDirName + File.separator;
		if(dir.mkdirs())
			return true;
		else
			return false;
	}
	
	public static String getExtensinName(String filename){
		if((filename != null) && (filename.length() > 0)){
			int dot = filename.lastIndexOf('.');
			if((dot > -1) && (dot <(filename.length() -1))){
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}
	
	public static String getFileNameNoEx(String filename){
		if((filename != null ) && (filename.length() >0)){
			int dot = filename.lastIndexOf('.');
			if((dot > -1) && (dot < (filename.length()))){
				return filename.substring(0,dot);
			}
		}
		return filename;
	}
	
	public static String getPakNameNoEx(String pakName){
		if((pakName != null) && (pakName.length() >0)){
			int dot = pakName.indexOf('.');
			if((dot > -1) && (dot < pakName.length())){
				return pakName.substring(0,dot);
			}
		}
		return pakName;
	}
	
	public static void deleteFile(File file){
		if(file.exists()){
			if(file.isFile())
				file.delete();
			else if(file.isDirectory()){
				File[] files = file.listFiles();
				for(int i=0;i<files.length;i++){
					deleteFile(files[i]);
				}
			}
			file.delete();
		}
	}
	
	public static String getVersionFromGzFile(String sfile) throws Exception{
		String version = null;
		if(createDir("./tmp/")){
			if(GZip.unArFile(sfile, "./tmp/")){
				if(GZip.unZipFile("./tmp/control.tar.gz", "./tmp")){
					String path = "./tmp/" + "/control";
					File control = new File(path);
					if(control.exists())		
						version = getVersionFromFile(path);
				}
			}
		}
		File fTmp = new File("./tmp/");
		deleteFile(fTmp);
		return version;	
	}
}