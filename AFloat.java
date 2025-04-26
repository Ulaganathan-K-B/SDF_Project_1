package arbitraryarithmetic;

/*
AFloat: Arithmetic operation on infinitely long Decimals
Similar as BigDecimal
We use the already created AInteger
*/
public class AFloat {
    /*
    AInteger is used to store integer and decimal part of the decimal.
    Int is used to store #digits in decimal part of decimal.
    IsNeg is used to store if or not the decimal is negative. 
    1000 is generally used precision for division.
    */
    private AInteger intPart;
    private AInteger fracPart;
    private int fracDigits;
    private boolean isNeg;
    private static final int chosenPrecision = 1000;


    //Default constructor. Intialising to +0.
    public AFloat() {
        intPart = new AInteger();
        fracPart = new AInteger();
        fracDigits = 0;
        isNeg = false;
    }

    //Constructor for converting from string.
    public AFloat(String s) {
    
        if (s.isEmpty()) {
            intPart = new AInteger();
            fracPart = new AInteger();
            fracDigits = 0;
            isNeg = false;
            return;
        }
    
        //Index where the decimal starts.
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
    
        //Finding decimal point '.'
        int deciPtIdx = s.indexOf('.', startingIndex);
    
        //No decimal point
        if (deciPtIdx == -1) {
            intPart = new AInteger(isNeg ? '-' + s.substring(startingIndex) : s.substring(startingIndex));
            fracPart = new AInteger();
            fracDigits = 0;
        }
        else {
            //Extracting Integer part.
            String integerPart = s.substring(startingIndex, deciPtIdx);
            if (integerPart.isEmpty()) integerPart ="0";
            intPart = new AInteger(isNeg ? '-' + integerPart:integerPart);
        
            //Extracting decimal part.
            String fractionalPart = s.substring(deciPtIdx+1);
            fracDigits = fractionalPart.length();
        
            if (fractionalPart.isEmpty()) {
                fracPart = new AInteger();
                fracDigits = 0;
            }
            else fracPart = new AInteger(fractionalPart);
        }
    }

    //Copy Constructor to create a copy.
    public AFloat(AFloat other) {
        this.intPart = new AInteger(other.intPart);
        this.fracPart = new AInteger(other.fracPart);
        this.fracDigits = other.fracDigits;
        this.isNeg = other.isNeg;
    }
    
    //Normalise the fractional part i.e remove trailing 0
    private void normalise() {
        
        String fracStr = fracPart.toString();
        int trailingZeroes = 0;
        
        if (fracDigits>0) {
            for (int i=fracStr.length()-1; i>=0; i--) {
                if (fracStr.charAt(i) == '0') trailingZeroes++;
                else break;
            }
        }
            
        if (trailingZeroes>0) {
            //FracPart only has 0.
            if (trailingZeroes == fracStr.length()) {
                fracPart = new AInteger();
                fracDigits = 0;
            }
            else {
                fracPart = new AInteger(fracStr.substring(0, fracStr.length() - trailingZeroes));
                fracDigits = fracDigits - trailingZeroes;
            }
        }
    }

    //For aligning the decimal points properly.
    private static void alignDeciPt (AFloat a, AFloat b) {
        int diff = a.fracDigits - b.fracDigits;
    
        //Pad '0' to b.
        if (diff>0) {
            String pad = b.fracPart.toString() + "0".repeat(diff);
            b.fracPart = new AInteger(pad);
            b.fracDigits = a.fracDigits;
        }
        //Pad '0' to a.
        else if (diff<0) {
            String pad = a.fracPart.toString() + "0".repeat(-diff);
            a.fracPart = new AInteger(pad);
            a.fracDigits = b.fracDigits;
        }
    }

    //Convert frac and int part into a single AInteger by *10^n
    private AInteger scaledInt() {
        String intStr = intPart.toString();
        
        if (intStr.equals("0") && isNeg) intStr = '-0';
        if (fracDigits == 0) return new AInteger(intStr);
        
        String fracStr = fracPart.toString();
        //10.0004, fracPart should be stored as 0004 and not 4 thus, pad '0'
        while (fracStr.length()<fracDigits) fracStr = "0"+fracStr;
    
        String fullStr;
        if (intStr.charAt(0) == '-') fullStr = '-' + intStr.substring(1)+fracStr;
        else fullStr = intStr+fracStr;
    
        return new AInteger(fullStr);
    }

    //Create AFloat from scaledInt
    private static AFloat fromScaledInt(AInteger scaled, int fracDigits) {
        
        String scaledStr = scaled.toString();
        String intStr = scaledStr.substring(0, scaledStr.length() - fracDigits);
        String fracStr = scaledStr.substring(scaledStr.length() - fracDigits);
        AFloat result = new AFloat();
        boolean isNeg = scaled.toString().charAt(0) == '-';
    
        //At 0th position, we have 0
        if (isNeg) scaledStr = scaledStr.substring(1);
        //Ensure enough 0 in frac part
        while (scaledStr.length() <= fracDigits) scaledStr = "0"+scaledStr;
    
        result.intPart = new AInteger(isNeg ? '-' + intStr : intStr);
        result.fracPart = new AInteger(fracStr);
        result.fracDigits = fracDigits;
        result.isNeg = isNeg;
        result.normalise();
        return result;
    }

    //Add operation
    public AFloat add(AFloat other) {
    
        //Copies are created to not change the original
        AFloat a = new AFloat(this);
        AFloat b = new AFloat(other);
    
        //Align the decimal pt.
        alignDeciPt(a, b);
    
        //Convert to scaled int
        AInteger aScaled = a.scaledInt();
        AInteger bScaled = b.scaledInt();
        
        //Do add from AInteger
        AInteger resultScaled = aScaled.add(bScaled);
        
        //Convert back to AFloat
        return fromScaledInt(resultScaled, a.fracDigits);
    }
    
    //Sub operation.
    public AFloat sub(AFloat other) {
        
        //Copies are created to not change the original
        AFloat a = new AFloat(this);
        AFloat b = new AFloat(other);
        
        //Align the decimal pt.
        alignDeciPt(a, b);
        
        //Convert to scaled int
        AInteger aScaled = a.scaledInt();
        AInteger bScaled = b.scaledInt();
        
        //Do sub from AInteger
        AInteger resultScaled = aScaled.sub(bScaled);
        
        //Convert back to AFloat
        return fromScaledInt(resultScaled, a.fracDigits);
    }
    
    //Mul operation
    public AFloat mul(AFloat other) {
        
        //Copies are created to not change the original
        AFloat a = new AFloat(this);
        AFloat b = new AFloat(other);
        
        //Convert to scaled int
        AInteger aScaled = a.scaledInt();
        AInteger bScaled = b.scaledInt();
        
        //Do mul from AInteger
        AInteger resultScaled = aScaled.mul(bScaled);
        
        //Decimal pt is at a.fracDigits+b.fracDigits
        int resultFracDigits = a.fracDigits+b.fracDigits;;
        
        //Convert to AFloat.
        return fromScaledInt(resultScaled, resultFracDigits);
    }
    
    //Division operation.
    public AFloat div(AFloat other) {
    
        //Div by 0.
        if (other.intPart.toString().equals("0") && (other.fracDigits == 0 || 
            other.fracPart.toString().replaceAll("0", "").isEmpty())) {
                throw new ArithmeticException("Division by 0");
            }
        
        //Copies are created to not change the original
        AFloat a = new AFloat(this);
        AFloat b = new AFloat(other);
    
        //Normalise to remove trailing 0 in frac part.
        a.normalise();
        b.normalise();
    
        //Scale for needed precision and then divide.
        int precision = chosenPrecision;
        String scaledDividentStr = a.scaledInt().toString();
    
        if (scaledDividentStr.charAt(0) == '-') scaledDividentStr = scaledDividentStr.substring(1);
        scaledDividentStr = scaledDividentStr+"0".repeat(precision + b.fracDigits);
    
        AInteger scaledDivident = new AInteger(a.isNeg ? '-' + scaledDividentStr : scaledDividentStr);
        AInteger scaledDivisor = b.scaledInt();
    
        //Do Division
        AInteger quotient = scaledDivident.divide(scaledDivisor);
    
        //Result.fracDigits = a.fracDigits + precision - b.fracDigits   
        int resultFracDigits = a.fracDigits + precision - b.fracDigits;
    
        //Convert back to AFloat
        AFloat result = fromScaledInt(quotient, resultFracDigits);
        result.normalise();
        return result;
    }

    //Reeturn string representation of AFloat
    @Override
    public String toString() {
    
        if (fracDigits == 0) return intPart.toString();
    
        String intStr = intPart.toString();
        String fracStr = fracPart.toString();
    
        //Pad frac part with 0
        while (fracStr.length() < fracDigits) fracStr = "0"+fracStr;
    
        return intStr + '.' + fracStr;
    }
}
