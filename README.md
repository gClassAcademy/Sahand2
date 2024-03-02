# Sahand2 

1-	Prerequisites to run the project: 
    
    a.	Install JDK 17 or above. 
    
    b.	Install Eclipse IDE4.21.0 or above. 
    
    c.	Install PostgreSQL 14 or above. 

2-	Restore the Sahand Model database into your DBMS
    
    a.	Create a user in PostgreSQL with username: gValAdmin	and password: 123
    
    b.	Create a database in PostgreSQL with name: gVal
    
    c.	Restore the blank Sahand Model from https://github.com/gClassAcademy/Sahand2/blob/main/gVal/src/main/resources/gVal.backup into gVal database. 

3-	Download the project source code from https://github.com/gClassAcademy/Sahand2

4-	Open the source code in Eclipse IDE. 

5-	Run the project and browse your Java Application to convert into Sahand Model. 

to any problem kindy contact me: Irani.gholamali@gmail.com

# To View Case Study Data (JavaParser or Tomcat Data)

1- Install PostgreSQL 14 or above from (https://www.enterprisedb.com/downloads/postgres-postgresql-downloads).

2- Download JavaParser or Tomcat databaase Backup file https://github.com/gClassAcademy/Sahand2/blob/main/gVal/src/main/resources/

3- Restore database backup file (JavaParser or Tomcat) into installed PostgreSQL. 
    Backup tutorial  is here: https://www.pgsclusters.com/docs/postgresql/Backing%20up%20and%20Restoring%20a%20Single%20Database%20via%20pgAdmin41605777509.html
    or follow these steps: 
    
    a. Open pgAdmin and connect to the PostgreSQL server where you want to restore the backup file.
    
    b. Right-click on the target database and select "Restore..." from the context menu.
    
    c. In the "Restore" dialog box, select the "Custom or Tar" format and specify the backup file you want to restore.
    
    d. On the "Options" tab, you can choose to include or exclude specific objects from the restore operation, set the restore mode, and adjust other settings.
    
    e. On the "Restore options" tab, you can specify additional command-line options for the restore operation.
    
    f. Click the "Restore" button to start the restore operation.
    
    g. After the restore operation completes, verify that the data has been successfully restored by connecting to the database and running queries to check the data.
    

4- open the restored database and explore the tables

