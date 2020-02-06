package bookmark;

/**
 * Bookmark.java
 * Authors: Ethan Garnier, Albert Stanica & Abdoalah Aboelneil
 */
public class Bookmark
{
    //Creating ID and WebAddress as Strings
    private String ID;
    private String WebAddress;

    //Creating a Constructor that receives ID and WebAddress in it's parameter
    public Bookmark( String ID, String WebAddress)
    {
        this.ID = ID;
        this.WebAddress = WebAddress;

    }

    public String getWebAddress(){
        return WebAddress;
    }

    public String getID()
    {
        return ID;
    }

    //Creating a toString method that prints the bookmark list
    public String toString()
    {
        return ID + ": " + WebAddress;
    }
}
