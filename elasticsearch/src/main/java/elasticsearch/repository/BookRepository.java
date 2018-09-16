package elasticsearch.repository;

import elasticsearch.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author bibek on 12/17/17
 * @project elasticsearch
 */
public interface BookRepository extends ElasticsearchRepository<Book,String> {
    Page<Book> findByAuthor(String author, Pageable pageable);

    List<Book> findByTitle(String title);
}
