

package com.neo.db;

public interface JdbcUserDao{
	public boolean checkUser(final String useId);
	public boolean resetPassword(final String useId,final String password);
}