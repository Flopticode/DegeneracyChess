package chess.network.api;


import java.net.*;
import java.io.*;

public class Connection
{
    private Socket socket;
    private BufferedReader fromServer;
    private PrintWriter toServer;

    public Connection(String pServerIP, int pServerPort)
    {       
        try
        {
            socket = new Socket(pServerIP, pServerPort);
            toServer = new PrintWriter(socket.getOutputStream(), true);
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (Exception e)
        {
            //Erstelle eine nicht-verbundene Instanz von Socket, wenn die avisierte
            //Verbindung nicht hergestellt werden kann
            socket = null;
            toServer = null;
            fromServer = null;
        }
    }

    public String receive()
    {
        if(fromServer != null)
            try
            {
                return fromServer.readLine();
            }
            catch (IOException e)
            {
            }
        return(null);
    }

    public void send(String pMessage)
    {
        if(toServer != null)
        {
            toServer.println(pMessage);
         }
    }

    public void close()
    {

        if(socket != null && !socket.isClosed())
            try
            {
                socket.close();
            }
            catch (IOException e)
            {
                /*
                 * Falls eine Verbindung geschlossen werden soll, deren Endpunkt nicht
                 * mehr existiert bzw. seinerseits bereits geschlossen worden ist oder
                 * die nicht korrekt instanziiert werden konnte (socket == null), geschieht
                 * nichts.
                 */
            }
    }
}
