# PGATool
A Java Tool to export and import tables as .csv files from and to a Postgress Database.

## Execution Steps
Takes (2) parameters

(1) - EXPORT/IMPORT

(2) - pga tool configuration file [<pgatool_properties>.properties]

### To export:
```
java -jar PGATool EXPORT <pgatool_properties>.properties 
```

### To import 

```
java -jar PGATool IMPORT <pgatool_properties>.properties
```
### Example of  [<pgatool_properties>.properties]
Configuration properties to be used for **EXPORT/IMPORT** the tables
```
url=jdbc:postgresql://localhost:5432/<databasename>
user=<username>
password=<password>
tables=<file_path_with_listoftables_to_export>.properties
backupPath=<path_of_backup_location>
```

### Example of <file_path_with_listoftables_to_export>.properties
The tables are imported and exported in the same order as they are mentioned in this file.
```
table_1
table_5
table_2
```
