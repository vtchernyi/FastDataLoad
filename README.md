# FastDataLoad
The demo starts a simple application that populates the partitioned caches in the Apache Ignite cluster
with values that originate from large relational tables.

The application uses the org.h2.tools.Csv library to convert a CSV file into java.sql.ResultSet. The result
set is then pre-processed using the information about which cluster node every partition will be mapped to. When
the pre-processing phase is complete, the application uses the ComputeTaskAdapter to spread the data across
the cluster.

The demo is created for the Fast Load Large Datasets into Apache Ignite tutorial. Follow the steps of the
tutorial to learn how to work with the demo:
https://www.gridgain.com/resources/blog/how-fast-load-large-datasets-apache-ignite-using-key-value-api
