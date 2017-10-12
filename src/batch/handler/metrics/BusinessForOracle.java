package batch.handler.metrics;

import batch.base.IDatabaseLegacy;
import batch.database.OracleInfo;

public class BusinessForOracle extends BusinessForBase {
    @Override
    IDatabaseLegacy getDatabaseInfo() {
        return new OracleInfo(getExtensionId());
    }
}
