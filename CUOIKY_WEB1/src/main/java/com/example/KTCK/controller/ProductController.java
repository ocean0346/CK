package com.example.KTCK.controller;

import com.example.KTCK.model.Products;
import com.example.KTCK.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.KTCK.exception.NotFoundException;
import com.example.KTCK.service.ProductService;

@Controller
@RequestMapping()
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    // Display details of a single product
    @GetMapping("/shop/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        model.addAttribute("product", product);
        model.addAttribute("rating", product.getRating());
        return "shop-detail";
    }

    // Display list of products with pagination
    @GetMapping("/shop")
    public String getProducts(@RequestParam(defaultValue = "0") int page, Model model) {
        int size = 20; // Number of products per page
        Page<Products> productPage = productService.findAll(PageRequest.of(page, size));
        model.addAttribute("productPage", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "shop";
    }

    // Custom exception handler for NotFoundException
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException ex, Model model) {
        model.addAttribute("message", "Không tìm thấy sản phẩm với id: " + ex.getMessage());
        return "error";
    }
}
