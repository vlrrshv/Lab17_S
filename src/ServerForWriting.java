import Connection.MessageForClient;
import collection.MyCollection;

import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Exchanger;

public class ServerForWriting implements Runnable {
    private SocketChannel clientDialog;
    private DataBaseWorker worker;
    private MyCollection collection;
    private Exchanger<MessageForClient> exchanger;


    public ServerForWriting(SocketChannel clientDialog, DataBaseWorker worker, MyCollection collection, Exchanger<MessageForClient> exchanger) {
        this.clientDialog = clientDialog;
        this.worker = worker;
        this.collection = collection;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        while(true){
            try {
                ByteBuffer buffer = ByteBuffer.allocate(65536);
                MessageForClient message = exchanger.exchange(null);
                write(message,buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private  byte[] serialize(MessageForClient message) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(message);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            System.out.println("Serialize problem");
        }
        return null;
    }
    private void write(MessageForClient message, ByteBuffer buffer) {
        buffer.put(serialize(message));
        buffer.flip();
        try {
            clientDialog.write(buffer);
            buffer.clear();
        } catch (IOException e) {
            try {
                clientDialog.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
