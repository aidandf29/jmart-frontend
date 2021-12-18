package aidanJmartBO.jmart_android.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class Payment - blueprint for Payment object
 *
 * @author Muh. Aidan Daffa J
 * @version (version number or date here)
 */

public class Payment extends Invoice{
    //field
    public ArrayList<Record> history = new ArrayList<>();
    public int productCount;
    public Shipment shipment;
    public static class Record{
        public final Date date;
        public String message;
        public Status status;
        public Record(Status status, String message){
            date = new Date();
            status = status;
            message = message;
        }
    }
    //constructor
    public Payment(int buyerId, int productId, int productCount, Shipment shipment){
        super(buyerId, productId);
        this.productCount = productCount;
        this.shipment = shipment;
    }
}