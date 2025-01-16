import org.json.JSONObject;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class SecretSharingSolver {

    public static void main(String[] args) {
        try {
            // Load the JSON file (adjust file paths as needed)
            JSONObject testCase1 = new JSONObject(new FileReader("testcase1.json"));
            JSONObject testCase2 = new JSONObject(new FileReader("testcase2.json"));

            // Process both test cases
            System.out.println("Secret for Test Case 1: " + solvePolynomial(testCase1));
            System.out.println("Secret for Test Case 2: " + solvePolynomial(testCase2));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to decode and solve the polynomial for the given JSON input
    private static double solvePolynomial(JSONObject testCase) {
        try {
            JSONObject keys = testCase.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            // Decode roots (x, y) from JSON
            List<Point> points = new ArrayList<>();
            for (String key : testCase.keySet()) {
                if (!key.equals("keys")) {
                    JSONObject root = testCase.getJSONObject(key);
                    int x = Integer.parseInt(key);
                    int base = root.getInt("base");
                    BigInteger value = new BigInteger(root.getString("value"), base);
                    points.add(new Point(x, value.doubleValue()));
                }
            }

            // Ensure we have at least k points
            if (points.size() < k) {
                throw new IllegalArgumentException("Not enough points to solve the polynomial.");
            }

            // Calculate the constant term using Lagrange Interpolation
            return calculateConstant(points, k);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Method to calculate the constant term using Lagrange interpolation
    private static double calculateConstant(List<Point> points, int k) {
        double constantTerm = 0.0;

        for (int i = 0; i < k; i++) {
            double xi = points.get(i).x;
            double yi = points.get(i).y;

            double li = 1.0;
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    double xj = points.get(j).x;
                    li *= (0 - xj) / (xi - xj);
                }
            }

            constantTerm += yi * li;
        }

        return constantTerm;
    }

    // Inner class to represent a point (x, y)
    static class Point {
        int x;
        double y;

        Point(int x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}

