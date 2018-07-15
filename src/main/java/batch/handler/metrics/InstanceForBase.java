package batch.handler.metrics;

import batch.handler.CommonHandler;
import batch.util.DBUtility;
import com.aries.extension.data.BatchData;
import com.aries.extension.data.batch.MetricsDataAsInstance;
import com.aries.extension.util.LogUtil;

import java.sql.*;

public abstract class InstanceForBase extends CommonHandler {
    private static String defaultTableName = null;

    @Override
    public boolean preHandle(long batchTime) {
        defaultTableName = createTableName(batchTime, "METRICS_AS_INSTANCE");
        return initHandler(batchTime, defaultTableName);
    }

    @Override
    public void process(BatchData[] models) {
        long time = System.currentTimeMillis();
        String query = DBUtility.createInsertQuery(defaultTableName, 58);

        Connection dbConnection = null;
        PreparedStatement statement = null;

        try {
            dbConnection = DBUtility.getDBConnection(getExtensionId(), getDatabaseInfo().getDriverName());
            statement = dbConnection.prepareStatement(query);
            dbConnection.setAutoCommit(false);

            for(int i = 0; i < models.length; i++) {
                MetricsDataAsInstance data = (MetricsDataAsInstance) models[i];
                statement.setInt(1, data.domainId);
                statement.setString(2, data.domainName);
                statement.setInt(3, data.instanceId);
                statement.setString(4, data.instanceName);
                statement.setTimestamp(5, new Timestamp(data.standardTime));
                statement.setDouble(6, data.gcTime);
                statement.setDouble(7, data.gcCount);
                statement.setDouble(8, data.fileCount);
                statement.setDouble(9, data.finalizePending);
                statement.setDouble(10, data.heapCommitted);
                statement.setDouble(11, data.heapUsed);
                statement.setDouble(12, data.heapUsage);
                statement.setDouble(13, data.nonHeapUsed);
                statement.setDouble(14, data.systemCpu);
                statement.setDouble(15, data.systemMemory);
                statement.setDouble(16, data.systemMemoryRate);
                statement.setDouble(17, data.processCpu);
                statement.setDouble(18, data.processMemory);
                statement.setDouble(19, data.threadDaemon);
                statement.setDouble(20, data.threadCurrent);
                statement.setDouble(21, data.threadStarted);
                statement.setDouble(22, data.collectionCount);
                statement.setDouble(23, data.socketCount);
                statement.setDouble(24, data.serviceCount);
                statement.setDouble(25, data.serviceTime);
                statement.setDouble(26, data.serviceErrorCount);
                statement.setDouble(27, data.serviceSlowCount);
                statement.setDouble(28, data.serviceMethodTime);
                statement.setDouble(29, data.serviceSqlTime);
                statement.setDouble(30, data.serviceExternalCallTime);
                statement.setDouble(31, data.serviceFetchTime);
                statement.setDouble(32, data.serviceRate);
                statement.setDouble(33, data.serviceCpu);
                statement.setDouble(34, data.activeService);
                statement.setDouble(35, data.activeUser);
                statement.setDouble(36, data.userRequestInterval);
                statement.setDouble(37, data.concurrentUser);
                statement.setDouble(38, data.visitHour);
                statement.setDouble(39, data.visitDay);
                statement.setDouble(40, data.eventNormalCount);
                statement.setDouble(41, data.eventWarningCount);
                statement.setDouble(42, data.eventFatalCount);
                statement.setDouble(43, data.eventCount);
                statement.setDouble(44, data.errorCount);
                statement.setDouble(45, data.sqlCount);
                statement.setDouble(46, data.sqlTime);
                statement.setDouble(47, data.externalCallCount);
                statement.setDouble(48, data.externalCallTime);
                statement.setDouble(49, data.fetchCount);
                statement.setDouble(50, data.fetchTime);
                statement.setDouble(51, data.frontendMeasureCount);
                statement.setDouble(52, data.averageFrontendTime);
                statement.setDouble(53, data.averageFrontendNetworkTime);
                statement.setDouble(54, data.aliveAgent);
                statement.setDouble(55, data.averageDbPoolActiveCount);
                statement.setDouble(56, data.averageDbPoolIdleCount);
                statement.setDouble(57, data.maxDbPoolActiveCount);
                statement.setDouble(58, data.maxTps);
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

        return DBUtility.updateQuery(getExtensionId(), getDatabaseInfo().getDriverName(), query);
    }

    @Override
    public String getExtensionId() {
        return "metrics_as_instance";
    }
}
