package bookmark;

/**
 * BookmarkList.java
 * Authors: Ethan Garnier, Albert Stanica & Abdoalah Aboelneil
 */
public class BookmarkList
{
    private Bookmark[] list;
    private int nBookmarks;

    public BookmarkList()
    {
        list = new Bookmark[1];
        nBookmarks = 0;
    }

    //Adds a bookmark to the bookmark list
    //If the bookmark list is full, increase the size
    public void addBookmark(Bookmark bookmark)
    {
        if (nBookmarks == list.length)
        {
            increaseSize();
        }

        list[nBookmarks] = bookmark;
        nBookmarks++;
    }

    //Double bookmark list size to allow for more bookmarks
    private void increaseSize()
    {
        Bookmark[] temp = new Bookmark[list.length * 2];
        for (int i = 0; i < list.length; i++)
        {
            temp[i] = list[i];
        }

        list = temp;
    }

    public Bookmark getBookmark(int index){
        if(index < nBookmarks)
        {
            return list[index];
        }

        return null;
    }

    public String[] getIDArray()
    {
        String[] ids = new String[nBookmarks];
        for (int i = 0; i < nBookmarks; i++)
        {
            ids[i] = list[i].getID();
        }

        return ids;
    }

    public Bookmark searchID(String ID) {
        for (int z = 0; z < list.length; z++) {
            if (list[z].getID().equals(ID)) {
                return getBookmark(z);
            }
        }
        return null;
    }

    public void printList()
    {
        for (int i = 0; i < list.length; i++)
        {
            //If the object is not null (bookmark exists), print the bookmark
            if (list[i] != null)
            {
                System.out.println("\t" + (i+1) + ") " + list[i]);
            }
        }
        System.out.println();
    }
}