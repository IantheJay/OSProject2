import java.util.concurrent.Semaphore;

public class PostOffice {
   //Enforces the max number of customers allowed within the post office
   private Semaphore max_capacity;
   //Number of postal workers available
   private Semaphore postWorkers;
   //Used to coordinate postal workers and customers
   private Semaphore workerAvailable;
   //Enforces use of scale by postal workers
   private Semaphore scaleMutex;
   //Allows correct customer to be released before signaling
   private Semaphore[] custFinished;

   public Postoffice() {
      max_capacity = new Semaphore(10, true);    //10 customers @ a time
      postWorkers = new Semaphore(3, true);      //3 postal workers

      //used to coordinate assigning workers to customers
      workerAvailable = new Semaphore(0, true);
      //One postal worker can access scale at a time
      scaleMutex = new Semaphore(1, true);
      //Establishes 50 customers
      custFinished = new Semaphore[50];

      for( int i = 0; i < 50; i++ ) {
         //Set to zero to make customer wait for the postal worker's
         //signal that their requested task is complete
         custFinished[i] = 0;
      }
   }
   
   public postalWorkerReady() {
      
   }
}
