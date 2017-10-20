package batch.handler.metrics;

import batch.base.IDatabaseLegacy;
import batch.database.MysqlInfo;

public class DomainForMysql extends DomainForBase {
    @Override
    protected IDatabaseLegacy getDatabaseInfo() {
        return new MysqlInfo(getExtensionId());
    }
}
