package edu.pja.sri.lab07.server.handlers;

import edu.pja.sri.lab07.Book;
import edu.pja.sri.lab07.LibraryService;
import edu.pja.sri.lab07.server.BooksData;
import org.apache.thrift.TException;

import java.util.Map;

public class LibraryHandler implements LibraryService.Iface {

    @Override
    public void addBook(int id) throws TException {
        String bookId = String.valueOf(id);
        Book book = BooksData.books.get(bookId);

        BooksData.library.put(bookId, book);
        System.out.println("Added book: " + book.getTitle());
    }

    @Override
    public Book borrowBook(String title) throws TException {

        for (Map.Entry<String, Book> entry : BooksData.library.entrySet()) {
            Book book = entry.getValue();

            if (book.title.equals(title)) {
                System.out.println("Borrow book: " + title);

                return book;
            } else {
                System.out.println("Book not found for title: " + title);
            }
        }
        return null;
    }

    @Override
    public void returnBook(Book book) throws TException {
        String bookId = String.valueOf(book.id);

        BooksData.library.put(bookId, book);
        System.out.println("Returned book: " + book.getTitle());
    }
}
