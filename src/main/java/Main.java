import testsmell.*;
import thresholds.DefaultThresholds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args == null) {
            System.out.println("Please provide the file containing the paths to the collection of test files");
            return;
        }
        if (!args[0].isEmpty()) {
            File inputFile = new File(args[0]);
            if (!inputFile.exists() || inputFile.isDirectory()) {
                System.out.println("Please provide a valid file containing the paths to the collection of test files");
                return;
            }
        }

        TestSmellDetector testSmellDetector = new TestSmellDetector(new DefaultThresholds());

        /*
          Read the input file and build the TestFile objects
         */
        BufferedReader in = new BufferedReader(new FileReader(args[0]));
        String str;

        String[] lineItem;
        TestFile testFile;
        List<TestFile> testFiles = new ArrayList<>();
        while ((str = in.readLine()) != null) {
            // use comma as separator
            lineItem = str.split(",");

            //check if the test file has an associated production file
            if (lineItem.length == 2) {
                testFile = new TestFile(lineItem[0], lineItem[1], "");
            } else {
                testFile = new TestFile(lineItem[0], lineItem[1], lineItem[2]);
            }

            testFiles.add(testFile);
        }

        /*
          Initialize the output file - Create the output file and add the column names
         */
        ResultsWriter resultsWriter = ResultsWriter.createResultsWriter();
        List<String> columnNames;
        List<String> columnValues;


        /*
          Iterate through all test files to detect smells and then write the output
        */
        TestFile tempFile;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date;
        for (TestFile file : testFiles) {
            date = new Date();
            System.out.println(dateFormat.format(date) + " Processing: " + file.getTestFilePath());
            System.out.println("Processing: " + file.getTestFilePath());

            //detect smells
            tempFile = testSmellDetector.detectSmells(file);

            //write output
            for (AbstractSmell smell : tempFile.getTestSmells()) {
                // TODO: etrapolare il nome dello smell, il metodo dello smell ed inserirlo nel csv

                if(smell.hasSmell()){
                    System.out.println(smell.getSmellName());
                    System.out.println(smell.getNumberOfSmellyTests());
                    Map<String, Set<String>> result = smell.getResult();
                    System.out.println(" \t " + result.values());
                    resultsWriter.writeResultOutput(smell.getResult());
                }
            }
        }

        System.out.println("end");
    }


}
