package be.vdab.luigi2.controllers;

import be.vdab.luigi2.domain.Pizza;
import be.vdab.luigi2.exceptions.KoersClientException;
import be.vdab.luigi2.forms.VanTotPrijsForm;
import be.vdab.luigi2.services.EuroService;
import be.vdab.luigi2.services.PizzaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("pizzas")
public class PizzaController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EuroService euroService;
    private final PizzaService pizzaService;
/*
    private final Pizza[] pizzas = {
            new Pizza(1, "Prosciutto", BigDecimal.valueOf(4), true),
            new Pizza(2, "Margherita", BigDecimal.valueOf(5), false),
            new Pizza(3, "Calzone", BigDecimal.valueOf(4), false)
    };

 */

    public PizzaController(EuroService euroService, PizzaService pizzaService) {
        this.euroService = euroService;
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public ModelAndView pizzas() {

        return new ModelAndView("pizzas", "pizzas", pizzaService.findAll());
    }

    @GetMapping("{id}")
    public ModelAndView pizza(@PathVariable long id) {
        var modelAndView = new ModelAndView("pizza");
        //Arrays.stream(pizzas).filter(pizza -> pizza.getId() == id).findFirst()
        pizzaService.findById(id)
                .ifPresent(pizza -> {
                    modelAndView.addObject("pizza", pizza);
                    try {
                        modelAndView.addObject(
                                "inDollar", euroService.naarDollar(pizza.getPrijs()));
                    } catch (KoersClientException ex) {
                        logger.error("Kan dollar koers niet lezen", ex);
// Hier komt later code die de exception verwerkt.
                    }
                });
        return modelAndView;
    }
/*
    private List<BigDecimal> uniekePrijzen() {
        return Arrays.stream(pizzas).map(Pizza::getPrijs).distinct().sorted().collect(Collectors.toList());
    }

 */

    @GetMapping("prijzen")
    public ModelAndView prijzen() {
        return new ModelAndView("prijzen", "prijzen", pizzaService.findUniekePrijzen());
    }
/*
    private List<Pizza> pizzasMetPrijs(BigDecimal prijs) {
        return Arrays.stream(pizzas)
                .filter(pizza -> pizza.getPrijs().compareTo(prijs) == 0)
                .collect(Collectors.toList());
    }

 */

    @GetMapping("prijzen/{prijs}")
    public ModelAndView pizzasMetEenPrijs(@PathVariable BigDecimal prijs) {
        //return new ModelAndView("prijzen", "pizzas", pizzasMetPrijs(prijs));
        var modelAndView = new ModelAndView("prijzen", "pizzas", pizzaService.findByPrijs(prijs));
        modelAndView.addObject("prijzen", pizzaService.findUniekePrijzen());
        return modelAndView;
    }

    @GetMapping("vantotprijs/form")
    public ModelAndView vanTotPrijsForm() {
        return new ModelAndView("vantotprijs")
                .addObject(new VanTotPrijsForm(null, null));
    }
    @GetMapping("vantotprijs")
    public ModelAndView vanTotPrijs(@Valid VanTotPrijsForm form, Errors errors) {
        var modelAndView = new ModelAndView("vantotprijs");
        if (errors.hasErrors()) {
            return modelAndView;
        }
        return modelAndView.addObject("pizzas",
                pizzaService.findByPrijsBetween(form.getVan(), form.getTot()));
    }
}
