package batch.handler.metrics;

import batch.base.IDatabaseLegacy;
import batch.database.MysqlInfo;

public class BusinessForMysql extends BusinessForBase {
    @Override
    protected IDatabaseLegacy getDatabaseInfo() {
        return new MysqlInfo(getExtensionId());
    }
}
