package batch.base;

public interface IDataLegacy {
    String getExtensionId();

    String getTableName(long batchTime);

    boolean createBatchTable();
}
