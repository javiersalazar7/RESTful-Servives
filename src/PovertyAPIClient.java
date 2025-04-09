//***************************************************************
//
//  Developer:    Javier Salazar
//
//  Program #:    3
//
//  File Name:    PovertyAPIClient.java
//
//  Course:       COSC 4301 Modern Programming
//
//  Due Date:     04/14/2025
//
//  Instructor:   Prof. Fred Kumi
//
//  Description:  This class makes exactly two REST API calls per U.S. poverty category
//                (Contiguous U.S., Alaska, and Hawaii) to retrieve poverty guideline
//                values for two household sizes. Using those values, it builds a formula
//                that estimates the poverty level for any household size based on state.
//                The data is retrieved from the HHS Poverty Guidelines API.
//
//***************************************************************

import java.net.URI;
import java.net.http.*;
import org.json.JSONObject;

public class PovertyAPIClient {

    //***************************************************************
    //
    //  Method:       fetchPovertyLevels
    //
    //  Description:  Makes two REST API calls to the HHS Poverty Guidelines
    //                API for the given state and household sizes. Extracts
    //                poverty income values for each size.
    //
    //  Parameters:   String stateCode - 2-letter state code (us, ak, hi)
    //                int size1        - first household size
    //                int size2        - second household size
    //
    //  Returns:      double[] - poverty levels for size1 and size2
    //
    //***************************************************************
    public static double[] fetchPovertyLevels(String stateCode, int size1, int size2) {
        try {
            double[] levels = new double[2];
            int[] sizes = { size1, size2 };

            for (int i = 0; i < 2; i++) {
                String uri = String.format("https://aspe.hhs.gov/topics/poverty-economic-mobility/poverty-guidelines/api/2025/%s/%d",
                        stateCode, sizes[i]);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(uri))
                        .GET()
                        .build();
                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JSONObject json = new JSONObject(response.body());
                    levels[i] = json.getJSONObject("data").getDouble("income");

                } else {
                    throw new RuntimeException("HTTP error: " + response.statusCode());
                }
            }

            return levels;
        } catch (Exception e) {
            e.printStackTrace();
            return new double[]{0, 0};
        }
    }

    //***************************************************************
    //
    //  Method:       buildFormula
    //
    //  Description:  Uses two fetched poverty levels to calculate and return
    //                a linear poverty formula: base + increment * (members - baseSize)
    //
    //  Parameters:   String stateCode - 2-letter state code (us, ak, hi)
    //                int size1        - first household size
    //                int size2        - second household size
    //
    //  Returns:      double[] - {base, increment, baseSize}
    //
    //***************************************************************
    public static double[] buildFormula(String stateCode, int size1, int size2) {
        double[] povertyLevels = fetchPovertyLevels(stateCode, size1, size2);
        double base = povertyLevels[0];
        double increment = (povertyLevels[1] - povertyLevels[0]) / (size2 - size1);
        return new double[]{base, increment, size1};  // base + increment * (members - baseSize)
    }
}

