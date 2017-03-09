
package com.neo.db;

import java.util.List;

public interface JdbcUpdateDao
{
	public int addVersion(String version,String path,String md5,long size,String notes);
	public void deleteVersion(String version);
	public List<versionMessage> getAllVersion();
	public String getPath(String version);
	public String getTime(String version);
	public String getMD5(String version);
	public String getNotes(String version);
	public long getSize(String size);
}