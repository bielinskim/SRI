package Shop.server;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class ShopServer {
    public static void main(String[] args) throws Exception {
        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);

        POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
        poa.the_POAManager().activate();

        ProductManagerServant productManagerServant = new ProductManagerServant();

        org.omg.CORBA.Object o = poa.servant_to_reference(productManagerServant);

        saveRefInNamingService(orb, o, "Shop");

        java.lang.Object sync = new java.lang.Object();
        synchronized (sync) {
            sync.wait();
        }
    }

    private static void saveRefInNamingService(ORB orb, org.omg.CORBA.Object ref, String refName) throws Exception {
        org.omg.CORBA.Object o = orb.resolve_initial_references("NameService");

        NamingContextExt rootContext = NamingContextExtHelper.narrow(o);

        NameComponent nc = new NameComponent(refName, "");

        NameComponent path[] = {nc};

        rootContext.rebind(path, ref);
    }
}
