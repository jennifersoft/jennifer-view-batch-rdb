package batch.util;

import com.aries.view.extension.util.LogUtil;
import com.aries.view.extension.util.PropertyUtil;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;

public class DBUtility {
    public static boolean checkDBConnection(String extensionId, String driverName) {
        Boolean isOK = true;
        Connection connection = null;

        try {
            Class.forName(driverName);

            connection = DriverManager.getConnection(
                    PropertyUtil.getValue(extensionId, "url"),
                    PropertyUtil.getValue(extensionId, "user"),
                    PropertyUtil.getValue(extensionId, "password")
            );
        } catch (ClassNotFoundException e) {
            LogUtil.error(e.getMessage());
            isOK = false;
        } catch (SQLException e) {
            LogUtil.error(e.getMessage());
            isOK = false;
        } finally {
            try {
                if(connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
               LogUtil.error(e.getMessage());
            }
        }

        return isOK;
    }

    public static Connection getDBConnection(String extensionId, String driverName) {
        try {
            return DataSource.getInstance(extensionId, driverName).getConnection();
        } catch (SQLException e) {
            LogUtil.error(e.getMessage());
        } catch (IOException e) {
            LogUtil.error(e.getMessage());
        } catch (PropertyVetoException e) {
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
            if(dbConnection != null) {
                try {
                    dbConnection.rollback();
                } catch (SQLException e1) {
                    LogUtil.error(e.getMessage());
                }
            }

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
        for (int i = 0; i < columnCount; i++) {
            if (i > 0) sb.append(",");
            sb.append("?");
        }

        return "INSERT INTO " + tableName + " VALUES(" + sb.toString() + ")";
    }
}