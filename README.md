# Zendesk-Coding-Chellenge
This repository contains code for the Zendesk Coding Challenge completed as a part of the Summer Intern recruitment process.

To run,
1. Clone the repository.
2. Update the subdomain, email and password variables in connection.java with your login credentials, for establishing connections via the Zendesk API.
3. In terminal, change to src directory, and run the following command.  
    javac -classpath ".:../JARS/*" initiator.java  
    Please replace : with ; when executing on Windows.  
3. Once it has compiled successfully, run below command to start execution.  
    java -classpath ".:../JARS/*" initiator  
    Please replace : with ; when executing on Windows.
    
Functionalities:  
The Zendesk Ticket Viewer can be used to display Tickets created on Zendesk.  
It can display these in 2 ways,  
1. Display All Tickets:  
   This option displays all the tickets stored at Zendesk at the mooment.  
   The tickets are displayed in pages, with each page having 25 tickets each.
2. Display Single Ticket based on it's ID:  
   This option displays the content for a particular ticket ID.
