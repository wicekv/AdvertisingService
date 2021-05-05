package net.javaguides.springboot.springsecurity.web;


import net.javaguides.springboot.springsecurity.model.User;
import net.javaguides.springboot.springsecurity.repository.ArticleRepository;
import net.javaguides.springboot.springsecurity.repository.UserRepository;
import net.javaguides.springboot.springsecurity.service.ArticleService;
import net.javaguides.springboot.springsecurity.service.UserService;
import net.javaguides.springboot.springsecurity.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping("/profil")
public class ProfilController {


    private final UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ProfilController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/display")
    public String showProfilePage(Model model)
    {
        //pobranie adresu email aktualnie zalgoowanego użytkownika
        String currentUserName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        //Utworzenie obiektu aktualnie zalogowanego użytkownika
        User user = userService.findByEmail(currentUserName);
        //Dodanie aktualnego użytkownika do modelu, aby móc go wyświetlić
        model.addAttribute(user);
        return "/profil";
    }

    @GetMapping("/editProfile")
    public String editProfile(Model model)
    {
        //pobranie adresu email aktualnie zalgoowanego użytkownika
        String currentUserName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        //Utworzenie obiektu aktualnie zalogowanego użytkownika
        User user = userService.findByEmail(currentUserName);
        //Dodanie aktualnego użytkownika do modelu, aby móc go wyświetlić
        model.addAttribute(user);
        return "/editProfile";
    }

    @PostMapping("/display")
    public String editUserAccount(@ModelAttribute("user") User user, Model model, BindingResult result)
    {
        String currentUserName = "";
        //pobranie emaila aktualnego użytkownika z kontekstu aplikacji
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        //Utworzenie obiektu aktualnego użytkownika
        User displayed = userService.findByEmail(currentUserName);
        //Po to żeby wyświetlało wszystkie pola użytkownika po przejściu do profil/display
        model.addAttribute(displayed);
        //Zmiana poszczególnych pól aktualnie zalogowanego użytkownika
        displayed.setFirstName(user.getFirstName());
        displayed.setLastName(user.getLastName());
        displayed.setPhone(user.getPhone());
        if (displayed.getFirstName().equals("")) {
            result.rejectValue("firstName", null, "Imię nie może być puste.");
        }

        if (displayed.getLastName().equals("")) {
            result.rejectValue("lastName", null, "Nazwisko nie może być puste.");
        }

        if (displayed.getPhone().equals("")) {
            result.rejectValue("phone", null, "Numer telefonu nie może być pusty.");
        }

        if (result.hasErrors()){
            return "editProfile";
        }
        //update rekordu bazy zawierającego aktualnego użytkownika
        userService.update(displayed);
        return "/profil";
    }

    @GetMapping("/editPassword")
    public String editPassword(Model model)
    {
        //pobranie adresu email aktualnie zalgoowanego użytkownika
        String currentUserName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        //Utworzenie obiektu aktualnie zalogowanego użytkownika
        User user = userService.findByEmail(currentUserName);
        //Dodanie aktualnego użytkownika do modelu, aby móc go wyświetlić
        model.addAttribute(user);
        return "/editPassword";
    }

    @PostMapping("/editPassword")
    public String editUserPassword(@ModelAttribute("user") User user, Model model, BindingResult result)
    {
        String currentUserName = "";
        //pobranie emaila aktualnego użytkownika z kontekstu aplikacji
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        //Utworzenie obiektu aktualnego użytkownika
        User displayed = userService.findByEmail(currentUserName);
        //Po to żeby wyświetlało wszystkie pola użytkownika po przejściu do profil/display
        model.addAttribute(displayed);
        //Zmiana poszczególnych pól aktualnie zalogowanego użytkownika
        displayed.setPassword(passwordEncoder.encode(user.getPassword()));
        //update rekordu bazy zawierającego aktualnego użytkownika
        userService.update(displayed);
        return "/profil";
    }

    @GetMapping("/deleteUser")
    public String deleteUserAccount(User user)
    {
        String currentUserName = "";
        //pobranie emaila aktualnego użytkownika z kontekstu aplikacji
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        User display = userService.findByEmail(currentUserName);
        userService.delete(display);
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:http://localhost:8080";
    }


/*    // Zapisywanie zmian w profilu uzytkownika
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(User user)
    {
        userRepository.save(user);
        return "redirect:/";
    }

    // Mozliwosc usuniecia konta uzytkownika
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String deleteUser(@PathVariable("id") Long userId, Model model)
    {
        userRepository.delete(userId);
        return "redirect:/";
    }*/

}
