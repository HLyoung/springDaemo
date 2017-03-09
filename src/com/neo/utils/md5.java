
package com.neo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.sun.corba.se.impl.ior.ByteBuffer;


public class md5{
	protected static char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	protected static MessageDigest messagedigest = null;
	static {
		try{
			messagedigest = MessageDigest.getInstance("MD5");
		}catch(NoSuchAlgorithmException nsaex){
			System.err.println(md5.class.getName() + "initialize failed");
			nsaex.printStackTrace();
		}
	}
	
	public static String getFileMD5String(File file) throws IOException{
		FileInputStream in = new FileInputStream(file);
	
		DigestInputStream digestInputStream = new DigestInputStream(in, messagedigest);
		byte[] buffer = new byte[256*1024];
		while(digestInputStream.read(buffer) > 0);
		
		messagedigest = digestInputStream.getMessageDigest();
		byte[] ret = messagedigest.digest();
		in.close();
		return bufferToHex(ret);
	}
	
	public static String getMD5String(String s){
		return getMD5String(s.getBytes());
	}
	
	public static String getMD5String(byte[] bytes){
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}
	
	private static String bufferToHex(byte bytes[]){
		return bufferToHex(bytes,0,bytes.length);
	}
	
	private static String bufferToHex(byte bytes[],int m,int n){
		StringBuffer stringbuffer = new StringBuffer(2*n);
		for(int i=m;i<m+n;i++){
			appendHexPair(bytes[i],stringbuffer);
		}
		return stringbuffer.toString();
	}
	
	public static void appendHexPair(byte bt,StringBuffer stringbuffer){
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0x0f];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
}
	
	