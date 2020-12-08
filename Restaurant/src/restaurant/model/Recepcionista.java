package restaurant.model;

import java.util.Observable;
import java.util.concurrent.Semaphore;

public class Recepcionista extends Observable implements Runnable{
 
    private boolean isOcuped;
    private Semaphore pasar = new Semaphore(0);
    private Object pasarRestaurant = new Object();
    
    
    public Recepcionista(){
    }

    @Override
    public void run() {
        System.out.println("RECEPCIONISTA CLASS -> running recepcionuista\n\n");
        while(true){
            try {
                pasar.acquire();
                
            } catch (Exception e) {
            }
            
        }
    }
    
    
    public  void pasarRestaurant() throws InterruptedException{
        synchronized(pasarRestaurant){
            while(isOcuped)
                pasarRestaurant.wait();

            isOcuped = true;
            
            //Thread.sleep(2000);
            pasar.release();
            
            isOcuped = false;
            pasarRestaurant.notifyAll();
        }
        
    }
    

    
}
