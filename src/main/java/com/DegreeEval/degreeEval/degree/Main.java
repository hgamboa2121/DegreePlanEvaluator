package com.DegreeEval.degreeEval.degree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {

    /*
     *****************************************************************************************************************************
     * START Variables will be changed and used across the program
     */
    //Used to get file path of Excel File.
    private String excelPath = "";
    private ExcelUTILS excel;
    private int totalRowCount;
    //String catalogYear = "";

    //Used to identify supported Degree Plans
    private String supportedYear2018_2019 = "Catalog 2018-2019";
    private String supportedYear2019_2020 = " Catalog 2019-2020";
    //------> Add new variable to support another year

    //Used to provide criteria to the user
    private ArrayList<String> coursesNeeded = new ArrayList<String>();
    private ArrayList<String> coursesNeedsAttention = new ArrayList<String>();
    private ArrayList<String> coursesNeedBetterGrade = new ArrayList<String>();
    private ArrayList<String> coursesPassed = new ArrayList<String>();
    private int totalLowerDivisionHours = 0;
    private int totalUpperDivisionHours = 0;

    //Used  for row and column of where the words HR are presented in lower division
    private int startPointRowLowerDivision;
    private int startPointColLowerDivision;

    //Used  for row and column of where the words HR are presented in lower division
    private int startPointRowUpperDivision;
    private int startPointColUpperDivision;

    /*
     * END of variables
     *****************************************************************************************************************************
     */
    
    public HashMap<String, ArrayList<String>> processFile(String path)throws IOException{
        excel = new ExcelUTILS(path);
        totalRowCount = ExcelUTILS.getRowCount();
        String catalogYear =  excel.getCellData(1, 0).toString();

        if(catalogYear.equals(supportedYear2018_2019)) {
            findStartPoints();
            DegreePlan2018_2019();
        }
        else if(catalogYear.equals(supportedYear2019_2020)) {
            findStartPoints();
            DegreePlan2019_2020();
        }
         // import the HashMap class

        HashMap<String, ArrayList<String>> results = new HashMap<String, ArrayList<String>>();
        results.put("needed", coursesNeeded);  
        results.put("grades", coursesNeedBetterGrade); 
        results.put("attention", coursesNeedsAttention);
        results.put("passed", coursesPassed); 
        return results; 
    }
    //-----------> create a new method to support another year.
    /*
     * How to create a new method to support another year:
     *
     *
     * 1. Use the method checkGrades when fixed course number is set and given.
     *
     * 2. Use checkCoreCurr method ONLY on Core Curriculum Electives to check grades and allowable course.
     *
     * 3. Use checkFreeElec  method ONLY on Free Electives to check grades and allowable course.
     *
     * 4. Use checkLifeAndPhy method ONLY ON Life and Physical Sciences to check grades and allowable course.
     *
     * 5. Use checkLifeAndPhy method ONLY ON Life and Physical Sciences to check grades and allowable course.
     *
     * 6. Use checkTechElec method ONLY ON Technical Electives to check grades and allowable course.
     *
     *
     * Note 1: all methods require parameters as follow...... the start row, the column, and the end row.
     * "the start row" : Needs to be the row you want the program to start looping will skip any blank rows between tables.
     * "the column": what column/division to work on. the column must be the column that has the amount of hours a course gives the student.
     * "the end row": must be the end row where the program ends the loop.
     * Note 2: You can use the variables "startPoint..." and the code finds the start points by finding the words "HR" in the file.
     * Note 3: When given the row make sure that you give the row -1. "WE COUNT FROM ZERO!"
     */
    public void DegreePlan2018_2019(){
        try {
            //Lower Division
            checkGrades(startPointRowLowerDivision,startPointColLowerDivision,18);
            checkCoreCurr(19,startPointColLowerDivision, 23);
            checkGrades(24,startPointColLowerDivision,33);
            checkFreeElec(36,startPointColLowerDivision, 36);
            checkGrades(39,startPointColLowerDivision, 39);
            checkLifeAndPhy(40, startPointColLowerDivision, 41);
            //Upper Division
            checkGrades(startPointRowUpperDivision,startPointColUpperDivision,14);
            checkTechElec(17,startPointColUpperDivision,23);
            printLists();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void DegreePlan2019_2020() {
        try {
            //Lower Division
            checkGrades(startPointRowLowerDivision,startPointColLowerDivision,17);
            checkCoreCurr(18,startPointColLowerDivision, 22);
            checkGrades(23,startPointColLowerDivision,34);
            checkLifeAndPhy(35, startPointColLowerDivision, 35);
            checkAddMath(38,startPointColLowerDivision, 38);
            //Upper Division
            checkGrades(startPointRowUpperDivision,startPointColUpperDivision,14);
            checkTechElec(17,startPointColUpperDivision,22);
            checkFreeElec(25,startPointColLowerDivision, 6);
            printLists();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }








    /*
     *****************************************************************************************************************************
     * START Methods to check grades for given tables
     */
	/*Method checkGrades checks grades, for a given set of rows.
	this method does not handle blank rows, or incorrect courses please refer to manual/about!!!
	This method should only be used if courses have been checked to be valid courses.
	"startRow" MUST BE A CELL THAT contains the hours to a course. */
    public void checkGrades(int startRowCell, int startColCell, int endRow) throws IOException{
        char division = checkDivision(startColCell);
        int tempCountHours = 0;
        //Loop across start cells until end cells
        for (int i = startRowCell; i <= endRow; i++) {
            try {
                //Although White spaces are handled in other method, precaution is taken with empty strings and with try catch.
                String stringNumberOfHours = excel.getCellData(i, startColCell).toString();
                if(!stringNumberOfHours.equals("")) {
                    int hours =Integer.parseInt(stringNumberOfHours);
                    int temp = 0;
                    if(division == 'U') {
                        temp = startPointColLowerDivision+2;
                    }
                    String courseNum = excel.getCellData(i, temp).toString();
                    courseNum = cleanInput(courseNum);
                    String grade = excel.getCellData(i, startColCell-1).toString();
                    grade = cleanInput(grade);
                    //Case 1: Valid course does not have grade
                    if(!courseNum.equals("") && grade.equals("")) {
                        coursesNeeded.add(courseNum);
                    }
                    //Case 2: Valid course has grade
                    else if(!courseNum.equals("") && !grade.equals("")) {
                        //Case 2.1: Valid course requires C or better(has plus sign).
                        if(courseNum.contains("+")) {
                            if(grade.equals("a") || grade.equals("b") || grade.equals("c")) {
                                tempCountHours = tempCountHours + hours;
                                coursesPassed.add(courseNum);
                            }
                            else if(!grade.equals("a") ||!grade.equals("b") || !grade.equals("c")) {
                                if(grade.equals("f") || grade.equals("d")) {
                                    coursesNeedBetterGrade.add(courseNum);
                                }
                                else {
                                    coursesNeedsAttention.add(courseNum);
                                }
                            }
                        }
                        //Case 2.2 Course requires D or better(does not have plus sign).
                        else {
                            if(grade.equals("a") || grade.equals("b") || grade.equals("c") || grade.equals("d")) {
                                tempCountHours = tempCountHours + hours;
                                coursesPassed.add(courseNum);
                            }
                            else if(!grade.equals("a") ||!grade.equals("b") || !grade.equals("c")  || !grade.equals("d")) {
                                if(grade.equals("f")) {
                                    coursesNeedBetterGrade.add(courseNum);
                                }
                                else {
                                    coursesNeedsAttention.add(courseNum);
                                }
                            }
                        }
                    }
                }
            }catch(NullPointerException NPE) {
                continue;
            }

        }
        //If grade and course meets qualifications a sum of course hours is added and set to variables for later reference
        if(division == 'L') {
            totalLowerDivisionHours=totalLowerDivisionHours+tempCountHours;
        }
        else if(division == 'U'){
            totalUpperDivisionHours=totalUpperDivisionHours+tempCountHours;
        }

    }
    /*
     * chcekCoreCurr():
     * Iterates through table "Core Curriculum" and finds if electives are allowable.
     * the input "int start row" must be the row where the first elective beings
     * the input "int endRowCell" must be the last elective row.
     *
     *
     */

    public void checkCoreCurr(int startRowCell, int startColCell, int endRowCell) throws IOException {
        //Checks the division/left or Right side table of the Excel Sheet
        char division = checkDivision(startColCell);
        int temp = 0;
        int counter = 0;
        for (int i = startRowCell; i <= endRowCell; i++) {
            try {
                if(division == 'U') {
                    temp = startPointColLowerDivision+2;
                }
                String courseNum = cleanInput(excel.getCellData(i, temp).toString());
                String courseDes = cleanInput(excel.getCellData(i, temp+1).toString());
                // The if statements compare to the number 10 since a course Number is no longer than 10 chars ex. ENGL 1212+
                //Case # 1: Empty
                if(courseNum.length() > 10 && (courseDes.equals(""))) {
                    coursesNeeded.add(courseNum);
                }
                //Case # 2 course number is present but no description of course.
                else if(courseNum.length() > 10 && (!courseDes.equals(""))) {
                    coursesNeedsAttention.add(courseNum);
                }
                //Case # 3: Check to see what Core curriculum elective we are checking.
                else if(courseNum.length() <= 10 && (!courseDes.equals(""))){
                    if(counter == 0) {
                        langPhilCul(courseNum, i,startColCell);
                    }
                    else if(counter == 1) {
                        creatArt(courseNum, i, startColCell);
                    }
                    else if(counter == 2) {
                        socBeh(courseNum, i, startColCell);
                    }
                    else if(counter == 3) {
                        comp(courseNum, i, startColCell);
                    }
                    else if(counter == 4) {
                        comp(courseNum, i, startColCell);
                    }

                }
                else {
                    coursesNeedsAttention.add(courseNum);

                }
            } catch (NullPointerException NPE) {
                continue;
            }
        }
        counter++;
    }
    /*
     * checkFreeElec():
     * Checks to see if valid electives are correct.
     *
     *
     */

    public void checkFreeElec(int startRowCell,int startColCell, int endRowCell) throws IOException {
        //Checks the division/left or Right side table of the Excel Sheet
        char division = checkDivision(startColCell);
        int temp = 0;
        int numberOfFreeElecCounter = 1;
        for (int i = startRowCell; i <= endRowCell; i++) {
            try {
                if(division == 'U') {
                    //temp is the column where the course numbers are present, if the the division is a U then the column
                    //to where the course number is contain(upper division) its always less columns that where hours are contained in lower division.
                    temp = startPointColLowerDivision+2;
                }
                String courseNum = cleanInput(excel.getCellData(i, temp).toString());
                if(!courseNum.equals("")) {
                    checkGrades(startRowCell,startColCell,endRowCell);
                }
                else {
                    //If the cell is blank "Free Elective" will be added to the list of missing courses.
                    coursesNeeded.add("FREE ELECTIVE("+numberOfFreeElecCounter+")");
                    numberOfFreeElecCounter++;
                }

            }catch (NullPointerException NPE) {
                continue;
            }
        }
    }
    /*
     * checkLifeAndPhy():
     *
     *  finds if Life and Physical Sciences are allowable courses
     *
     */

    public void checkLifeAndPhy(int startRowCell,int startColCell, int endRowCell) throws IOException{
        //Checks the division/left or Right side table of the Excel Sheet
        char division = checkDivision(startColCell);
        int temp = 0;
        int lifeandPhyCounter = 1;
        for (int i = startRowCell; i <= endRowCell; i++) {
            try {
                if(division == 'U') {
                    //temp is the column where the course numbers are present, if the the division is a U then the column
                    //to where the course number is contain(upper division) its always less columns that where hours are contained in lower division.
                    temp = startPointColLowerDivision+2;
                }
                String courseNum = cleanInput(excel.getCellData(i, temp).toString());
                if(!courseNum.equals("")) {
                    lifePhy(courseNum, startRowCell, startColCell);
                }
                else {
                    //If the cell is blank "Life and Physical Sciences" will be added to the list of missing courses.
                    coursesNeeded.add("Life and Physical Sciences("+lifeandPhyCounter+")");
                    lifeandPhyCounter++;
                }

            }catch (NullPointerException NPE) {
                continue;
            }
        }
    }
    /*
     * checkAddMath():
     *
     *  finds if Life and additional math are allowable courses
     *
     */
    public void checkAddMath(int startRowCell,int startColCell, int endRowCell) throws IOException{
        //Checks the division/left or Right side table of the Excel Sheet
        char division = checkDivision(startColCell);
        int temp = 0;
        int addMathCounter = 1;
        for (int i = startRowCell; i <= endRowCell; i++) {
            try {
                if(division == 'U') {
                    //temp is the column where the course numbers are present, if the the division is a U then the column
                    //to where the course number is contain(upper division) its always less columns that where hours are contained in lower division.
                    temp = startPointColLowerDivision+2;
                }
                String courseNum = cleanInput(excel.getCellData(i, temp).toString());
                if(!courseNum.equals("")) {
                    addMath(courseNum, startRowCell, startColCell);
                }
                else {
                    coursesNeeded.add("Additional Mathematics or Science("+addMathCounter+")");
                    addMathCounter++;
                }

            }catch (NullPointerException NPE) {
                continue;
            }
        }
    }

    /*
     * checkTechElec():
     *
     *  finds if Life and Physical Sciences are allowable courses
     *
     */
    public void checkTechElec(int startRowCell,int startColCell, int endRow) throws IOException{
        //Checks the division/left or Right side table of the Excel Sheet
        char division = checkDivision(startColCell);
        int tempCountHours = 0;
        int techElecCounter = 0;
        int currentHours = totalUpperDivisionHours;
        int coursesNeedBetterGradeCounter = 0;
        //Loop across start cells until end cells
        for (int i = startRowCell; i <= endRow; i++) {
            try {
                //Although White spaces are handled in other method, precaution is taken with empty strings and with try catch.
                String stringNumberOfHours = excel.getCellData(i, startColCell).toString();
                if(!stringNumberOfHours.equals("") && techElecCounter != 5) {
                    int hours =Integer.parseInt(stringNumberOfHours);
                    int temp = 0;
                    if(division == 'U') {
                        //temp is the column where the course numbers are present, if the the division is a U then the column
                        //to where the course number is contain(upper division) its always less columns that where hours are contained in lower division.
                        temp = startPointColLowerDivision+2;
                    }
                    String courseNum = excel.getCellData(i, temp).toString();
                    techElec(courseNum);

                    courseNum = cleanInput(courseNum);
                    String grade = excel.getCellData(i, startColCell-1).toString();
                    grade = cleanInput(grade);

                    //Case 1: Valid course does not have grade
                    if(!courseNum.equals("") && grade.equals("")) {
                        coursesNeeded.add(courseNum);
                    }
                    //Case 2: Valid course has grade
                    else if(!courseNum.equals("") && !grade.equals("")) {
                        //Case 2.1: Valid course requires C or better(has plus sign).
                        if(courseNum.contains("+")) {
                            if((grade.equals("a") || grade.equals("b") || grade.equals("c"))) {
                                tempCountHours = tempCountHours + hours;
                                techElecCounter++;
                                coursesPassed.add(courseNum);
                            }
                            else if(!grade.equals("a") ||!grade.equals("b") || !grade.equals("c")) {
                                if(grade.equals("f") || grade.equals("d")) {
                                    coursesNeedBetterGrade.add(courseNum);
                                    coursesNeedBetterGradeCounter++;
                                }
                                else {
                                    coursesNeedsAttention.add(courseNum);
                                }
                            }
                        }
                        //Case 2.2 Course requires D or better(does not have plus sign).
                        else {
                            if((grade.equals("a") || grade.equals("b") || grade.equals("c") || grade.equals("d"))) {
                                tempCountHours = tempCountHours + hours;
                                techElecCounter++;
                                coursesPassed.add(courseNum);
                            }
                            else if(!grade.equals("a") ||!grade.equals("b") || !grade.equals("c")  || !grade.equals("d")) {
                                if(grade.equals("f")) {
                                    coursesNeedBetterGrade.add(courseNum);
                                    coursesNeedBetterGradeCounter++;
                                }
                                else {
                                    coursesNeedsAttention.add(courseNum);
                                }
                            }
                        }
                    }
                }
            }catch(NullPointerException NPE) {
                continue;
            }

        }
        //If grade and course meets qualifications a sum of course hours is added and set to variables for later reference
        if(division == 'L') {
            totalLowerDivisionHours=totalLowerDivisionHours+tempCountHours;
        }
        else if(division == 'U'){
            totalUpperDivisionHours=totalUpperDivisionHours+tempCountHours;
        }
        int hourDiff = totalUpperDivisionHours - currentHours;
        int numberOfCourses = hourDiff/3;
        int length = coursesNeedBetterGrade.size();
        int missingCourses = 5- numberOfCourses;
        if(hourDiff == 15) {
            while(length != 0) {
                int index = length -1;
                coursesNeedBetterGrade.remove(index);
                length--;
            }

        }

        else if(hourDiff < 15 ){
            for (int i = 1; i <= missingCourses; i++) {
                coursesNeeded.add("Technical Elective("+i+")");
            }
        }

    }
    //-----------> create a new method to support another table
    /*
     *****************************************************************************************************************************
     * END to check grades for given tables
     */








    /*
     *****************************************************************************************************************************
     * START to utilities for checking grades
     */
    public void printLists() {
        System.out.println("****************************************************8***********************");
        System.out.println("* Missing Courses: ");
        System.out.println("* "+coursesNeeded);
        System.out.println("****************************************************************************");
        System.out.println();
        System.out.println();
        System.out.println("****************************************************************************");
        System.out.println("* Courses need better grade:");
        System.out.println("* "+coursesNeedBetterGrade);
        System.out.println("****************************************************************************");
        System.out.println();
        System.out.println();
        System.out.println("****************************************************************************");
        System.out.println("* Courses that need attention: ");
        System.out.println("* "+coursesNeedsAttention);
        System.out.println("****************************************************************************");
        System.out.println("* Courses that Passed ");
        System.out.println("* "+coursesPassed);
        System.out.println("****************************************************************************");

    }
    
    /*
     * findStartPoints():
     * Finds the start points in the files to start iterating/loop.
     * The start points can be identified  by the words "HR"
     * This method does not return anything but does set values to variables that can be used later.
     * these variables contain the row and column number of the words "HR"
     *
     */

    private void findStartPoints() throws IOException{
        int HRCount = 0;
        for (int i = 0; i <= totalRowCount; i ++) {
            for (int j = 0; j <= 26; j++) {
                try {
                    String currentCellData = excel.getCellData(i, j).toString();
                    if(currentCellData.equals("HR") && HRCount == 0) {
                        startPointRowLowerDivision = i+1;
                        startPointColLowerDivision = j;
                        HRCount = 1;
                    }

                    else if(currentCellData.equals("HR") && HRCount == 1) {
                        startPointRowUpperDivision = i+1;
                        startPointColUpperDivision = j;
                    }

                }catch(NullPointerException NPE) {
                    continue;
                }
            }
        }

    }

    public String cleanInput(String userInput) {
        userInput = userInput.replaceAll("\\s","");
        String clean = userInput.toLowerCase();
        return clean;
    }
    public char checkDivision(int startColCell) {
        //Checks the division/left or Right side table of the Excel Sheet
        char division;
        if(startColCell == startPointColLowerDivision) {
            division = 'L';
        }
        else {
            division = 'U';
        }
        return division;
    }
    public void printList(ArrayList<String> array) {

        System.out.println(array);
    }
    public void arrayIterr(String courseNum, int row, String[] array, int col) throws IOException {
        for (int i = 0; i < array.length; i++) {
            if(array[i].equals(courseNum)) {
                checkGrades(row, col, row);
                return;
            }
            if(i == array.length && !array[i].equals(courseNum)) {
                coursesNeedsAttention.add(courseNum );
            }
        }

    }
    /*
     *****************************************************************************************************************************
     * END to utilities for checking grades
     */









    /*
     *****************************************************************************************************************************
     * START to methods that hold arrays to check for valid elective courses.
     */
    public void langPhilCul(String courseNum, int row, int col) throws IOException {
        String[] langPhilCulArray = new String[] {"engl2311+","engl2312+","engl2313+",
                "engl2314+","engl2318+","fren2322+","hist2301+","hist2302+",
                "phil1201+","phil2306+","rs1301+","span2340+","ws2300+","ws2350+"};

        arrayIterr(courseNum, row, langPhilCulArray, col);
    }
    public void creatArt(String courseNum, int row, int col) throws IOException{
        String[] creatArtArray = new String[] {"art1300+","arth1305+","arth1360+",
                "danc1340+","film1390+","musl1324+","musl1327+","musl2321+","thea1313+"};
        arrayIterr(courseNum, row, creatArtArray, col);
    }
    public void socBeh(String courseNum, int row, int col) throws IOException{
        String[] socBehArray = new String[] {"anth1301+","anth1302+","anth1310+","anth2320+",
                "ce2326+","comm2350+","comm2372+","econ2303+","econ2304+","edpc1301+","edu1342+",
                "engl2320+", "geog1310+","ling2320+","ling2340+","psyc1301+","soci1301+","soci1310+"};
        arrayIterr(courseNum, row, socBehArray, col);
    }
    public void comp(String courseNum, int row, int col) throws IOException{
        String[] compArray = new String[] {"busin1301+","comm1301+","comm1302+","cs1310+",
                "cs1320+","sci1301+","univ1301+"};
        arrayIterr(courseNum, row, compArray, col);
    }


    public void lifePhy(String courseNum, int row, int col) throws IOException{
        String[] lifePhyArray = new String[] {"astr1307", "astr1107", "phys2421", "biol1305",
                "biol1107", "biol1306",  "biol1108","chem1305", "chem1105", "chem1306",
                "chem1106", "geol1313", "geol1103", "geol1314", "geol1104"};
        arrayIterr(courseNum, row, lifePhyArray, col);
    }
    public void addMath(String courseNum, int row, int col) throws IOException{
        String[] addMathArray = new String[] {"math2313", "math2325", "math2326", "math3320",
                "math325", "math4329","stat3381", "stat4380", "stat4385", "biol1305",
                "biol1107", "biol1306",  "biol1108","chem1305", "chem1105", "chem1306",
                "chem1106", "geol1313", "geol1103", "geol1314", "geol1104"};
        arrayIterr(courseNum, row, addMathArray, col);
    }
    public void techElec(String courseNum) throws IOException{
        courseNum = cleanInput(courseNum);
        if(!courseNum.equals("")) {
            char C = courseNum.charAt(0);
            char S = courseNum.charAt(1);
            String checkIfCS = new StringBuilder().append(C).append(S).toString();
            String newCourseNum = courseNum;
            newCourseNum = newCourseNum.substring(1);
            newCourseNum = newCourseNum.substring(1);
            int justCourseNum = Integer.parseInt(newCourseNum);

            if(!checkIfCS.equals("cs") ) {
                coursesNeedsAttention.add(courseNum);
            }
            else if((justCourseNum < 3000 || justCourseNum > 4999)){
                if(justCourseNum != 1190){
                    if(justCourseNum != 1290) {
                        coursesNeedsAttention.add(courseNum);
                    }

                }
            }
        }

    }
    /*
     *****************************************************************************************************************************
     * END to methods that hold arrays to check for valid elective courses.
     */
}
