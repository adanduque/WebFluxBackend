package com.adam.projectwebflux.model;


import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Document(collection = "courses")
public class CourseModel {

    @EqualsAndHashCode.Include
    @Id
    private String id;

    @NotNull(message = "{validation.course.name.null}")
    @Size(max = 50, message = "{validation.course.name.size}")
    private String name;

    @NotNull(message = "{validation.course.acronym.null}")
    @Size(max = 10,message = "{validation.course.acronym.size}")
    private String acronym;

    @NotNull(message = "{validation.course.state.null}")
    private Boolean state;
}
