
package com.neo.db;

import java.sql.SQLException;
import java.sql.Types;
import javax.annotation.Resource;

import java.util.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

@Repository("JdbcUserDaoImpl")
public class JdbcUserDaoImpl implements JdbcUserDao{
	@Resource
	private JdbcTemplate jdbctemplate;

	public void setJdbcTemplate(JdbcTemplate jdbctemplate){
		this.jdbctemplate = jdbctemplate;
	}
	
	@Override
	public boolean checkUser(final String useId){
		
		System.out.println("checkUser useId:" + useId);
		final List<String> list = new ArrayList<String>();
		jdbctemplate.query(new PreparedStatementCreator(){
			@Override
			public java.sql.PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				String sql = "select * from wis_user_tbl where name=?";
				java.sql.PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, useId);
				return ps;
			}
		},new RowCallbackHandler(){
			@Override
			public void processRow(java.sql.ResultSet rs) throws SQLException {
				list.add(rs.getString("name"));
				System.out.println(rs.getString("name") + " exists");
			}
		});
		return list.size()>0?true:false;
	}
	
	@Override
	public boolean resetPassword(final String useId,final String password){
		if(checkUser(useId)){
			System.out.println("reset password useId:" + useId + " password:" + password );
		    String sql = "update wis_user_tbl set password=? where name=?";
			Object[] param = {password,useId};
			int[] types = {Types.VARCHAR,Types.VARCHAR};
			try{
				jdbctemplate.update(sql,param,types);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
		}else
			return false;
	}
}