package arwa.Resources;


import arwa.DTO.OrderItemDto;
import arwa.Services.OrderItemService;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Path("/order-items")
@Produces(MediaType.APPLICATION_JSON)
public class OrderItemResource {
    OrderItemService itemService;
    @GET
    @Path("/order/{id}")
    public List<OrderItemDto> findByOrderId(@PathParam("id") Long id) {
        return this.itemService.findByOrderId(id);
    }
    @GET
    @Path("/{id}")
    public OrderItemDto findById(@PathParam("id") Long id) {
        return this.itemService.findById(id);
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public OrderItemDto create(OrderItemDto orderItemDto) {
        return this.itemService.create(orderItemDto);
    }
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.itemService.delete(id);
    }
}
