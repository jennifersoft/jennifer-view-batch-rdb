package batch.base;

public interface IDatabaseLegacy {
    String getDriverName();

    String getNumericColumn();

    String getTimestampColumn();

    boolean existBatchTable(String tableName);

    boolean resetBatchTable(String tableName);
}
