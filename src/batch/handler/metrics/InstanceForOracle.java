package batch.handler.metrics;

import batch.base.IDatabaseLegacy;
import batch.database.OracleInfo;

public class InstanceForOracle extends InstanceForBase {
    @Override
    protected IDatabaseLegacy getDatabaseInfo() {
        return new OracleInfo(getExtensionId());
    }
}
