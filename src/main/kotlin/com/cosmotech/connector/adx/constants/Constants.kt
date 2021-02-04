package com.cosmotech.connector.adx.constants

// ############################
// ## Commons
// ############################

/** Environment variable in which the Azure tenant is stored */
const val AZURE_TENANT_ID_KEY = "azure.tenant.id"

/** Environment variable in which the Azure tenant is stored */
const val AZURE_CLIENT_ID_KEY = "azure.client.id"


// ############################
// ## Azure Data Explorer
// ############################

/** Create table instruction prefix */
const val CREATE_MERGE_TABLE_PREFIX = ".create-merge table "

/** Show table instruction prefix */
const val SHOW_TABLE_PREFIX = ".show table "

/** Schema as json instruction suffix */
const val SCHEMA_AS_JSON_SUFFIX = " schema as json"

/** Environment variable in which the ADX Cluster endpoint is stored */
const val ADX_RESOURCE_URI_KEY = "azure.data.explorer.resource.uri"

/** Environment variable in which the ADX Cluster endpoint is stored */
const val ADX_APP_ID_KEY = "azure.data.explorer.app.id"

/** Environment variable in which the ADX targeted database name is stored */
const val ADX_DATABASE_NAME_KEY = "azure.data.explorer.database.name"

// ############################
// ## Azure Storage
// ############################

/** Environment variable in which the Azure storage account name is stored */
const val AZURE_STORAGE_ACCOUNT_NAME_KEY = "azure.storage.account.name"

/** Environment variable in which the Azure storage account key is stored */
const val AZURE_STORAGE_ACCOUNT_KEY_KEY = "azure.storage.account.key"

/** Environment variable in which the Azure storage SAS duration is stored */
const val AZURE_STORAGE_SAS_DURATION_KEY = "azure.storage.shared.access.signature.duration"

/** Environment variable in which the Azure storage SAS permissions are stored */
const val AZURE_STORAGE_SAS_PERMISSIONS_KEY = "azure.storage.shared.access.signature.permissions"

/** Environment variable in which the Azure storage SAS services are stored */
const val AZURE_STORAGE_SAS_SERVICES_KEY = "azure.storage.shared.access.signature.services"

/** Environment variable in which the Azure storage SAS resource types are stored */
const val AZURE_STORAGE_SAS_RESOURCE_TYPES_KEY = "azure.storage.shared.access.signature.resource.types"