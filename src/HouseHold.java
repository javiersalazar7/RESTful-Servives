//***************************************************************
//
//  Developer:    Javier Salazar
//
//  Program #:    3
//
//  File Name:    HouseHold.java
//
//  Course:       COSC 4301 Modern Programming
//
//  Due Date:     04/14/2025
//
//  Instructor:   Prof. Fred Kumi
//
//  Description:  This class represents a household with fields for ID,
//                income, household size, and state. It provides methods
//                to format and print data for income comparison and
//                poverty level analysis.
//
//***************************************************************

public class HouseHold {
    private int id;
    private double income;
    private int members;
    private String state;

    //***************************************************************
    //
    //  Constructor:  HouseHold
    //
    //  Description:  Initializes a HouseHold object with ID, income,
    //                number of members, and state.
    //
    //  Parameters:   int id         - Household identification number
    //                double income  - Annual income of the household
    //                int members    - Number of household members
    //                String state   - State where the household resides
    //
    //  Returns:      None
    //
    //***************************************************************

    public HouseHold(int id, double income, int members, String state) {
        this.id = id;
        this.income = income;
        this.members = members;
        this.state = state;
    }

    //***************************************************************
    //
    //  Method:       getId
    //
    //  Description:  Returns the household's identification number.
    //
    //  Parameters:   None
    //
    //  Returns:      int - the ID of the household
    //
    //***************************************************************
    public int getId() {
        return id;
    }

    //***************************************************************
    //
    //  Method:       getIncome
    //
    //  Description:  Returns the annual income of the household.
    //
    //  Parameters:   None
    //
    //  Returns:      double - the income of the household
    //
    //***************************************************************
    public double getIncome() {
        return income;
    }

    //***************************************************************
    //
    //  Method:       getMembers
    //
    //  Description:  Returns the number of members in the household.
    //
    //  Parameters:   None
    //
    //  Returns:      int - number of people in the household
    //
    //***************************************************************
    public int getMembers() {
        return members;
    }

    //***************************************************************
    //
    //  Method:       getState
    //
    //  Description:  Returns the name of the state where the household resides.
    //
    //  Parameters:   None
    //
    //  Returns:      String - state name
    //
    //***************************************************************
    public String getState() {
        return state;
    }

    //***************************************************************
    //
    //  Method:       getPovertyLevel
    //
    //  Description:  Calculates the poverty level based on household size using
    //                the 2025 federal poverty level formula for the 48 contiguous states.
    //                This method is used for Part D (static formula version).
    //
    //  Parameters:   None
    //
    //  Returns:      double - the calculated poverty level
    //
    //***************************************************************
    public double getPovertyLevel() {
        return 15060 + (members - 1) * 5380;
    }


    //***************************************************************
    //
    //  Method:       toString
    //
    //  Description:  Returns a formatted string representation of the
    //                household data for display.
    //
    //  Parameters:   None
    //
    //  Returns:      String - formatted household info
    //
    //***************************************************************
    @Override
    public String toString() {
        return String.format("%-10d %-15.2f %-10d %s", id, income, members, state);
    }
    //***************************************************************
    //
    //  Method:       toPovertyString
    //
    //  Description:  Returns a formatted string of the household data
    //                along with calculated poverty level (used for display).
    //
    //  Parameters:   None
    //
    //  Returns:      String - formatted household and poverty level info
    //
    //***************************************************************

    public String toPovertyString() {
        return String.format("%-10d %-15.2f %-15.2f %-10d %s", id, income, getPovertyLevel(), members, state);
    }
}

