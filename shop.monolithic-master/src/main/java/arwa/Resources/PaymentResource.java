package arwa.Resources;

import arwa.DTO.PaymentDto;
import arwa.Services.PaymentService;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentResource {
    PaymentService paymentService;
    @GET
    public List<PaymentDto> findAll() {
        return this.paymentService.findAll();
    }
    @GET
    @Path("/{id}")
    public PaymentDto findById(@PathParam("id") Long id) {
        return this.paymentService.findById(id);
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public PaymentDto create(PaymentDto orderItemDto) {
        return this.paymentService.create(orderItemDto);
    }
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.paymentService.delete(id);
    }
    @GET
    @Path("/price/{max}")
    public List<PaymentDto> findPaymentsByAmountRangeMax(@PathParam("max") double max) {
        return this.paymentService.findByPriceRange(max);
    }
}
