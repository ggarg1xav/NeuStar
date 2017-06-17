package chbasic.utils;

/** This class is to be used for 
 * Initializing/Destroying DB Connection pools and Getting the DB connection
 */
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConnectionSource {

	private static Map<String, DBConnectionSource> dsMap = new HashMap<String, DBConnectionSource>();
	private ComboPooledDataSource cpds = null;
	Properties prop = new Properties();
	String dbFile = "resources/db.properties";

	// private static Logger LOG =Logger.getLogger(DBConnectionSource.class);

	private DBConnectionSource(String dataSourceName) throws IOException, SQLException, PropertyVetoException {
		cpds = new ComboPooledDataSource();
		prop = ReadProperties.loadProperties(dbFile);

		// LOG.info("Initializing datasource {} ", dataSourceName);
		
		// LOG.info(" with properties {}/{}/{}", jdbcUrl, user, password);
		cpds.setDriverClass(prop.getProperty(dataSourceName + "_DRIVER"));
		cpds.setJdbcUrl(prop.getProperty(dataSourceName + "_JDBCURL"));
		cpds.setUser(prop.getProperty(dataSourceName + "_USER"));
		cpds.setPassword(prop.getProperty(dataSourceName + "_PASSWORD"));

		// the settings below are optional -- c3p0 can work with defaults
		cpds.setMinPoolSize(Integer.parseInt(prop.getProperty("MIN_POOL_SIZE")));
		cpds.setAcquireIncrement(Integer.parseInt(prop.getProperty("ACQUIRE_INCREMENT")));
		cpds.setMaxIdleTime(Integer.parseInt(prop.getProperty("MAX_IDLE_TIME")));
		cpds.setIdleConnectionTestPeriod(Integer.parseInt(prop.getProperty("IDLE_CONNECTION_TEST_PERIOD")));
		cpds.setMaxIdleTimeExcessConnections(Integer.parseInt(prop.getProperty("IDLE_TIME_EXCESS_CONNECTIONS")));
		cpds.setMaxPoolSize(Integer.parseInt(prop.getProperty("MAX_POOL_SIZE")));
		cpds.setMaxStatements(Integer.parseInt(prop.getProperty("MAX_STATEMENTS")));
		cpds.setUnreturnedConnectionTimeout(Integer.parseInt(prop.getProperty("UNRETURNED_CONNECTION_TIME"))); //10 min
	}

	/**
	 * Returns the DB connection from connection pool
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		return cpds.getConnection();
	}

	/**
	 * Static method for initializing DB Connection pool
	 * 
	 * @param dataSourceName
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 * @throws PropertyVetoException
	 */
	public static DBConnectionSource getInstance(String dataSourceName)
			throws IOException, SQLException, PropertyVetoException {
		DBConnectionSource dataSource = dsMap.get(dataSourceName);
		if (dataSource == null) {
			synchronized (DBConnectionSource.class) {
				if (dataSource == null) {
					dataSource = new DBConnectionSource(dataSourceName);
					dsMap.put(dataSourceName, dataSource);

				}
			}
		}

		return dataSource;

	}

	@SuppressWarnings("static-access")
	/**
	 * Mehtod for destroying Connection pool
	 * 
	 */
	public static void destroy() {
		for (DBConnectionSource ds : dsMap.values()) {
			ds.destroy();
		}
	}
}
