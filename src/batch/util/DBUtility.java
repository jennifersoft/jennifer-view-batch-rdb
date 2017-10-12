package batch.util;

import com.aries.view.extension.util.LogUtil;
import com.aries.view.extension.util.PropertyUtil;

import java.sql.*;

public class DBUtility {
    public static Connection getDBConnection(String extensionId, String driverName) {
        String url = PropertyUtil.getValue(extensionId, "url");
        String user = PropertyUtil.getValue(extensionId, "user");
        String password = PropertyUtil.getValue(extensionId, "password");

        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            LogUtil.error(e.getMessage());
        }

        try {
            if(url != null && user != null && password != null) {
                return DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            LogUtil.error(e.getMessage());
        }

        return null;
    }

    public static boolean updateSimpleQuery(String extensionId, String driverName, String query) {
        Connection dbConnection = getDBConnection(extensionId, driverName);
        Statement statement = null;

        try {
            statement = dbConnection.createStatement();
            statement.execute(query);

            return true;
        } catch (SQLException e) {
            LogUtil.error(e.getMessage());
        } finally {
            DBUtility.finallyDBConnection(dbConnection, statement);
        }

        return false;
    }

    public static void finallyDBConnection(Connection dbConnection, Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LogUtil.error(e.getMessage());
            }
        }

        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException e) {
                LogUtil.error(e.getMessage());
            }
        }
    }

    public static String createInsertQuery(String tableName, int columnCount) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < columnCount; i++) {
            if(i > 0) sb.append(",");
            sb.append("?");
        }

        return "INSERT INTO " + tableName + " VALUES(" + sb.toString() + ")";
    }
}
