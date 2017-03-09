

package com.neo.db;

public class versionMessage{
	private String _version;
	private int    _verId;
	private String _url;
	private int    _fileSize;
	private String _md5;
	private String _uploadTime;
	private String _notes;
	
	
	
	public void setVersion(String version){
		this._version = version;
	}
	public void setVersionId(String version){
		System.out.println(version);
		int mIndex = version.indexOf('.');
		int nIndex = version.lastIndexOf('.');
		if(nIndex > mIndex){
			if(version.substring(mIndex+1,nIndex-1).indexOf('.') == -1){			
				int major = Integer.parseInt((version.substring(0, mIndex)));	
				int minor = Integer.parseInt(version.substring(mIndex+1,nIndex));
				int patch = Integer.parseInt(version.substring(nIndex+1,version.length()));
				this._verId = major<<24 | minor<<16 | patch;
			}
		}
		
	}
	public void setUrl(String url){
		this._url = url;
	}
	public void setFileSize(int fileSize){
		this._fileSize = fileSize;
	}
	public void setMD5(String md5){
		this._md5 = md5;
	}
	public void setUploadTime(String uploadTime){
		this._uploadTime = uploadTime;
	}
	public void setNotes(String explain){
		this._notes = explain;
	}
	
	
	
	public String getVersion(){
		return _version;
	}
	public int getVersinId(){
		return _verId;
	}
	public String getUrl(){
		return _url;
	}
	public int getFileSize(){
		return _fileSize;
	}
	public String getMD5(){
		return _md5;
	}
	public String getUploadTime(){
		return _uploadTime;
	}
	public String getNotes(){
		return _notes;
	}
	
}