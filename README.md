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
 

<img width="799" src="https://raw.githubusercontent.com/jennifersoft/jennifer-extension-manuals/master/res/img/view_server_batch/1.png">

### ID and class name mapping table for data classification ##

The following table shows the required options for this adapter

| Classification | ID | Class names |
| ------------- |:-------------:|:-------------:|
| METRICS_AS_INSTANCE | metrics_as_instance | batch.handler.metrics.InstanceFor**Oracle**<br>batch.handler.metrics.InstanceFor**Mssql**<br>batch.handler.metrics.InstanceFor**Mysql**<br> |
| METRICS_AS_BUSINESS | metrics_as_business | batch.handler.metrics.BusinessFor**Oracle**<br>batch.handler.metrics.BusinessFor**Mssql**<br>batch.handler.metrics.BusinessFor**Mysql**<br> |
| METRICS_AS_DOMAIN | metrics_as_domain | batch.handler.metrics.DomainFor**Oracle**<br>batch.handler.metrics.DomainFor**Mssql**<br>batch.handler.metrics.DomainFor**Mysql**<br> |
| APPLICATION_SERVICE | application_service | batch.handler.service.ApplicationFor**Oracle**<br>batch.handler.service.ApplicationFor**Mssql**<br>batch.handler.service.ApplicationFor**Mysql**<br> |

### Options ##

The following table shows the required options for this adapter

| Key           | Required      | Description | Example |
| ------------- |:-------------:|:-------------:|:-------------:|
| slack_webhook | YES           | Set Slack Incoming Webhook URL ||
| slack_channel | YES           | Set target destination for message. <br>You can either send messages to a Slack Channel (use #)  or to a Slack User (use @).  |1. Example sending notification to channel : #monitoring <br>2.Example sending notification to  user: @bob|
| message_color | NO            | Optional: Value to set message color using color's hex value | #551A8B
| slack_username| NO            | Option: This will change the "From" username when receiving slack message| JENNIFER Extension


<img width="802" alt="slack_adapter_options" src="https://user-images.githubusercontent.com/3861725/27722333-eef01af0-5da1-11e7-8235-c993c88580af.png">


zip -d jennifer-view-batch-rdb.jar META-INF/*.RSA META-INF/*.DSA META-INF*.SF
