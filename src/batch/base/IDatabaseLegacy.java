package batch.base;

public interface IDatabaseLegacy {
    String getDriverName();

    String getNumericColumn();

    String getTimestampColumn();

    boolean existTable(String tableName);

    boolean resetTable(String tableName);
}
