package com.jiva.cloud.juniatime.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.jiva.cloud.juniatime.dto.LabelValue;
import com.jiva.cloud.juniatime.event.CreatedResourceEvent;
import com.jiva.cloud.juniatime.exception.ProductInexistenteOuInvalidoException;
import com.jiva.cloud.juniatime.model.Product;
import com.jiva.cloud.juniatime.repository.ProductRepository;

/**
 * @author highlander.dantas
 */
@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ApplicationEventPublisher publisher;

  /**
   * 
   * @return todos os produtos
   */
  public List<Product> findAll() {
    return productRepository.findAll();
  }

  /**
   * 
   * @param id
   * @return um produto especifico pelo id
   */
  public Product findOne(String id) {
    Optional<Product> product = productRepository.findById(id);
    if (!product.isPresent()) throw new ProductInexistenteOuInvalidoException();
    
    return product.get();
  }

  /**
   * 
   * @param id
   * @param product
   * @return um produto atualizado
   */
  public Product update(String id, Product product) {
    Product savedProduct = findOne(id);
    BeanUtils.copyProperties(product, savedProduct, "id");
    return productRepository.save(savedProduct);
  }

  /**
   * 
   * @param product
   * @param response
   * @return  um produto criado
   */
  public Product create(Product product, HttpServletResponse response) {
    Product savedProduct = productRepository.save(product);
    publisher.publishEvent(new CreatedResourceEvent(this, response, savedProduct.getId()));
    return savedProduct;
  }

  /**
   * 
   * @param id
   * Deleta um produto
   */
  public void delete(String id) {
    productRepository.deleteById(id);
  }

  /**
   * 
   * @return uma json proprio para dropdowns contendo os produtos
   * 
   */
  public List<LabelValue> findProductLabel() {
    return findAll().stream()
        .map(product -> new LabelValue(product.getId(), StringUtils.capitalize(product.getName())))
        .collect(Collectors.toList());
  }
}
