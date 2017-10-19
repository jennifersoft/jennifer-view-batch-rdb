package batch.handler.metrics;

import batch.handler.CommonHandler;
import batch.util.DBUtility;
import com.aries.view.extension.data.Model;
import com.aries.view.extension.data.MetricsAsInstance;
import com.aries.view.extension.util.LogUtil;
import com.aries.view.extension.util.PropertyUtil;

import java.sql.*;
import java.text.SimpleDateFormat;

public abstract class InstanceForBase extends CommonHandler {
    private static String defaultTableName = null;

    @Override
    public boolean preHandle(long batchTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        defaultTableName = PropertyUtil.getValue(getExtensionId(), "table", "METRICS_AS_INSTANCE") + "_" + sdf.format(new Date(batchTime));

        return initHandler(defaultTableName);
    }

    @Override
    public void process(Model[] models) {
        long time = System.currentTimeMillis();
        String query = DBUtility.createInsertQuery(defaultTableName, 58);

        Connection dbConnection = null;
        PreparedStatement statement = null;

        try {
            dbConnection = DBUtility.getDBConnection(getExtensionId(), getDatabaseInfo().getDriverName());
            statement = dbConnection.prepareStatement(query);
            dbConnection.setAutoCommit(false);

            for(int i = 0; i < models.length; i++) {
                MetricsAsInstance data = (MetricsAsInstance) models[i];
                statement.setInt(1, data.getDomainId());
                statement.setString(2, data.getDomainName());
                statement.setInt(3, data.getInstanceId());
                statement.setString(4, data.getInstanceName());
                statement.setTimestamp(5, new Timestamp(data.getStandardTime()));
                statement.setDouble(6, data.getGcTime());
                statement.setDouble(7, data.getGcCount());
                statement.setDouble(8, data.getFileCount());
                statement.setDouble(9, data.getFinalizePending());
                statement.setDouble(10, data.getHeapCommitted());
                statement.setDouble(11, data.getHeapUsed());
                statement.setDouble(12, data.getHeapUsage());
                statement.setDouble(13, data.getNonHeapUsed());
                statement.setDouble(14, data.getSystemCpu());
                statement.setDouble(15, data.getSystemMemory());
                statement.setDouble(16, data.getSystemMemoryRate());
                statement.setDouble(17, data.getProcessCpu());
                statement.setDouble(18, data.getProcessMemory());
                statement.setDouble(19, data.getThreadDaemon());
                statement.setDouble(20, data.getThreadCurrent());
                statement.setDouble(21, data.getThreadStarted());
                statement.setDouble(22, data.getCollectionCount());
                statement.setDouble(23, data.getSocketCount());
                statement.setDouble(24, data.getServiceCount());
                statement.setDouble(25, data.getServiceTime());
                statement.setDouble(26, data.getServiceErrorCount());
                statement.setDouble(27, data.getServiceSlowCount());
                statement.setDouble(28, data.getServiceMethodTime());
                statement.setDouble(29, data.getServiceSqlTime());
                statement.setDouble(30, data.getServiceExternalCallTime());
                statement.setDouble(31, data.getServiceFetchTime());
                statement.setDouble(32, data.getServiceRate());
                statement.setDouble(33, data.getServiceCpu());
                statement.setDouble(34, data.getActiveService());
                statement.setDouble(35, data.getActiveUser());
                statement.setDouble(36, data.getUserRequestInterval());
                statement.setDouble(37, data.getConcurrentUser());
                statement.setDouble(38, data.getVisitHour());
                statement.setDouble(39, data.getVisitDay());
                statement.setDouble(40, data.getEventNormalCount());
                statement.setDouble(41, data.getEventWarningCount());
                statement.setDouble(42, data.getEventFatalCount());
                statement.setDouble(43, data.getEventCount());
                statement.setDouble(44, data.getErrorCount());
                statement.setDouble(45, data.getSqlCount());
                statement.setDouble(46, data.getSqlTime());
                statement.setDouble(47, data.getExternalCallCount());
                statement.setDouble(48, data.getExternalCallTime());
                statement.setDouble(49, data.getFetchCount());
                statement.setDouble(50, data.getFetchTime());
                statement.setDouble(51, data.getFrontendMeasureCount());
                statement.setDouble(52, data.getAverageFrontendTime());
                statement.setDouble(53, data.getAverageFrontendNetworkTime());
                statement.setDouble(54, data.getAliveAgent());
                statement.setDouble(55, data.getAverageDbPoolActiveCount());
                statement.setDouble(56, data.getAverageDbPoolIdleCount());
                statement.setDouble(57, data.getMaxDbPoolActiveCount());
                statement.setDouble(58, data.getMaxTps());
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
                + "DOMAIN_NAME VARCHAR(20), "
                + "INSTANCE_ID " + numericColumn + ", "
                + "INSTANCE_NAME VARCHAR(20), "
                + "STANDARD_TIME " + timestampColumn + ", "
                + "GC_TIME " + numericColumn + ", "
                + "GC_COUNT " + numericColumn + ", "
                + "FILE_COUNT " + numericColumn + ", "
                + "FINALIZE_PENDING " + numericColumn + ", "
                + "HEAP_COMMITTED " + numericColumn + ", "
                + "HEAP_USED " + numericColumn + ", "
                + "HEAP_USAGE " + numericColumn + ", "
                + "NON_HEAP_USED " + numericColumn + ", "
                + "SYSTEM_CPU " + numericColumn + ", "
                + "SYSTEM_MEMORY " + numericColumn + ", "
                + "SYSTEM_MEMORY_RATE " + numericColumn + ", "
                + "PROCESS_CPU " + numericColumn + ", "
                + "PROCESS_MEMORY " + numericColumn + ", "
                + "THREAD_DAEMON " + numericColumn + ", "
                + "THREAD_CURRENT " + numericColumn + ", "
                + "THREAD_STARTED " + numericColumn + ", "
                + "COLLECTION_COUNT " + numericColumn + ", "
                + "SOCKET_COUNT " + numericColumn + ", "
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
                + "ACTIVE_USER " + numericColumn + ", "
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
                + "ALIVE_AGENT " + numericColumn + ", "
                + "AVERAGE_DBPOOL_ACTIVE_COUNT " + numericColumn + ", "
                + "AVERAGE_DBPOOL_IDLE_COUNT " + numericColumn + ", "
                + "MAX_DBPOOL_ACTIVE_COUNT " + numericColumn + ", "
                + "MAX_TPS " + numericColumn
                + ")";

        boolean isOK = DBUtility.updateSimpleQuery(getExtensionId(), getDatabaseInfo().getDriverName(), query);

        if(isOK) LogUtil.info("Table \"" + defaultTableName + "\" is created!");
        return isOK;
    }

    @Override
    public String getExtensionId() {
        return "metrics_as_instance";
    }
}
