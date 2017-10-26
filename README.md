## Important Notice
Please use the **`old`** branch if you are using JENNIFER Server prior to **`5.3.0.8`** 

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
