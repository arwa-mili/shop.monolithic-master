package arwa.Resources;


import arwa.DTO.ProductDto;
import arwa.Services.ProductService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Path("/products") @Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
    ProductService productService;
    @GET
    public List<ProductDto> findAll() {
        return this.productService.findAll();
    }
    @GET @Path("/count")
    public Long countAllProducts() {
        return this.productService.countAll();
    }
    @GET @Path("/{id}")
    public ProductDto findById(@PathParam("id") Long id) {
        return this.productService.findById(id);
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public ProductDto create(ProductDto productDto) {
        return this.productService.create(productDto);
    }
    @DELETE @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.productService.delete(id);
    }
    @GET @Path("/category/{id}")
    public List<ProductDto> findByCategoryId(@PathParam("id") Long id) {
        return this.productService.findByCategoryId(id);
    }
    @GET @Path("/count/category/{id}")
    public Long countByCategoryId(@PathParam("id") Long id) {
        return this.productService.countByCategoryId(id);
    }
}

