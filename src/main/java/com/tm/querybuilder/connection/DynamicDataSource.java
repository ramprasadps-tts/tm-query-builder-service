package com.tm.querybuilder.connection;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class DynamicDataSource  implements DataSource{

    private String jdbcUrl;
    private String username;
    private String password;

    public DynamicDataSource(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		//comment
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		//null
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

    // Other overridden methods (not implemented for simplicity)
    // You may need to implement the other methods of the DataSource interface
    // depending on your specific use case.
}