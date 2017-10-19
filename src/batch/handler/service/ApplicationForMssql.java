package batch.handler.service;

import batch.base.IDatabaseLegacy;
import batch.database.MssqlInfo;

public class ApplicationForMssql extends ApplicationForBase {
    @Override
    protected IDatabaseLegacy getDatabaseInfo() {
        return new MssqlInfo(getExtensionId());
    }
}
