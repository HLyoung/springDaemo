
package com.neo.utils;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESedeUtils {
	
	public static final String Key 		= "123456789012345678901234";
	public static final String IV  		= "01234567";
	public static final String Algo 	= "DESede";
	public static final String Padding 	= "DESede/CBC/PKCS5Padding";

	public static String encrypt( String plainText ) throws Exception {
		Key deskey = null;
		byte[] originKey = Key.getBytes();
		byte[] keyBytes = new byte[DESedeKeySpec.DES_EDE_KEY_LEN];
		
		for( int i = 0; i < originKey.length && i < DESedeKeySpec.DES_EDE_KEY_LEN; i++ ) {
			keyBytes[i] = originKey[i];
		}			
		DESedeKeySpec spec = new DESedeKeySpec(keyBytes);
		deskey = SecretKeyFactory.getInstance(Algo).generateSecret(spec); 
        Cipher cipher = Cipher.getInstance( Padding );  
        
        cipher.init(Cipher.ENCRYPT_MODE, deskey, new IvParameterSpec(IV.getBytes()));  
        byte[] encryptData = cipher.doFinal( plainText.getBytes() );  
        return Base64.encode(encryptData);
	}  
}
