package com.adam.projectwebflux.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "users")
public class User {

    @EqualsAndHashCode.Include
    @Id
    private String id;

    @NotNull
    @Size(max = 5)
    private String username;

    @NotNull
    @Size(max = 150)
    private String password;

    @NotNull
    private Boolean status;

    private List<Role> roles;

}
