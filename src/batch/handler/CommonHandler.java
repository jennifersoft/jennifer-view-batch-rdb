package batch.handler;

import batch.base.IDataLegacy;
import batch.base.IDatabaseLegacy;
import batch.util.DBUtility;
import com.aries.view.extension.handler.Batch;

public abstract class CommonHandler implements Batch, IDataLegacy {
    protected boolean initHandler(String tableName) {
        if(DBUtility.checkDBConnection(getExtensionId(), getDatabaseInfo().getDriverName())) {
            boolean isOK = true;

            if(getDatabaseInfo().existTable(tableName)) {
                getDatabaseInfo().resetTable(tableName);
            } else {
                isOK = createTable();
            }

            return isOK;
        }

        return false;
    }

    protected abstract IDatabaseLegacy getDatabaseInfo();
}
