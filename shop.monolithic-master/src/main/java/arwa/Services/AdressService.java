package arwa.Services;

import arwa.DTO.AdressDto;

import arwa.Entities.Adress;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdressService {
    public static Adress createFromDto(AdressDto addressDto) {
        return new Adress(
                addressDto.getAddress1(),
                addressDto.getAddress2(),
                addressDto.getCity(),
                addressDto.getPostcode(),
                addressDto.getCountry()
        );
    }
    public static AdressDto mapToDto(Adress address) {
        return new AdressDto(
                address.getAddress1(),
                address.getAddress2(),
                address.getCity(),
                address.getPostcode(),
                address.getCountry()
        );
    }
}