
/**
 * This code is a instruction Manual that generates Machine Language instructions for 
 * inputed Postfix expressions
 * The code checks the input character by character for Operators and Operands and performs a series of actions
 * if the code permits it
 * If the input includes invalid characters the Machine Language instructions will halt altogether from the 
 * stopping point
 * 
 * @version 1.0
 * @author sDantzler
 * */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PostFixInstructions {

   public static void main(String[] args) throws IOException {

      BufferedReader input;
      BufferedWriter output;
      StringStack strStack = new StringStack(100);

      // created arrays to trace the tempN variable and inputCount because
      // arrays pass by value unlike primitive type int
      int[] tempN = { 0 };
      int[] inputCount = { 1 };

      // Check for command line arguments.
      if (args.length != 2) {
         System.out.println(
               "Usage: java PostFixInstructions [input file pathname] [output filepathname]");
         System.exit(1);
      }

      // Open the files that will be used for input and output
      try {
         input = new BufferedReader(new FileReader(args[0]));
         output = new BufferedWriter(new FileWriter(args[1]));
         try {

            // create a boolean value to determine if a line input is
            // invalid if it is the program will continue reading until the next
            // line ('\n')
            boolean invalidLine = false;

            int c = 0;
            // Read character by character until the EOF
            while ((c = input.read()) != -1) {

               // This will convert everything in the file to a character
               char character = (char) c;
               
               // spaces will be ignored and do not impact the output
               if(character == ' ') {
                  continue;
                  
               }

               // If the invalid Line is true the input will continue to the
               // next line
               if (invalidLine) {
                  if (character == '\n') {
                     output.write("Invalid Postfix Expression for Input"
                           + inputCount[0] + "\n\n");
                     // decrement the inputCount to account for the increment in
                     // reset
                     inputCount[0]--;
                     reset(strStack, inputCount, tempN);
                     invalidLine = false;
                  } else {
                     continue;
                  }
               } // end invalidLine if statement

               // Create a boolean value isCharacter to account for a potential
               // stack overflow
               boolean isCharacter = false;
               try {
                  isCharacter = isOperand(character);
               } // end isCharacter try
               catch (ArrayIndexOutOfBoundsException ex) {
                  invalidLine = true;
                  // Invalid Input, Stack Overflow
                  continue;
               } // end isCharacter catch

               // Create a boolean value isOperator to account for a potential
               // stack underflow
               boolean isOperator = false;
               try {
                  isOperator = isOperator(character);
               } // end isOperator try
               catch (ArrayIndexOutOfBoundsException ex) {
                  invalidLine = true;
                  // Invalid Input, Stack Underflow
                  continue;
               } // end isOperator catch

               // if the character is an operand it will be converted to a
               // string and pushed onto the stack
               if (isCharacter) {
                  try {
                     strStack.push(Character.toString((character)));
                  } // end push stack try
                  catch (ArrayIndexOutOfBoundsException ex) {
                     invalidLine = true;
                     continue;
                  } // end push stack catch
               }

               // if the character is an operator the method to a pick an
               // operator will be invoked to carry out a series of actions
               // including popping the stack
               else if (isOperator) {

                  if (strStack.isEmpty()) {
                     invalidLine = true;
                  }
                  try {
                     operate(character, strStack, tempN, inputCount, output);

                  } // end operate try
                  catch (ArrayIndexOutOfBoundsException ex) {
                     invalidLine = true;
                     continue;
                  } // end operate catch
               }
               // if the character is a \n the next input will start with the
               // appropriate variables reset
               else if (character == '\n') {
                  reset(strStack, inputCount, tempN);
               }
               // if the character is not any of the above then the remainder
               // of the line will be discarded and move onto the next input
               else if (!isOperand(character) && !isOperator(character)
                     && (character != '\n')) {
                  invalidLine = true;
               }
            } // end while
         } // end inner try
           // Clean up and return to the operating system
         finally {
            try {
               input.close();
               output.close();
            } // end finally try
            catch (Exception x) {
               output.write(x.toString());
            } // end finally catch
         }
         return;
      } // end try
      catch (IOException e) {
         System.out.println("Input and/or Output file is invalid");
      } // end catch

   }// end main method

   /**
    * The isOperand method checks if the character from the input is a valid
    * operand
    * 
    * @param character
    */
   private static boolean isOperand(char character) {

      // This will ensure that the letter entered has the correct unicode value
      character = Character.toUpperCase(character);

      if (character >= 'A' && character <= 'Z') {
         return true;
      }
      return false;

   }// end method isOperand

   /**
    * The isOperator method checks if the character from the input is a valid
    * operator
    * 
    * @param character
    */
   private static boolean isOperator(char character) {
      if (character == '+' || character == '-' || character == '/'
            || character == '$' || character == '*') {
         return true;
      }
      return false;
   }// end isOperator method

   /**
    * The operate method is invoked when there is an operator and it will calls
    * for the stack to be popped twice to perform the operation and then pushes
    * the temporary stored variable in the stack and writes a series of Machine
    * Language instructions to the output
    * 
    * @param character, strStack, tempN[], inputCount[], output
    * @throws IOException
    */
   private static void operate(char character, StringStack strStack,
         int[] tempN, int[] inputCount, BufferedWriter output)
         throws IOException {

      String a, b, tempVar;

      try {
         if (tempN[0] == 0) {
            output.write("\n#Input" + inputCount[0]
                  + " instructions are as follows: \n\n");
         }

         switch (character) {
            case '+':
               a = strStack.pop();
               b = strStack.pop();
               tempN[0]++;
               tempVar = "TEMP" + tempN[0];
               strStack.push(tempVar);
               output.write("LD " + b + "\nAD " + a + "\nST " + tempVar + "\n");
               break;
            case '-':
               a = strStack.pop();
               b = strStack.pop();
               tempN[0]++;
               tempVar = "TEMP" + tempN[0];
               strStack.push(tempVar);
               output.write("LD " + b + "\nSB " + a + "\nST " + tempVar + "\n");
               break;
            case '/':
               a = strStack.pop();
               b = strStack.pop();
               tempN[0]++;
               tempVar = "TEMP" + tempN[0];
               strStack.push(tempVar);
               output.write("LD " + b + "\nDV " + a + "\nST " + tempVar + "\n");
               break;
            case '*':
               a = strStack.pop();
               b = strStack.pop();
               tempN[0]++;
               tempVar = "TEMP" + tempN[0];
               strStack.push(tempVar);
               output.write("LD " + b + "\nML " + a + "\nST " + tempVar + "\n");
               break;
            case '$':
               a = strStack.pop();
               b = strStack.pop();
               tempN[0]++;
               tempVar = "TEMP" + tempN[0];
               strStack.push(tempVar);
               output.write("LD " + b + "\nEX " + a + "\nST " + tempVar + "\n");
               break;
         }// end switch
      } // end try
      catch (IOException ioe) {
         output.write("No output file defined");
      } // end catch
   }// end operate method

   /**
    * The reset method allows for the next line of input to start with an empty
    * stack and sets the tempN variable back to factory settings of 0 and
    * increases the input count to print out the instructions
    * 
    * @param strStack, inputCount[],tempN[]
    */
   private static void reset(StringStack strStack, int[] inputCount,
         int[] tempN) {

      while (!strStack.isEmpty()) {
         strStack.pop();
      }
      inputCount[0]++;
      tempN[0] = 0;

   }// end reset method

}// end PostFixInstructions
