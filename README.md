## Overview
It is an extension function to backup JENNIFER data to RDB.


## Getting started

The first step is to register the adapter: 
1. In JENNIFER Client, open the management area and Navigate to  **Extension and Notice** > **Adapter and Plugin**
2. Make sure the adapter tab is selected then click the **[+Add]** button
3. Select the data classification.
4. Enter an ID according to the data classification. The ID types are discussed in detail below.
5. Enter the path to the adapter JAR file ``jennifer-view-batch-rdb.jar`` or upload the JAR file from you local machine.
6. Enter the class name according to the data classification. Class types are discussed in detail below.
 
<img src="https://raw.githubusercontent.com/jennifersoft/jennifer-extension-manuals/master/res/img/view_server_batch/1.png">


### ID and class name mapping table for data classification ##

The following table shows the required options for this adapter

| Classification | ID | Class names |
| ------------- |:-------------:|:-------------:|
| METRICS_AS_INSTANCE | metrics_as_instance | batch.handler.metrics.InstanceFor**Oracle**<br>batch.handler.metrics.InstanceFor**Mssql**<br>batch.handler.metrics.InstanceFor**Mysql**<br> |
| METRICS_AS_BUSINESS | metrics_as_business | batch.handler.metrics.BusinessFor**Oracle**<br>batch.handler.metrics.BusinessFor**Mssql**<br>batch.handler.metrics.BusinessFor**Mysql**<br> |
| METRICS_AS_DOMAIN | metrics_as_domain | batch.handler.metrics.DomainFor**Oracle**<br>batch.handler.metrics.DomainFor**Mssql**<br>batch.handler.metrics.DomainFor**Mysql**<br> |
| APPLICATION_SERVICE_DAILY | application_service | batch.handler.service.ApplicationFor**Oracle**<br>batch.handler.service.ApplicationFor**Mssql**<br>batch.handler.service.ApplicationFor**Mysql**<br> |
| APPLICATION_SERVICE_HOURLY | application_service | batch.handler.service.ApplicationFor**Oracle**<br>batch.handler.service.ApplicationFor**Mssql**<br>batch.handler.service.ApplicationFor**Mysql**<br> |


### Options for database connections ##

The following table shows the required options for this batch

| Key           | Required      | Default |
| ------------- |:-------------:|:-------------:|
| url | YES | |
| user | YES | |
| password | YES | |
| unique_table | NO | false |
| min_pool_size | NO | 5 |
| acquire_increment | NO | 5 |
| max_pool_size | NO | 20 |
| max_statements | NO | 180 |

<img src="https://github.com/jennifersoft/jennifer-extension-manuals/blob/master/res/img/view_server_batch/2.png">


### Top toolbar description ##

| Name | Description |
| ------------- |:-------------:|
| Manual Build | Daily backups can be done manually. |
| Auto build settings | It is an automatic build related setting. |
| Show build logs | You can view the manual and automatic build logs. |


### Supported version ##
 
| Adapter version           | Jennifer server version |
| ------------- |:-------------:|
| 0.0.4       | Greater than or equal to version 5.3.0.8 |
| 0.0.6       | Greater than or equal to version 5.3.0.13 |