package arbitraryarithmetic;

/*
AFloat: Arithmetic operation on infinitely long Decimals
Similar as BigDecimal
We use the already created AInteger
*/
public class AFloat {
    private AInteger intPart;  //Stores integer part
    private AInteger fracPart; //Stores fractional part
    private int fracDigits;    //#Digits in fractional Part
    private boolean isNeg;
    private static final int chosenPrecision = 30;

    // Default constructor. Initialising to +0.0
    public AFloat() {
        intPart = new AInteger();
        fracPart = new AInteger();
        fracDigits = 0;
        isNeg = false;
    }

    // Constructor for converting from string.
    public AFloat(String s) {
        if (s.isEmpty()) {
            intPart = new AInteger();
            fracPart = new AInteger();
            fracDigits = 0;
            isNeg = false;
            return;
        }

        int startingIndex = 0;
        
        if (s.charAt(0) == '-') {
            isNeg = true;
            startingIndex = 1;
        }
        
        else if (s.charAt(0) == '+') {
            isNeg = false;
            startingIndex = 1;
        } 
        else isNeg = false;
        
        //Index of the decimal point.
        int deciPtIdx = s.indexOf('.', startingIndex);
        
        /*
        No decimal point
        Thus, entire input is stored in intPart
        fracPart = 0
        */
        if (deciPtIdx == -1) {
            intPart = new AInteger(isNeg ? '-' + s.substring(startingIndex) : s.substring(startingIndex));
            fracPart = new AInteger();
            fracDigits = 0;
        } 
        
        else {
            //Check if >1 decimal points
            if (s.indexOf('.', deciPtIdx + 1) != -1) throw new NumberFormatException("Multiple decimal points");
            
            //integerPart stores all that comes before decimal point
            String integerPart = s.substring(startingIndex, deciPtIdx);
            if (integerPart.isEmpty()) integerPart = "0";
            //Assign sign to onteger part
            intPart = new AInteger(isNeg ? '-' + integerPart : integerPart);
            
            //fractionalPart stores all that comes after decimal point
            String fractionalPart = s.substring(deciPtIdx + 1);
            fracDigits = fractionalPart.length();
            if (fractionalPart.isEmpty()) {
                fracPart = new AInteger();
                fracDigits = 0;
            } 
            else fracPart = new AInteger(fractionalPart);
        }
        normalise(); 
    }

    // Copy Constructor
    public AFloat(AFloat other) {
        this.intPart = new AInteger(other.intPart);
        this.fracPart = new AInteger(other.fracPart);
        this.fracDigits = other.fracDigits;
        this.isNeg = other.isNeg;
    }
    
    //Parse
    public static AFloat parse(String s) {
        return new AFloat(s);
    }


    //Remove trailing 0 of fractional part 
    private void normalise() {
        
        //fracPart is converted to string
        String fracStr = fracPart.toString();
        //Assume no trailing zeroes
        int trailingZeroes = 0;
        
        //Find number of trailling zeroes.
        if (fracDigits > 0) {
            for (int i = fracStr.length() - 1; i >= 0; i--) {
                if (fracStr.charAt(i) == '0') trailingZeroes++;
                else break;
            }
        }
        
        if (trailingZeroes > 0) {
            //If #trailingZeroes == Length of fracPart fracPart=0
            if (trailingZeroes == fracStr.length()) {
                fracPart = new AInteger();
                fracDigits = 0;
            } 
            //Remove trailing zeroes
            else {
                fracPart = new AInteger(fracStr.substring(0, fracStr.length() - trailingZeroes));
                fracDigits = fracDigits - trailingZeroes;
            }
        }
    }

    //For aligning the decimal points properly.
    private static void alignDeciPt(AFloat a, AFloat b) {
        
        //Difference between the index of decimal point of a,b
        int diff = a.fracDigits - b.fracDigits;
        
        if (diff > 0) {
            String pad = b.fracPart.toString() + "0".repeat(diff);
            b.fracPart = new AInteger(pad);
            b.fracDigits = a.fracDigits;
        } 
        else if (diff < 0) {
            String pad = a.fracPart.toString() + "0".repeat(-diff);
            a.fracPart = new AInteger(pad);
            a.fracDigits = b.fracDigits;
        }
    }

    //Convert frac and int part into a single AInteger by mul with 10^n
    private AInteger scaledInt() {
        
        String intStr = intPart.toString();               //Int part is converted to String
        if (intStr.equals("0") && isNeg) intStr = "-0";   //If I/P is -0.88
        if (fracDigits == 0) return new AInteger(intStr); //If I/P is 9(No frac part)
        
        String fracStr = fracPart.toString(); //Frac part converted to string
        while (fracStr.length() < fracDigits) fracStr = "0" + fracStr;
        
        String combined = intStr + fracStr;
        return new AInteger(combined);
    }

    // Set value from scaled integer and scale
    private void fromScaledInt(AInteger n, int scale) {
        
        String numStr = n.toString();
        boolean isNegative = false;
        
        //Removes '-' from index 1
        if (numStr.charAt(0) == '-') {
            isNegative = true;
            numStr = numStr.substring(1);
        }
        
        int splitPoint = numStr.length() - scale;
        
        if (splitPoint <= 0) {
            intPart = new AInteger("0");
            String fracStr = "0".repeat(-splitPoint) + numStr;
            fracPart = new AInteger(fracStr);
            fracDigits = scale;
        } 
        
        else if (splitPoint >= numStr.length()) {
            intPart = new AInteger(isNegative ? "-" + numStr : numStr);
            fracPart = new AInteger("0");
            fracDigits = 0;
        } 
        
        else {
            String intStr = numStr.substring(0, splitPoint);
            if (intStr.isEmpty()) intStr = "0";
            intPart = new AInteger(isNegative ? "-" + intStr : intStr);
            fracPart = new AInteger(numStr.substring(splitPoint));
            fracDigits = scale;
        }

        this.isNeg = isNegative;
        normalise();
    }

    //Addition
    public AFloat add(AFloat other) {
        AFloat a = new AFloat(this);
        AFloat b = new AFloat(other);
        alignDeciPt(a, b);
        AInteger aScaled = a.scaledInt();
        AInteger bScaled = b.scaledInt();
        AInteger sum = aScaled.add(bScaled);
        AFloat result = new AFloat();
        result.fromScaledInt(sum, a.fracDigits);
        return result;
    }

    //Subtraction
    public AFloat sub(AFloat other) {
        AFloat a = new AFloat(this);
        AFloat b = new AFloat(other);
        alignDeciPt(a, b);
        AInteger aScaled = a.scaledInt();
        AInteger bScaled = b.scaledInt();
        AInteger diff = aScaled.sub(bScaled);
        AFloat result = new AFloat();
        result.fromScaledInt(diff, a.fracDigits);
        return result;
    }

    // Multiplication
    public AFloat mul(AFloat other) {
        AInteger aScaled = this.scaledInt();
        AInteger bScaled = other.scaledInt();
        AInteger prod = aScaled.mul(bScaled);
        int scale = this.fracDigits + other.fracDigits;
        AFloat result = new AFloat();
        result.fromScaledInt(prod, scale);
        return result;
    }

    //Division
    public AFloat div(AFloat other) {
        //Check div by 0
        if (other.intPart.toString().equals("0") && other.fracPart.toString().equals("0")) throw new ArithmeticException("Division by zero");

                AInteger a = this.scaledInt();
        AInteger b = other.scaledInt();
        
        // #decimal places = dividend decimal places - divisor decimal places + chosen precision
        int effectiveScale = this.fracDigits - other.fracDigits + chosenPrecision;
        
        //Scale the dividend
        String aStr = a.toString();
        boolean isNegative = false;
        if (aStr.charAt(0) == '-') {
            isNegative = true;
            aStr = aStr.substring(1);
        }
        
        //Add zeros for precision
        aStr = aStr + "0".repeat(chosenPrecision);
        if (isNegative) aStr = "-" + aStr;
        
        //Scaled dividend
        AInteger scaledDividend = new AInteger(aStr);
        
        //Perform division
        AInteger quotient = scaledDividend.div(b);
        
        // Create result with proper scale
        AFloat divResult = new AFloat();
        divResult.fromScaledInt(quotient, effectiveScale);
        
        return divResult;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isNeg && !(intPart.toString().equals("0") && fracPart.toString().equals("0"))) sb.append('-');
        sb.append(intPart.toString());
        if (fracDigits > 0 && !fracPart.toString().equals("0")) {
            String fracStr = fracPart.toString();
            while (fracStr.length() < fracDigits) fracStr = "0" + fracStr;
            sb.append('.').append(fracStr);
        }
        return sb.toString();
    }
}
