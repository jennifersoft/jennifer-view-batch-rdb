package batch.handler.metrics;

import batch.handler.CommonHandler;
import batch.util.DBUtility;
import com.aries.extension.data.batch.MetricsDataAsBusiness;
import com.aries.extension.data.BatchData;
import com.aries.extension.util.LogUtil;

import java.sql.*;

public abstract class BusinessForBase extends CommonHandler {
    private static String defaultTableName = null;

    @Override
    public boolean preHandle(long batchTime) {
        defaultTableName = createTableName(batchTime, "METRICS_AS_BUSINESS");
        return initHandler(batchTime, defaultTableName);
    }

    @Override
    public void process(BatchData[] models) {
        long time = System.currentTimeMillis();
        String query = DBUtility.createInsertQuery(defaultTableName, 33);

        Connection dbConnection = null;
        PreparedStatement statement = null;

        try {
            dbConnection = DBUtility.getDBConnection(getExtensionId(), getDatabaseInfo().getDriverName());
            statement = dbConnection.prepareStatement(query);
            dbConnection.setAutoCommit(false);

            for(int i = 0; i < models.length; i++) {
                MetricsDataAsBusiness data = (MetricsDataAsBusiness) models[i];
                statement.setInt(1, data.domainId);
                statement.setString(2, data.domainName);
                statement.setInt(3, data.businessId);
                statement.setString(4, data.businessName);
                statement.setTimestamp(5, new Timestamp(data.standardTime));
                statement.setDouble(6, data.serviceCount);
                statement.setDouble(7, data.serviceTime);
                statement.setDouble(8, data.serviceErrorCount);
                statement.setDouble(9, data.serviceSlowCount);
                statement.setDouble(10, data.serviceMethodTime);
                statement.setDouble(11, data.serviceSqlTime);
                statement.setDouble(12, data.serviceExternalCallTime);
                statement.setDouble(13, data.serviceFetchTime);
                statement.setDouble(14, data.serviceRate);
                statement.setDouble(15, data.serviceCpu);
                statement.setDouble(16, data.activeService);
                statement.setDouble(17, data.userRequestInterval);
                statement.setDouble(18, data.concurrentUser);
                statement.setDouble(19, data.eventNormalCount);
                statement.setDouble(20, data.eventWarningCount);
                statement.setDouble(21, data.eventFatalCount);
                statement.setDouble(22, data.eventCount);
                statement.setDouble(23, data.errorCount);
                statement.setDouble(24, data.sqlCount);
                statement.setDouble(25, data.sqlTime);
                statement.setDouble(26, data.externalCallCount);
                statement.setDouble(27, data.externalCallTime);
                statement.setDouble(28, data.fetchCount);
                statement.setDouble(29, data.fetchTime);
                statement.setDouble(30, data.frontendMeasureCount);
                statement.setDouble(31, data.averageFrontendTime);
                statement.setDouble(32, data.averageFrontendNetworkTime);
                statement.setDouble(33, data.maxTps);
                statement.addBatch();
                statement.clearParameters();
            }

            statement.executeBatch();
            dbConnection.commit();

            LogUtil.info(models.length + " Batch data inserted! (" + (System.currentTimeMillis() - time) + "ms)");
        } catch (SQLException e) {
            LogUtil.error(e.getMessage());
        } finally {
            DBUtility.finallyDBConnection(dbConnection, statement);
        }
    }

    @Override
    public boolean createTable() {
        String timestampColumn = getDatabaseInfo().getTimestampColumn();
        String numericColumn = getDatabaseInfo().getNumericColumn();

        String query = "CREATE TABLE " + defaultTableName + "("
                + "DOMAIN_ID " + numericColumn +  ", "
                + "DOMAIN_NAME VARCHAR(50), "
                + "BUSINESS_ID " + numericColumn + ", "
                + "BUSINESS_NAME VARCHAR(50), "
                + "STANDARD_TIME " + timestampColumn + ", "
                + "SERVICE_COUNT " + numericColumn + ", "
                + "SERVICE_TIME " + numericColumn + ", "
                + "SERVICE_ERROR_COUNT " + numericColumn + ", "
                + "SERVICE_SLOW_COUNT " + numericColumn + ", "
                + "SERVICE_METHOD_TIME " + numericColumn + ", "
                + "SERVICE_SQL_TIME " + numericColumn + ", "
                + "SERVICE_EXTERNALCALL_TIME " + numericColumn + ", "
                + "SERVICE_FETCH_TIME " + numericColumn + ", "
                + "SERVICE_RATE " + numericColumn + ", "
                + "SERVICE_CPU " + numericColumn + ", "
                + "ACTIVE_SERVICE " + numericColumn + ", "
                + "USER_REQUEST_INTERVAL " + numericColumn + ", "
                + "CONCURRENT_USER " + numericColumn + ", "
                + "EVENT_NORMAL_COUNT " + numericColumn + ", "
                + "EVENT_WARNING_COUNT " + numericColumn + ", "
                + "EVENT_FATAL_COUNT " + numericColumn + ", "
                + "EVENT_COUNT " + numericColumn + ", "
                + "ERROR_COUNT " + numericColumn + ", "
                + "SQL_COUNT " + numericColumn + ", "
                + "SQL_TIME " + numericColumn + ", "
                + "EXTERNALCALL_COUNT " + numericColumn + ", "
                + "EXTERNALCALL_TIME " + numericColumn + ", "
                + "FETCH_COUNT " + numericColumn + ", "
                + "FETCH_TIME " + numericColumn + ", "
                + "FRONTEND_MEASURE_COUNT " + numericColumn + ", "
                + "AVERAGE_FRONTEND_TIME " + numericColumn + ", "
                + "AVERAGE_FRONTEND_NETWORK_TIME " + numericColumn + ", "
                + "MAX_TPS " + numericColumn
                + ")";

        return DBUtility.updateQuery(getExtensionId(), getDatabaseInfo().getDriverName(), query);
    }

    @Override
    public String getExtensionId() {
        return "metrics_as_business";
    }
}
