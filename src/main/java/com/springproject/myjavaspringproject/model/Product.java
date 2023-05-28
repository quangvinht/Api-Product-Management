package com.springproject.myjavaspringproject.model;


import jakarta.persistence.*;
import org.springframework.context.annotation.Primary;

import java.util.Calendar;
import java.util.Objects;

//POJO : Plan object java Object : convert to JSON
@Entity
@Table(name="Product")

public class Product {

    @Id // This is id
    //@GeneratedValue(strategy = GenerationType.AUTO) // id auto increment
    @SequenceGenerator(name = "product_sequence" , sequenceName ="product_sequence" , allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "product_sequence") //
    private Integer id ; // primary key

    @Column(nullable = false ,unique = true  ,length = 300) // validate
    private String name ;
    private Integer year_release ;
    private Double price ;
    private String url ;



    //calculated field = transient and not show in Database MySql
    @Transient()
    private Integer age;

    public Integer getAge() {
        return Calendar.getInstance().get(Calendar.YEAR) - year_release;
    }
    // contrustor :

    public Product( String name, Integer year_release, Double price, String url) {

        this.name = name;
        this.year_release = year_release;
        this.price = price;
        this.url = url;
    }
    //default constructor
    public Product() {

    }
    //getter and setter

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear_release() {
        return year_release;
    }

    public void setYear_release(Integer year_release) {
        this.year_release = year_release;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", year_release=" + year_release +
                ", price=" + price +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(year_release, product.year_release) && Objects.equals(price, product.price) && Objects.equals(url, product.url) && Objects.equals(age, product.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, year_release, price, url, age);
    }
}
