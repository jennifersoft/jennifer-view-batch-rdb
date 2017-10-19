package batch.handler.service;

import batch.base.IDatabaseLegacy;
import batch.database.OracleInfo;

public class ApplicationForOracle extends ApplicationForBase {
    @Override
    IDatabaseLegacy getDatabaseInfo() {
        return new OracleInfo(getExtensionId());
    }
}
