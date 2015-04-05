import java.util.Random;

public class Customer implements Runnable {
   Random rand = new Random();
   final private int purpose;
   final private int cust_number;
   final private PostOffice office;

   public Customer( int cust_number, PostOffice office ) {
      this.cust_number = cust_number;
      this.office = office;

      purpose = rand.nextInt(3);
      System.out.println("Customer " + this.cust_number + " created.");
   }

   public void run() {
      office.customerReady();
   }

   public int getPurpose() {
      return purpose;
   }
}
