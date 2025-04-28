import arbitraryarithmetic.AInteger;
import arbitraryarithmetic.AFloat;

//Need input of format java MyInfArith <int/float> <add/sub/mul/div> <num1> <num2>
public class MyInfArith {
    public static void main(String[] args) {
        
        if (args.length != 4) {
            System.err.println("Usage: java MyInfArith <int/float> <add/sub/mul/div> <num1> <num2>");
            System.exit(1);
        }
        
        String type = args[0].toLowerCase();
        String operation = args[1].toLowerCase();
        String num1 = args[2];
        String num2 = args[3];
        
        try {
            if (type.equals("int")) {
                AInteger a = new AInteger(num1);
                AInteger b = new AInteger(num2);
                
                switch (operation) {
                    case "add":
                        System.out.println(a.add(b));
                        break;
                    case "sub":
                        System.out.println(a.sub(b));
                        break;
                    case "mul":
                        System.out.println(a.mul(b));
                        break;
                    case "div":
                        System.out.println(a.div(b));
                        break;
                    default:
                        System.err.println("Invalid operation: " + operation);
                        System.exit(1);
                }
            }
            else if (type.equals("float")) {
                AFloat a = new AFloat(num1);
                AFloat b = new AFloat(num2);
                
                switch (operation) {
                    case "add":
                        System.out.println(a.add(b));
                        break;
                    case "sub":
                        System.out.println(a.sub(b));
                        break;
                    case "mul":
                        System.out.println(a.mul(b));
                        break;
                    case "div":
                        System.out.println(a.div(b));
                        break;
                    default:
                        System.err.println("Invalid operation: " + operation);
                        System.exit(1);
                }
            }
            else {
                System.err.println("Invalid Type: " + type)
                System.exit(1);
            }
        }
        catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
            System.exit(1);
        }
    }
}
