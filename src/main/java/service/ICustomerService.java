package service;

import model.Customer;

import java.util.List;

public interface ICustomerService {
    List<Customer> findAll();
    Customer findOne(int id);
    void save(Customer  customer);
    void edit(int id,Customer customer);
    void delete(int id);
}
