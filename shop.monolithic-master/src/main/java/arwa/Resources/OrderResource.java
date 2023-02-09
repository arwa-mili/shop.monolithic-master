package arwa.Resources;

import arwa.DTO.OrderDto;
import arwa.Services.OrderService;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
    OrderService orderService;
    @GET
    public List<OrderDto> findAll() {
        return this.orderService.findAll();
    }
    @GET
    @Path("/customer/{id}")
    public List<OrderDto> findAllByUser(@PathParam("id") Long id) {
        return this.orderService.findAllByUser(id);
    }
    @GET
    @Path("/{id}")
    public OrderDto findById(@PathParam("id") Long id) {
        return this.orderService.findById(id);
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public OrderDto create(OrderDto orderDto) {
        return this.orderService.create(orderDto);
    }
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.orderService.delete(id);
    }
    @GET
    @Path("/exists/{id}")
    public boolean existsById(@PathParam("id") Long id) {
        return this.orderService.existsById(id);
    }
}
