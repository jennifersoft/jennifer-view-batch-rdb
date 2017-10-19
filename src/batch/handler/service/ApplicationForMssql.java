package batch.handler.service;

import batch.base.IDatabaseLegacy;
import batch.database.MssqlInfo;

public class ApplicationForMssql extends ApplicationForBase {
    @Override
    IDatabaseLegacy getDatabaseInfo() {
        return new MssqlInfo(getExtensionId());
    }
}
