package restaurant.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import restaurant.model.Cliente;
import restaurant.model.Cocinero;
import restaurant.model.Mesero;
import restaurant.model.Orden;
import restaurant.model.Recepcionista;
import restaurant.model.Restaurant;

public class RestaurantController implements Observer, Initializable{
    Restaurant restaurant;
    Recepcionista recepcionista ;
    private Semaphore pasarComensal = new Semaphore(0);
    private ArrayList<Rectangle> listEstufas = new ArrayList<>();
    private ArrayList<Circle> listAsientos = new ArrayList<>();
    private ArrayList<Circle> listVIPAsientos = new ArrayList<>();
    private String[] asientosLibres = new String[18];
    private String[] reservacionesLibres = new String[2];
    private String[] ordenesListas = new String[4];

    @FXML
    private ImageView imgClient11;

    @FXML
    private ImageView imgClient12;

    @FXML
    private ImageView imgClient13;

    @FXML
    private ImageView imgClient14;

    @FXML
    private ImageView imgClient15;

    @FXML
    private ImageView imgClient16;

    @FXML
    private ImageView imgClient17;

    @FXML
    private ImageView imgClient18;

    @FXML
    private ImageView imgClient19;

    @FXML
    private ImageView imgClient20;

    @FXML
    private ImageView imgClient8;

    @FXML
    private ImageView imgClient9;

    @FXML
    private ImageView imgClient10;

    @FXML
    private ImageView imgClient7;

    @FXML
    private ImageView imgClient6;

    @FXML
    private ImageView imgClient5;

    @FXML
    private ImageView imgClient4;

    @FXML
    private ImageView imgClient3;

    @FXML
    private ImageView imgClient2;

    @FXML
    private ImageView imgClient1;

    @FXML
    private Rectangle estufa1;

    @FXML
    private Rectangle estufa2;

    @FXML
    private Rectangle estufa3;

    @FXML
    private Rectangle estufa4;

    @FXML
    private Rectangle estufa5;

    @FXML
    private TextField noClientes;

    @FXML
    private Circle mesa1;

    @FXML
    private Circle mesa2;

    @FXML
    private Circle mesa3;

    @FXML
    private Circle mesa4;

    @FXML
    private Circle mesa5;

    @FXML
    private Circle mesa6;

    @FXML
    private Circle mesa7;

    @FXML
    private Circle mesa8;

    @FXML
    private Circle mesa9;

    @FXML
    private Circle mesa10;

    @FXML
    private Circle mesa11;

    @FXML
    private Circle mesa12;

    @FXML
    private Circle mesa13;

    @FXML
    private Circle mesa14;

    @FXML
    private Circle mesa15;

    @FXML
    private Circle mesa16;

    @FXML
    private Circle mesa17;

    @FXML
    private Circle mesa18;

    @FXML
    private Circle mesa19;

    @FXML
    private Circle mesa20;

    @FXML
    void btnExit(MouseEvent event) {  System.exit(1);    }

    @FXML
    void btnStart(MouseEvent event) {
        crearPersonajes(Integer.parseInt(noClientes.getText()));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listEstufas.add(estufa1);
        listEstufas.add(estufa2);
        listEstufas.add(estufa3);
        listEstufas.add(estufa4);
        listEstufas.add(estufa5);
        listVIPAsientos.add(mesa19);
        listVIPAsientos.add(mesa20);
        listAsientos.add(mesa1);
        listAsientos.add(mesa2);
        listAsientos.add(mesa3);
        listAsientos.add(mesa4);
        listAsientos.add(mesa5);
        listAsientos.add(mesa6);
        listAsientos.add(mesa7);
        listAsientos.add(mesa8);
        listAsientos.add(mesa9);
        listAsientos.add(mesa10);
        listAsientos.add(mesa11);
        listAsientos.add(mesa12);
        listAsientos.add(mesa13);
        listAsientos.add(mesa14);
        listAsientos.add(mesa15);
        listAsientos.add(mesa16);
        listAsientos.add(mesa17);
        listAsientos.add(mesa18);
    }
    
    
    
    private void crearPersonajes(int numeroComensales){
        ArrayList<Mesero> listMeseros = new ArrayList<>();
        ArrayList<Cocinero> listCocineros = new ArrayList<>();
        ArrayList<Orden> bufferOrdenes = new ArrayList<>();
        Thread hiloMesero;
        Thread hiloCocinero;
        int numEmpleados = (int) (numeroComensales*.10);
        recepcionista = new Recepcionista();
        //recepcionista.
        Thread hiloRecepcionista = new Thread(recepcionista);
        hiloRecepcionista.setDaemon(true);
        hiloRecepcionista.start();
        Cliente cliente;
        Cocinero cocinero;
        Mesero mesero;
        for(int i  = 0; i < asientosLibres.length; i++){
            asientosLibres[i] = "libre";
        }
        for(int i  = 0; i < ordenesListas.length; i++){
            ordenesListas[i] = "libre";
        }
        for (int i = 0; i < numEmpleados; i++){
            cocinero=new Cocinero("COCINERO "+i);
            mesero = new Mesero("MESERO "+i);
            cocinero.addObserver(this);
            mesero.addObserver(this);
            hiloMesero= new Thread(mesero);
            hiloMesero.setDaemon(true);
            hiloMesero.start();
            hiloCocinero = new Thread(cocinero);
            hiloCocinero.setDaemon(true);
            hiloCocinero.start();
            listMeseros.add(mesero);
            listCocineros.add(cocinero);
        }
        
        restaurant = new Restaurant(numeroComensales, listMeseros, listCocineros, bufferOrdenes);
        for (int i = 0; i < numEmpleados; i++){
            listMeseros.get(i).setRestaurant(restaurant);
            listCocineros.get(i).setRestaurant(restaurant);
        }
        int totalComensales = (int) (numeroComensales*.20);
        for(int i = 0; i < totalComensales; i++){
            cliente = new Cliente(recepcionista, restaurant,"Cliente "+i);
            cliente.addObserver(this);
            new Thread(cliente,"CLIENTE "+i).start();
        }
    }
    
    
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Cliente ){
            String [] datos = String.valueOf(arg).split(":");
            //datos[0]= entrando || saliendo || comiendo
            //datos[1]= 1 || 0 (tipo 1= vip, 0 = normal)
            //datos[2]= numero de mesa asignada
            int posicion = Integer.parseInt(datos[2]);
            if(datos[0].equals("entrando")){
                if(datos[1].equals("0"))
                    listAsientos.get(posicion).setFill(Color.GREEN);
                else 
                    listVIPAsientos.get(posicion).setFill(Color.PURPLE);
            }else if (datos[0].equals("salio")) {
                listAsientos.get(posicion).setFill(Color.BLACK);
            } else {
                listAsientos.get(posicion).setFill(Color.BLUE);
            }
        
        }else if (o instanceof Cocinero ){
            if(String.valueOf(arg).equals("cocinando")){
                estufa2.setFill(Color.RED);
            }else if (String.valueOf(arg).equals("esperando")){
                estufa2.setFill(Color.GREEN);
            }
        }
    }
    
    public synchronized int asignarLugarComensal(){
        int posicion ;
        for(posicion = 0; posicion < asientosLibres.length; posicion++){
            if(asientosLibres[posicion].equals("libre"))
                break;
        }
        return posicion;
    }
    public synchronized int lugarPlatoBarra(){
        int posicion ;
        for(posicion = 0; posicion < ordenesListas.length; posicion++){
            if(ordenesListas[posicion].equals("libre"))
                break;
        }
        return posicion;
    }
}