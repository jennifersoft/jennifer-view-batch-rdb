package batch.database;

import batch.base.IDatabaseLegacy;
import batch.util.DBUtility;
import com.aries.view.extension.util.LogUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MssqlInfo implements IDatabaseLegacy {
    private String extensionId;

    public MssqlInfo(String extensionId) {
        this.extensionId = extensionId;
    }

    @Override
    public String getDriverName() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    @Override
    public String getNumericColumn() {
        return "NUMERIC";
    }

    @Override
    public String getTimestampColumn() {
        return "DATETIME";
    }

    @Override
    public boolean existTable(String tableName) {
        Connection dbConnection = DBUtility.getDBConnection(extensionId, getDriverName());
        Statement statement = null;

        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + tableName + "'";
        int rowcount = 0;

        try {
            statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            if(rs.next()) {
                rowcount = rs.getInt(1);
            }

            if(rowcount == 1) {
                return true;
            }
        } catch (SQLException e) {
            LogUtil.error(e.getMessage());
        } finally {
            DBUtility.finallyDBConnection(dbConnection, statement);
        }

        return false;
    }

    @Override
    public boolean resetTable(String tableName) {
        boolean isOK = DBUtility.updateSimpleQuery(extensionId, getDriverName(), "TRUNCATE TABLE " + tableName);
        if(isOK) LogUtil.info("Table \"" + tableName +  "\" has been reset!");

        return isOK;
    }
}
