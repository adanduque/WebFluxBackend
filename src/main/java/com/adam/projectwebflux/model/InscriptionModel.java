package com.adam.projectwebflux.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "inscriptions")
public class InscriptionModel {

    @EqualsAndHashCode.Include
    @Id
    private String id;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private StudentModel student;

    @NotNull
    private List<CourseModel> courses;

    @NotNull
    private Boolean state;


    @JsonIgnore
    public List<String> getIds(){
        List<String> listIds = new ArrayList<>();
        this.courses.forEach((course) -> listIds.add(course.getId()));
        return  listIds;
    }
}
