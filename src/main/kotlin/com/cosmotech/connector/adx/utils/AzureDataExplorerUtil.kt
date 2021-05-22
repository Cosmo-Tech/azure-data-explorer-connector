package com.cosmotech.connector.adx.utils

import com.cosmotech.connector.storage.utils.GenerateSASUtil
import com.cosmotech.connector.adx.constants.*
import com.cosmotech.connector.adx.extensions.toADXType
import com.microsoft.azure.kusto.data.ConnectionStringBuilder
import org.eclipse.microprofile.config.Config
import org.eclipse.microprofile.config.ConfigProvider

/**
 * Utility class to manage Azure storage / data explorer interactions
 */
class AzureDataExplorerUtil {

    companion object Builder {

        private val configuration: Config = ConfigProvider.getConfig()

        /**
         * Construct the connection string use to interact with Azure Data Explorer
         * @return the connection string
         */
        @JvmStatic
        fun getConnectionString(): ConnectionStringBuilder? {
            return ConnectionStringBuilder.createWithAadApplicationCredentials(
                getResourceUri(),
                getApplicationClientId(),
                getApplicationKey(),
                getAuthorityId()
            )
        }

        fun getConnectionStringIngest(): ConnectionStringBuilder? {
            return ConnectionStringBuilder.createWithAadApplicationCredentials(
                getResourceUriIngest(),
                getApplicationClientId(),
                getApplicationKey(),
                getAuthorityId()
            )
        }

        /** Get the Shared Access Signature */
        @JvmStatic
        fun getSharedAccessSignature(): String {
            return GenerateSASUtil.getFileSAS(
                getStorageAccountName(),
                getStorageAccountKey(),
                getTokenPermissions(),
                getTokenServices(),
                getTokenResourceTypes(),
                getTokenDuration()
            )
        }

        /**
         * Construct Create table queries
         * @param tableDefinition the data structure containing data information
         * @return a list of ".create table" query
         */
        @JvmStatic
        fun constructCreateQueries(tableDefinition: MutableMap<String, MutableMap<String, String>>): MutableMap<String, String> {
            val queries = mutableMapOf<String, String>()

            tableDefinition.forEach { (tableName, fields) ->
                val queryBuilder = StringBuilder(CREATE_MERGE_TABLE_PREFIX)
                    .append(tableName)
                    .append("(")

                fields.forEach { (field, type) ->
                    queryBuilder
                        .append(field)
                        .append(":")
                        .append(type.toADXType())
                        .append(",")
                }
                val query = queryBuilder.removeSuffix(",").toString().plus(")")
                queries.putIfAbsent(tableName, query)
            }
            return queries
        }

        /**
         * Build insert queries for Azure Data Explorer.
         * @param insertDataInformation data information to ingest
         * @param sasKey the SAS key to use during ingestion
         * @param ignoreFirstRecord the flag for ignoring the first record of the data (default value: true)
         * @return the list of insert query
         */
        @JvmStatic
        fun constructInsertQueries(
            insertDataInformation: MutableMap<String, String>,
            sasKey: String,
            ignoreFirstRecord: Boolean = true
        ): List<String> {
            val result = mutableListOf<String>()

            insertDataInformation.forEach { (tableName, storageUrl) ->
                result.add(".ingest into table $tableName (h'$storageUrl?$sasKey') with (ignoreFirstRecord=$ignoreFirstRecord)")
            }
            return result
        }


        /** Get the Authority Id */
        @JvmStatic
        private fun getAuthorityId(): String {
            return configuration.getValue(AZURE_TENANT_ID_KEY, String::class.java)
        }

        /** Get the Application Key */
        @JvmStatic
        fun getApplicationKey(): String {
            return configuration.getValue(ADX_APP_ID_KEY, String::class.java)
        }

        /** Get the Application client Id */
        @JvmStatic
        fun getApplicationClientId(): String {
            return configuration.getValue(AZURE_CLIENT_ID_KEY, String::class.java)
        }

        /** Get the Storage Account Name */
        @JvmStatic
        fun getStorageAccountName(): String {
            return configuration.getValue(AZURE_STORAGE_ACCOUNT_NAME_KEY, String::class.java)
        }

        /** Get the Storage Account Name */
        @JvmStatic
        fun getStorageAccountKey(): String {
            return configuration.getValue(AZURE_STORAGE_ACCOUNT_KEY_KEY, String::class.java)
        }

        /** Get the resource URI */
        @JvmStatic
        private fun getResourceUri(): String {
            return configuration.getValue(ADX_RESOURCE_URI_KEY, String::class.java)
        }

        /** Get the resource URI */
        @JvmStatic
        private fun getResourceUriIngest(): String {
            return configuration.getValue(ADX_RESOURCE_INGEST_URI_KEY, String::class.java)
        }

        /** Get the Database name */
        @JvmStatic
        fun getDatabaseName(): String {
            return configuration.getValue(ADX_DATABASE_NAME_KEY, String::class.java)
        }

        /** Get the SAS token duration */
        @JvmStatic
        private fun getTokenDuration(): Int {
            return configuration.getValue(AZURE_STORAGE_SAS_DURATION_KEY, Int::class.java)
        }
        /** Get the SAS token resource types */
        @JvmStatic
        private fun getTokenResourceTypes() : String {
            return configuration.getValue(AZURE_STORAGE_SAS_RESOURCE_TYPES_KEY, String::class.java)
        }

        /** Get the SAS token services */
        @JvmStatic
        private fun getTokenServices() : String {
            return configuration.getValue(AZURE_STORAGE_SAS_SERVICES_KEY, String::class.java)
        }

        /** Get the SAS token permissions */
        @JvmStatic
        private fun getTokenPermissions(): String {
            return configuration.getValue(AZURE_STORAGE_SAS_PERMISSIONS_KEY, String::class.java)
        }
    }
}
