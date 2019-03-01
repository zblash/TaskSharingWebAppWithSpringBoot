package com.tasksharing.tasksharing.controllers;

import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.services.Concrete.EMailService;
import com.tasksharing.tasksharing.services.Concrete.SecurityService;
import com.tasksharing.tasksharing.services.Concrete.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private EMailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/register")
    public String Register(Model model){
        model.addAttribute("user",new User());
        return "register";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        model.addAttribute("user", user);

        if (result.hasErrors()){
            return "register";
        } else {
            userService.Add(user);

        }
        return "redirect:/login";
    }

    @GetMapping("/me")
    public String Me(Authentication authentication,Model model){
        model.addAttribute("user",userService.findByUserName(authentication.getName()));
        return "me/index";
    }

    @GetMapping("me/groups")
    public String meGroups(Model model){
        securityService.reloadLoggedInUser();
        model.addAttribute("groups",userService.findById(securityService.findLoggedInUser().getId()).getGroups());
        return "me/groups";
    }

    @RequestMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(){
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordPagePost(@RequestParam String email, Model model, HttpServletRequest request){
       User user = userService.findByEmail(email);

       user.setResetToken(UUID.randomUUID().toString());
       userService.Update(user);

        String appUrl = request.getScheme() + "://" + request.getServerName()+":"+request.getServerPort();

        String mailMessage = "Şifrenizi sıfırlamak için linke tıklayın:\n" + appUrl
                + "/password-reset?token=" + user.getResetToken();

        emailService.sendPasswordResetMail(user.getEmail(),"Şifre Sıfırlama",mailMessage);

        model.addAttribute("messageSended","Şifre sıfırlama linki email adresinize gönderildi.");
        return "forgot-password";
    }

    @GetMapping("/password-reset")
    public String passwordResetPage(@RequestParam String token,Model model){
        Optional<User> optionalUser = userService.findByResetToken(token);
        if (!optionalUser.isPresent()){
            model.addAttribute("errorMessage","Böyle bir kullanıcı bulunamadı");
        }
        return "password-reset";
    }

    @PostMapping("/password-reset")
    public String passwordResetPost(@RequestParam Map<String, String> requestParams, Model model, RedirectAttributes redirectAttributes){
        Optional<User> optionalUser = userService.findByResetToken(requestParams.get("token"));
        if (!optionalUser.isPresent()){
            model.addAttribute("errorMessage","Böyle bir kullanıcı bulunamadı");
            return "password-reset";
        }else{
            User user = optionalUser.get();
                    user.setPassword(passwordEncoder.encode(requestParams.get("password")));
                    user.setResetToken(" ");
            userService.Update(user);
        }
        redirectAttributes.addFlashAttribute("message","Şifreniz Başarıyla Değiştirildi");
        return "redirect:/login";
    }
}
