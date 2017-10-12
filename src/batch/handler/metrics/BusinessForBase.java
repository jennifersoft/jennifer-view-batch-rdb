package batch.handler.metrics;

import batch.base.IDatabaseLegacy;
import batch.base.IDataLegacy;
import batch.util.DBUtility;
import com.aries.view.extension.data.MetricsAsBusiness;
import com.aries.view.extension.data.Model;
import com.aries.view.extension.handler.Batch;
import com.aries.view.extension.util.LogUtil;
import com.aries.view.extension.util.PropertyUtil;

import java.sql.*;
import java.text.SimpleDateFormat;

public abstract class BusinessForBase implements Batch, IDataLegacy {
    private static String defaultTableName = null;

    @Override
    public boolean preHandle(long batchTime) {
        defaultTableName = getTableName(batchTime);

        // 테이블이 존재하고, 리셋 허용이면 모든 테이블을 삭제한다.
        if(getDatabaseInfo().existBatchTable(defaultTableName)) {
            getDatabaseInfo().resetBatchTable(defaultTableName);
        } else {
            createBatchTable();
        }

        return true;
    }

    @Override
    public void process(Model[] models) {
        long time = System.currentTimeMillis();
        String query = DBUtility.createInsertQuery(defaultTableName, 33);

        Connection dbConnection = null;
        PreparedStatement statement = null;

        try {
            dbConnection = DBUtility.getDBConnection(getExtensionId(), getDatabaseInfo().getDriverName());
            statement = dbConnection.prepareStatement(query);
            dbConnection.setAutoCommit(false);

            for(int i = 0; i < models.length; i++) {
                MetricsAsBusiness data = (MetricsAsBusiness) models[i];
                statement.setInt(1, data.getDomainId());
                statement.setString(2, data.getDomainName());
                statement.setInt(3, data.getBusinessId());
                statement.setString(4, data.getBusinessName());
                statement.setTimestamp(5, new Timestamp(data.getStandardTime()));
                statement.setDouble(6, data.getServiceCount());
                statement.setDouble(7, data.getServiceTime());
                statement.setDouble(8, data.getServiceErrorCount());
                statement.setDouble(9, data.getServiceSlowCount());
                statement.setDouble(10, data.getServiceMethodTime());
                statement.setDouble(11, data.getServiceSqlTime());
                statement.setDouble(12, data.getServiceExternalCallTime());
                statement.setDouble(13, data.getServiceFetchTime());
                statement.setDouble(14, data.getServiceRate());
                statement.setDouble(15, data.getServiceCpu());
                statement.setDouble(16, data.getActiveService());
                statement.setDouble(17, data.getUserRequestInterval());
                statement.setDouble(18, data.getConcurrentUser());
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
                statement.setDouble(33, data.getMaxTps());
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
    public String getTableName(long batchTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        return PropertyUtil.getValue(getExtensionId(), "table", "METRICS_AS_BUSINESS") + "_" + sdf.format(new Date(batchTime));
    }

    @Override
    public boolean createBatchTable() {
        String timestampColumn = getDatabaseInfo().getTimestampColumn();
        String numericColumn = getDatabaseInfo().getNumericColumn();

        String query = "CREATE TABLE " + defaultTableName + "("
                + "DOMAIN_ID " + numericColumn +  ", "
                + "DOMAIN_NAME VARCHAR(20), "
                + "BUSINESS_ID " + numericColumn + ", "
                + "BUSINESS_NAME VARCHAR(20), "
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

        boolean isOK = DBUtility.updateSimpleQuery(getExtensionId(), getDatabaseInfo().getDriverName(), query);

        if(isOK) LogUtil.info("Table \"" + defaultTableName + "\" is created!");
        return isOK;
    }

    @Override
    public String getExtensionId() {
        return "metrics_as_business";
    }

    abstract IDatabaseLegacy getDatabaseInfo();
}