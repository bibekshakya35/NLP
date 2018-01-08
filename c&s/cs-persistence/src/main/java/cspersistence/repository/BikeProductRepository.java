package cspersistence.repository;

import cspersistence.document.BikeProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author bibek on 12/30/17
 * @project cs-persistence
 */
public interface BikeProductRepository extends ElasticsearchRepository<BikeProduct,String> {
}
