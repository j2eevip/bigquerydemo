package com.github.sy.service;

import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.BigQuery.DatasetListOption;
import com.google.cloud.bigquery.BigQuery.TableListOption;
import com.google.cloud.bigquery.Dataset;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableResult;
import java.io.IOException;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Sherlock
 * @since 2021/11/1-22:39
 */
public interface BigQueryService {
    String getDatasetName();
    ListenableFuture<Job> writeDataToTable(MultipartFile file, String tableName) throws IOException;
    ListenableFuture<Job> insertBigQueryTable(String csvData, String tableName);
    Page<Dataset> listDatasets(DatasetListOption options);
    Page<Table> listTables(String datasetId, TableListOption options);
    TableResult listTableData(String dataset, String table, long pageSize);
}
