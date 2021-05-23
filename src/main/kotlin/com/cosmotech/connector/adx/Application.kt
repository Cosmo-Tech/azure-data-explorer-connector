package com.cosmotech.connector.adx

import com.cosmotech.connector.adx.impl.AzureDataExplorerConnector
import com.cosmotech.connector.adx.utils.AzureDataExplorerUtil
import com.cosmotech.connector.adx.utils.CsvDataListUtil
import com.cosmotech.connector.commons.pojo.CsvData
import java.nio.file.Files
import java.nio.file.Paths

fun main() {

/*    // Minimal how-to
    // Working test directory
    val testTempFolder = "/tmp/AzureStorageConnector"
    Files.createDirectories(Paths.get(testTempFolder))

    // Construct simple data
    val csvData = mutableMapOf("https://samplestorage.blob.core.windows.net/csv/2021%2F2%2F2%2F9%2F14%2F54%2FTest1.csv" to
        CsvData(
            "Test1",
            mutableMapOf("column1" to "string", "column2" to "datetime", "column3" to "int"),
            mutableListOf(
                mutableListOf("data11", "2020-09-01T00:00:00+00:00", "1"),
                mutableListOf("data21", "2020-09-02T00:00:00+00:00", "2"),
                mutableListOf("data31", "2020-09-03T00:00:00+00:00", "3")
            ),
            testTempFolder,
        ),
        "https://samplestorage.blob.core.windows.net/csv/2021%2F2%2F2%2F9%2F14%2F54%2FTest2.csv" to
        CsvData(
            "Test2",
            mutableMapOf("column4" to "string", "column5" to "datetime", "column6" to "int"),
            mutableListOf(
                mutableListOf("data41", "2020-09-05T00:00:00+00:00", "4"),
                mutableListOf("data51", "2020-09-06T00:00:00+00:00", "5"),
                mutableListOf("data61", "2020-09-07T00:00:00+00:00", "6")
            ),
            testTempFolder,
        )
    )

    AzureDataExplorerConnector(csvData).process()*/
    /*val csvData = mapOf(
        "/tmp/adt/Bar.csv" to
        CsvData(
            fileName = "Bar",
            headerNameAndType = mutableMapOf(
              "NbWaiters" to "string",
              "RestockQty" to "string",
              "Stock" to "string",
              "_id" to "string"),
            rows = mutableListOf(),
        ),
        "/tmp/adt/BarFull.csv" to
        CsvData(
            fileName = "BarFull",
            headerNameAndType = mutableMapOf(
              "NbWaiters" to "string",
              "RestockQty" to "string",
              "Stock" to "string",
              "_id" to "string"),
            rows = mutableListOf(),
        ),
      )
      */
    var csvData: MutableMap<String, CsvData> = mutableMapOf()
    val datasetPath = AzureDataExplorerUtil.getDatasetPath()
    if (datasetPath.isPresent) {
      CsvDataListUtil.addCsv(datasetPath.get(), csvData)
    }

    val parametersPath = AzureDataExplorerUtil.getParametersPath()
    if (parametersPath.isPresent) {
      CsvDataListUtil.addCsv(parametersPath.get(), csvData)
    }

    AzureDataExplorerConnector(csvData).process()

}
