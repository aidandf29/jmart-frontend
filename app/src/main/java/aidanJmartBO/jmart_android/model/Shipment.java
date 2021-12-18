package aidanJmartBO.jmart_android.model;

/**
 * Class Shipment - blueprint for Shipment object
 *
 * @author Muh. Aidan Daffa J
 * @version (version number or date here)
 */

public class Shipment {
    //field
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
