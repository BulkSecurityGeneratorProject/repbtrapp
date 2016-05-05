package com.btapp.service;

import com.btapp.domain.Btr;
import com.btapp.domain.User;
import com.btapp.repository.BtrRepository;
import com.btapp.repository.UserRepository;
import com.btapp.repository.search.BtrSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Btr.
 */
@Service
@Transactional
public class BtrService {

    private final Logger log = LoggerFactory.getLogger(BtrService.class);
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private BtrRepository btrRepository;
    
    @Inject
    private BtrSearchRepository btrSearchRepository;
    
    /**
     * Save a btr.
     * @return the persisted entity
     */
    public Btr save(Btr btr) {
        log.debug("Request to save Btr : {}", btr);
        
        btr.getUser();
        Optional<User> user = userRepository.findOneByLogin(User.getCurrentUser());
        
        if(btr.getStatus() == null)
        	btr.setStatus("Initiated");    
        else
	        if(btr.getStatus() == "Initiated")
	        	btr.setStatus("Waiting for approval");
        
        if(btr.getId() == null)
        {
	        btr.setAssigned_from((User)user.get());
	        
	        btr.setSupplier(btr.getAssigned_to()); // ma intereseaza supplier-ul curent care se ocupa de btr 
	        btr.setManager((User)user.get());
	        btr.setRequest_date(ZonedDateTime.now());
	        btr.setLast_modified_date(ZonedDateTime.now()); // modificat 25.03.2016
        }
        else
        {
        	//if(btr.getStatus() == "Waiting for approval"){
        	//	btr.setAssigned_from(btr.getSupplier());
        	//}
        	btr.setAssigned_from((User)user.get());
	        btr.setLast_modified_date(ZonedDateTime.now()); // modificat 25.03.2016
        }
        
        Btr result = btrRepository.save(btr);
        btrSearchRepository.save(result);
        return result;
       
    }

    /**
     * Save a approved btr.
     * @return the persisted entity
     
    public Btr approve(Btr btr) {
        log.debug("Request to approve Btr : {}", btr);
         Optional<User> user = userRepository.findOneByLogin(User.getCurrentUser());
	   
        if(user.get().getAuthorities().equals("ROLE_MANAGER")) 
        		if( user.get().getIdManager() != null || btr.getAssigned_to() != null )           
		        {
		        	btr.setStatus("Waiting for approval"); // de la manager n+2
		        	btr.setAssigned_from((User)user.get());
		        	btr.setAssigned_to(null); 
		        }
		        else
		        	if( user.get().getIdManager() == null || btr.getAssigned_to() == null ) 
		        {
		        	btr.setStatus("Issuing tickets");
		            btr.setAssigned_to(btr.getSupplier());
		            btr.setAssigned_from(btr.getAssigned_to());
		        }
       //btr.getUser().getAuthorities().equals("ROLE_MANAGER") 
       //  if(btr.getAssigned_to().getAuthorities().equals("ROLE_MANAGER")
      //		   && btr.getAssigned_from().getAuthorities().equals("ROLE_SUPPLIER") 
      	//	   && btr.getStatus().equals("Waiting for approval") )
       //  {
      	//   btr.setAssigned_from((User)user.get());
      	  // btr.setAssigned_to(btr.getUser().getIdManager());
     // 	   btr.setStatus("Asa da!");
      //   }
      //   else
      //	   if(btr.getUser().getIdManager().isEmpty())
      //			{
      //    			btr.setStatus("Issuing tickets");
       //   			btr.setAssigned_to(btr.getSupplier());
       //   			btr.setAssigned_from(btr.getAssigned_to());
      //			}
    
       // if(btr.getStatus() == "Waiting for approval"){
       // 	btr.setStatus("Issuing tickets");
       // }
        Btr result = btrRepository.save(btr);
        btrSearchRepository.save(result);
        return result;
        //Btr result = ((BtrService) btrRepository).approve(btr);
       // ((BtrService) btrSearchRepository).approve(result);
       // return result;
    }
    */
    /**
     * Save a rejected btr.
     * @return the persisted entity
     
    public Btr reject(Btr btr) {
        log.debug("Request to reject Btr : {}", btr);
        Optional<User> user = userRepository.findOneByLogin(User.getCurrentUser());
        	btr.setStatus("Initiated");
        	btr.setAssigned_to(btr.getSupplier());
			btr.setAssigned_from((User)user.get());
        Btr result = btrRepository.save(btr);
        btrSearchRepository.save(result);
        return result;
    }
    */
    /**
     * Save a canceled btr.
     * @return the persisted entity
     
    public Btr cancel(Btr btr) {
        log.debug("Request to cancel Btr : {}", btr);
        btr.setStatus("Canceled");
        
        Btr result = btrRepository.save(btr);
        btrSearchRepository.save(result);
        return result;
    }
    */
	/** OLD
     *  get all the btrs.
     *  @return the list of entities
    
    @Transactional(readOnly = true) 
    public Page<Btr> findAll(Pageable pageable) {
        log.debug("Request to get all Btrs");
        Page<Btr> result = btrRepository.findAll(pageable); 
        return result;
    }
     */
    
    /** NEW
     *  get all the btrs.
     *  @return the list of entities
    */
    @Transactional(readOnly = true) 
    public Page<Btr> findAll(Pageable pageable) {
        log.debug("Request to get all Btrs");
        Page<Btr> result = btrRepository.finByAssigned_toOrEmployeeIsCurrentUser(pageable); 
        return result;
    }

    /**
     *  get one btr by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Btr findOne(Long id) {
        log.debug("Request to get Btr : {}", id);
        Btr btr = btrRepository.findOne(id);
        return btr;
    }

    /**
     *  delete the  btr by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Btr : {}", id);
        btrRepository.delete(id);
        btrSearchRepository.delete(id);
    }

    /**
     * search for the btr corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Btr> search(String query) {
        
        log.debug("REST request to search Btrs for query {}", query);
        return StreamSupport
            .stream(btrSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
