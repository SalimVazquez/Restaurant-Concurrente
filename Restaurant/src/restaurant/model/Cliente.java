package restaurant.model;

import java.util.Observable;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Cliente extends Observable implements Runnable{
    private static Random random = new Random(System.currentTimeMillis());
    int tipo;
    int numMesaAsignada ;
    private String name;
    private boolean flag = false;
    private Mesero mesero;
    private Restaurant restaurant;
    private Recepcionista recepcionista;
   
    
    private Semaphore comer = new Semaphore(0);
    
    public Cliente(Recepcionista recepcionista, Restaurant restaurant, String name){
        this.recepcionista = recepcionista;
        this.restaurant = restaurant;
        this.name = name;
        
    }
    
    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        
        try {
            tipo = (int)(Math.random()*2);
           
            if(tipo == 1){
                numMesaAsignada = restaurant.entrarRestaurantVIP();// se bloquea si no hay espacios libres
                Thread.sleep(2000);
            }else
                numMesaAsignada = restaurant.entrarRestaurant();// se bloquea si no hay espacios libres
            
            
            
            recepcionista.pasarRestaurant();
            if(tipo == 1)
                System.out.println("CLIENTE CLASS ->\t"+name+"(VIP) mesa :"+numMesaAsignada+" \tsolicitando mesero");
            else
                System.out.println("CLIENTE CLASS ->\t"+name+" mesa :"+numMesaAsignada+" \tsolicitando mesero");

            this.setChanged();
            this.notifyObservers("entrando:"+tipo+":"+numMesaAsignada);

            mesero = restaurant.solicitarMesero(name);//this.name//se bloquea si no hay meseros libres
            //System.out.println("CLIENTE CLASS ->\t"+name +" Mesero asignado = "+mesero.getName());

            System.out.println("CLIENTE CLASS ->\t"+name+" mesa :"+numMesaAsignada+" Ordenando Comida");
            mesero.tomarOrden(this);



            
            comer.acquire();
            this.setChanged();
            this.notifyObservers("comiendo:"+tipo+":"+numMesaAsignada);
            
            Thread.sleep(10000);
            if(tipo == 1)
                System.out.println("CLIENTE CLASS ->\t"+name+"(VIP) Salio");
            else
                System.out.println("CLIENTE CLASS ->\t"+name+" Salio");
            
            this.setChanged();
            this.notifyObservers("salio:"+tipo+":"+numMesaAsignada);
            
            if(tipo == 1 )
                restaurant.salirRestaurantVIP(numMesaAsignada);// se bloquea si no hay espacios libres
            else
                 restaurant.salirRestaurant(numMesaAsignada);// se bloquea si no hay espacios libres
            
            
        } catch (Exception e) {
        }
        
    }
    
    
    public void comer(){ comer.release();}
    
    public String getName(){return name;}
    
    public int getNumMesaAsignada(){ return numMesaAsignada; }
    
   
    
    
}
