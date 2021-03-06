package batch.database;

import batch.base.IDatabaseLegacy;
import batch.util.DBUtility;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        String query = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + tableName + "'";
        int count = DBUtility.getCountQuery(extensionId, getDriverName(), query);

        return count == 1;
    }

    @Override
    public boolean resetTable(String tableName, long batchTime) {
        boolean isOK;

        if(batchTime == -1) {
            isOK = DBUtility.updateQuery(extensionId, getDriverName(), "TRUNCATE TABLE " + tableName);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            isOK = DBUtility.updateQuery(extensionId, getDriverName(),
                    "DELETE FROM " + tableName + " WHERE '" + sdf.format(new Date(batchTime)) + "'=CONVERT(CHAR(8), STANDARD_TIME, 112)");
        }

        return isOK;
    }

    @Override
    public boolean createIndex(String tableName) {
        return false;
    }
}
