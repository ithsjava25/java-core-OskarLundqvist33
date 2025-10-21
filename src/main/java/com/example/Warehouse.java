package com.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {

    private static final Map<String, Warehouse> INSTANCES = new HashMap<>();

    private final String name;
    private final List<Product> products = new ArrayList<>();
    private final Set<Product> changedProducts = new HashSet<>();


    private Warehouse(String name)
    {
        this.name = name;
    }


    public static synchronized Warehouse getInstance(String name)
    {
        return INSTANCES.computeIfAbsent(name, Warehouse::new);
    }

    public static Warehouse getInstance()
    {
        return getInstance("DefaultWarehouse");
    }


    public void addProduct(Product product)
    {
        if (product == null)
        {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        boolean duplicateUUID = products.stream()
                .anyMatch(existing -> existing.uuid().equals(product.uuid()));

        if (duplicateUUID)
        {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }

        products.add(product);
    }


    public List<Product> getProducts()
    {
        return Collections.unmodifiableList(new ArrayList<>(products));
    }


    public Optional<Product> getProductById(UUID id)
    {
        return products.stream()
                .filter(p -> p.uuid().equals(id))
                .findFirst();
    }


    public void updateProductPrice(UUID id, BigDecimal newPrice)
    {
        Optional<Product> optionalProduct = getProductById(id);

        if (optionalProduct.isEmpty())
        {
            throw new NoSuchElementException("Product not found with id: " + id);
        }

        Product product = optionalProduct.get();
        product.setPrice(newPrice);
        changedProducts.add(product);
    }


    public Set<Product> getChangedProducts()
    {
        return Collections.unmodifiableSet(new HashSet<>(changedProducts));
    }


    public List<Perishable> expiredProducts()
    {
        List<Perishable> expired = new ArrayList<>();
        for (Product p : products)
        {
            if (p instanceof Perishable per && per.expirationDate().isBefore(LocalDate.now()))
            {
                expired.add(per);
            }
        }
        return expired;
    }


    public List<Shippable> shippableProducts()
    {
        return products.stream()
                .filter(p -> p instanceof Shippable)
                .map(p -> (Shippable) p)
                .collect(Collectors.toList());
    }


    public void remove(UUID id)
    {
        products.removeIf(p -> p.uuid().equals(id));
    }


    public void clearProducts()
    {
        products.clear();
        changedProducts.clear();
    }


    public boolean isEmpty()
    {
        return products.isEmpty();
    }


    public Map<Category, List<Product>> getProductsGroupedByCategories()
    {
        if (products.isEmpty())
        {
            return Collections.emptyMap();
        }
        return products.stream().collect(Collectors.groupingBy(Product::category));
    }


    public String getName()
    {
        return name;
    }
}