package io.pivotal.apac;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequestMapping("/form")
public class FormController {

    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity<Object> processFormSubmit(@Valid FormBean formBean, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        System.out.printf(formBean.toString());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

@SuppressWarnings("unused")
class FormBean {

    @NotEmpty
    private String name;

    @Min(21)
    private int age;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String toString() {
        return "name='" + (this.name != null ? this.name : "") + "', age=" + age;
    }

}