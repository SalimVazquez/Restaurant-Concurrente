package restaurant.model;

import java.util.Observable;
import java.util.concurrent.Semaphore;

public class Cocinero extends Observable implements Runnable{
    private String name;
    private boolean flag = false;
    private boolean isOcuped;
    private boolean buscandoOrdenes = false;
    private Semaphore cocinar = new Semaphore(0);
    
    
    //private Semaphore despierto = new Semaphore(0);
    Restaurant restaurant;
    
    public Cocinero(){}
    public Cocinero(String name){
        this.name = name;
        
    }
    
    
    @Override
    public void run() {
        Orden orden;
        try {
             cocinar.acquire();
             flag = true;
            while(true){
                
                this.setChanged();
                this.notifyObservers("esperando");
                
                orden = restaurant.getCocinarOrden(name);
                
                System.out.println("\nCOCINERO CLASS ->\t\t"+name+" COCINANDO ORDEN..."+orden.getNumOrden());
                
                //cocinando
                this.setChanged();
                this.notifyObservers("cocinando");
                
                Thread.currentThread().sleep(4000);
                restaurant.setOrdenPrerada(orden.getNumOrden());
                
                this.setChanged();
                this.notifyObservers("orden lista");
            }
        } catch (Exception e) {
            System.out.println("Cocinero"+e);
        }
    }
    
    public void setRestaurant(Restaurant restaurant){
        this.restaurant=restaurant;
    }
   
    
    public synchronized boolean isOcuped(){
        return isOcuped;
    }
    
    public void setOcuped(boolean isOcuped){
        this.isOcuped = isOcuped;
    }
    
    public void cocinar(){
        //despierto.release();
        try {
            if(!flag)
                cocinar.release();
        } catch (Exception e) {
        }
    }
    
    
    
    
}
