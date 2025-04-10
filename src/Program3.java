import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//***************************************************************
//
//  Developer:    Javier Salazar
//
//  Program #:    3
//
//  File Name:    Program3.java
//
//  Course:       COSC 4301 Modern Programming
//
//  Due Date:     04/14/2025
//
//  Instructor:   Prof. Fred Kumi
//
//  Description:  This program reads household survey data from a text file,
//                prints all records, calculates the average household income,
//                identifies households below the federal poverty level,
//                and determines Medicaid eligibility using formulas created
//                from REST API data retrieved for each state category.
//
//***************************************************************

public class Program3 {
    private ArrayList<HouseHold> households = new ArrayList<>();

    //***************************************************************
    //
    //  Method:       main
    //
    //  Description:  The main entry point for the program. Delegates
    //                all input, processing, and output to instance methods.
    //
    //  Parameters:   String[] args - command-line arguments (not used)
    //
    //  Returns:      void
    //
    //***************************************************************
    public static void main(String[] args) {
        Program3 program = new Program3();
        program.developerInfo();
        program.run();
    }

    //***************************************************************
    //
    //  Method:       run (Non Static)
    //
    //  Description:  Orchestrates the flow of the program by delegating
    //                work to helper methods for reading, processing, and output.
    //
    //  Parameters:   None
    //
    //  Returns:      None
    //
    //***************************************************************
    public void run() {
        readHouseholdsFromFile();
        printAllHouseholds();
        double averageIncome = calculateAverageIncome();
        printAboveAverageHouseholds(averageIncome);
        printHouseholdsBelowPoverty();
        checkMedicaidEligibility();
    }

    //***************************************************************
    //
    //  Method:       readHouseholdsFromFile (Non Static)
    //
    //  Description:  Reads household data from Program3.txt and stores
    //                it in an ArrayList.
    //
    //  Parameters:   None
    //
    //  Returns:      None
    //
    //***************************************************************
    private void readHouseholdsFromFile() {
        try {
            Scanner scanner = new Scanner(new File("Program3.txt"));
            while (scanner.hasNext()) {
                int id = scanner.nextInt();
                double income = scanner.nextDouble();
                int members = scanner.nextInt();
                String state = scanner.nextLine().trim();
                households.add(new HouseHold(id, income, members, state));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File Program3.txt not found.");
        }
    }

    //***************************************************************
    //
    //  Method:       printAllHouseholds (Non Static)
    //
    //  Description:  Prints all household records from the list.
    //
    //  Parameters:   None
    //
    //  Returns:      None
    //
    //***************************************************************
    private void printAllHouseholds() {
        System.out.println("\nALL HOUSEHOLDS:");
        System.out.printf("%-10s %-15s %-10s %s\n", "ID", "Income", "Members", "State");
        System.out.println("---------------------------------------------------------------");
        for (HouseHold h : households) {
            System.out.println(h);
        }
    }

    //***************************************************************
    //
    //  Method:       calculateAverageIncome (Non Static)
    //
    //  Description:  Calculates and displays the average household income.
    //
    //  Parameters:   None
    //
    //  Returns:      double - the average income
    //
    //***************************************************************
    private double calculateAverageIncome() {
        double totalIncome = 0;
        for (HouseHold h : households) {
            totalIncome += h.getIncome();
        }
        double average = totalIncome / households.size();
        System.out.printf("\nAverage Household Income: $%.2f\n", average);
        return average;
    }

    //***************************************************************
    //
    //  Method:       printAboveAverageHouseholds (Non Static)
    //
    //  Description:  Prints households with income above the average.
    //
    //  Parameters:   double averageIncome - the calculated average income
    //
    //  Returns:      None
    //
    //***************************************************************
    private void printAboveAverageHouseholds(double averageIncome) {
        System.out.println("\nHOUSEHOLDS WITH INCOME ABOVE AVERAGE:");
        System.out.printf("%-10s %-15s %-10s %s\n", "ID", "Income", "Members", "State");
        System.out.println("---------------------------------------------------------------");
        for (HouseHold h : households) {
            if (h.getIncome() > averageIncome) {
                System.out.println(h);
            }
        }
    }

    //***************************************************************
    //
    //  Method:       printHouseholdsBelowPoverty (Non Static)
    //
    //  Description:  Identifies and prints households below the poverty level.
    //
    //  Parameters:   None
    //
    //  Returns:      None
    //
    //***************************************************************
    private void printHouseholdsBelowPoverty() {
        System.out.println("\nHOUSEHOLDS BELOW 2025 FEDERAL POVERTY LEVEL:");
        System.out.printf("%-10s %-15s %-15s %-10s %s\n", "ID", "Income", "PovertyLevel", "Members", "State");
        System.out.println("--------------------------------------------------------------------------");
        int belowPovertyCount = 0;

        for (HouseHold h : households) {
            if (h.getIncome() < h.getPovertyLevel()) {
                System.out.println(h.toPovertyString());
                belowPovertyCount++;
            }
        }

        double percentage = ((double) belowPovertyCount / households.size()) * 100;
        System.out.printf("\nPercentage of Households Below Poverty Level: %.2f%%\n", percentage);
    }

    //***************************************************************
    //
    //  Method:       checkMedicaidEligibility (Non Static)
    //
    //  Description:  Uses API-generated formulas to determine Medicaid eligibility
    //                for each household and prints the results.
    //
    //  Parameters:   None
    //
    //  Returns:      None
    //
    //***************************************************************
    private void checkMedicaidEligibility() {
        System.out.println("\nPART F: MEDICAID ELIGIBILITY CHECK (138% of FPL)");

        double[] usFormula = PovertyAPIClient.buildFormula("us", 1, 4);
        double[] akFormula = PovertyAPIClient.buildFormula("ak", 1, 4);
        double[] hiFormula = PovertyAPIClient.buildFormula("hi", 1, 4);

        System.out.printf("\nFormula for Contiguous US: poverty = %.2f + %.2f × (members - %d)\n",
                usFormula[0], usFormula[1], (int) usFormula[2]);
        System.out.printf("Formula for Alaska: poverty = %.2f + %.2f × (members - %d)\n",
                akFormula[0], akFormula[1], (int) akFormula[2]);
        System.out.printf("Formula for Hawaii: poverty = %.2f + %.2f × (members - %d)\n",
                hiFormula[0], hiFormula[1], (int) hiFormula[2]);

        int eligibleCount = 0;

        for (HouseHold h : households) {
            String state = h.getState().toLowerCase();
            double base, increment, baseSize;

            if (state.equals("alaska")) {
                base = akFormula[0];
                increment = akFormula[1];
                baseSize = akFormula[2];
            } else if (state.equals("hawaii")) {
                base = hiFormula[0];
                increment = hiFormula[1];
                baseSize = hiFormula[2];
            } else {
                base = usFormula[0];
                increment = usFormula[1];
                baseSize = usFormula[2];
            }

            double povertyLevel = base + increment * (h.getMembers() - baseSize);
            double medicaidThreshold = povertyLevel * 1.38;

            if (h.getIncome() < medicaidThreshold) {
                eligibleCount++;
            }
        }

        double percentEligible = ((double) eligibleCount / households.size()) * 100;
        System.out.printf("\nNumber of Households Eligible for Medicaid: %d\n", eligibleCount);
        System.out.printf("Percentage of Households Eligible for Medicaid: %.2f%%\n", percentEligible);
    }

    //***************************************************************
    //
    //  Method:       developerInfo (Non Static)
    //
    //  Description:  Displays developer and course information.
    //
    //  Parameters:   None
    //
    //  Returns:      None
    //
    //***************************************************************
    public void developerInfo() {
        System.out.println("Name: Javier Salazar");
        System.out.println("Course: COSC 4301 Modern Programming");
        System.out.println("Program #: 3");
    }
}

