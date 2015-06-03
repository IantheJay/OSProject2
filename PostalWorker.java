import java.lang.InterruptedException;
import java.lang.System;

public class PostalWorker implements Runnable {
    private int c_number;   //customer number
    private int task;       //customer task
    private int powNum;     //Postal worker number
    private PostOffice office;  //Shared PostOffice object

    public PostalWorker(int number, PostOffice office) {
        powNum = number;
        this.office = office;
    }

    public void run() {
        try {
            System.out.println("Post office worker " + powNum + " was created");
            while (true) {
                //waits for customer to write to queue before worker
                //reads from it
                office.pw_wait.acquire();
                office.queueMutex.acquire();
                gatherCustInformation();
                System.out.println("Postal worker " + powNum + " is serving customer " + c_number);

                //Releases queue so that it can be used by other threads
                office.queueMutex.release();

                //assign worker to customer, then signal that customer can read
                //which postal worker is assigned
                office.setWorkerAssignment(powNum,c_number);
                office.workerNbrSet.release();

                //Waits until customer request is made
                office.customerRequest.acquire();

                //Postal worker handles customer's task
                processTask();
                System.out.println("Postal worker " + powNum + " is finished serving customer " + c_number);
                
                //Release customer by signaling its semaphore for finishing
                office.custFinished[c_number].release();
            }
        }catch (InterruptedException ex) {
            //Default catch in case of threads being interrupted
            ex.printStackTrace();
            return;
        }
    }

    private void gatherCustInformation() {
        //customer number is dequeued
        c_number = office.dequeue();
        //customer task is dequeued
        task = office.dequeue();
    }

    //Handles appropriate actions for the type of task the customer requested
    private void processTask() {
        try {
            switch (task) {
                case 0:
                    Thread.sleep(1000);
                    break;
                case 1:
                    Thread.sleep(1500);
                    break;
                case 2:
                    office.scaleMutex.acquire();
                    System.out.println("Scales in use by postal worker " + powNum);
                    Thread.sleep(2000);
                    office.scaleMutex.release();
                    System.out.println("Scales released by postal worker " + powNum);
                    break;
                default:
                    System.out.println("An error occurred.");
                    break;
            }
        }catch (InterruptedException ex) {
            //Default catch in case of threads being interrupted
            ex.printStackTrace();
        }
    }
}
