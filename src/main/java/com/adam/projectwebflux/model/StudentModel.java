package com.adam.projectwebflux.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "students")
public class StudentModel {

    @EqualsAndHashCode.Include
    @Id
    private String id;

    @NotNull(message = "{validation.student.firstName.null}")
    @Size(max = 100, message = "{validation.student.firstName.size}")
    private String firstName;

    @NotNull(message = "{validation.student.lastName.null}")
    @Size(max = 100, message = "{validation.student.lastName.size}")
    private String lastName;

    @NotNull(message = "{validation.student.dni.null}")
    @Size(max = 8, message = "{validation.student.dni.size}")
    private String dni;

    @NotNull(message = "{validation.student.age.null}")
    @Max(value = 120, message = "{validation.student.age.max}")
    private Integer age;
}
