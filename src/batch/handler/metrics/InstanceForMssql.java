package batch.handler.metrics;

import batch.base.IDatabaseLegacy;
import batch.database.MssqlInfo;

public class InstanceForMssql extends InstanceForBase {
    @Override
    IDatabaseLegacy getDatabaseInfo() {
        return new MssqlInfo(getExtensionId());
    }
}
