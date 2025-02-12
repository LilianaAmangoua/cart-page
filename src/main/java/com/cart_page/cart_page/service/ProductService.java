package com.cart_page.cart_page.service;

import com.cart_page.cart_page.daos.ProductDao;
import com.cart_page.cart_page.entities.Product;
import com.cart_page.cart_page.exceptions.InsufficientStock;
import com.cart_page.cart_page.exceptions.ProductNotFound;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void increaseStock(Product productToFind, int quantity){
        Product product = productDao.findById(productToFind.getProductId());
        product.setStock(product.getStock() + quantity);
        productDao.save(product);
    }

    public Product decreaseStock(Product productToFind, int quantity){
        Product product = productDao.findById(productToFind.getProductId());

        if(!productDao.productExists(productToFind.getProductId())){
            throw new ProductNotFound("Erreur: Produit " + product.getName() + " non trouvé");
        }

        if (product.getStock() < quantity) {
            throw new InsufficientStock("Erreur : Stock non suffisant pour le produit : " + productToFind.getProductId());
        }

        product.setStock(product.getStock() - quantity);
        return productDao.save(product);
    }

    public boolean isStockSufficient(Product product){
        if(product.getStock() > 0){
            return true;
        }
        else {
            return false;
        }
    }


}
