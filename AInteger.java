package arbitraryarithmetic; //Package that is asked.

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
AInteger - Class to do arithmetic operation(add,sub,mul,div) on a infinitely long integer.
This is similar to BigInteger class.
*/

public class AInteger {
	private List<Integer> digits; //List to store each element of input.
	private boolean isNeg; //To know if the given input is negative or not.

	/*
	Default Constructor
	Assume the given integer to positive.
	Create a new list for storing the input.
	Initiaise the AInteger to 0.
	*/
	public AInteger() {
		isNeg = false;
		digits = new ArrayList<>();
		digits.add(0);
	}

	/*
	Constructor for string representation.
	Parameter is "string S"
	*/
	public AInteger(String s) {

		digits = new ArrayList<>();

		// No input is given.
		if (s.isEmpty()) {
			digits.add(0);
			isNeg = false;
			return;
		}

		/*
		Index from which the integer starts
		1234 = startingIndex=0
		-123 or +123 = startingIndex=1
		0000012345 = startingIndex=5'
		We assume case 1 and thus, initialise to 0
		*/
		int startingIndex = 0;

		//Of form +415 or +0054
		if (s.charAt(0) == '+') {
			isNeg = false;
			startingIndex = 1;
		}

		//Of form -65 or -0054
		else if (s.charAt(0) == '-') {
			isNeg = true;
			startingIndex = 1;
		}

		//Of form 54653 or 000094546
		else {
			isNeg = false;
			startingIndex = 0;
		}

		//Move startingIndex to after the leading zeroes.
		while ((startingIndex < s.length()) && s.charAt(startingIndex) == '0') {
			startingIndex++;

			//Input is of form 000000....
			if (startingIndex == s.length()) {
				digits.add(0);
				isNeg = false;
				return;
			}
		}

		/*
		Check if any element of input is invalid.
		Reverse the string (Easier to add, sub)
		Convert char to int
		*/
		for (int i=s.length()-1; i>=startingIndex; i--) {
			char c = s.charAt(i);

			//Checks for invalid input
			if (c<'0' || c>'9') {
				throw new NumberFormatException("Invalid input: Contains" + c);
			}
			//Converts char to int
			digits.add(c - '0');
		}
	}

	//Constructor to create a copy
	public AInteger(AInteger other) {
		this.digits = new ArrayList<>(other.digits);
		this.isNeg = other.isNeg;
	}

	//Creating AInteger from String
	public static AInteger parse(String s) {
		return new AInteger(s);
	}
	
	/*
	To find the bigger number, we are going to use whichIsBig.
	Useful for sub,mul,div
	How it works:
	If size of this != size of other
	    return the size difference
	        if <0, other>this
	        else this>other
	else find the 1st different number and find it's difference
	*/

	private int whichIsBig(AInteger other) {
		if(this.digits.size() != other.digits.size()) {
			return this.digits.size() - other.digits.size();
		}

		//As, we have stored the numbers in rebverse
		for (int i = this.digits.size()-1; i>=0; i--) {
			if (this.digits.get(i) != other.digits.get(i)) {
				return this.digits.get(i) - other.digits.get(i);
			}
		}
		//If this == other
		return 0;
	}
	
	private void removeLeadingZeroes() {
	    while (digits.size() > 1 && digits.get(digits.size()-1) == 0) {
	        digits.remove(digits.size()-1);
	    }
	    //If only one '0' keep it +ve
	    if (digits.size() == 1 && digits.get(0) == 0) isNeg = false;
	}

	//Addition operation
	public AInteger add(AInteger other) {

		//If sign of this != other, assign to sub.
		if (this.isNeg != other.isNeg) {
			//Create new AInteger same as other but opposite sign
			AInteger newOther = new AInteger(other);
			newOther.isNeg = !newOther.isNeg;
			return this.sub(newOther);
		}

		//'result' is used to store this+other and has same sign of this and other
		AInteger result = new AInteger();
		result.digits.clear();
		result.isNeg = this.isNeg;

		int carry = 0;
		int maxSize = Math.max(this.digits.size(), other.digits.size());

		for (int i=0; i<maxSize || carry>0; i++) {

			//Initially, sum=carry
			int sum = carry;

			//Check if this, other are big i.e has enough digits
			if (i < this.digits.size()) sum = sum + this.digits.get(i);
			if (i < other.digits.size()) sum = sum + other.digits.get(i);

			result.digits.add(sum%10);
			carry = sum/10;
		}
		result.removeLeadingZeroes();
		return result;
	}

	//Subtraction opreation
	public AInteger sub(AInteger other) {

		//If sign of this != other, assign to add.
		if (this.isNeg != other.isNeg) {
			//Create new AInteger same as other but opposite sign
			AInteger newOther = new AInteger(other);
			newOther.isNeg = !newOther.isNeg;
			return this.add(newOther);
		}
			//Find which among this, other is Big
			int whichIsBigger = this.whichIsBig(other);
			//Result stores this-other
			AInteger result = new AInteger();
			result.digits.clear();
			//To store the bigger and smaller digits of the two
			AInteger bigNum, smallNum;

			//this == other
			if (whichIsBigger == 0) {
				result.digits.add(0);
				return result;
			}
			//this>ohter
			else if (whichIsBigger > 0) {
				bigNum = this;
				smallNum = other;
				result.isNeg = this.isNeg;
			}
			//other>this
			else {
				bigNum = other;
				smallNum = this;
				result.isNeg = other.isNeg;
			}

			int borrow = 0;
			for (int i=0; i<bigNum.digits.size(); i++) {

				int diff = bigNum.digits.get(i) - borrow;
				if (i<smallNum.digits.size()) diff = diff-smallNum.digits.get(i);

				if (diff<0) {
					diff = diff+10;
					borrow=1;
				} else borrow=0;

				result.digits.add(diff);
			}
			result.removeLeadingZeroes();
			return result;
		}

		/*
		Multiplication operation
		Long multiplication (Digit by digit is implemented)
		*/
		public AInteger mul(AInteger other) {

			//If this or other is 0
			if ((this.digits.size() == 1 && this.digits.get(0) == 0) ||
			        (other.digits.size() == 1 && other.digits.get(0) == 0)) {
				return new AInteger();
			}

			//Result is used to store this*other
			AInteger result = new AInteger();
			result.digits.clear();
			for (int i=0; i<(this.digits.size()+other.digits.size()); i++) {
				result.digits.add(0);
			}

			//Long multiplication
			for (int i=0; i<this.digits.size(); i++) {
				int carry = 0;

				for (int j=0; j<other.digits.size(); j++) {
					int pdt = result.digits.get(i+j)+carry+(this.digits.get(i)*other.digits.get(j));
					carry = pdt/10;
					result.digits.set(i+j, pdt%10);
				}
				result.digits.set(i + other.digits.size(), carry);
			}

			//Result is neg iff one of the 2 this, other is neg
			result.isNeg = (this.isNeg != other.isNeg);
			result.removeLeadingZeroes();
			return result;
		}

		/*
		Division operation
		Numerator = this and Denomenator = other
		*/
		public AInteger div(AInteger other) {


			//Numerator=0 i.e ans=0
			if (this.digits.size()==1 && this.digits.get(0)==0) return new AInteger();
			//Denometor is 0 i.e zero error
			if (other.digits.size()==1 && other.digits.get(0)==0) throw new ArithmeticException("Division by 0");

			AInteger divident = new AInteger(this);
			divident.isNeg = false;

			AInteger divisor = new AInteger(other);
			divisor.isNeg = false;

			AInteger quotient = new AInteger();
			quotient.digits.clear();

			//currDivident is used to store current divident
			List<Integer> currDivident = new ArrayList<>();

			/*
			As, it is integer division,

			if |Numerator|<|Denometor| answer=0
			else if |Numerator|=|Denometor|

			    if Numerator.isNeg == Denometor.isNeg
			        ans=1
			    else
			        ans=1
			else
			    perform division
			*/

			int whichIsBigger = this.whichIsBig(other);
			if (whichIsBigger < 0) return new AInteger();

			if (whichIsBigger == 0) {
				AInteger result = new AInteger("1");
				result.isNeg = (this.isNeg != other.isNeg);
				return result;
			}

			for (int i=divident.digits.size()-1; i>=0; i--) {

				//Add one digit of divident at a time
				currDivident.add(0, divident.digits.get(i));

				//Remove leading zeroes
				while (currDivident.size()>1 && currDivident.get(currDivident.size()-1)==0) {
					currDivident.remove(currDivident.size()-1);
				}

				//Find largest int q such that, currDivident-(Divisor*q) > 0
				int q=0;

				while(true) {

					//Create temp = currDivident
					AInteger temp = new AInteger();
					temp.digits.clear();
					for (int digit : currDivident) temp.digits.add(digit);
					Collections.reverse(temp.digits);

					//qDivisor = Divisor*(q+1)
					AInteger qDivisor = divisor.mul(new AInteger(Integer.toString(q+1)));

					//q is the answer if, qDivisor>currDivident
					if (temp.whichIsBig(qDivisor)<0) break;
					q++;
				}
				quotient.digits.add(0, q);

				//Subtract divisor*q from currDivident

				AInteger subDivident = divisor.mul(new AInteger(Integer.toString(q)));
				List<Integer> subDigits = new ArrayList<>(subDivident.digits);
				Collections.reverse(subDigits);

				//Do sub on currDivident
				int borrow = 0;
				for (int j=0; j<Math.max(currDivident.size(), subDigits.size()); j++) {
					int bufval = (j < currDivident.size()) ? currDivident.get(j):0;
					int subVal = (j < subDigits.size()) ? subDigits.get(j) : 0;

					int diff = bufval - subVal - borrow;
					if (diff<0) {
						diff = diff+10;
						borrow = 1;
					}
					else borrow = 0;

					if (j < currDivident.size()) currDivident.set(j, diff);
					else currDivident.add(diff);
				}

				//Remove leading zeroes from currDivident
				while (currDivident.size()>1 && currDivident.get(currDivident.size()-1)==0) {
					currDivident.remove(currDivident.size()-1);
				}
			}
			//Reverese quotient
			Collections.reverse(quotient.digits);
			quotient.removeLeadingZeroes()
			//Sign of quotient
			quotient.isNeg = (this.isNeg != other.isNeg);
			return quotient;
		}

		//Return as String
		@Override
		public String toString() {

			if (digits.isEmpty() || (digits.size() == 1 && digits.get(0) == 0)) return "0";
			StringBuilder sb = new StringBuilder();
			if (isNeg) sb.append('-');
			for (int i = digits.size() - 1; i >= 0; i--) sb.append(digits.get(i));

			return sb.toString();
		}
	}
