package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;

public class Warehouse {
    private static final Map<String, Warehouse> instances = new HashMap<>();
    private final String name;
    private final List<ProductRecord> products = new ArrayList<>();
    private final Set<ProductRecord> changedProducts = new HashSet<>();

    private Warehouse(String name) {
        this.name = name;

    }
    public static Warehouse getInstance() {
        return getInstance("Default Warehouse");

    }
    public static Warehouse getInstance(String name) {
        return instances.computeIfAbsent(name, Warehouse::new);

    }
    public boolean isEmpty() {
        return products.isEmpty();

    }
    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(products);

    }
    public ProductRecord addProduct(UUID uuid, String name, Category category, BigDecimal price) {
        for (ProductRecord product : products) {
            if (product.uuid().equals(uuid)) {
                throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");

            }
        }
        ProductRecord newProduct = new ProductRecord(uuid, name, category, price);
        products.add(newProduct);
        return newProduct;
    }
    public Optional<ProductRecord> getProductById(UUID uuid) {
        return products.stream().filter(p -> p.uuid().equals(uuid)).findFirst();
    }
    public void updateProductPrice(UUID uuid, BigDecimal newPrice) {
        ProductRecord product = getProductById(uuid).orElseThrow(() ->
                new IllegalArgumentException("Product with that id doesn't exist."));
        products.remove(product);
        ProductRecord updatedProduct = new ProductRecord(uuid, product.name(), product.category(), newPrice);
        products.add(updatedProduct);
        changedProducts.add(updatedProduct);
    }
    public List<ProductRecord> getChangedProducts() {
        return new ArrayList<>(changedProducts);
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        Map<Category, List<ProductRecord>> groupedProducts = new HashMap<>();
        for (ProductRecord product : products) {
            groupedProducts.computeIfAbsent(product.category(), k -> new ArrayList<>()).add(product);
        }
        return groupedProducts;
    }

    public List<ProductRecord> getProductsBy(Category category) {
        return products.stream().filter(p -> p.category().equals(category)).toList();
    }
}
