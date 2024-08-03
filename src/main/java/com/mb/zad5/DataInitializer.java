package com.mb.zad5;

import com.mb.zad5.model.Book;
import com.mb.zad5.repo.BookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);

    private final BookRepository bookRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) { initData(); }

    private void initData() {
        Book b1 = Book
                .builder()
                .title("Testowy tytul 1")
                .author("Testowy autor 1")
                .totalPages(311)
                .publishedDate(LocalDate.of(1990, 01, 01))
                .build();

        Book b2 = Book
                .builder()
                .title("Testowy tytul 2")
                .author("Testowy autor 2")
                .totalPages(125)
                .publishedDate(LocalDate.of(1995, 01, 01))
                .build();

        Book b3 = Book
                .builder()
                .title("Testowy tytul 3")
                .author("Testowy autor 3")
                .totalPages(233)
                .publishedDate(LocalDate.of(2000, 01, 01))
                .build();

        bookRepository.saveAll(Arrays.asList(b1, b2, b3));
        LOG.info("Data initialized");
    }

}
