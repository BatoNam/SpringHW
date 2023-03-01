package ru.pflb.person.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonAutoDetect
public class Person {
    private String name;
    // Поставил отрицательное начальное число, т.к. при пустом поле возвращает "0"
    private int age = -1;

//    @JsonIgnore
    private int id;

}
