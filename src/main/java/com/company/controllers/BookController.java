package com.company.controllers;

import com.company.DAO.BookDAO;
import com.company.DAO.PersonDAO;
import com.company.Models.Book;
import com.company.Models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final PersonDAO personDAO;
    private final BookDAO bookDAO;

    @Autowired
    public BookController(PersonDAO personDAO, BookDAO bookDAO) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
    }

    @GetMapping()
    public String showBooks(@RequestParam(value = "receive", required = false) String receive,
                            Model model){
        List<Book> books;

        if(receive != null && receive.equals("free")){
            model.addAttribute("titleText", "Свободные книги:");
            books = bookDAO.getFree();
        }else if(receive != null && receive.equals("busy")){
            model.addAttribute("titleText", "Занятые книги:");
            books = bookDAO.getBusy();
        }
        else {
            model.addAttribute("titleText", "Все книги:");
            books = bookDAO.getAny();
        }
        model.addAttribute("books", books);
        return "book/index";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model){
        Book book = bookDAO.getForBookID(id);
        model.addAttribute("book", book);
        if(!book.isFree()){
            model.addAttribute("owner", personDAO.get(book.getPersonId()));
        }
        else{
            model.addAttribute("person", new Person());
            model.addAttribute("people", personDAO.getAny());
        }
        return "book/show";
    }

    @GetMapping("/new")
    public String addForm(Model model){
        model.addAttribute("book", new Book());
        return "book/add";
    }

    @PostMapping()
    public String save(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("book", book);
            return "/book/add";
        }
        bookDAO.add(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable("id") int id, Model model){
        Book book = bookDAO.getForBookID(id);
        model.addAttribute("book", book);
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                       BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "/book/edit";
        }
        bookDAO.edit(book, id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        bookDAO.getBack(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/take")
    public String take(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        bookDAO.take(id, person.getId());
        return "redirect:/books";
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookDAO.delete(id);
        return "redirect:/books";
    }
}
