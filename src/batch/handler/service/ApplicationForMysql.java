package batch.handler.service;

import batch.base.IDatabaseLegacy;
import batch.database.MysqlInfo;

public class ApplicationForMysql extends ApplicationForBase {
    @Override
    protected IDatabaseLegacy getDatabaseInfo() {
        return new MysqlInfo(getExtensionId());
    }
}
