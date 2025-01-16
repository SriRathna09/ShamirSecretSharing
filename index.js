const fs = require('fs');

// Helper to decode values from given base
function decodeValue(value, base) {
    return parseInt(value, base);
}

// Lagrange interpolation to find the constant term
function lagrangeInterpolation(points, k) {
    let constant = 0;
    for (let i = 0; i < k; i++) {
        let xi = points[i].x;
        let yi = points[i].y;
        let term = yi;

        for (let j = 0; j < k; j++) {
            if (i !== j) {
                let xj = points[j].x;
                term *= -xj / (xi - xj);
            }
        }

        constant += term;
    }

    return Math.round(constant); // Return the integer constant
}

// Main function to process input and calculate the secret
function findSecret(jsonFile) {
    const data = JSON.parse(fs.readFileSync(jsonFile, 'utf8'));
    const n = data.keys.n;
    const k = data.keys.k;

    const points = [];
    // Iterate over actual keys in the JSON
    Object.keys(data).forEach((key) => {
        if (key !== 'keys') {
            const base = parseInt(data[key].base, 10);
            const value = data[key].value;
            points.push({ x: parseInt(key, 10), y: decodeValue(value, base) });
        }
    });

    // Ensure we have enough points
    if (points.length < k) {
        throw new Error(`Not enough points to solve the polynomial. Required: ${k}, Available: ${points.length}`);
    }

    // Sort points by x-value for consistency (optional but helpful for debugging)
    points.sort((a, b) => a.x - b.x);

    // Use the first k points for interpolation
    return lagrangeInterpolation(points.slice(0, k), k);
}

// Run for the provided test case
try {
    const secret = findSecret('./testcase2.json'); // Replace with the actual file path
    console.log('Secret:', secret);
} catch (error) {
    console.error(error.message);
}
