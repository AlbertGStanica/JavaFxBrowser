package bookmark;

import java.util.LinkedHashSet;

/**
 * BookmarkList.java
 * Authors: Ethan Garnier, Albert Stanica & Abdoalah Aboelneil
 */
public class BookmarkList
{
    private Bookmark[] list;
    private int nBookmarks;
    private boolean isEqual = false;
    private EqualIDsException eql;

    public BookmarkList()
    {
        list = new Bookmark[1];
        nBookmarks = 0;
    }

    //Adds a bookmark to the bookmark list
    //If the bookmark list is full, increase the size
    public void addBookmark(Bookmark bookmark) throws EqualIDsException
    // addBookmark(Bookmark) -> Void
    // addBookmark method adds a a bookmark to the list
    // It makes sure that there are no duplicates
    // by comparing the Coin IDs and increases the size
    // of the list by one before adding the new** bookmark
    {
        if(list.length > 1){
            for(int z = 0; z < list.length; z++){
                if(list[z] != null && list[z].getID().equals(bookmark.getID())){
                    isEqual = true;
                    // Throws Exception
                    eql = new EqualIDsException("You can not have two bookmarks with the same name!");
                    //throw eql;
                }
            }
        }
        if(!isEqual){
            if (nBookmarks == list.length)
            {
                increaseSize();
            }

            list[nBookmarks] = bookmark;
            nBookmarks++;
        }
        isEqual = false;

    }

    //Double bookmark list size to allow for more bookmarks
    private void increaseSize()
    // increaseSize() -> Void
    // Increases the size of the Bookmark list tl a larger one

    {
        Bookmark[] temp = new Bookmark[list.length + 1];
        for (int i = 0; i < list.length; i++)
        {
            temp[i] = list[i];
        }

        list = temp;
    }

    public Bookmark getBookmark(int index){
        // getBookmark(int index ) - > Index of Bookmark
        // Returns the Index of a bookmark
        if(index < nBookmarks)
        {
            return list[index];
        }

        return null;
    }

    public String[] getIDArray()
    {
        // Returns the array of all ids
        // returns as a String[] list
        String[] ids = new String[nBookmarks];
        for (int i = 0; i < nBookmarks; i++)
        {
            ids[i] = list[i].getID();
        }

        return ids;
    }

    public void createTestList()
    {
        // Creates the default test list if there is no .dat
        // file available
        try
        {
            Bookmark bookmark;
            bookmark = new Bookmark("UNB", "https://www.unb.ca/");
            addBookmark(bookmark);
            bookmark = new Bookmark("Google","https://www.google.ca/");
            addBookmark(bookmark);
            bookmark = new Bookmark("Bing","https://www.bing.ca/");
            addBookmark(bookmark);
        }catch (EqualIDsException e)
        {
            e.printStackTrace();

        }
    }

    public int size()
    {
        // size() -> return (int) amount of bookmarks
        return list.length;
    }

    //Method has been commented out as it is no longer needed.

    /*public Bookmark searchID(String ID) {
        // searchID(String id) --> Bookmark book
        // returns the bookmark at that ID
        for (int z = 0; z < list.length; z++) {
            if (list[z].getID().equals(ID)) {
                return getBookmark(z);
            }
        }
        return null;
    }*/


    // This was uses in the terminal version
    // It is no longer needed.
    /*public void printList()
    {
        // Print out the bookmark list
        for (int i = 0; i < list.length; i++)
        {
            //If the object is not null (bookmark exists), print the bookmark
            if (list[i] != null)
            {
                System.out.println("\t" + (i+1) + ") " + list[i]);
            }
        }
        System.out.println();
    }*/


}