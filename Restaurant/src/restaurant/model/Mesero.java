package restaurant.model;

import java.util.Observable;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mesero extends Observable implements Runnable{
    
    
    private String name;
    private int numOrden;
    private boolean isOcuped;
    
    private Cliente cliente;
    private Restaurant restaurant;
    
    private Semaphore despierto = new Semaphore(0);
    private Semaphore esperarOrdenLista = new Semaphore(0);
    
    public Mesero(){}
    public Mesero(String name){
        this.name = name;
        
    }
    
    public void setRestaurant(Restaurant restaurant){
        this.restaurant=restaurant;
        
    }
    
    @Override
    public void run() {
        //tengo referencia de cliente asignado
        Orden orden;
        while(true){
            try {
                despierto.acquire();//se bloquea hasta que alguien pida un mesero
               
                
                System.out.println("\nMESERO CLASS ->\t\t"+name+" Esperando orden lista...");
                
                this.setChanged();
                this.notifyObservers("esperando orden:"+cliente.getNumMesaAsignada()+":"+cliente.tipo);
                
                restaurant.obtenerOrdenLista(numOrden);
                
                System.out.println("MESERO CLASS ->\t\t"+name+"Dando orden al cliente "+cliente.getName()+" ...");
                cliente.comer();
                
                this.setChanged();
                this.notifyObservers("sirviendo orden:"+cliente.getNumMesaAsignada()+":"+cliente.tipo);
                
                Thread.sleep(1000);
                isOcuped = false;
                
                this.setChanged();
                this.notifyObservers("esperando orden:"+cliente.getNumMesaAsignada()+":"+cliente.tipo);
                
                restaurant.notifyReleaseMesero();
            } catch (InterruptedException ex) {
                Logger.getLogger(Cocinero.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    
    
    public  boolean isOcuped(){
        return isOcuped;
    }
    
    public void setOcuped(boolean isOcuped){
        this.isOcuped = isOcuped;
    }
    
    public String getName(){
        return name;
    }
    
    public void tomarOrden(Cliente cliente) throws InterruptedException{
        this.cliente = cliente;
        
        this.setChanged();
        this.notifyObservers("tomando orden:"+cliente.getNumMesaAsignada()+":"+cliente.tipo);
        Thread.sleep(2000);
        
        despierto.release();
        
        numOrden = restaurant.generarOrden(cliente, this);
        
        
    }
    
    public void notificarOrdenLista(){
        this.notifyAll();//se desbloquea para volver a preguntars
    }
    
    
    
}
