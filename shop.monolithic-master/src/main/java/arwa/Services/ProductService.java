package arwa.Services;

import arwa.DTO.ProductDto;
import arwa.Entities.Product;
import arwa.Repository.CategoryRepository;
import arwa.Repository.ProductRepository;

import arwa.enumerates.ProductStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j

@Transactional
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    public List<ProductDto> findAll() {
        log.debug("Request to get all Products");
        return this.productRepository.findAll()
                .stream().map(ProductService::mapToDto)
                .collect(Collectors.toList());
    }
    public ProductDto findById(Long id) {
        log.debug("Request to get Product : {}", id);
        return this.productRepository.findById(id)
                .map(ProductService::mapToDto).orElse(null);
    }
    public Long countAll() {
        return this.productRepository.count();
    }
    public Long countByCategoryId(Long id) {
        return this.productRepository.countAllByCategoryId(id);
    }
    public ProductDto create(ProductDto productDto) {
        log.debug("Request to create Product : {}", productDto);
        return mapToDto(this.productRepository.save(
                new Product(
                        productDto.getName(),
                        productDto.getDescription(),
                        productDto.getPrice(),
                        ProductStatus.valueOf(productDto.getStatus()),
                        productDto.getSalesCounter(),
                        Collections.emptySet(),
                        categoryRepository.findById(productDto.getCategoryId())
                                .orElse(null)
                )));
    }
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        this.productRepository.deleteById(id);
    }
    public List<ProductDto> findByCategoryId(Long id) {
        return this.productRepository.findByCategoryId(id).stream()
                .map(ProductService::mapToDto).collect(Collectors.toList());
    }
    public static ProductDto mapToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStatus().name(),
                product.getSalesCounter(),
                product.getReviews().stream().map(ReviewService::mapToDto)
                        .collect(Collectors.toSet()),
                product.getCategory().getId()
        );
    }
}
