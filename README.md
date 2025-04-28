Problem Statement-1 (Arbitrary precision arithmetic class in Java)

Implementation of an infinite/arbitrary-precision arithmetic library in Java using OOP concepts. Given two strings as input, you need to add support for addition, subtraction, multiplication and division, for both integer and float data types. The answer should be printed on the terminal screen.

Library specification:
Create a package called arbitraryarithmetic which consists of two classes AInteger and AFloat.

Integer class:
Your class should support the following constructors:
1. Default constructor AInteger() that initializes the instance with value 0.
2. Constructor AInteger(String s) that initializes the instance by the number whose string representation is given by ’s’. Eg: AInteger(“-34534536454”);
3. Copy constructor that creates an instance of AInteger

Your class should support the following functions:
1. parse(String s) - a static function that returns an instance of AInteger class.
2. Overload the following integer arithmetic operators: (+, -, *, /)

Float class:
Your class should support the following constructors/destructors:
1. Default constructor AFloat() that initializes the instance with value 0.0.
2. Constructor AFloat(string s) that initializes the instance by the number whose string representation is given by ’s’. Eg: AFloat(“-345.32393298”);
3. Copy constructor that creates an instance of AFloat

Your class should support the following functions:
1. parse (String s) - a static function that returns an instance of AFloat class.
2. Overload the following integer arithmetic operators: (+, -, *, /)

Note:

Using these classes, one should be able to compute any expression involving arbitrarily large integers and arbitrarily large floating point precision numbers with arbitrary precision.

All the operations that you implement should preserve the complete information of the number and should not introduce any kind of round-off errors.

The actual decimal precision may vary depending on how your MyInfArith handles decimal scale/rounding, we will follow Java’s BigDecomal class and truncate at 30 decimal digits.

Expected classes in the arbitraryarithmetic/aarithmetic.jar:
1. arbitraryarithmetic/AInterger.class
2. arbitraryarithmetic/AFloat.class

Makefile instructions:
You can use Ant,Maven . It should have at least these targets.
1. More details to come

Two Modes:
As a python script with command -line arguments.
Create a public class MyInfArith with a main method , taking 4 command line arguments
0. <int/float>
1. <add/sub/mul/div>
2. First operand
3. Second operand
Create a Python script to compile and run the main Project.

As a Library:
The arbitraryarithmetic/aarithmetic.jar library which can be linked to any executable/library.

Evaluation Criteria (Total 100 marks)
Stage Domain          Operations Point
1     Integers           +, -     45
2     Integers           *, /     20
3     Floating point     +, -     20
4     Floating point     *, /     15

Submission guidelines:
Here are some general guidelines that you should follow: (All of these factors will have weights in the grading of this project)

Git:
● Use git commands to upload the assignment in GitHub classroom
● Assign git tags for each stage of the project.

Code:
● The code should be properly organized: what goes into each classes, functions, blocks etc
● The code should have appropriate separation between the source files and headers, etc.
● All the code should be properly documented. This includes
● proper variable names, proper indentation, …
● documentation (at local level, function level, class level).
● Report: Submit a report.pdf written in LaTeX describing your approach. It should clearly write the names and IDs of the team. It should outline at least the following:
● The design section, which describes the design decisions that you made. It should also include the class diagrams in UML format.
● A README section that explains to the users how to use your library.
● A snapshot of the git commits.
● Any limitations of your library.
● Verification approach
● Any key learnings from the overall project.

A few test Cases:

Input: java MyInfArith int add 2365007822491294949731093324025042939783262467113798386384401498
Output: 66589861487380063295697317641748

Input: java MyInfArith int sub 311651167400659980649551275857757745242300346381144446453884008
Output: -54628730626339781337950941125431

Input: java MyInfArith int mul 1434416316044592994268069731232223017167694823904478474013730519
Output: 330162008905899217578310782382075660760972861550182008086155118

Input: java MyInfArith int div 8792726365283060579833950521677211493835253617089647454998358
Output:17804979

Input: java MyInfArith float div 8792726365283060579833950521677211.0493835253617089647454998358
Output: 17804979.091469989302961159520087878533

Input: java MyInfArith float add 84486723.420039 70974199.843732
Output: 155460923.263771

Input: java MyInfArith float sub 840196454.51725 712586963.70283
Output: 127609490.81442

Input: java MyInfArith float mul 6400251.9377695 2326541.6827934
Output: 14890452913599.9717457253213

Input: java MyInfArith float div 244727.15202 75964.3891
Output: 3.22160363453775211100855150561

Input : java MyInfArith int div 25 123
Output : 0

Input : java MyInfArith float div 3227 555
Output : 5.8144144… (repeating till 30 digits)

Input : java MyInfArith float div 5.5 2
Output : 2.75

Input : java MyInfArith int div 2 0
Output : Division by zero error
