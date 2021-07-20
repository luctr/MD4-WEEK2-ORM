package service.impl;

import model.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import service.ICustomerService;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomerService implements ICustomerService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Customer> findAll() {
        String sql = "SELECT c FROM Customer AS c";
        TypedQuery<Customer> query = entityManager.createQuery(sql, Customer.class);
        return query.getResultList();
    }

    @Override
    public Customer findOne(int id) {
        String queryStr = "SELECT c FROM Customer AS c WHERE c.id = :id";
        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void save(Customer customer) {
        Session session = null;
        Transaction transaction = null;
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        session.save(customer);
        transaction.commit();
    }

    @Override
    public void edit(int id, Customer customer) {
        Session session = null;
        Transaction transaction = null;
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        Customer customer1 = findOne(id);
        customer1.setName(customer.getName());
        customer1.setAddress(customer.getAddress());
        customer1.setEmail(customer.getEmail());
        session.update(customer1);
        transaction.commit();
    }

    @Override
    public void delete(int id) {
        Session session = null;
        Transaction transaction = null;
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        session.delete(findOne(id));
        transaction.commit();
    }
}
