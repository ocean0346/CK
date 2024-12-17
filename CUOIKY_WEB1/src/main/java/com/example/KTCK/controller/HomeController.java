package com.example.KTCK.controller;

import com.example.KTCK.model.Products;
import com.example.KTCK.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/home")
    public String homePage(){
        return "home";
    }
    @GetMapping("/home2")
    public String home2Page(@RequestParam(defaultValue = "0") int page, Model model){
        int size = 4; // Number of products per page
        Page<Products> productPage = productService.findAll(PageRequest.of(page, size));
        model.addAttribute("productPage", productPage.getContent());
        return "home2";
    }



    @GetMapping("/contact")
    public String contactPage(){
        return "contact";
    }

    @GetMapping("/product-details")
    public String productdetails(){
        return "product-details";
    }
    @GetMapping("/products")
    public String productsPage(){return "products";}
    @GetMapping("/about-us")
    public String aboutus(){
        return "about-us";
    }
    @GetMapping("/cart")
    public String cartPage(){
        return "cart";
    }
}
