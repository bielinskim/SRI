package com.mb.zad5.repo;

import com.mb.zad5.model.Book;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    @Override
    List<Book> findAll();
}
