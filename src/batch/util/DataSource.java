package batch.util;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.aries.view.extension.util.PropertyUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSource {
    private static DataSource datasource;
    private static String driverClass;

    private ComboPooledDataSource cpds;

    private DataSource(String extensionId, String driverName) throws IOException, SQLException, PropertyVetoException {
        String url = PropertyUtil.getValue(extensionId, "url");
        String user = PropertyUtil.getValue(extensionId, "user");
        String password = PropertyUtil.getValue(extensionId, "password");

        int minPoolSize = Integer.parseInt(PropertyUtil.getValue(extensionId, "min_pool_size", "5"));
        int acquireIncrement = Integer.parseInt(PropertyUtil.getValue(extensionId, "acquire_increment", "5"));
        int maxPoolSize = Integer.parseInt(PropertyUtil.getValue(extensionId, "max_pool_size", "20"));
        int maxStatements = Integer.parseInt(PropertyUtil.getValue(extensionId, "max_statements", "180"));

        cpds = new ComboPooledDataSource();
        cpds.setDriverClass(driverName); //loads the jdbc driver
        cpds.setJdbcUrl(url);
        cpds.setUser(user);
        cpds.setPassword(password);

        // the settings below are optional -- c3p0 can work with defaults
        cpds.setMinPoolSize(minPoolSize);
        cpds.setAcquireIncrement(acquireIncrement);
        cpds.setMaxPoolSize(maxPoolSize);
        cpds.setMaxStatements(maxStatements);
    }

    public static DataSource getInstance(String extensionId, String newDriverClass) throws IOException, SQLException, PropertyVetoException {
        if (datasource == null || driverClass != newDriverClass) {
            driverClass = newDriverClass;
            datasource = new DataSource(extensionId, driverClass);
            return datasource;
        } else {
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }
}