package batch.handler.metrics;

import batch.base.IDatabaseLegacy;
import batch.database.MssqlInfo;

public class BusinessForMssql extends BusinessForBase {
    @Override
    protected IDatabaseLegacy getDatabaseInfo() {
        return new MssqlInfo(getExtensionId());
    }
}
