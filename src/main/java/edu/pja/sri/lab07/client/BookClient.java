package edu.pja.sri.lab07.client;

import edu.pja.sri.lab07.Book;
import edu.pja.sri.lab07.BookService;
import edu.pja.sri.lab07.LibraryService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.layered.TFramedTransport;


public class BookClient {
    public static void main(String[] args) {
        try {

            TTransport transport = new TFramedTransport( new TSocket("localhost", 9090));
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);

            TMultiplexedProtocol mpBook = new TMultiplexedProtocol(protocol, "Book");
            BookService.Client bookClient = new BookService.Client(mpBook);

            TMultiplexedProtocol mpLibrary = new TMultiplexedProtocol(protocol, "Library");
            LibraryService.Client libraryClient = new LibraryService.Client(mpLibrary);


            perform(bookClient, libraryClient);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    private static void perform(BookService.Client bookClient, LibraryService.Client libraryClient) throws TException {


        Book book = new Book(1, "Testowy tytuł", "Testowy autor", 100);

        bookClient.createBook(book);

        Book updatedBook = new Book(1, "Testowy tytuł updated", "Testowy autor", 100);

        bookClient.updateBook(updatedBook);

        Book readBook = bookClient.readBook(1);

        bookClient.deleteBook(readBook.id);

        Book book2 = new Book(1, "Testowy tytuł", "Testowy autor", 100);

        bookClient.createBook(book);

        libraryClient.addBook(1);
        Book borrowedBook = libraryClient.borrowBook("Testowy tytuł");
        libraryClient.returnBook(borrowedBook);
    }
}
