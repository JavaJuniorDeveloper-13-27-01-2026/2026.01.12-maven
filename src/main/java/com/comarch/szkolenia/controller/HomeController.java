package com.comarch.szkolenia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Strona Główna");
        model.addAttribute("welcomeMessage", "Witamy na naszej stronie wizytówce!");
        return "index";
    }

    @GetMapping("/kontakt")
    public String contact(Model model) {
        model.addAttribute("pageTitle", "Kontakt");
        model.addAttribute("companyName", "Comarch Szkolenia");
        model.addAttribute("email", "kontakt@comarch-szkolenia.pl");
        model.addAttribute("phone", "+48 123 456 789");
        model.addAttribute("address", "ul. Przykładowa 123, 00-001 Warszawa");
        return "kontakt";
    }

    //metoda do wyliczania sredniej liczb z tablicy
    public double obliczSrednia(int[] liczby) {
        if (liczby == null || liczby.length == 0) {
            return 0.0;
        }

        int suma = 0;
        for (int liczba : liczby) {
            suma += liczba;
        }

        return (double) suma / liczby.length;
    }
}

