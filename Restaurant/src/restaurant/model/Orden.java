package restaurant.model;

public class Orden {
    private int numOrden;
    private Cliente cliente;
    private Mesero mesero;
    private String status;
    
    public Orden(){}
    public Orden(int numOrden, Cliente cliente, Mesero mesero) {
        this.numOrden = numOrden;
        this.cliente = cliente;
        this.mesero = mesero;
    }

    public int getNumOrden() {
        return numOrden;
    }

    public void setNumOrden(int numOrden) {
        this.numOrden = numOrden;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Mesero getMesero() {
        return mesero;
    }

    public void setMesero(Mesero mesero) {
        this.mesero = mesero;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
