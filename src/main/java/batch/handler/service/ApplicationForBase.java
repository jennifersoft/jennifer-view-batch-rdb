package batch.handler.service;

import batch.handler.CommonHandler;
import batch.util.DBUtility;
import com.aries.extension.data.batch.ApplicationServiceData;
import com.aries.extension.data.BatchData;
import com.aries.extension.util.LogUtil;

import java.sql.*;

public abstract class ApplicationForBase extends CommonHandler {
    private static String defaultTableName = null;

    @Override
    public boolean preHandle(long batchTime) {
        defaultTableName = createTableName(batchTime, "APPLICATION_SERVICE");
        return initHandler(batchTime, defaultTableName);
    }

    @Override
    public void process(BatchData[] models) {
        long time = System.currentTimeMillis();
        String query = DBUtility.createInsertQuery(defaultTableName, 24);

        Connection dbConnection = null;
        PreparedStatement statement = null;

        try {
            dbConnection = DBUtility.getDBConnection(getExtensionId(), getDatabaseInfo().getDriverName());
            statement = dbConnection.prepareStatement(query);
            dbConnection.setAutoCommit(false);

            for(int i = 0; i < models.length; i++) {
                ApplicationServiceData data = (ApplicationServiceData) models[i];
                String name = data.applicationName;

                statement.setInt(1, data.domainId);
                statement.setString(2, data.domainName);
                statement.setInt(3, data.instanceId);
                statement.setString(4, data.instanceName);
                statement.setTimestamp(5, new Timestamp(data.standardTime));
                statement.setString(6, (name.length() > 500) ? name.substring(0, 500) : name);
                statement.setLong(7, data.callCount);
                statement.setDouble(8, data.responseTime);
                statement.setLong(9, data.failureCount);
                statement.setLong(10, data.badResponseCount);
                statement.setDouble(11, data.sqlCountPerTransaction);
                statement.setDouble(12, data.sqlTimePerTransaction);
                statement.setLong(13, data.sqlTimeSum);
                statement.setDouble(14, data.externalCallCountPerTransaction);
                statement.setDouble(15, data.externalCallTimePerTransaction);
                statement.setLong(16, data.externalCallTimeSum);
                statement.setDouble(17, data.fetchTimePerTransaction);
                statement.setDouble(18, data.frontEndTimePerTransaction);
                statement.setDouble(19, data.frontEndNetworkTimePerTransaction);
                statement.setDouble(20, data.cpuTimePerTransaction);
                statement.setDouble(21, data.frontEndMeasureCount);
                statement.setDouble(22, data.methodTimePerTransaction);
                statement.setLong(23, data.maxResponseTime);
                statement.setDouble(24, data.responseTimeStandardDeviation);

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
                + "DOMAIN_ID " + numericColumn + ", "
                + "DOMAIN_NAME VARCHAR(50), "
                + "INSTANCE_ID " + numericColumn + ", "
                + "INSTANCE_NAME VARCHAR(50), "
                + "STANDARD_TIME " + timestampColumn + ", "
                + "APPLICATION_NAME VARCHAR(500), "
                + "CALL_COUNT " + numericColumn + ", "
                + "RESPONSE_TIME " + numericColumn + ", "
                + "FAILURE_COUNT " + numericColumn + ", "
                + "BAD_RESPONSE_COUNT " + numericColumn + ", "
                + "SQL_COUNT_PER_TX " + numericColumn + ", "
                + "SQL_TIME_PER_TX " + numericColumn + ", "
                + "SQL_TIME_SUM " + numericColumn + ", "
                + "EXTERNALCALL_COUNT_PER_TX " + numericColumn + ", "
                + "EXTERNALCALL_TIME_PER_TX " + numericColumn + ", "
                + "EXTERNALCALL_TIME_SUM " + numericColumn + ", "
                + "FETCH_TIME_PER_TX " + numericColumn + ", "
                + "FRONTEND_TIME_PER_TX " + numericColumn + ", "
                + "FRONTEND_NETWORK_TIME_PER_TX " + numericColumn + ", "
                + "CPU_TIME_PER_TX " + numericColumn + ", "
                + "FRONTEND_MEASURE_COUNT " + numericColumn + ", "
                + "METHOD_TIME_PER_TX " + numericColumn + ", "
                + "MAX_RESPONSE_TIME " + numericColumn + ", "
                + "RESPONSE_TIME_STD " + numericColumn
                + ")";

        return DBUtility.updateQuery(getExtensionId(), getDatabaseInfo().getDriverName(), query);
    }

    @Override
    public String getExtensionId() {
        return "application_service";
    }
}
