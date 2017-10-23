package com.example.demo.models;



import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

/**
 * A DAO for the entity User is simply created by extending the CrudRepository
 * interface provided by spring. The following methods are some of the ones
 * available from such interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature!
 *
 * @author netgloo
 */
@Transactional
public interface UserDao extends CrudRepository<User, Long> {

    public User findByFirstNameAndLastNameAndPassword(String firstName, String lastName, String password);
    public User findByFirstNameAndLastName(String firstName, String lastName);

} // class UserDao
