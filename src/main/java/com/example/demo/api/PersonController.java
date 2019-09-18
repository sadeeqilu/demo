package com.example.demo.api;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.PersonService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/person")
@RestController
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public String addPerson(@Valid @NotNull @RequestBody Person person){
        int i = personService.addPerson(person);
        if (i > 0)
            return "successfully added a person to db";
        return "Could not add person to db";
    }

    @GetMapping
    public List<Person> getAllPeople(){
        return this.personService.getAllPeople();
    }

    @GetMapping(path = "{id}")
    public Person getPersonById(@PathVariable("id") UUID id){
        return personService.getPersonByID(id)
                .orElse(null);
    }

    @DeleteMapping(path = "{id}")
    public void deletePeronById(@PathVariable("id") UUID id){
        personService.deletePerson(id);
    }

    @PutMapping(path = "{id}")
    public void updatePerson(@PathVariable("id") UUID id,@Valid @NotNull @RequestBody Person person){
        personService.updatePerson(id,person);
    }
}
