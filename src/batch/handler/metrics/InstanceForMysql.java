package batch.handler.metrics;

import batch.base.IDatabaseLegacy;
import batch.database.MysqlInfo;

public class InstanceForMysql extends InstanceForBase {
    @Override
    protected IDatabaseLegacy getDatabaseInfo() {
        return new MysqlInfo(getExtensionId());
    }
}
