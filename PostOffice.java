import java.lang.InterruptedException;
import java.util.concurrent.Semaphore;
import java.util.Queue;
import java.util.LinkedList;

public class PostOffice {
    //Enforces the max number of customers allowed within the post office
    protected Semaphore max_capacity = new Semaphore(10, true);
    //Enforces customer request before postal workers action
    protected Semaphore customerRequest = new Semaphore(0, true);
    //Signals customer to get worker number
    protected Semaphore workerNbrSet= new Semaphore(0, true);
    //Establishes when postal worker is available
    protected Semaphore workerAvailable = new Semaphore(3, true);
    //Enforces use of scale by postal workers
    protected Semaphore scaleMutex = new Semaphore(1, true);
    //Allows only one thread to access queue at a time
    protected Semaphore queueMutex = new Semaphore(1, true);
    //Postal worker waits until customer has input number and task
    protected Semaphore pw_wait = new Semaphore(0, true);

    //Allows correct customer to be released before signaling
    protected Semaphore[] custFinished = new Semaphore[50];

    //Stores worker number for customer that is using it
    private int[] workerAssignment = new int[50];
    //Queue to hold customer number and task , coordinated by queueMutex
    private Queue<Integer> queue = new LinkedList<Integer>();

    private int finishedCount = 0;

    public PostOffice() {
        for( int i = 0; i < 50; i++ ) {
            //Set to zero to make customer wait for the postal worker's
            //signal that their requested task is complete
            custFinished[i] = new Semaphore(0, true);
        }
    }

    //Returns worker number assigned to a specific customer
    public int acquireWorker( int cust_number ) throws InterruptedException {
        return workerAssignment[cust_number];
    }

    //Utilizes the queue to pass customer information to the Postal Worker
    public void enqueue( int cust_number, int task ) throws InterruptedException {
        queue.add(cust_number);
        queue.add(task);
    }

    /*
      Returns the current head of the queue to the Postal worker.
      Run twice for each postal worker to get the customer number and
      requested type of task.
    */
    public int dequeue() {return queue.remove();}

    
    //Assigns postal worker to thread signified by location within workerAssignment
    public void setWorkerAssignment( int workerNumber, int location ) throws InterruptedException {
        workerAssignment[location] = workerNumber;
    }

    /*public static void main(String[] args) throws InterruptedException {
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
            pow[i].setDaemon(true);
            pow[i].start();
        }

        for( int i = 0; i < 50; i++ ) {
            cust[i].join();
            System.out.println("Joined customer " + i);
        }

        System.out.println("\n\nSimulation finished.\n\n");
        return;
    }*/
}
