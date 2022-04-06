package ru.job4j.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OrdersStoreTest {
    private BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("src/main/db/update_001.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));

        List<Order> all = (List<Order>) store.findAll();

        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenFindByName() {
        OrdersStore store = new OrdersStore(pool);
        Order order = Order.of("name2", "description2");
        store.save(order);
        Order rsl = store.findByName("name2");
        assertEquals(rsl, order);
    }

    @Test
    public void whenUpdateOrder() {
        OrdersStore store = new OrdersStore(pool);
        Order order = Order.of("name3", "description3");
        store.save(order);
        Order rsl = store.findById(order.getId());
        assertEquals(rsl, order);

        Order newOrder = Order.of("newOrder", "newnew");
        boolean resultUpdate = store.update(newOrder, 1);
        assertEquals(store.findById(1).getName(), newOrder.getName());
        assertEquals(store.findById(1).getDescription(), newOrder.getDescription());
        assertTrue(resultUpdate);
    }

    @After
    public void afterTest() {
        try (Connection con = pool.getConnection();
             PreparedStatement pr = con.prepareStatement(
                     "DROP TABLE orders")) {
            pr.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}