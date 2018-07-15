package batch.util;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.aries.extension.util.PropertyUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSource {
    private static DataSource datasource;
    private static String driverClass;
    private static String userKey;

    private ComboPooledDataSource cpds;

    private DataSource(String extensionId, String driverName) throws IOException, SQLException, PropertyVetoException {
        int minPoolSize = Integer.parseInt(PropertyUtil.getValue(extensionId, "min_pool_size", "5"));
        int acquireIncrement = Integer.parseInt(PropertyUtil.getValue(extensionId, "acquire_increment", "5"));
        int maxPoolSize = Integer.parseInt(PropertyUtil.getValue(extensionId, "max_pool_size", "20"));
        int maxStatements = Integer.parseInt(PropertyUtil.getValue(extensionId, "max_statements", "180"));

        cpds = new ComboPooledDataSource();
        cpds.setDriverClass(driverName); //loads the jdbc driver
        cpds.setJdbcUrl(PropertyUtil.getValue(extensionId, "url"));
        cpds.setUser(PropertyUtil.getValue(extensionId, "user"));
        cpds.setPassword(PropertyUtil.getValue(extensionId, "password"));

        // the settings below are optional -- c3p0 can work with defaults
        cpds.setMinPoolSize(minPoolSize);
        cpds.setAcquireIncrement(acquireIncrement);
        cpds.setMaxPoolSize(maxPoolSize);
        cpds.setMaxStatements(maxStatements);
    }

    public static DataSource getInstance(String extensionId, String newDriverClass) throws IOException, SQLException, PropertyVetoException {
        String newUserKey = createUserKey(extensionId);

        if (datasource == null || !driverClass.equals(newDriverClass) || !userKey.equals(newUserKey)) {
            driverClass = newDriverClass;
            userKey = newUserKey;
            datasource = new DataSource(extensionId, driverClass);

            return datasource;
        } else {
            return datasource;
        }
    }

    private static String createUserKey(String extensionId) {
        return PropertyUtil.getValue(extensionId, "url") +
            PropertyUtil.getValue(extensionId, "user") +
            PropertyUtil.getValue(extensionId, "password");
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }
}