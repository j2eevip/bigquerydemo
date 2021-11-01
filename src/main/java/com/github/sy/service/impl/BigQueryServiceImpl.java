package com.github.sy.service.impl;

import com.github.sy.config.BigQueryConfig.BigQueryFileGateway;
import com.github.sy.service.BigQueryService;
import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQuery.DatasetListOption;
import com.google.cloud.bigquery.BigQuery.TableDataListOption;
import com.google.cloud.bigquery.BigQuery.TableListOption;
import com.google.cloud.bigquery.Dataset;
import com.google.cloud.bigquery.FormatOptions;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableId;
import com.google.cloud.bigquery.TableResult;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.bigquery.core.BigQueryTemplate;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Sherlock
 * @since 2021/11/1-22:39
 */
@Service
public class BigQueryServiceImpl implements BigQueryService {
    private final BigQueryFileGateway bigQueryFileGateway;
    private final BigQueryTemplate bigQueryTemplate;
    private final BigQuery bigQuery;

    public BigQueryServiceImpl(final GatewayProxyFactoryBean bigQueryFileGateway, final BigQueryTemplate bigQueryTemplate, final BigQuery bigQuery) {
        this.bigQueryFileGateway = (BigQueryFileGateway) bigQueryFileGateway.getObject();
        this.bigQueryTemplate = bigQueryTemplate;
        this.bigQuery = bigQuery;
    }
    @Value("${spring.cloud.gcp.bigquery.dataset-name}")
    private String datasetName;

    @Override
    public String getDatasetName() {
        return this.bigQueryTemplate.getDatasetName();
    }

    @Override
    public ListenableFuture<Job> writeDataToTable(MultipartFile file, String tableName) throws IOException {
        return this.bigQueryTemplate.writeDataToTable(tableName, file.getInputStream(), FormatOptions.csv());
    }

    @Override
    public ListenableFuture<Job> insertBigQueryTable(String csvData, String tableName) {
        return this.bigQueryFileGateway.insertBigQueryTable(csvData.getBytes(StandardCharsets.UTF_8), tableName);
    }

    @Override
    public Page<Dataset> listDatasets(DatasetListOption options) {
        return bigQuery.listDatasets(options);
    }

    @Override
    public Page<Table> listTables(String datasetId, TableListOption options) {
        return bigQuery.listTables(datasetId, options);
    }

    @Override
    public TableResult listTableData(String dataset, String table, long pageSize) {
        TableId tableId = TableId.of(dataset, table);
        return bigQuery.listTableData(tableId, TableDataListOption.pageSize(pageSize));
    }
}
