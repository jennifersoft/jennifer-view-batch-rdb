package batch.handler.metrics;

import batch.base.IDatabaseLegacy;
import batch.database.MssqlInfo;

public class DomainForMssql extends DomainForBase {
    @Override
    protected IDatabaseLegacy getDatabaseInfo() {
        return new MssqlInfo(getExtensionId());
    }
}
