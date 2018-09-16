package cspersistence;

import com.google.gson.reflect.TypeToken;
import cspersistence.document.BikeProduct;
import cspersistence.service.BikeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import util.FileUtils;
import util.JsonUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CsPersistenceApplication implements ApplicationRunner{
	@Autowired
	private BikeProductService bikeProductService;
	public static void main(String[] args) {
		SpringApplication.run(CsPersistenceApplication.class, args);
	}
	@Autowired
	private ElasticsearchOperations elasticsearchOperations;
	private void init(){
		elasticsearchOperations.deleteIndex(BikeProduct.class);
		elasticsearchOperations.createIndex(BikeProduct.class);
		elasticsearchOperations.putMapping(BikeProduct.class);
		elasticsearchOperations.refresh(BikeProduct.class);
	}
	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.init();
		Type bikeProdutClazz = new TypeToken<ArrayList<BikeProduct>>() {
		}.getType();
		File bikeProductFile = new File("/home/bibek/bs-workspace/bike/preprocess/transform/bike_products.json");
		String bikeProductStr = FileUtils.readFileToString(bikeProductFile, StandardCharsets.UTF_8);
		List<BikeProduct> bikeProducts = JsonUtils.fromJsonToList(bikeProductStr,bikeProdutClazz);
		bikeProducts.forEach(b->this.bikeProductService.saveBike(b));
	}

}
