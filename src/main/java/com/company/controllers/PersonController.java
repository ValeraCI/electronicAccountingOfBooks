package com.company.controllers;

import com.company.DAO.BookDAO;
import com.company.DAO.PersonDAO;
import com.company.Models.Book;
import com.company.Models.Person;
import com.company.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonDAO personDAO;
    private final BookDAO bookDAO;
        private final PersonValidator personValidator;

    @Autowired
    public PersonController(PersonDAO personDAO, BookDAO bookDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(@RequestParam(value = "receive", required = false) String receive,
                        Model model){
        List<Person> people;
        if(receive != null && receive.equals("free")){
            model.addAttribute("titleText", "Лиди без книг:");
            people = personDAO.getWithoutBooks();
        }else if(receive != null && receive.equals("busy")){
            model.addAttribute("titleText", "Люди с книгами:");
            people = personDAO.getWithBooks();
        }
        else {
            model.addAttribute("titleText", "Все люди:");
            people = personDAO.getAny();
        }
        model.addAttribute("people", people);
        return "person/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model model){
        Person person = personDAO.get(id);
        model.addAttribute("person", person);
        List<Book> bookList = bookDAO.getForPersonID(id);
        for(Book book: bookList){
            System.out.println(book.getTitle());
        }
        model.addAttribute("personBooks", bookList);
        return "person/show";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable int id, Model model){
        Person person = personDAO.get(id);
        model.addAttribute("person", person);
        return "person/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable int id, @ModelAttribute("person") @Valid Person person,
                       BindingResult bindingResult, Model model){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            model.addAttribute("person", person);
            return "person/edit";
        }
        personDAO.edit(person, id);
        return "redirect:/people";
    }

    @GetMapping("/new")
    public String addForm(Model model){
        model.addAttribute("person", new Person());
        return "person/add";
    }

    @PostMapping()
    public String save(@ModelAttribute("person") @Valid Person person,
                       BindingResult bindingResult, Model model){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            model.addAttribute("person", person);
            return "person/add";
        }
        personDAO.add(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        personDAO.delete(id);
        return "redirect:/people";
    }
}
