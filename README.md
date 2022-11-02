# Azure Storage to Azure data explorer

The aim of this project is to ingest data into ADX using CSV files stored into an Azure storage.

# Properties to overwrite :
Here is the list of properties that should be changed (in ```META-INF/microprofile-config.properties``` file):
* Send mode: 'direct' (default) or 'storage':
- send.mode
* Optional parameters for parameters and datasets
- csm.dataset.absolute.path
- csm.parameters.absolute.path
- csm.send.datawarehouse.parameters
- csm.send.datawarehouse.datasets
* Mandatory for direct mode:
- azure.tenant.id
- azure.client.id
- azure.client.secret
- csm.simulation.id
- azure.data.explorer.resource.uri
- azure.data.explorer.resource.ingest.uri
- azure.data.explorer.database.name
* Additional mandatory for storage mode:
- azure.storage.account.name
- azure.storage.account.key
- azure.storage.shared.access.signature.duration
- azure.storage.shared.access.signature.permissions
- azure.storage.shared.access.signature.services
- azure.storage.shared.access.signature.resource.types

If you want to overwrite these properties, you can write your own property values in the ```META-INF/microprofile-config.properties``` file, or set a property's system, or an environment variable named :

- SEND_MODE: the send mode, direct (default) or storage
- CSM_DATASET_ABSOLUTE_PATH: The dataset absolute path
- CSM_PARAMETERS_ABSOLUTE_PATH: The parameters absolute path
- CSM_SEND_DATAWAREHOUSE_PARAMETERS: whether or not to send parameters (parameters path is mandatory then)
- CSM_SEND_DATAWAREHOUSE_DATASETS: whether or not to send datasets (datasets path is mandatory then)
- AZURE_DATA_EXPLORER_RESOURCE_INGEST_URI : the ADX cluster ingest path (URI info can be found into ADX cluster page)
- AZURE_DATA_EXPLORER_RESOURCE_URI : the ADX cluster path (URI info can be found into ADX cluster page)
- AZURE_TENANT_ID : the Azure Tenant id (can be found under the App registration screen)
- AZURE_CLIENT_ID : the Azure client id (can be found under the App registration screen)
- AZURE_CLIENT_SECRET: the Azure client secret (can be found under the App registration screen)
- CSM_SIMULATION_ID: the Simulation Id to add to records (direct mode only)
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
      <groupId>com.github.Cosmo-Tech</groupId>
      <artifactId>azure-data-explorer-connector</artifactId>
      <version>1.0.3</version>
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
