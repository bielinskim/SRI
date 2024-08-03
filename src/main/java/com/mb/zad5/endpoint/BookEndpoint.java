package com.mb.zad5.endpoint;

import com.mb.zad5.config.SoapWSConfig;
import com.mb.zad5.model.Book;
import com.mb.zad5.repo.BookRepository;
import localhost.books.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Endpoint
@RequiredArgsConstructor
public class BookEndpoint {
    private final BookRepository bookRepository;

    @PayloadRoot(namespace = SoapWSConfig.BOOK_NAMESPACE, localPart="getBooksRequest")
    @ResponsePayload
    public GetBooksResponse getBooks(@RequestPayload GetBooksRequest req) {
        List<Book> allBooks = bookRepository.findAll();
        List<BookDto> bookDtos = allBooks.stream().map(this::convertToDto).toList();
        GetBooksResponse res = new GetBooksResponse();
        res.getBooks().addAll(bookDtos);

        return res;
    }

    @PayloadRoot(namespace = SoapWSConfig.BOOK_NAMESPACE, localPart="getBookByIdRequest")
    @ResponsePayload
    public GetBookByIdResponse getBookById(@RequestPayload GetBookByIdRequest req) {
        long id = req.getBookId().longValue();
        Optional<Book> book = bookRepository.findById(id);
        GetBookByIdResponse res = new GetBookByIdResponse();
        res.setBooks(convertToDto((book.orElse(null))));

        return res;
    }

    @PayloadRoot(namespace = SoapWSConfig.BOOK_NAMESPACE, localPart="addBookRequest")
    @ResponsePayload
    public AddBookResponse addBook(@RequestPayload AddBookRequest req) {
        Book book = convertToEntity(req.getBooks());
        Long id = bookRepository.save(book).getId();
        AddBookResponse res = new AddBookResponse();
        res.setBookId(new BigDecimal(id));

        return res;
    }

    private BookDto convertToDto(Book e) {
        if(e == null) return null;

        try {
            BookDto dto = new BookDto();
            dto.setId(BigDecimal.valueOf(e.getId()));
            dto.setTitle(e.getTitle());
            dto.setAuthor(e.getAuthor());
            dto.setTotalPages(e.getTotalPages());
            dto.setPublishedDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(e.getPublishedDate().toString()));

            return dto;
        } catch(DatatypeConfigurationException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Book convertToEntity(BookDto dto) {
        return Book.builder()
                .id(dto.getId() != null ? dto.getId().longValue() : null)
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .totalPages(dto.getTotalPages())
                .build();
    }
}
