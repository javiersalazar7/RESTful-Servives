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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Program3 {
    //***************************************************************
    //
    //  Method:       main
    //
    //  Description:  The main entry point for the program. It reads household data
    //                from Program3.txt, prints all records, calculates average income,
    //                identifies households below poverty level, and determines Medicaid
    //                eligibility using API-generated formulas.
    //
    //  Parameters:   String[] args - command-line arguments (not used)
    //
    //  Returns:      void
    //
    //***************************************************************

    public static void main(String[] args) {

        // Display developer info at the start of the program
        Program3 dev = new Program3();
        dev.developerInfo();

        ArrayList<HouseHold> households = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File("Program3.txt"));

            while (scanner.hasNext()) {
                int id = scanner.nextInt();
                double income = scanner.nextDouble();
                int members = scanner.nextInt();
                String state = scanner.nextLine().trim();

                households.add(new HouseHold(id, income, members, state));
            }

            // Part a) Print all records
            System.out.println("\nALL HOUSEHOLDS:");
            System.out.printf("%-10s %-15s %-10s %s\n", "ID", "Income", "Members", "State");
            System.out.println("---------------------------------------------------------------");
            for (HouseHold h : households) {
                System.out.println(h);
            }

            // Part b) Calculate and print average income
            double totalIncome = 0;
            for (HouseHold h : households) {
                totalIncome += h.getIncome();
            }
            double averageIncome = totalIncome / households.size();
            System.out.printf("\nAverage Household Income: $%.2f\n", averageIncome);

            // Part c) List households above average
            System.out.println("\nHOUSEHOLDS WITH INCOME ABOVE AVERAGE:");
            System.out.printf("%-10s %-15s %-10s %s\n", "ID", "Income", "Members", "State");
            System.out.println("---------------------------------------------------------------");
            for (HouseHold h : households) {
                if (h.getIncome() > averageIncome) {
                    System.out.println(h);
                }
            }

            // Part d) List households below 2025 poverty level
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

            // Part e) Percentage of households below FPL
            double percentageBelowFPL = ((double) belowPovertyCount / households.size()) * 100;
            System.out.printf("\nPercentage of Households Below Poverty Level: %.2f%%\n", percentageBelowFPL);

            // Part f) Medicaid eligibility (138% of FPL)
            System.out.println("\nPART F: MEDICAID ELIGIBILITY CHECK (138% of FPL)");

            // Build formulas for each category using 2 API calls per region (1 and 4 members)
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


        } catch (FileNotFoundException e) {
            System.out.println("Error: File Program3.txt not found.");
        }
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

