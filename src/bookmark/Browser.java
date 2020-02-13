/*
package bookmark;

*/
/**
 * Browser.java
 * Authors: Ethan Garnier, Albert Stanica & Abdoalah Aboelneil
 *//*

import java.util.Scanner;

public class Browser {

    public static void main(String[] args) {

        boolean running = true;
        int input;
        String ID, url, address, command;

        Scanner sc = new Scanner(System.in);

        //Creates runtime instance to execute commands
        Runtime runtime = Runtime.getRuntime();

        BookmarkList bookmark_list = new BookmarkList();

        while (running)
        {
            //printsTheMenu
            printMenu();

            input = sc.nextInt();

            if (input == 0)
            {
                running = false;
            }
            else {
                if(input == 1)
                {
                    // Add Bookmark
                    System.out.print("ID for the bookmark: ");
                    ID = sc.next();

                    System.out.print("Web address or file name: ");
                    url = sc.next();

                    Bookmark new_bookmark = new Bookmark(ID, url);
                    bookmark_list.addBookmark(new_bookmark);
                }

                if(input == 2)
                {
                    //Printout Bookmark
                    System.out.println("Bookmark list: ");
                    bookmark_list.printList();
                }

                if(input == 3){
                    // Launches the Website
                    System.out.print("Which bookmark do you want to display? ");

                    int bookMarkIndex = sc.nextInt();

                    if(bookMarkIndex == 0 || bookmark_list.getBookmark(bookMarkIndex - 1) == null)
                    {
                        System.out.println("That bookmark does not exist");
                    }
                    else
                    {
                        address = bookmark_list.getBookmark(bookMarkIndex -1).getWebAddress();
                        ID = bookmark_list.getBookmark(bookMarkIndex - 1).getID();


                        System.out.println("Displaying " + ID + "...");

                        try
                        {
                            if (address.endsWith(".pdf"))
                            {
                                command = "C:\\Program Files (x86)\\Adobe\\Acrobat Reader DC\\Reader\\AcroRd32 " + address;
                                runtime.exec(command);
                            }
                            else
                            {
                                command = "C:\\Program Files (x86)\\Internet Explorer\\iexplore " + address;
                                runtime.exec(command);
                            }
                        }
                        catch (Exception e)
                        {

                        }
                    }


                }

                if (input == 4)
                {
                    String[] ids = bookmark_list.getIDArray();

                    for (String id: ids)
                    {
                        System.out.println("\t" + id);
                    }

                    System.out.print("Which bookmark (keyword) do you want to display? ");

                    String keyword = sc.next();

                    Bookmark bm = bookmark_list.searchID(keyword);

                    if (bm == null)
                    {
                        System.out.println("That bookmark doesn't exist");
                    }

                    address = bm.getWebAddress();

                    try
                    {
                        if (address.endsWith(".pdf"))
                        {
                            command = "C:\\Program Files (x86)\\Adobe\\Acrobat Reader DC\\Reader\\AcroRd32 " + address;
                            runtime.exec(command);
                        }
                        else
                        {
                            command = "C:\\Program Files (x86)\\Internet Explorer\\iexplore " + address;
                            runtime.exec(command);
                        }
                    }
                    catch (Exception e)
                    {

                    }
                }

            }

        }

    }

    private static void printMenu(){
        System.out.println("Menu");
        System.out.println("-------");
        System.out.println("\t1 - Add Bookmark");
        System.out.println("\t2 - Print list of Bookmarks");
        System.out.println("\t3 - Display a Bookmark (by index)");
        System.out.println("\t4 - Display a Bookmark (by keyword)");
        System.out.print("Your choice? (0 to quit) ");
    }
}*/
