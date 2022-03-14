package lazy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;



public class HbmRun {
    public static void main(String[] args) {

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            List<Brand> list = new ArrayList<>();
            list = session.createQuery("from Brand").list();
            for (Brand brand : list) {
                for (Model model : brand.getModels()) {
                    System.out.println(model);
                }
            }

            System.out.println("123");

            /**
            Brand brand = Brand.of("Brand 1");
            Model m1 = Model.of("Model 1", brand);
            Model m2 = Model.of("Model 2", brand);
            brand.getModels().add(m1);
            brand.getModels().add(m2);
            session.save(m1);
            session.save(m2);
            session.save(brand);
             **/

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}