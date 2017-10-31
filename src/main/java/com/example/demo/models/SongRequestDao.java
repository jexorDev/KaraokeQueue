package com.example.demo.models;



import java.util.List;

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
public interface SongRequestDao extends CrudRepository<SongRequest, Long> {

	public List<SongRequest> findAllByIsComplete(boolean isComplete);
    public List<SongRequest> findByUserId(long userId);    
    public List<SongRequest> findTop3ByOrderBySequence();

}
