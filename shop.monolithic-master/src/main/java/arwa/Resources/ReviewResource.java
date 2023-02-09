package arwa.Resources;

import arwa.DTO.ReviewDto;
import arwa.Services.ReviewService;
import com.oracle.svm.core.annotate.Inject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Path("/reviews") @Produces(MediaType.APPLICATION_JSON)
public class ReviewResource {
    @Inject
    ReviewService reviewService;
    @GET
    @Path("/product/{id}")
    public List<ReviewDto> findAllByProduct(@PathParam("id") Long id) {
        return this.reviewService.findReviewsByProductId(id);
    }
    @GET @Path("/{id}")
    public ReviewDto findById(@PathParam("id") Long id) {
        return this.reviewService.findById(id);
    }
    @POST
    @Path("/product/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ReviewDto create(ReviewDto reviewDto, @PathParam("id") Long id) {
        return this.reviewService.create(reviewDto, id);
    }
    @DELETE @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.reviewService.delete(id);
    }
}