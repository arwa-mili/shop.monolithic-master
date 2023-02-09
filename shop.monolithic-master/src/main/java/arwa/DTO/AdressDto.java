package arwa.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdressDto {
    private String address1;
    private String address2;
    private String city;
    private String postcode;
    @Size(min = 2, max = 2)
    private String country;
}