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
        
        return result;
    }
    
    
    /*
    Subtrcation is done as (big number)-(small number).
    To find big number, we are going to use whichIsBig.
    How it works:
    If size of this != size of other
        return the difference 
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
    
    //Subtraction opreation
    public AInteger sub(AInteger other) {
        
        //If sign of this != other, assign to add.
        if (this.isNeg != other.isNeg) {
            //Create new AInteger same as other but opposite sign
            AInteger newOther = new AInteger(other);
            newOther.isNeg = !newOther.isNeg;
            return this.add(newOther);
            
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
            if (i<smallNum.digits.size()) diff = diff-smallNum..digits.get(i);
            
            if (diff<0) {
                diff = diff+10;
                borrow=1;
            } else borrow=0;
            
            result.digits.add(diff);
        }
        return result;
    }
}
