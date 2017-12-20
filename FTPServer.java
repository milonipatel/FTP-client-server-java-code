
package ftpserver;

import java.net.*;
import java.io.*;
import java.util.*;

public class FTPServer
{
    public static void main(String args[]) throws Exception
    {
        ServerSocket soc=new ServerSocket(3333);
        System.out.println("FTP Server Started on Port Number 3333");
        while(true)
        {
            System.out.println("Waiting for Connection ...");
            transferfile t=new transferfile(soc.accept());
            
        }
    }
}

class transferfile extends Thread
{
    Socket ClientSoc;

    DataInputStream din;
    DataOutputStream dout;
    
    transferfile(Socket soc)
    {
        try
        {
            ClientSoc=soc;                        
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            System.out.println("FTP Client Connected ...");
            start();
            
        }
        catch(Exception ex)
        {
        }        
    }
    void SendFile() throws Exception
    {        
        String filename=din.readUTF();
        File f=new File(filename);
        if(!f.exists())
        {
            dout.writeUTF("File Not Found");
            return;
        }
        else
        {
            dout.writeUTF("READY");
            FileInputStream fin=new FileInputStream(f);
            int ch;
            do
            {
                ch=fin.read();
                dout.writeUTF(String.valueOf(ch));
            }
            while(ch!=-1);    
            fin.close();    
            dout.writeUTF("File downloaded Successfully");                            
        }
    }
    
    

    public void run()
    {
        while(true)
        {
            try
            {
            System.out.println("Waiting for Command ...");
            String Command=din.readUTF();
            if(Command.compareTo("GET")==0)
            {
                System.out.println("\tGET Command Received ...");
                SendFile();
                continue;
            }
      
            else if(Command.compareTo("DISCONNECT")==0)
            {
                System.out.println("\tDisconnect Command Received ...");
                System.exit(1);
            }
            }
            catch(Exception ex)
            {
            }
        }
    }
}