package com.adam.projectwebflux.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "roles")

public class Role {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @NotNull
    @Size(max = 100)
    private String name;

}
