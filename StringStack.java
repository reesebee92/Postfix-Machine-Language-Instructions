/**
 * A stack of Strings
 * @version 1.0
 * @author sDantzler
 * 
 * */


public class StringStack{
      
      private int stackMax;
      private int top;
      private String [] items;
      
      /**
       * StringStack constructor will create a new stack of Strings
       * @param the arraySize used to determine the max number 
       * of elements the stack can hold
       * 
       * */
      public StringStack(int arraySize) {
         
         stackMax = arraySize;
         top = -1;
         items = new String[stackMax];
         
      }// end constructor
      
      /**
       * Checks to see if the stack is empty
       * returns true or false value
       * */
      public boolean isEmpty() {
         
         if(top == -1) {
            return true;
         }
         else 
            return false;
      }// end isEmpty method
      
      /**
       * Checks to see if the stack is full
       * returns true or false value
       * */
      public boolean isFull() {
         if (top == stackMax - 1) {
            return true;
         }
         else
            return false;
      }// end isFull method
      
      /**
       * Pushes a value onto the stack if there is space 
       * returns a statement if the stack is full
       * @throws Exception 
       * */
      public void push(String x) throws ArrayIndexOutOfBoundsException {
         
         if (isFull()) {
            throw new ArrayIndexOutOfBoundsException ("The stack is full!");
         }
         else {
            top++;
            items[top] = x;
         }
      }// end push method
      
      public String pop() throws ArrayIndexOutOfBoundsException {
         if(isEmpty()) {
            throw new ArrayIndexOutOfBoundsException ("The stack is empty!");
         }
            return items[top--];
      }// end pop method
      
      /**
       * This will allow a view of the top item without
       * removing the item from the stack
       * 
       * return top item view
       * */
      public String peek() {
         if(isEmpty()) {
            System.out.println("The stack is empty");
         }
         return (items[top]);
         
      }// end peek method
      
      /**
       * This will display all the items in the stack
       * */
      public void display() {
         
         for (int index = 0; index <= top; index++) {
            System.out.print(items[index] + " ");
         }
         System.out.println();
      }// end display method

}// end class StringStack