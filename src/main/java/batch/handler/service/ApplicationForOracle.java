package batch.handler.service;

import batch.base.IDatabaseLegacy;
import batch.database.OracleInfo;

public class ApplicationForOracle extends ApplicationForBase {
    @Override
    protected IDatabaseLegacy getDatabaseInfo() {
        return new OracleInfo(getExtensionId());
    }
}
