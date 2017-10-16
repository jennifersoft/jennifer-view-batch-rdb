package batch.handler.metrics;

import batch.base.IDatabaseLegacy;
import batch.database.OracleInfo;

public class DomainForOracle extends DomainForBase {
    @Override
    IDatabaseLegacy getDatabaseInfo() {
        return new OracleInfo(getExtensionId());
    }
}
