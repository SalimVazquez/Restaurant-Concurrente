
package restaurant.model;

import java.util.ArrayList;

public class Restaurant {
    private int tamRestaurant;
    private int numReservaciones;
    private int numEspaciosLibres;
    private int numOrden = 1;
    
    private ArrayList<Mesero> listMeseros = new ArrayList<>();
    private ArrayList<Cocinero> listCocineros = new ArrayList<>();
    private ArrayList<Orden> bufferOrdenes = new ArrayList<>();
    
    private Object ServirOrden=new Object();
    private Object PedirMesero = new Object();
    private Object PasarRestaurant = new Object();
    private Object PasarRestaurantVIP = new Object();
    private Object BuscarBufferOrden = new Object();
    
    private String [] lugaresLibres = new String[8];
    private String [] lugaresVIPLibres = new String[2];
    private String [] lugaresPlatoBarraLibres = new String[4];
    
    private boolean buscandoLugar = false;
    private boolean buscandoLugarVIP = false;
    private boolean buscandoOrdenes = false;
    
    
    
    public Restaurant(){}
    
    public Restaurant(int numEspacios, ArrayList<Mesero> listMeseros,
            ArrayList<Cocinero> listCocineros,ArrayList<Orden> bufferOrdenes ){
        this.tamRestaurant = numEspacios;
        this.listMeseros = listMeseros;
        this.listCocineros = listCocineros;
        this.bufferOrdenes = bufferOrdenes;
        numReservaciones = 2;
        numEspaciosLibres = 8;
       
        for(int i = 0; i < lugaresLibres.length; i++){
            lugaresLibres[i] = "LIBRE";
        }
        for(int i = 0; i < lugaresVIPLibres.length; i++){
            lugaresVIPLibres[i] = "LIBRE";
        }
        
        for(int i = 0; i < lugaresPlatoBarraLibres.length; i++){
            lugaresPlatoBarraLibres[i] = "LIBRE";
        }
        
    }
    public int getTamRestaurant() {
        return tamRestaurant;
    }

    public void setTamRestaurants(int numEspacios) {
        this.tamRestaurant = numEspacios;
    }

    public ArrayList<Mesero> getListMeseros() {
        return listMeseros;
    }

    public ArrayList<Cocinero> getListCocineros() {
        return listCocineros;
    }
    
    public ArrayList<Orden> getBufferOrdenes(){
        return bufferOrdenes;
    }
  
    
    public int entrarRestaurantVIP() throws InterruptedException{
        int asientoAsignado =0;
        synchronized(PasarRestaurantVIP){
            while(numReservaciones==0 || buscandoLugarVIP)
                PasarRestaurantVIP.wait();
            
            buscandoLugarVIP = true;
            
            numReservaciones --;
            Thread.sleep(1000);
            for(int i = 0; i < lugaresVIPLibres.length; i++){
                
                if(lugaresVIPLibres[i].equals("LIBRE")){
                    asientoAsignado = i;
                    lugaresVIPLibres[i] = "OCUPADO";
                    break;
                }
            }
            
            buscandoLugarVIP = false;
        }
        
        return asientoAsignado;
    }
    public void salirRestaurantVIP(int lugarAsignado) throws InterruptedException{
        synchronized(PasarRestaurantVIP){    
            
             System.out.println("Salio un VIP");
            numReservaciones ++;
            
            lugaresVIPLibres[lugarAsignado] = "LIBRE";
            
            PasarRestaurantVIP.notifyAll();
        }
    }
    public int entrarRestaurant() throws InterruptedException{
        int asientoAsignado=0 ;
        synchronized(PasarRestaurant){
            while(numEspaciosLibres==0 || buscandoLugar)
                PasarRestaurant.wait();
            
            buscandoLugar = true;
            numEspaciosLibres --;
            Thread.sleep(1000);
            for(int i = 0; i < lugaresLibres.length; i++){
                if(lugaresLibres[i].equals("LIBRE")){
                    asientoAsignado = i;
                    lugaresLibres[i] = "OCUPADO";
                    break;
                }
            }
            buscandoLugar = false;
            
        }
        return asientoAsignado;
    }
    public void salirRestaurant(int lugarAsignado) throws InterruptedException{
        synchronized(PasarRestaurant){    
            numEspaciosLibres ++;
            
            lugaresLibres[lugarAsignado] = "LIBRE";
            
            PasarRestaurant.notifyAll();
        }
    }
    
    public  Mesero solicitarMesero(String nameClient) throws InterruptedException{
        boolean flag = false;//para saber si hay algun mesero libre
        Mesero mesero = new Mesero();
        synchronized(PedirMesero){
             while(!flag){
                 
                for(int i = 0; i < listMeseros.size(); i++){
                    //System.out.println(nameClient+""+listMeseros.get(i).getName()+":"+listMeseros.get(i).isOcuped());
                    if(!listMeseros.get(i).isOcuped()){
                        flag = true;
                        listMeseros.get(i).setOcuped(true);
                        mesero = listMeseros.get(i);
                        break;
                    }
                }
                if(!flag)
                    PedirMesero.wait();//
             }
        }
        return mesero;
    }
    public void notifyReleaseMesero(){
        synchronized(PedirMesero){
            PedirMesero.notifyAll();
        }
    }
    
    public  int generarOrden(Cliente client, Mesero mesero){
        for(int i = 0; i < listCocineros.size(); i++){
            listCocineros.get(i).cocinar();
        } 
        synchronized(BuscarBufferOrden){
            Orden orden = new Orden(numOrden, client, mesero);
            orden.setStatus("EN PROCESO");
            bufferOrdenes.add(orden);
            //System.out.println("\nRESTAURANT CLASS ->\t\t buffer ordenes -->\t"+bufferOrdenes.get(numOrden).getStatus());
            numOrden ++;
            BuscarBufferOrden.notifyAll();
        }
        //imprimirOrdenes();
        return numOrden-1;
    }
    private synchronized void imprimirOrdenes(){
        Orden orden;
        System.out.println("\t\n=================Historial ordenes=================");
        for(int i = 0; i < bufferOrdenes.size(); i++){
            orden = bufferOrdenes.get(i);
            System.out.println("\tNum Orden..."+orden.getNumOrden()+"\t| status: +"+orden.getStatus());
        }
        System.out.println("\t===================================================\n");
    }
    
    public Orden getCocinarOrden(String name) throws InterruptedException{
        Orden orden = new Orden();
        
        synchronized(BuscarBufferOrden){
            while(!newOrdenes())
                BuscarBufferOrden.wait();
                
            
            buscandoOrdenes = true;
            for(int i = 0; i < bufferOrdenes.size(); i++){
                //System.out.println("\nRESTAURANT CLASS ->\t\t Orden Status..."+bufferOrdenes.get(i).getStatus());
                //System.out.println(bufferOrdenes.get(i).getStatus().equals("EN PROCESO"));
                if(bufferOrdenes.get(i).getStatus().equals("EN PROCESO") ){
                    bufferOrdenes.get(i).setStatus("COCINANDO");
                    orden = bufferOrdenes.get(i);
                    //System.out.println("\n137: RESTAURANT CLASS ->\t\t Cocinando Num orden..."+orden.getNumOrden());
                    break;
                }
                    
            }
            buscandoOrdenes = false;
            BuscarBufferOrden.notifyAll();
        }
        return orden;
        
    }
    public synchronized boolean newOrdenes(){
        for(int i = 0; i < bufferOrdenes.size(); i++){
            if(bufferOrdenes.get(i).getStatus().equals("EN PROCESO"))
                return true;
        }
        return false;
    }
    
    public void obtenerOrdenLista(int numOrden) throws InterruptedException{
        synchronized(ServirOrden){
            while(!isMiOrdenLista(numOrden))
                ServirOrden.wait();
        }
    }
    
    public boolean isMiOrdenLista(int numOrden){
        for(int i = 0; i < bufferOrdenes.size(); i++){
            if(bufferOrdenes.get(i).getNumOrden() == numOrden && 
                    bufferOrdenes.get(i).getStatus().equals("PREPARADA"))
                return true;
        }
        return false;
    }
    public synchronized void setOrdenPrerada(int numOrden){
        for(int i = 0; i < bufferOrdenes.size(); i++){
            if(bufferOrdenes.get(i).getNumOrden()== numOrden )
                bufferOrdenes.get(i).setStatus("PREPARADA");
            
            synchronized(ServirOrden){
                ServirOrden.notifyAll();
            }
                
        }
    }
    
    public synchronized void aumentarReservaciones(){
        numReservaciones ++;
    }
    
  
    
}
