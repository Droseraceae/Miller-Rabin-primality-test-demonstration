import java.util.Random;

import java.util.Scanner;

import java.math.*;

/**
 * @Author Josh Alcoba
 * Dr. Cicirello
 * CSIS 4481 Cryptography and Data Security
 * 12/16/2018
 * @version 1.0
 * 
 * Homework Assignment 11 implementing Miller-Rabin primality test. 
 * 
 */

public class MillerRabin {

	/**
	 * Tests if n is a prime number. Returns False if n is definitely a composite.
	 * Returns True if n might be a prime number.
	 * 
	 * Miller-Rabin Pseudocode: 
	 * 
	 * TEST (n)
	 * 		Find integers k,q, k > 0, q odd, such that (n-1) = (2^k)*q
	 * 		Select random Integer a, 1 < a < n-1
	 * 		IF (a^q) MOD n = 1 THEN RETURN ("Maybe prime")
	 * 		FOR j = 0 to k -1  do
	 * 			IF (a^[(2^j)*q]) MOD n = n-1
	 * 				THEN RETURN ("Maybe prime)"
	 * 			RETURN ("Composite")
	 *
	 * @param n the number to test for primality.
	 */
	
	public static boolean millerRabin(int n) {

		// Algorithm line 1
		int k = 0;
		int value = (n - 1);

		while ((value & 1) != 1) {
			if (n == 1) 
				return false; // 1 is not prime, return false
			
			// Change values and retest
			k++;
			value = (value >> 1);
		}
		// We've found our q
		int q = value;

		// Algorithm line 2
		Random rng = new Random();
		// Choose a random number between 0 and n-1
		int a = rng.nextInt((n - 1));
		// Make sure it is not equal to 0 or n-1
		while ((a == 0) || (a == (n - 1))) {
			a = rng.nextInt((n - 1));
		}

		// Algorithm line 3

		/*
		 * ModPow_J takes in 3 int values as arguments; the base, the exponent, and the
		 * n for the mod n function. So for line 3, I just need to assign the int mod_pow to whatever
		 * the method spits out. I test it to see if it passes the first test, if not I
		 * reuse that value and further test in the for loop in lines 4, 5, and 6.
		 */

		int mod_pow = ModPow_J(a, q, n);
		if (mod_pow == 1)
			return true;
		
		// Algorithm line 4, 5, 6
		for (int j = 0; j <= (k - 1); j++) {

			/*
			 * Check if it meets the conditions. Because we tested for j = 0 in line 3, we
			 * can reuse that value
			 */

			if (mod_pow == (n - 1))
				return true;
			/*
			 * So if not, then we need to keep testing. Each successive iteration is the
			 * previous value squared and mod n. This is fairly simple to do.
			 */
			mod_pow = (mod_pow * mod_pow);
			mod_pow = (mod_pow % n);

		}
		/*
		 * If we didn't hit any conditions up to this point, we return false. Our number
		 * is composite.
		 */
		return false;

	}

	/**
	 * Tests if n is a prime number. If this function returns False, then n is
	 * definitely a composite number. If this function returns True, then the
	 * probability that n is a prime number is at least p.
	 *
	 * @param n the number to test for primality.
	 * @param p target probability.
	 */
	public static boolean isProbablyPrime(int n, double p) {
		for (int i = 0; i < p; i++) {
			if (millerRabin(n) == false) {
				return false; // Failed the test at some point
			}
		}
		return true; // Passed the test for p iterations, is probably prime
	}

	public static int ModPow_J(int a, int exp, int mod_n) {

		// Initalize our result to the base value
		BigInteger result = BigInteger.valueOf(a);

		// Raise to the power of our exp. value
		result = result.pow(exp);

		// Apply mod n
		BigInteger m = BigInteger.valueOf(mod_n);
		result = result.mod(m);

		// This was for testing
		// System.out.println("Using the ModPow_J method, my values (" + a + ", " + exp
		// + ", " + mod_n + ") gives me: " + result.intValue());

		// Return our final result
		return result.intValue();
	}

	public static void main(String[] args) {
		// Write some code here to test that your functions work.

		System.out.println("Let's test our code!");
		
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Enter an integer to do primality testing: ");
		int n = keyboard.nextInt(); // This is the number we are testing. 89899 was my test number.
		double p = 10; // How many times we are testing for primality; 10 is certainty to > 0.99999

		// Initalize our variable and then run the test for value n
		boolean test = false;
		test = millerRabin(n);

		// Outcome 1, it failed first round of testing
		if (test == false)
			System.out.println("1: Our value n: " + n + " is composite.");

		// Outcome 2, our number may be prime and we run the next tests
		else if (test == true) {
			System.out.println("2: Our value n: " + n + " might be prime. Let's check.");

			// Run our isProbablyPrime method for number n, p times
			test = isProbablyPrime(n, p);
			// Outcome 3, the number passed the first test but failed repeat tests
			if (test == false)
				System.out.println("3: Our value n: " + n + " is definitely composite.");
			// Outcome 4, passed our primality tests for p iterations
			else if (test == true)
				System.out.println("4: Our value n: " + n + " is probably prime; number of tests: " + p + " tests.");
		}

	}

}