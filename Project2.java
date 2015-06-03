import java.lang.InterruptedException;
import java.util.concurrent.Semaphore;

public class Project2 {
	public static void main(String[] args) throws InterruptedException {
		PostOffice postOffice = new PostOffice();
       	Thread[] cust = new Thread[50];
       	boolean finished = false;

       	System.out.println("Simulating Post Office with 50 customers and 3 postal workers\n");

        //Instantiates customer threads and starts their execution
        for( int i = 0; i < 50; i++ ) {
            cust[i] = new Thread(new Customer(i, postOffice));
            cust[i].start();
        }

        Thread[] pow = new Thread[3];

        //Instantiates postal worker threads and starts their execution
        for( int i = 0; i < 3; i++ ) {
            pow[i] = new Thread(new PostalWorker(i, postOffice));
            pow[i].setDaemon(true);	//allows for threads to be properly terminated when the program ends
            pow[i].start();
        }

	//joins customers as they finish executing
        for( int i = 0; i < 50; i++ ) {
            cust[i].join();
            System.out.println("Joined customer " + i);
        }

        System.out.println("\n\nSimulation finished.\n\n");

        return;
	}
}
