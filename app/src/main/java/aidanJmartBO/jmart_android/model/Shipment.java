package aidanJmartBO.jmart_android.model;

public class Shipment {
    public String address;
    public int cost;
    public byte plan;
    public String receipt;

    //constructor
    public Shipment(String address, int cost, byte plan, String receipt){
        this.address = address;
        this.cost = cost;
        this.plan = plan;
        this.receipt = receipt;
    }
}
