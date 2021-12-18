package aidanJmartBO.jmart_android.model;

import java.util.Date;
/**
 * Class Invoice - blueprint for Invoice object
 *
 * @author Muh. Aidan Daffa J
 * @version (version number or date here)
 */
public abstract class Invoice extends Serializable{
    //field
    public int buyerId;
    public int complaintId;
    public final Date date;
    public int productId;
    public Rating rating;
    public enum Status{
        WAITING_CONFIRMATION, CANCELLED, ON_PROGRESS, ON_DELIVERY,
        COMPLAINT, FINISHED, FAILED, DELIVERED
    }
    //enum type for rating
    public enum Rating{
        NONE, BAD, NEUTRAL, GOOD
    }
    class Record{
        public Date date;
        public String message;
        public Status status;
    }
    //constructor
    protected Invoice(int buyerId, int productId){
        this.buyerId = buyerId;
        this.productId = productId;
        this.date = new Date();
        this.rating = Rating.NONE;
        this.complaintId = -1;
    }

}