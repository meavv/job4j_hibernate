package lazy;


import model.Candidate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            /**
             * Сохранение объектов
             Candidate candidate1 = Candidate.of("Name1", "Exp1", 100);
             Candidate candidate2 = Candidate.of("Name2", "Exp2", 5.55);
             Candidate candidate3 = Candidate.of("Name3", "Exp3", 99.99);
             session.save(candidate1);
             session.save(candidate2);
             session.save(candidate3);
             */

            /**
             * Выборка по всем кандидатам
             Query query = session.createQuery("from Candidate");
             System.out.println(query.getResultList());
             */

            /**
             * Поиск по id
             Query query = session.createQuery("from Candidate where id = :cId");
             query.setParameter("cId", 2);
             System.out.println(query.uniqueResult());
             */


             Query query = session.createQuery("from Candidate where name = :cName");
             query.setParameter("cName", "newName");
             System.out.println(query.list());


            /**
             * Update через hibernate
             Candidate candidate = session.get(Candidate.class, 1);
             candidate.setName("newName");
             session.save(candidate);
             */

            /**
             * Update через HQL
             session.createQuery("update Candidate c set c.name= :pName where c.id = :pid")
             .setParameter("pName", "Name222")
             .setParameter("pid", 2)
             .executeUpdate();
             */

            /**
             * Delete через HQL
             session.createQuery("delete Candidate c where c.id = :pId")
             .setParameter("pId", 3)
             .executeUpdate();
             */

            /**
             * Delete через Hibernate
             Candidate s = session.load(Candidate.class, 2);
             session.delete(s);
             */


            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}