# PGATool
A Java Tool to export and import tables as .csv files from and to a Postgress Database.

## Execution Steps
### To export:
Takes (3) parameters

(1) - EXPORT

(2) - Database configuration properties file

(3) - properties file with List of tables to export

```
java -jar PGATool EXPORT <database_properties>.properties <file_with_listoftables_to_export>.properties
```

### To import 
Takes (4) parameters

(1) - EXPORT

(2) - Database configuration properties file

(3) - Properties file with List of tables to export

(4) - Path to read the .csv files

```
java -jar PGATool IMPORT <database_properties>.properties <file_with_listoftables_to_export>.properties <path_to_store_the_backup>
```
### Example of <database_properties>.properties
Datbase properties to be used for *exporting/importing* the tables
```
url=jdbc:postgresql://localhost:5432/<databasename>
user=<username>
password=<password>
```

### Example of <file_with_listoftables_to_export>.properties
The tables are imported and exported in the same order as they are mentioned in this file.
```
table_1
table_5
table_2
```
