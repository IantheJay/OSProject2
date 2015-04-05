public class PostalWorker implements Runnable {
   private Customer cstmr;
   private int powNum;

   public PostalWorker( int number ) {
      cstmr = null;
      powNum = number;
   }

   public void run() {
      System.out.println("Post office worker " + powNum + "was created.");
   }

   public void setCurrentCustomer( Customer cst ) {
      cstmr = cst;
   }

   public void releaseCustomer() {
      cstmr = null;
   }

   
