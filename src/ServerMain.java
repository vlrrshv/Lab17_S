import Connection.DataToOutput;
import Connection.MessageForClient;
import collection.MyCollection;
import commands.*;
import data.Vehicle;

import java.io.*;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private static ServerSocketChannel serverSocketChannel;
    //private static File file;

    public static void main(String[] args) throws IOException {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        ExecutorService fixedPool = Executors.newFixedThreadPool(2);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
        }

        String jdbcURL = "jdbc:postgresql://localhost:1111/studs";
        DataBaseWorker worker = new DataBaseWorker(jdbcURL, "s313321", "imp678");
        worker.connectToDataBase();
        MyCollection collection = new MyCollection();
        worker.fillCollection(collection);

            try {
                serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.bind(new InetSocketAddress(3346));
                //ByteBuffer buffer = ByteBuffer.allocate(65536);

                while(true){
                    SocketChannel client = accept(collection);
                    Exchanger<MessageForClient> exchanger = new Exchanger<>();
                    cachedPool.submit(new ServerForReading(client, worker, collection,exchanger));
                    fixedPool.submit(new ServerForWriting(client, worker, collection,exchanger));
                }



                //cachedPool.shutdown();
                //fixedPool.shutdown();
            } catch (IOException e) {
                System.out.println("There is no such open server port");
            }



    }


    private static SocketChannel accept(MyCollection collection) {
        SocketChannel client = null;
        try {
            client = serverSocketChannel.accept();
            System.out.println("Client connected");
            //ServerInput input = new ServerInput(collection, new File("lol"));
            //input.start();
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        return client;
    }

}

