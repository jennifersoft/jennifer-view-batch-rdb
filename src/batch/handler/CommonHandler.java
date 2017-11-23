package batch.handler;

import batch.base.IDataLegacy;
import batch.base.IDatabaseLegacy;
import batch.util.DBUtility;
import com.aries.view.extension.handler.Batch;
import com.aries.view.extension.util.LogUtil;
import com.aries.view.extension.util.PropertyUtil;

import java.sql.Date;
import java.text.SimpleDateFormat;

public abstract class CommonHandler implements Batch, IDataLegacy {
    protected boolean initHandler(long batchTime, String tableName) {
        if(DBUtility.checkDBConnection(getExtensionId(), getDatabaseInfo().getDriverName())) {
            boolean isOK = getDatabaseInfo().existTable(tableName);

            if(isOK) {
                isOK = getDatabaseInfo().resetTable(tableName, (!isUniqueTable() ? -1 : batchTime));

                if(isOK) LogUtil.info("Table \"" + tableName +  "\" has been reset!");
                else LogUtil.info("Table reset failed!");
            } else {
                isOK = createTable();

                if(isOK) {
                    LogUtil.info("Table \"" + tableName + "\" is created!");

                    if(getDatabaseInfo().createIndex(tableName)) {
                        LogUtil.info("Index \"" + tableName + "\" is created!");
                    }
                }
                else LogUtil.info("Table creation failed!");
            }

            return isOK;
        }

        return false;
    }

    protected String createTableName(long batchTime, String optTableName) {
        String tableName = PropertyUtil.getValue(getExtensionId(), "table_name", optTableName);

        if(!isUniqueTable()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
            tableName += "_" + sdf.format(new Date(batchTime));
        }

        return tableName;
    }

    private boolean isUniqueTable() {
        return Boolean.parseBoolean(PropertyUtil.getValue(getExtensionId(), "unique_table", "false"));
    }

    protected abstract IDatabaseLegacy getDatabaseInfo();
}
