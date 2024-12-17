package com.example.KTCK.controller;

import com.example.KTCK.model.Products;
import com.example.KTCK.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/admin/products")
public class AdminController {
    @Autowired
    private ProductService productService;


    @GetMapping("/home")
    public String adminHome() {
        return "admin/admin";
    }
    @GetMapping
    public String viewAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Products());
        return "admin/add-product";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Products product,
                             @RequestParam("imageFile") MultipartFile imageFile) {
        // Xử lý file tải lên
        if (!imageFile.isEmpty()) {
            String imagePath = saveUploadedFile(imageFile); // Lưu file và lấy đường dẫn
            product.setImageUrl(imagePath); // Gán đường dẫn hình ảnh
        }

        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Optional<Products> products = productService.getProductById(id);
        if (products.isPresent()) {
            model.addAttribute("product", products.get());
            return "admin/edit-product";
        } else {
            return "redirect:/admin/products";
        }
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id,
                              @ModelAttribute("product") Products product,
                              @RequestParam("imageFile") MultipartFile imageFile) {
        // Xử lý file tải lên
        if (!imageFile.isEmpty()) {
            String imagePath = saveUploadedFile(imageFile); // Lưu file và lấy đường dẫn
            product.setImageUrl(imagePath); // Gán đường dẫn hình ảnh
        }

        product.setId(id);
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
    // Phương thức để lưu file
    private String saveUploadedFile(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Get the absolute path to the upload directory
                String uploadDir = new File("src/main/resources/static/images").getAbsolutePath();
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs(); // Create the directory if it doesn't exist
                }

                // Save the file to the upload directory
                String filePath = uploadDir + File.separator + file.getOriginalFilename();
                File destinationFile = new File(filePath);
                file.transferTo(destinationFile);

                // Return the relative path for the database
                return "/images/" + file.getOriginalFilename(); // Adjust the path as needed
            } catch (IOException e) {
                e.printStackTrace(); // Consider using a logger instead
                // Handle the error appropriately, e.g., throw a custom exception
            }
        }
        return null; // Return null if the file is empty
    }
}
