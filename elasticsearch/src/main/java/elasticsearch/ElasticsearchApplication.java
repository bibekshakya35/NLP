package elasticsearch;

import elasticsearch.model.Book;
import elasticsearch.service.BookService;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ElasticsearchApplication implements CommandLineRunner {
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        displayElasticSearchInfo();
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book("1001", "Elasticsearch Basics", "Bibek Shakya", "23-FEB-2017"));
        bookList.add(new Book("1002", "Apache Lucene Basics", "Bibek Shakya", "13-MAR-2017"));
        bookList.add(new Book("1003", "Apache Solr Basics", "Bibek Shakya", "21-MAR-2017"));
        bookList.add(new Book("1007", "Spring Data + ElasticSearch", "Bibek Shakya", "01-APR-2017"));
        bookList.add(new Book("1008", "Spring Boot + MongoDB", "Roshan Shakya", "25-FEB-2017"));
        bookList.forEach(b -> bookService.save(b));
        //fuzzy search
        Page<Book> books = bookService.findByAuthor("Bibek", PageRequest.of(0, 10));
        books.forEach(b->System.out.println(b));
    }

    //useful for debug and print elastic search details
    private void displayElasticSearchInfo() {
        System.out.println("----Elastic Search------");
        Client client = elasticsearchOperations.getClient();
        Map<String, String> asMap = client.settings().getAsMap();
        asMap.forEach((k, v) -> {
            System.out.println(k + " :  " + v);
        });
    }
}
