package arbitraryarithmetic; //Package that is asked.

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
AInteger - Class to do arithmetic operation(add,sub,mul,div) on a infinitely long integer.
This is similar to BigInteger class.
*/

public class AInteger {
    private List<Integer> digits //List to store each element of input. 
    private boolean isNeg //To know if the given input is negative or not.
    
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
        startingIndex = 0;
        
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
    
    //Addition operation
    public AInteger add(AInteger other) {
        
        //If sign of this != other, assign to sub.
        if (this.isNeg != other.isNeg) {
            //Create new AInteger with same as other and opposite sign
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
        
        return result;
    }   
}
