package edu.pja.sri.lab07.server;

import edu.pja.sri.lab07.BookService;
import edu.pja.sri.lab07.LibraryService;
import edu.pja.sri.lab07.server.handlers.BookHandler;
import edu.pja.sri.lab07.server.handlers.LibraryHandler;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;
import org.apache.thrift.transport.layered.TFramedTransport;

public class BookServer {

    public static void main(String[] args) {

        try {
            BookService.Processor bookProcessor = new BookService.Processor(new BookHandler());
            LibraryService.Processor libraryProcessor = new LibraryService.Processor(new LibraryHandler());

            Runnable simple = new Runnable() {
                public void run() {
                    registerServices(bookProcessor, libraryProcessor);
                }
            };

            new Thread(simple).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void registerServices(BookService.Processor bookProcessor, LibraryService.Processor libraryProcessor) {
        try {
            TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
            multiplexedProcessor.registerProcessor("Book", bookProcessor);
            multiplexedProcessor.registerProcessor("Library", libraryProcessor);
            TServerTransport serverTransport = new TServerSocket(9090);

            TTransportFactory factory = new TFramedTransport.Factory();

            TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
            args.processor(multiplexedProcessor);
            args.transportFactory(factory);
            TThreadPoolServer server = new TThreadPoolServer(args);

            System.out.println("Starting the thread pool server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
