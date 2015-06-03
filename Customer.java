import java.lang.InterruptedException;
import java.lang.System;
import java.util.Random;

public class Customer implements Runnable {
    Random rand = new Random();
    final private int task;
    final private int cust_number;
    final private PostOffice office;
    final private int workerNumber;

    public Customer( int cust_number, PostOffice office ) {
        this.cust_number = cust_number;
        this.office = office;
        workerNumber = -1;

        //Randomizes the task that each customer gets
        task = rand.nextInt(3);
    }

    public void run() {
        try {
            System.out.println("Customer " + cust_number + " was created.");
            //Checks if post office is full and acquires a permit if it is not
            office.max_capacity.acquire();
            System.out.println("Customer " + cust_number + " entered the post office.");
           
            //Acquires a postal worker if one is available
            office.workerAvailable.acquire();

            //Protects the queue from multiple threads trying to read or write from it
            //at the same time.
            office.queueMutex.acquire();
            office.enqueue(cust_number, task);
            office.pw_wait.release();
            office.queueMutex.release();

            //Only acquires the worker number when a signal has been sent to prevent
            //the customer from prematurely reading from its workerAssignment location
            office.workerNbrSet.acquire();
            System.out.println("Customer " + cust_number + " asks postal worker " + office.acquireWorker(cust_number) + " to " + stringTask());
            
            //Releases a customerRequest permit when customer has already output its
            //requested task
            office.customerRequest.release();

            //Wait until customer's task is completed by postal worker
            office.custFinished[cust_number].acquire();
            System.out.println("Customer " + cust_number + " finished their task to " + stringTask());

            //Releases workerAvailable and max_capacity permits so that a customer
            //can be assigned a postal worker and another can enter the post office
            System.out.println("Customer " + cust_number + " leaves the post office");
            office.workerAvailable.release();
            office.max_capacity.release();
        }catch (InterruptedException ex) {
            //Default catch in case of threads being interrupted
            ex.printStackTrace();
        }
    }

    //Puts task in a String format for output purposes
    public String stringTask() {
        String tempString = null;

        switch (task) {
            case 0:
                tempString = "buy stamps";
                break;
            case 1:
                tempString = "mail letter";
                break;
            case 2:
                tempString = "mail package";
                break;
            default:
                System.out.println("An error occurred.");
                break;
        }

        return tempString;
    }
}
