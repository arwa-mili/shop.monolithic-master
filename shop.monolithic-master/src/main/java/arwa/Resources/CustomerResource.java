package arwa.Resources;

import arwa.DTO.CustomerDto;
import arwa.Services.CustomerService;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {
    CustomerService customerService;
    @GET
    public List<CustomerDto> findAll() {
        return this.customerService.findAll();
    }
    @GET
    @Path("/{id}")
    public CustomerDto findById(@PathParam("id") Long id) {
        return this.customerService.findById(id);
    }
    @GET
    @Path("/active")
    public List<CustomerDto> findAllActive() {
        return this.customerService.findAllActive();
    }
    @GET
    @Path("/inactive")
    public List<CustomerDto> findAllInactive() {
        return this.customerService.findAllInactive();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public CustomerDto create(CustomerDto customerDto) {
        return this.customerService.create(customerDto);
    }
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.customerService.delete(id);
    }
}

