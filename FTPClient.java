
package ftpclient;

import java.net.*;
import java.io.*;
import java.util.*;


public class FTPClient
{
    public static void main(String args[]) throws Exception
    {
        Socket soc=new Socket("localhost",3333);
        transferfileClient t=new transferfileClient(soc);
        t.displayMenu();
        
    }
}
class transferfileClient
{
    Socket ClientSoc;

    DataInputStream din;
    DataOutputStream dout;
    BufferedReader br;
    transferfileClient(Socket soc)
    {
        try
        {
            ClientSoc=soc;
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            br=new BufferedReader(new InputStreamReader(System.in));
        }
        catch(Exception ex)
        {
        }        
    }
   
    void ReceiveFile() throws Exception
    {
        String fileName;
        
        File folder = new File("C:\\Users\\Miloni\\Documents\\NetBeansProjects\\FTPServer");
                File[] listOfFiles = folder.listFiles();

                for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());}}
                System.out.print("Enter File Name :");
        fileName=br.readLine();
        dout.writeUTF(fileName);
        String msgFromServer=din.readUTF();
        
        if(msgFromServer.compareTo("File Not Found")==0)
        {
            System.out.println("File not found on Server ...");
            return;
        }
        else if(msgFromServer.compareTo("READY")==0)
        {
            System.out.println("Receiving File ...");
            File f=new File(fileName);
            if(f.exists())
            {
                String Option;
                System.out.println("File Already Exists. Want to OverWrite (Y/N) ?");
                Option=br.readLine();            
                if(Option=="N")    
                {
                    dout.flush();
                    return;    
                }                
            }
            FileOutputStream fout=new FileOutputStream(f);
            int ch;
            String temp;
            do
            {
                temp=din.readUTF();
                ch=Integer.parseInt(temp);
                if(ch!=-1)
                {
                    fout.write(ch);                    
                }
            }while(ch!=-1);
            fout.close();
            System.out.println(din.readUTF());
                
        }
        
        
    }

    public void displayMenu() throws Exception
    {
        while(true)
        {    
            System.out.println("---MENU---");
           
            System.out.println("1. Download File");
            System.out.println("2. Exit");
            System.out.print("\nEnter Choice :");
            int choice;
            choice=Integer.parseInt(br.readLine());
           
            if(choice==1)
            {
                dout.writeUTF("GET");
                ReceiveFile();
            }
            else
            {
                dout.writeUTF("DISCONNECT");
                System.exit(1);
            }
        }
    }
}
