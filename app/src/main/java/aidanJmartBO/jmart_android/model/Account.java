package aidanJmartBO.jmart_android.model;

/**
 * Class Account - blueprint for account object
 *
 * @author Muh. Aidan Daffa J
 * @version (version number or date here)
 */

public class Account extends Serializable{
    //field
    public double balance;
    public String email;
    public String name;
    public String password;
    public Store store;

    //constructor
    public Account(String name, String email, String password, double balance){
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }
}
