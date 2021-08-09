- Build environment details:
    - Requires java version - 11

    - Uses maven as the build and packaging tool

- Assumptions made:
    - The search is case-sensitive

    - The search is based on full value matching i.e. "mar" won't return "mary"

    - To search for a missing value type null on the command prompt when prompted with 
        'Enter search value' e.g. to search for tickets missing the type property do the following when prompted
        
        Enter search term
        
        type
        
        Enter search value
        
        null
    
    - Tag search is based on contains i.e. you can search for a specific value from the
      tag array e.g.
      
      Enter search term
      
      tags
      
      Enter search value
      
      Ohio
      
    - Date search assumes that the date provided is in ISO 8601 Time zone format otherwise
      an exception will be thrown e.g. 2016-07-28T05:29:25-10:00

- Instructions to run the program
    - Make sure you have java 11 and maven installed on your machine
    
    - Open command prompt at the root of this project and execute mvn clean install
      The command will compile the code, run the tests and create two jars in your target directory 
      CommandLineSearchApp-1.0-SNAPSHOT.jar and CommandLineSearchApp-1.0-SNAPSHOT-jar-with-dependencies.jar
    
    - Use the CommandLineSearchApp-1.0-SNAPSHOT-jar-with-dependencies.jar to test this solution as it has
      the java code and dependencies bundled into one jar file

    - To execute the CommandLineSearchApp-1.0-SNAPSHOT-jar-with-dependencies.jar file change directory
      to target folder and execute - 
      
      java -cp .\CommandLineSearchApp-1.0-SNAPSHOT-jar-with-dependencies.jar bootstrap.Cli

    - Type quit at any time to quit the program
