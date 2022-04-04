package hbrnt;

import liquibase.pro.packaged.C;
import model.Candidate;
import model.Vacancy;
import model.VacancyBase;
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

            System.out.println(session.get(Candidate.class, 1));

            Candidate rsl = session.createQuery(
                    "select distinct c from Candidate c "
                            + "join fetch c.vacancyBase vb "
                            + "join fetch vb.vacancies v "
                            + "where c.id = :sId", Candidate.class
            ).setParameter("sId", 1).uniqueResult();

            System.out.println("RSL " + rsl);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}