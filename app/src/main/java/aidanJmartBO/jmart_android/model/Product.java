package aidanJmartBO.jmart_android.model;

/**
 * Class Product - blueprint for Product object
 *
 * @author Muh. Aidan Daffa J
 * @version (version number or date here)
 */
public class Product extends Serializable{

    //field
    public int accountId;
    public ProductCategory category;
    public boolean conditionUsed;
    public double discount;
    public String name;
    public double price;
    public byte shipmentPlans;
    public int weight;

    @Override
    public String toString(){
        return name;
    }
}
