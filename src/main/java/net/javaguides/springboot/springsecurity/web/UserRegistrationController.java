package net.javaguides.springboot.springsecurity.web;

import net.javaguides.springboot.springsecurity.model.User;
import net.javaguides.springboot.springsecurity.service.UserService;
import net.javaguides.springboot.springsecurity.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result){

        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null){
            result.rejectValue("email", null, "Istnieje konto z podanym adresem email.");
        }

        if (!(userDto.getEmail().equals(userDto.getConfirmEmail()))) {
            result.rejectValue("email", null, "Adresy email nie są identyczne.");
        }

        if (!(userDto.getPassword().equals(userDto.getConfirmPassword()))) {
            result.rejectValue("password", null, "Hasła nie są identyczne.");
        }

        if (userDto.getPassword().equals("")) {
            result.rejectValue("password", null, "Hasło nie może być puste.");
        }

        if (userDto.getEmail().equals("")) {
            result.rejectValue("email", null, "Email nie może być pusty.");
        }

        if (userDto.getPhone().equals("")) {
            result.rejectValue("phone", null, "Numer telefonu nie może być pusty.");
        }

        if (userDto.getFirstName().equals("")) {
            result.rejectValue("firstName", null, "Imię nie może być puste.");
        }

        if (userDto.getLastName().equals("")) {
            result.rejectValue("lastName", null, "Nazwisko nie może być puste.");
        }

        if (userDto.getTerms() == false) {
            result.rejectValue("terms", null, "Pole jest wymagane.");
        }

        if (result.hasErrors()){
            return "registration";
        }

        userService.save(userDto);

        return "redirect:/registration?success";
    }

}
