package dev.dluks.minervamoney.dtos.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

    private Long id;
    private String name;
    private String description;
    private boolean isDefault;
}
