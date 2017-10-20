package batch.database;

import batch.base.IDatabaseLegacy;
import batch.util.DBUtility;
import com.aries.view.extension.util.LogUtil;

public class MysqlInfo implements IDatabaseLegacy {
    private String extensionId;

    public MysqlInfo(String extensionId) {
        this.extensionId = extensionId;
    }

    @Override
    public String getDriverName() {
        return "com.mysql.jdbc.Driver";
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
    public boolean resetTable(String tableName) {
        boolean isOK = DBUtility.updateQuery(extensionId, getDriverName(), "TRUNCATE TABLE " + tableName);
        if(isOK) LogUtil.info("Table \"" + tableName +  "\" has been reset!");

        return isOK;
    }
}
