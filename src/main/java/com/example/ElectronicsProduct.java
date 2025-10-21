package com.example;

import java.math.BigDecimal;
import java.util.UUID;


public class ElectronicsProduct extends Product implements Shippable {

    private final int warrantyMonths;
    private final BigDecimal weight;


    public ElectronicsProduct(UUID id, String name, Category category, BigDecimal price, int warrantyMonths, BigDecimal weight)
    {
        super(id, name, category, price);

        if (warrantyMonths < 0)
        {
            throw new IllegalArgumentException("Warranty months cannot be negative.");
        }
        if (weight == null)
        {
            throw new IllegalArgumentException("Weight cannot be null.");
        }
        if (weight.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }
        this.warrantyMonths = warrantyMonths;
        this.weight = weight;
    }


    @Override
    public double weight()
    {
        return weight.doubleValue();
    }


    @Override
    public BigDecimal calculateShippingCost()
    {
        BigDecimal base = new BigDecimal("79");

        double w = weight.doubleValue();

        if (w > 5.0) {
            base = base.add(new BigDecimal("49"));
        }
        return base;
    }


    public int warrantyMonths()
    {
        return warrantyMonths;
    }


    @Override
    public String productDetails()
    {
        return String.format("Electronics: %s, Warranty: %d months", name(), warrantyMonths);
    }
}
