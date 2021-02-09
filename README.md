# Azure Storage to Azure data explorer

The aim of this project is to ingest data into ADX using CSV files stored into an Azure storage.

# Properties to overwrite :
Here is the list of properties that should be changed (in ```META-INF/microprofile-config.properties``` file):
- azure.data.explorer.resource.uri
- azure.data.explorer.app.id
- azure.client.id
- azure.tenant.id
- azure.data.explorer.database.name
- azure.storage.account.name
- azure.storage.account.key
- azure.storage.shared.access.signature.duration
- azure.storage.shared.access.signature.permissions
- azure.storage.shared.access.signature.services
- azure.storage.shared.access.signature.resource.types

If you want to overwrite these properties, you can write your own property values in the ```META-INF/microprofile-config.properties``` file, or set a property's system, or an environment variable named :

- AZURE_DATA_EXPLORER_RESOURCE_URI : the ADX cluster path (URI info can be found into ADX cluster page)
- AZURE_DATA_EXPLORER_APP_ID : the app registration id (with the appropriate database rights)
- AZURE_CLIENT_ID : the Azure client id (can be found under the App registration screen)
- AZURE_TENANT_ID : the Azure Tenant id (can be found under the App registration screen)
- AZURE_DATA_EXPLORER_DATABASE_NAME : the targeted database name
- AZURE_STORAGE_ACCOUNT_NAME : the Azure storage account name (can be found under the Storage account screen)
- AZURE_STORAGE_ACCOUNT_KEY : the Azure storage account key (can be found under the specific Storage account screen, under Access keys section)
- AZURE_STORAGE_SHARED_ACCESS_SIGNATURE_DURATION : the SAS Token valid duration (obviously, should be positive and be as small as possible )
- AZURE_STORAGE_SHARED_ACCESS_SIGNATURE_PERMISSIONS : the SAS Token permissions (should be the most restrictive possible)
- AZURE_STORAGE_SHARED_ACCESS_SIGNATURE_SERVICES : the SAS Token services (should be the most restrictive possible)
- AZURE_STORAGE_SHARED_ACCESS_SIGNATURE_RESOURCE_TYPES :the SAS Token resource types (should be the most restrictive possible)


## How-to

```
<dependency>
  <groupId>com.cosmotech</groupId>
  <artifactId>azure-data-explorer-connector</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>

```
or 
```
    <dependency>
      <groupId>com.github.Cosmo-Tech</groupId>
      <artifactId>azure-data-explorer-connector</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
```

## Tasks :

- [ ] Add more logs (maybe with some metrics)
- [ ] Add unit tests
- [ ] Tables bulk creation
- [ ] Use IngestionMapping and try to manage "update schema" for existing tables
- [ ] Data bulk ingestion
- [ ] Manage creation response correctly
- [ ] Manage ingestion response correctly
