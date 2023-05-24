package com.adoptpet.server.adopt.dto.request;

import com.adoptpet.server.adopt.domain.Adopt;
import com.adoptpet.server.adopt.domain.Gender;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class AdoptUpdateRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    @Length(max = 100)
    private String content;

    @NotBlank
    private String address;

    @NotBlank
    private String age;

    @NotNull
    private Gender gender;

    @NotBlank
    private String species;

    @NotBlank
    private String name;

    @NotNull
    private Float latitude;

    @NotNull
    private Float longitude;

    private AdoptImageRequestDto[] image;


    public Adopt toEntity() {
        return Adopt.builder()
                .age(age)
                .content(content)
                .title(title)
                .gender(gender)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .name(name)
                .species(species)
                .build();
    }
}
