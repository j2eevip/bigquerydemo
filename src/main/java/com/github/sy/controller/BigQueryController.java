package com.github.sy.controller;

import com.github.sy.model.QueryMessage;
import com.github.sy.service.BigQueryService;
import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.BigQuery.DatasetListOption;
import com.google.cloud.bigquery.BigQuery.TableListOption;
import com.google.cloud.bigquery.Dataset;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Sherlock
 * @since 2021/11/1-23:01
 */
@Slf4j
@RestController
public class BigQueryController {

    private final BigQueryService bigQueryService;

    public BigQueryController(final BigQueryService bigQueryService) {
        this.bigQueryService = bigQueryService;
    }

    @PostMapping("/push")
    public ResponseEntity<?> uploadText(@RequestBody QueryMessage message) {
        if (message.getCsvDataRow().isEmpty()) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        try {
            ListenableFuture<Job> payloadJob = bigQueryService.insertBigQueryTable(message.getCsvDataRow(), message.getTableName());
            return new ResponseEntity<>(payloadJob, new HttpHeaders(), HttpStatus.OK);

        } catch (Exception e) {
            log.error("push data error.", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/upload/{tableName}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file", required = true) MultipartFile uploadfile, @PathVariable("tableName") String tableName) {
        if (uploadfile.isEmpty()) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        try {
            ListenableFuture<Job> payloadJob = bigQueryService.writeDataToTable(uploadfile, tableName);
            return new ResponseEntity<>(payloadJob, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("upload csv file error.", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{dataset}/{table}/{pageSize}")
    public TableResult listTableData(@PathVariable("dataset") String dataset, @PathVariable("table") String table, @PathVariable("pageSize") long pageSize) {
        return bigQueryService.listTableData(dataset, table, pageSize);
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> getDatasetName() {
        try {
            String payload = bigQueryService.getDatasetName();
            return new ResponseEntity<>(payload, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("get dataset name error.", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/datasets")
    public ResponseEntity<?> listDatasets(@RequestBody DatasetListOption options) {
        try {
            Page<Dataset> payload = bigQueryService.listDatasets(options);
            return new ResponseEntity<>(payload, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("query dataset list error.", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/dataset/{datasetId}")
    public ResponseEntity<?> listTables(@PathVariable("datasetId") String datasetId, @RequestBody TableListOption options) {
        try {
            Page<Table> payload = bigQueryService.listTables(datasetId, options);
            return new ResponseEntity<>(payload, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("query dataset by id is error.", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
