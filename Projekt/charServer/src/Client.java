import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client
{
    Socket socket;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;

    public Client(Socket socket)
    {
        try
        {
            this.socket = socket;
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
