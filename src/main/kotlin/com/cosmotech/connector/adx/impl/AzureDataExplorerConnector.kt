package com.cosmotech.connector.adx.impl

import com.cosmotech.connector.adx.AzureDataExplorerQueries
import com.cosmotech.connector.adx.extensions.toShowTableQuery
import com.cosmotech.connector.adx.utils.AzureDataExplorerUtil
import com.cosmotech.connector.adx.utils.AzureDataExplorerUtil.Builder.getConnectionString
import com.cosmotech.connector.adx.utils.AzureDataExplorerUtil.Builder.getDatabaseName
import com.cosmotech.connector.commons.Connector
import com.cosmotech.connector.commons.pojo.CsvData
import com.microsoft.azure.kusto.data.Client
import com.microsoft.azure.kusto.data.ClientFactory
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Connector for Azure Data Explorer
 */
class AzureDataExplorerConnector(private val storageData:Map<String,CsvData>): Connector<Client,AzureDataExplorerQueries,Unit> {

    // Azure Data Explorer Client
    private var adxClient: Client = createClient()

    companion object {
        val LOGGER: Logger = Logger.getLogger(AzureDataExplorerConnector::class.simpleName)
    }

    /**
     * Build an Azure Data explorer client
     */
    override fun createClient(): Client {
        LOGGER.info("Client creation")
        val csb = getConnectionString()
        return ClientFactory.createClient(csb)
    }

    override fun prepare(client: Client): AzureDataExplorerQueries {
        val createQueryData = mutableMapOf<String, MutableMap<String, String>>()
        LOGGER.info("Construct create queries")
        storageData
            .values
            .forEach {
                createQueryData[it.fileName] = it.headerNameAndType
            }
        val insertQueryData = mutableMapOf<String,String>()
        LOGGER.info("Construct insert queries")
        storageData
            .forEach {
                    (storageUrl, csvData) -> insertQueryData[csvData.fileName] = storageUrl
            }
        return AzureDataExplorerQueries(createQueryData,insertQueryData)
    }

    override fun process() {
        val adxQueries = prepare(adxClient)
        val sharedAccessSignature = AzureDataExplorerUtil.getSharedAccessSignature()
        val databaseName = getDatabaseName()
        this.createTables(databaseName,adxQueries.create)
        this.insertTables(databaseName,adxQueries.insert,sharedAccessSignature)
    }

    /**
     * Build and Execute create queries into Azure Data Explorer
     * One client is created and create queries are run sequentially
     * @param databaseName the name the database targeted
     * @param tableInformation a MutableMap containing as keys "table names" and as values the pair's list {"table field name","table field type"}
     */
    private fun createTables(databaseName:String, tableInformation:MutableMap<String, MutableMap<String, String>>) {
        val createQueries = AzureDataExplorerUtil.constructCreateQueries(tableInformation)
        //TODO batch creation
        createQueries.forEach{ (tableName, createQuery) ->
            adxClient.execute(databaseName, createQuery)
            LOGGER.info("Create table $tableName")
            val response = adxClient.execute(databaseName, tableName.toShowTableQuery())
            //TODO handle response correctly
            if (!response.resultTables.first().data.isNullOrEmpty()) {
                LOGGER.info("The following query has created the table (or the table was here before): \n$createQuery ")
            } else {
                LOGGER.log(Level.SEVERE,"Something went wrong with the following query: \n$createQuery")
            }
        }
    }

    /**
     * Build and Execute insert queries into Azure Data Explorer
     * @param databaseName the name the database targeted
     * @param insertDataInformation data information to ingest
     * @param sasKey the SAS key to use during ingestion
     */
    private fun insertTables(databaseName:String, insertDataInformation: MutableMap<String, String>, sasKey:String) {
        val insertQueries = AzureDataExplorerUtil
            .constructInsertQueries(insertDataInformation,sasKey)
        //TODO handle response correctly: check the flag HasErrors in response
        //TODO batch ingestion
        insertQueries.forEach {
            val kustoOperationResult = adxClient.execute(databaseName, it)
            kustoOperationResult.primaryResults
            LOGGER.info("The following query has been launched: \n$it ")
        }
    }

}