package Shop.server;

import Shop.Product;
import Shop.ProductManagerPOA;
import java.util.HashMap;
import java.util.Map;

public class ProductManagerServant extends ProductManagerPOA {

    private Map<String, Product> products;

    public ProductManagerServant() {
        products = new HashMap<>();
    }

    @Override
    public boolean addProduct(Product product) {
        String name = product.name;

        if (!products.containsKey(name)) {
            products.put(name, product);

            return true;
        }

        return false;
    }

    @Override
    public boolean removeProduct(String name) {
        if (products.containsKey(name)) {
            products.remove(name);

            return true;
        }

        return false;
    }

    @Override
    public Product getProduct(String name) {
        Product product = products.get(name);

        return product;
    }

    @Override
    public double getProductPrice(String name) {
        Product product = products.get(name);

        if (product != null) {
            return product.price;
        }

        return 0;
    }

    @Override
    public String getProductDescription(String name) {
        Product product = products.get(name);

        if (product != null) {
            return product.description;
        }

        return "";
    }
}
