package io.jnthn.productservice;

import com.mongodb.assertions.Assertions;
import io.jnthn.productservice.dto.ProductRequest;
import io.jnthn.productservice.model.Product;
import io.jnthn.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	private final String productServiceUrl = "/api/product";


	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}
	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post(productServiceUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());


		Assertions.assertTrue(productRepository.findAll().size()==1);
	}

	@Test
	void shouldGetProducts() throws Exception {

		productRepository.save(Product.builder()
				.name("Sample Product")
				.description("Sample Product")
				.price(BigDecimal.valueOf(1200)).build());


		mockMvc.perform(MockMvcRequestBuilders.get(productServiceUrl)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Sample Product")));

	}



	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Iphone 13")
				.description("Iphone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

}
