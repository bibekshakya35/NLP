package elasticsearch;

import elasticsearch.model.Book;
import elasticsearch.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class)
public class ElasticsearchApplicationTests {
	@Autowired
	private BookService bookService;
	@Autowired
	private ElasticsearchOperations elasticsearchOperations;
	@Before
    public void before(){
	    elasticsearchOperations.deleteIndex(Book.class);
	    elasticsearchOperations.createIndex(Book.class);
	    elasticsearchOperations.putMapping(Book.class);
	    elasticsearchOperations.refresh(Book.class);
    }
    @Test
    public void shouldSaveBook(){
        Book book = new Book("1001","Elastic search First Journey","Bibek Shakya","18-DEC-2017");
        Book afterSave = bookService.save(book);
        assertNotNull(afterSave);
        assertNotNull(afterSave.getId());
        assertEquals(afterSave.getId(),book.getId());
    }
    @Test
	public void testFindByTitle(){
		Book book = new Book("1001", "Elasticsearch Basics", "Bibek Shakya", "23-FEB-2017");
		bookService.save(book);
		List<Book> byTitle = bookService.findByTitle(book.getTitle());
		assertThat(byTitle.size(),is(1));
	}
	@Test
	public void testFindByAuthor(){
		List<Book> bookList = new ArrayList<>();
		bookList.add(new Book("1001", "Elasticsearch Basics", "Bibek Shakya", "23-FEB-2017"));
		bookList.add(new Book("1002", "Apache Lucene Basics", "Bibek Shakya", "13-MAR-2017"));
		bookList.add(new Book("1003", "Apache Solr Basics", "Bibek Shakya", "21-MAR-2017"));
		bookList.add(new Book("1007", "Spring Data + ElasticSearch", "Bibek Shakya", "01-APR-2017"));
		bookList.add(new Book("1008", "Spring Boot + MongoDB", "Roshan Shakya", "25-FEB-2017"));
		bookList.forEach(b->bookService.save(b));
		Page<Book> byAuthor = bookService.findByAuthor("Bibek",PageRequest.of(0,10));
		assertThat(byAuthor.getTotalElements(),is(4L));

		Page<Book> byAuthor2 = bookService.findByAuthor("Roshan",PageRequest.of(0,10));
		assertThat(byAuthor2.getTotalElements(),is(1L));

	}
	@Test
	public void shouldDeleteBook(){
		Book book = new Book("1001", "Elasticsearch Basics", "Bibek Shakya", "23-FEB-2017");
		bookService.save(book);
		bookService.delete(book);
		Optional<Book> bookNeeded = bookService.findOne("1001");
		assertFalse(bookNeeded.isPresent());
	}
	@Test
	public void contextLoads() {
	}

}
