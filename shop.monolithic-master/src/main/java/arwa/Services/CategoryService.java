package arwa.Services;

import arwa.DTO.CategoryDto;
import arwa.DTO.ProductDto;
import arwa.Entities.Category;
import arwa.Repository.CategoryRepository;
import arwa.Repository.ProductRepository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j


public class CategoryService {
    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    public static CategoryDto mapToDto(Category category, Long productsCount) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                productsCount);
    }
    public List<CategoryDto> findAll() {
        log.debug("Request to get all Categories");
        return this.categoryRepository.findAll()
                .stream().map(category ->
                        mapToDto(category,
                                productRepository
                                        .countAllByCategoryId(category.getId())))
                .collect(Collectors.toList());
    }
    public CategoryDto findById(Long id) {
        log.debug("Request to get Category : {}", id);
        return this.categoryRepository.findById(id).map(category ->
                        mapToDto(category,
                                productRepository
                                        .countAllByCategoryId(category.getId())))
                .orElse(null);
    }
    public CategoryDto create(CategoryDto categoryDto) {
        log.debug("Request to create Category : {}", categoryDto);
        return mapToDto(this.categoryRepository.save(new Category(
                        categoryDto.getName(),
                        categoryDto.getDescription())
                ), 0L);
    }
    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        log.debug("Deleting all products for the Category : {}", id);
        this.productRepository.deleteAllByCategoryId(id);
        log.debug("Deleting Category : {}", id);
        this.categoryRepository.deleteById(id);
    }
    public List<ProductDto> findProductsByCategoryId(Long id) {
        return this.productRepository.findAllByCategoryId(id)
                .stream()
                .map(ProductService::mapToDto)
                .collect(Collectors.toList());
    }}
