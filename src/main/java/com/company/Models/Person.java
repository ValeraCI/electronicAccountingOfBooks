package com.company.Models;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Person {
    private int id;

    @NotEmpty(message = "ФИО не должно быть пустым")
    @Size(min = 6, max=25, message = "Длинна ФИО от 6 до 25 символов")
    private String LFP;

    @Min(value = 1925, message = "Человек слишком стар")
    private int yearOfBirth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLFP() {
        return LFP;
    }

    public void setLFP(String LFP) {
        this.LFP = LFP;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    @Override
    public String toString() {

        return "Person{" +
                "id=" + id +
                ", LFP='" + LFP + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                '}';
    }
}
