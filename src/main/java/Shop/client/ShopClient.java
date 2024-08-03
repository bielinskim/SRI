package Shop.client;

import Shop.Product;
import Shop.ProductManager;
import Shop.ProductManagerHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class ShopClient {
    public static void main(String[] args) throws Exception {
        ORB orb = ORB.init(args, null);

        org.omg.CORBA.Object obj = readRefFromNamingService(orb, "Shop");

        ProductManager productManager = ProductManagerHelper.narrow(obj);

        Product product = new Product();

        product.name = "Produkt testowy";
        product.price = 5.21;
        product.description = "Opis testowy";

        boolean addStatus = productManager.addProduct(product);

        System.out.println(addStatus);

        Product resProduct = productManager.getProduct("Produkt testowy");

        System.out.println(resProduct.price);

        double price = productManager.getProductPrice("Produkt testowy");

        System.out.println(price);

        String description = productManager.getProductDescription("Produkt testowy");

        System.out.println(description);

        boolean removeStatus = productManager.removeProduct("Produkt testowy");

        System.out.println(removeStatus);

    }

    private static org.omg.CORBA.Object readRefFromNamingService(ORB orb, String refName) throws Exception {
        org.omg.CORBA.Object o = orb.resolve_initial_references("NameService");

        NamingContextExt rootContext = NamingContextExtHelper.narrow(o);

        NameComponent nc = new NameComponent(refName, "");

        NameComponent path[] = {nc};

        return rootContext.resolve(path);
    }
}
