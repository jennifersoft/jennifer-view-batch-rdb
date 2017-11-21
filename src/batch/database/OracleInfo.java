package batch.database;

import batch.base.IDatabaseLegacy;
import batch.util.DBUtility;
import com.aries.view.extension.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OracleInfo implements IDatabaseLegacy {
    private String extensionId;

    public OracleInfo(String extensionId) {
        this.extensionId = extensionId;
    }

    @Override
    public String getDriverName() {
        return "oracle.jdbc.driver.OracleDriver";
    }

    @Override
    public String getNumericColumn() {
        return "NUMBER";
    }

    @Override
    public String getTimestampColumn() {
        return "TIMESTAMP";
    }

    @Override
    public boolean existTable(String tableName) {
        String query = "SELECT COUNT(*) FROM ALL_TABLES WHERE TABLE_NAME = '" + tableName + "'";
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
                    "DELETE FROM " + tableName + " WHERE TO_CHAR(STANDARD_TIME, 'YYYYMMDD')='" + sdf.format(new Date(batchTime)) + "'");
        }

        return isOK;
    }
}
