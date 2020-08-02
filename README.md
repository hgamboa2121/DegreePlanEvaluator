## Overview
This program is intended for The University of Texas at El Paso Computer Science Department Staff and or Students. The purpose of this program is to evaluate the student's progression through the Computer Science Program. The user can enter the past or present grades in an Excel File, and the program will then output any missing courses, courses that need a better grade, and or courses that might need attention. The user can then perform any modifications to the program and run the program, and see the newer results.

## Description 

This program takes an Excel File as an input using Maven apache POI lib. The first thing it does, it finds the year it is going to be working with. Then the program loops across the entire file to find columns where there are any instances of present tables. Furthermore, the program then iterates across all tables to output certain criteria. 

## Gettings Started

### Dependencies

- The user MUST use the created template file created by the Computer Science Staff.

- The user may NOT change any table sizes, fixed text, or descriptions within the file. 

- The user may NOT change the name of the Excel File. The fixed file name is used to identify the location of the Excel File.

- The user may ONLY change the Course Name and or Course Decpriton if the text field is blank. For example, electives carry student option ranges. 

- The user MUST at least enter a Course Number if the user wants the course to be counted.

- Inside empty text filed options within the course number, the user MUST use the character "+" for the program to require a C or better. For more information please refer to the KEY at the bottom of the template. (ex. ENGL 1321+)

- The user MUST change the Core Curriculum Electives descriptions to actual course numbers along with the character "+" if they require a C or better. (Ex. Comp. Area Opt+ ----> BUSN 1301+) For courses that are allowable to be taken please refer to the KEY at the bottom of the template. If the course needs to be taken please leave default text inside the cell.

- The user MUST perform some modifications to the program to support older or newer catalog years. Comments inside the code that start with "//---> "are indicators of where and what modifications have to be done. 
  - The ONLY years that are currently supported are the following:
    - 2018-2019
    - 2019-2020

### Supporting another catalog 

The only changes that have to be made  to support another year are in the Main.java file located in: DegreePlanEvaluator/src/test/java/UTILS/Main.java

1. Create a global variable that is as follows
String supportedYearYEAR_YEAR = "Catalog YEAR_YEAR"; Where YEAR_YEAR is the year plan is going to be added. 

2. Create a new method like the following:
  - DegreePlanYEAR_YEAR();
  - Call each method to check the appropriate please reference notes inside code for additional details.
  
Note: About three or more small additional changes have to be made to the code to support another year. Please find comments that start "//---> " to find the rest of the changes that have to be made.

### Understanding the Code

Most of the action takes place within the main file named Main.java file located in: DegreePlanEvaluator/src/test/java/UTILS/Main.java
This java file has been separated into six sections:
1. Global variables to be accessed across the java file. 
2. The Main method, initiates the first call to execute the program.
3. Its followed by the methods that make the appropriate calls depending on what year we are dealing.
4. Methods that check grades according to a specific table and or a specific row.  
5. Utilities used across the file. Such as debugging methods, print methods...
6. Methods that hold arrays of data to verify if valid courses are being taken

These 6 sections are divided with comments such as the following:

	/*
	 *****************************************************************************************************************************
