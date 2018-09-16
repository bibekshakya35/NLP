package elasticsearch.service;

import elasticsearch.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * @author bibek on 12/17/17
 * @project elasticsearch
 */
public interface BookService {
    Book save(Book book);

    void delete(Book book);

    Optional<Book> findOne(String id);

    Iterable<Book> findAll();

    Page<Book> findByAuthor(String author, PageRequest pageRequest);

    List<Book> findByTitle(String title);
}
