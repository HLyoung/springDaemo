package com.neo.utils;

import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

/**
	uncompress *tar.gz
 *
 */
	
public class GZip{
	
	public static Boolean unZipFile(String zipfileName,String outputDirectory){
		try{
			FileInputStream fis = new FileInputStream(zipfileName);
			GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(fis));
			ArchiveInputStream aIs = new ArchiveStreamFactory().createArchiveInputStream("tar",is);
			return analyStream(aIs, outputDirectory);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	public static  Boolean unArFile(String zipfileName,String outputDirectory){
		try{
			FileInputStream fis = new FileInputStream(zipfileName);
			ArArchiveInputStream in = new ArArchiveInputStream(new BufferedInputStream(fis));
			return analyStream(in,outputDirectory);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	
	private static void mkFolder(String fileName){	
		File f = new File(fileName);
		String parent = f.getParent();
		File fParent = new File(parent);
		if(!fParent.exists()){
			f.mkdirs();
		}else{
			f.mkdir();
		}
	}
	
	private static  File mkFile(String fileName){
		File f = new File(fileName);
		String parent = f.getParent();
		File fParent = new File(parent);
		if(!fParent.exists()){
			mkFolder(parent);
		}
		try{
			f.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
		return f;
	}
	
	private static  Boolean analyStream(ArchiveInputStream aIs,String outputDirectory){
		BufferedInputStream bufferedInputStream = new BufferedInputStream(aIs);
		try{
			ArchiveEntry entry = aIs.getNextEntry();
			while(entry != null){
				String name = entry.getName();
				if(name.endsWith("/"))
					mkFolder(outputDirectory + File.separator + name.replace("/", File.separator));
				else{
					File file = mkFile(outputDirectory + File.separator + name.replace("/", File.separator));
					BufferedOutputStream  bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
					int b;
					while((b = bufferedInputStream.read()) != -1){
						bufferedOutputStream.write(b);		
					}
					bufferedOutputStream.flush();
					bufferedOutputStream.close();
				}
				entry = aIs.getNextEntry();
			}	
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}finally{
			try{
				aIs.close();
				bufferedInputStream.close();
			}catch(IOException ex){
				ex.printStackTrace();
				return false;
			}
		}
		return true;
	}
}	
