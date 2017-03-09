
package com.neo.db;

import java.sql.SQLException;
import java.sql.Types;
import javax.annotation.Resource;

import java.util.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;


@Repository("jdbcDataDao")
public class JdbcUpdateDaoImpl implements JdbcUpdateDao
{
	@Resource
	private JdbcTemplate jdbcTemplate;
	public void setJdbcTemplate(JdbcTemplate jdbctemplate){
		this.jdbcTemplate = jdbctemplate;
	}
	
	@Override
	public int addVersion(final String version,final String path,final String md5,long size,String notes){
		String linuxPath = path.replace("\\","/");    //all sava as linux path.
		String sql = "insert into wis_update_manage values (?,?,CURRENT_TIMESTAMP,?,?,?)";
		Object[] param = {version,linuxPath,md5,size,notes};
		int[] types={Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.BIGINT,Types.VARCHAR};
		try{
			jdbcTemplate.update(sql,param,types);
		}catch(org.springframework.dao.DuplicateKeyException e)
		{
			return 2;
		}catch(Exception e){
			return -1;
		}
		return 0;
	}
	@Override
	public void deleteVersion(final String version){
		String sql = "delete from wis_update_manage where version=?";
		Object[] param = {version};
		int[] types={Types.VARCHAR};
		jdbcTemplate.update(sql,param,types);
		
	}
	
	@Override
	public String getPath(final String version){
		return getStringByVersion(version,"path");
	}
	
	@Override
	public String getMD5(final String version){
		return getStringByVersion(version,"md5");
	}
	@Override
	public String getTime(final String version){
		return getStringByVersion(version,"time");
	}
	@Override
	public String getNotes(final String version){
		return getStringByVersion(version,"notes");
	}
	@Override
	public long getSize(final String version){
		return getLongByVersion(version,"size");
	}
	
	@Override
	public List<versionMessage> getAllVersion()
	{
		final List<versionMessage>  list = new ArrayList<versionMessage>();
		jdbcTemplate.query(new PreparedStatementCreator(){
			@Override
			public java.sql.PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				String sql = "select version,path,time,md5,size,notes from wis_update_manage";
				java.sql.PreparedStatement ps = con.prepareStatement(sql);
				return ps;
			}
		},new RowCallbackHandler(){
			@Override
			public void processRow(java.sql.ResultSet rs) throws SQLException {
				versionMessage oVer = new versionMessage();
				oVer.setVersion(rs.getString("version"));
				oVer.setVersionId(rs.getString("version"));
				oVer.setMD5(rs.getString("md5"));
				oVer.setFileSize(rs.getInt("size"));
				oVer.setUploadTime(rs.getString("time"));
				oVer.setUrl(rs.getString("path"));
				oVer.setNotes(rs.getString("notes"));
				list.add(oVer);
			}
		});
		return list;
	}
	
	public String getStringByVersion(final String version,final String param){
		final List<String>  list = new ArrayList<String>();
		jdbcTemplate.query(new PreparedStatementCreator(){
			@Override
			public java.sql.PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException{
				String sql = "select * from wis_update_manage where version=?";
				java.sql.PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, version);
				return ps;
			}
		}, new RowCallbackHandler(){
			@Override
			public void processRow(java.sql.ResultSet rs) throws SQLException{
				System.out.println(rs.getString(param));
				list.add(rs.getString(param));
			}
		});
		return list.size()>0?list.get(0):null;
	}
	
	public long getLongByVersion(final String version,final String param){
		final List<Long>  list = new ArrayList<Long>();
		jdbcTemplate.query(new PreparedStatementCreator(){
			@Override
			public java.sql.PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException{
				String sql = "select * from wis_update_manage where version=?";
				java.sql.PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, version);
				return ps;
			}
		}, new RowCallbackHandler(){
			@Override
			public void processRow(java.sql.ResultSet rs) throws SQLException{
				System.out.println(rs.getString(param));
				list.add(rs.getLong(param));
			}
		});
		return list.size()>0?list.get(0):null;
	}
}