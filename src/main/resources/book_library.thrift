namespace java edu.pja.sri.lab07

struct Book {
    1: required i32 id,
    2: required string title,
    3: required string author,
    4: required i32 totalPages
}

service BookService {
    void createBook(1: Book book),
    Book readBook(1: i32 id),
    void updateBook(1: Book book),
    void deleteBook(1: i32 id)
}

service LibraryService {
    void addBook(1: i32 id)
    Book borrowBook(1: string title),
    void returnBook(1: Book book),
}