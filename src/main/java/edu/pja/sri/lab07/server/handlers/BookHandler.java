package edu.pja.sri.lab07.server.handlers;

import edu.pja.sri.lab07.Book;
import edu.pja.sri.lab07.BookService;
import edu.pja.sri.lab07.server.BooksData;
import org.apache.thrift.TException;

public class BookHandler implements BookService.Iface {

    @Override
    public void createBook(Book book) throws TException {
        String bookId = String.valueOf(book.getId());

        BooksData.books.put(bookId, book);
        System.out.println("Created book: " + book.getTitle());
    }

    @Override
    public Book readBook(int id) throws TException {
        String bookId = String.valueOf(id);

        Book book = BooksData.books.get(bookId);

        if (book != null) {
            System.out.println("Read book: " + book.getTitle());
        } else {
            System.out.println("Book not found for id: " + id);
        }
        return book;
    }

    @Override
    public void updateBook(Book book) throws TException {
        String bookId = String.valueOf(book.id);

        if (BooksData.books.containsKey(bookId)) {
            BooksData.books.put(bookId, book);

            System.out.println("Updated book: " + book.getTitle());
        } else {
            System.out.println("Book not found for id: " + bookId);
        }
    }

    @Override
    public void deleteBook(int id) throws TException {
        String bookId = String.valueOf(id);

        Book book = BooksData.books.remove(bookId);

        if (book != null) {
            System.out.println("Deleted book: " + book.getTitle());
        } else {
            System.out.println("Book not found for id: " + bookId);
        }
    }

}
