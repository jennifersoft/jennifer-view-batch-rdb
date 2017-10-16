package batch.handler.metrics;

import batch.base.IDatabaseLegacy;
import batch.database.MssqlInfo;

public class DomainForMssql extends DomainForBase {
    @Override
    IDatabaseLegacy getDatabaseInfo() {
        return new MssqlInfo(getExtensionId());
    }
}
