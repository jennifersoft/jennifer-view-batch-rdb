package batch.handler.metrics;

import batch.handler.CommonHandler;
import batch.util.DBUtility;
import com.aries.view.extension.data.MetricsAsDomain;
import com.aries.view.extension.data.Model;
import com.aries.view.extension.util.LogUtil;
import com.aries.view.extension.util.PropertyUtil;

import java.sql.*;
import java.text.SimpleDateFormat;

public abstract class DomainForBase extends CommonHandler {
    private static String defaultTableName = null;

    @Override
    public boolean preHandle(long batchTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        defaultTableName = PropertyUtil.getValue(getExtensionId(), "table", "METRICS_AS_DOMAIN") + "_" + sdf.format(new Date(batchTime));

        return initHandler(defaultTableName);
    }

    @Override
    public void process(Model[] models) {
        long time = System.currentTimeMillis();
        String query = DBUtility.createInsertQuery(defaultTableName, 37);

        Connection dbConnection = null;
        PreparedStatement statement = null;

        try {
            dbConnection = DBUtility.getDBConnection(getExtensionId(), getDatabaseInfo().getDriverName());
            statement = dbConnection.prepareStatement(query);
            dbConnection.setAutoCommit(false);

            for(int i = 0; i < models.length; i++) {
                MetricsAsDomain data = (MetricsAsDomain) models[i];
                statement.setInt(1, data.getDomainId());
                statement.setString(2, data.getDomainName());
                statement.setTimestamp(3, new Timestamp(data.getStandardTime()));
                statement.setDouble(4, data.getAliveInstanceCount());
                statement.setDouble(5, data.getServiceCount());
                statement.setDouble(6, data.getServiceTime());
                statement.setDouble(7, data.getServiceErrorCount());
                statement.setDouble(8, data.getServiceSlowCount());
                statement.setDouble(9, data.getServiceMethodTime());
                statement.setDouble(10, data.getServiceSqlTime());
                statement.setDouble(11, data.getServiceExternalCallTime());
                statement.setDouble(12, data.getServiceFetchTime());
                statement.setDouble(13, data.getServiceRate());
                statement.setDouble(14, data.getActiveService());
                statement.setDouble(15, data.getUserRequestInterval());
                statement.setDouble(16, data.getConcurrentUser());
                statement.setDouble(17, data.getVisitHour());
                statement.setDouble(18, data.getVisitDay());
                statement.setDouble(19, data.getEventNormalCount());
                statement.setDouble(20, data.getEventWarningCount());
                statement.setDouble(21, data.getEventFatalCount());
                statement.setDouble(22, data.getEventCount());
                statement.setDouble(23, data.getErrorCount());
                statement.setDouble(24, data.getSqlCount());
                statement.setDouble(25, data.getSqlTime());
                statement.setDouble(26, data.getExternalCallCount());
                statement.setDouble(27, data.getExternalCallTime());
                statement.setDouble(28, data.getFetchCount());
                statement.setDouble(29, data.getFetchTime());
                statement.setDouble(30, data.getFrontendMeasureCount());
                statement.setDouble(31, data.getAverageFrontendTime());
                statement.setDouble(32, data.getAverageFrontendNetworkTime());
                statement.setDouble(33, data.getAverageDbPoolActiveCount());
                statement.setDouble(34, data.getAverageDbPoolIdleCount());
                statement.setDouble(35, data.getAverageDbPoolConfiguredCount());
                statement.setDouble(36, data.getMaxDbPoolActiveCount());
                statement.setDouble(37, data.getMaxTps());
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
                + "DOMAIN_NAME VARCHAR(20), "
                + "STANDARD_TIME " + timestampColumn + ", "
                + "ALIVE_INSTANCE_COUNT " + numericColumn + ", "
                + "SERVICE_COUNT " + numericColumn + ", "
                + "SERVICE_TIME " + numericColumn + ", "
                + "SERVICE_ERROR_COUNT " + numericColumn + ", "
                + "SERVICE_SLOW_COUNT " + numericColumn + ", "
                + "SERVICE_METHOD_TIME " + numericColumn + ", "
                + "SERVICE_SQL_TIME " + numericColumn + ", "
                + "SERVICE_EXTERNALCALL_TIME " + numericColumn + ", "
                + "SERVICE_FETCH_TIME " + numericColumn + ", "
                + "SERVICE_RATE " + numericColumn + ", "
                + "ACTIVE_SERVICE " + numericColumn + ", "
                + "USER_REQUEST_INTERVAL " + numericColumn + ", "
                + "CONCURRENT_USER " + numericColumn + ", "
                + "VISIT_HOUR " + numericColumn + ", "
                + "VISIT_DAY " + numericColumn + ", "
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
                + "AVERAGE_DB_POOL_ACTIVE_COUNT " + numericColumn + ", "
                + "AVERAGE_DB_POOL_IDLE_COUNT " + numericColumn + ", "
                + "AVERAGE_DB_POOL_CONFIGURED_COUNT " + numericColumn + ", "
                + "MAX_DB_POOL_ACTIVE_COUNT " + numericColumn + ", "
                + "MAX_TPS " + numericColumn
                + ")";

        boolean isOK = DBUtility.updateSimpleQuery(getExtensionId(), getDatabaseInfo().getDriverName(), query);

        if(isOK) LogUtil.info("Table \"" + defaultTableName + "\" is created!");
        return isOK;
    }

    @Override
    public String getExtensionId() {
        return "metrics_as_domain";
    }

}
