package com.springproject.myjavaspringproject.controller;


import com.springproject.myjavaspringproject.model.Product;
import com.springproject.myjavaspringproject.model.ResponseObject;
import com.springproject.myjavaspringproject.repository.ProductRepository;
import com.springproject.myjavaspringproject.services.IsStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final IsStorageService isStorageService;


    public ProductController(ProductRepository productRepository, IsStorageService isStorageService) {
        this.productRepository = productRepository;
        this.isStorageService = isStorageService;
    }


    @GetMapping()
    List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("{product_id}")
    ResponseEntity<ResponseObject> getSingleProduct(@PathVariable("product_id") Integer product_id) {
        Optional<Product> foundProduct = productRepository.findById(product_id);

        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "success", foundProduct)
                )
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can not find by this id = " + product_id, "")
        );

    }

    @GetMapping("/search/")
    ResponseEntity<ResponseObject> searchProductByName(@RequestParam String name,
                                                       @RequestParam String url,
                                                       @RequestParam Double price
    ) {
        List<Product> foundProductByName = productRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name.trim());
        List<Product> foundByNameOrPriceOrUrl = productRepository.findByNameContainingIgnoreCaseOrUrlContainingOrPriceOrderByIdAsc(name, url, price);
        if (foundByNameOrPriceOrUrl.size() > 0) {
            return
                    ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("ok", "success", foundByNameOrPriceOrUrl));
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can not find product", "")
        );
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> addProduct(@RequestParam("url") MultipartFile url,
                                                     @RequestParam("name") String name,
                                                     @RequestParam("year_release") Integer year_release,
                                                     @RequestParam("price") Double price
    ) {

        try {

            String generatedFileName = isStorageService.storeFile(url);
            List<Product> newProduct = productRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name.trim());
            if (newProduct.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Product name  already have", "")
                );
            } else {
                Product createProduct = new Product();
                createProduct.setName(name);
                createProduct.setYear_release(year_release);
                createProduct.setPrice(price);
                createProduct.setUrl(generatedFileName);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Success", productRepository.save(createProduct))
                );
            }

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }


//        List<Product> newProduct = productRepository.findByNameContainingIgnoreCaseOrderByNameAsc(req.getName().trim());
//        return newProduct.size() > 0 ?
//                ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                        new ResponseObject("failed", "Product name  already have", "")
//                )
//                : ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("success", "Insert data success ", productRepository.save(req))
//        );


    }


    @PutMapping("{product_id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestParam(value ="url" ,required = false) MultipartFile url,
                                                 @RequestParam(value ="name",required = false) String name,
                                                 @RequestParam(value ="year_release",required = false) Integer year_release,
                                                 @RequestParam(value ="price",required = false) Double price,
                                                 @PathVariable("product_id") Integer product_id) {
        String generatedFileName = isStorageService.storeFile(url);
        Product foundProduct = (productRepository.findById(product_id)
                .map(product -> {
                    if (!name.isEmpty() ) {
                        product.setName(name);
                    } else {
                        product.setName(product.getName());
                    }

                    if (year_release != null) {
                        product.setYear_release(year_release);
                    } else {
                        product.setYear_release(product.getYear_release());
                    }

                    if (price != null) {
                        product.setPrice(price);
                    } else {
                        product.setPrice(product.getPrice());
                    }

                    if ( !url.isEmpty()) {
                        product.setUrl(generatedFileName);
                    } else {
                        product.setUrl(product.getUrl());
                    }


                    return productRepository.save(product);
                }).orElseGet(() -> {
                            Product createProduct = new Product();
                            createProduct.setName(name);
                            createProduct.setYear_release(year_release);
                            createProduct.setPrice(price);
                            createProduct.setUrl(generatedFileName);
                            return productRepository.save(createProduct);
                        }

                )); // craete a new one if can not update because can find id

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update success", foundProduct)
        );
    }

    @DeleteMapping("{product_id}")
    ResponseEntity<ResponseObject> deleteSingleProduct(@PathVariable("product_id") Integer product_id) {
        boolean existProduct = productRepository.existsById(product_id);
        if (existProduct) {
            Optional<Product> foundProduct = productRepository.findById(product_id);
            productRepository.deleteById(product_id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete success", foundProduct)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can not delete because can't find this id = " + product_id
                        , "")
        );
    }
}
