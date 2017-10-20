package batch.handler.service;

import batch.handler.CommonHandler;
import batch.util.DBUtility;
import com.aries.view.extension.data.ApplicationService;
import com.aries.view.extension.data.Model;
import com.aries.view.extension.util.LogUtil;
import com.aries.view.extension.util.PropertyUtil;

import java.sql.*;
import java.text.SimpleDateFormat;

public abstract class ApplicationForBase extends CommonHandler {
    private static String defaultTableName = null;

    @Override
    public boolean preHandle(long batchTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        defaultTableName = PropertyUtil.getValue(getExtensionId(), "table", "APPLICATION_SERVICE") + "_" + sdf.format(new Date(batchTime));

        return initHandler(defaultTableName);
    }

    @Override
    public void process(Model[] models) {
        long time = System.currentTimeMillis();
        String query = DBUtility.createInsertQuery(defaultTableName, 23);

        Connection dbConnection = null;
        PreparedStatement statement = null;

        try {
            dbConnection = DBUtility.getDBConnection(getExtensionId(), getDatabaseInfo().getDriverName());
            statement = dbConnection.prepareStatement(query);
            dbConnection.setAutoCommit(false);

            for(int i = 0; i < models.length; i++) {
                ApplicationService data = (ApplicationService) models[i];
                String name = data.getApplicationName();

                statement.setInt(1, data.getDomainId());
                statement.setString(2, data.getDomainName());
                statement.setInt(3, data.getInstanceId());
                statement.setString(4, data.getInstanceName());
                statement.setString(5, (name.length() > 500) ? name.substring(0, 500) : name);
                statement.setLong(6, data.getCallCount());
                statement.setDouble(7, data.getResponseTime());
                statement.setLong(8, data.getFailureCount());
                statement.setLong(9, data.getBadResponseCount());
                statement.setDouble(10, data.getSqlCountPerTransaction());
                statement.setDouble(11, data.getSqlTimePerTransaction());
                statement.setLong(12, data.getSqlTimeSum());
                statement.setDouble(13, data.getExternalCallCountPerTransaction());
                statement.setDouble(14, data.getExternalCallTimePerTransaction());
                statement.setLong(15, data.getExternalCallTimeSum());
                statement.setDouble(16, data.getFetchTimePerTransaction());
                statement.setDouble(17, data.getFrontEndTimePerTransaction());
                statement.setDouble(18, data.getFrontEndNetworkTimePerTransaction());
                statement.setDouble(19, data.getCpuTimePerTransaction());
                statement.setDouble(20, data.getFrontEndMeasureCount());
                statement.setDouble(21, data.getMethodTimePerTransaction());
                statement.setLong(22, data.getMaxResponseTime());
                statement.setDouble(23, data.getResponseTimeStandardDeviation());

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
        String numericColumn = getDatabaseInfo().getNumericColumn();

        String query = "CREATE TABLE " + defaultTableName + "("
                + "DOMAIN_ID " + numericColumn + ", "
                + "DOMAIN_NAME VARCHAR(20), "
                + "INSTANCE_ID " + numericColumn + ", "
                + "INSTANCE_NAME VARCHAR(20), "
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

        boolean isOK = DBUtility.updateQuery(getExtensionId(), getDatabaseInfo().getDriverName(), query);

        if(isOK) LogUtil.info("Table \"" + defaultTableName + "\" is created!");
        return isOK;
    }

    @Override
    public String getExtensionId() {
        return "application_service";
    }
}
