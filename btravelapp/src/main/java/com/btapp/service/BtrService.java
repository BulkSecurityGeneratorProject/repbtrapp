package com.btapp.service;

import com.btapp.domain.Btr;
import com.btapp.domain.Historybtr;
import com.btapp.domain.User;
import com.btapp.repository.AuthorityRepository;
import com.btapp.repository.BtrRepository;
import com.btapp.repository.UserRepository;
import com.btapp.repository.search.BtrSearchRepository;
import com.btapp.security.AuthoritiesConstants;

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

	@Inject
	private AuthorityRepository authorityRepository;
	
	 @Inject
	 private UserService userService;


	/**
	 * Save a btr.
	 * 
	 * @return the persisted entity
	 */
	public Btr save(Btr btr) {
		log.debug("Request to save Btr : {}", btr);

		btr.getUser();
		Optional<User> user = userRepository.findOneByLogin(User.getCurrentUser());

		if (btr.getId() == null) {// new -> save
			btr.setAssigned_from((User) user.get());

			btr.setSupplier(btr.getAssigned_to()); // ma intereseaza supplier-ul
													// curent care se ocupa de
													// btr
			btr.setManager((User) user.get());
			btr.setRequest_date(ZonedDateTime.now());
			btr.setLast_modified_date(ZonedDateTime.now());

			btr.setSuma_totala(null);
			btr.setStatus("Initiated"); // a
		} else {
			if (btr.getStatus().equals("Initiated")) { // a
				btr.setAssigned_from((User) user.get());
				btr.setSupplier(btr.getAssigned_to()); // a
				btr.setLast_modified_date(ZonedDateTime.now());
			} else if (btr.getStatus().equals("Waiting for approval") && btr.getAssigned_to().getId().equals(btr.getSupplier().getId())) {
				
				btr.setAssigned_from((User) user.get());
				btr.setAssigned_to(btr.getManager());
				btr.setLast_modified_date(ZonedDateTime.now());
			} else if (btr.getStatus().equals("Waiting for approval") && btr.getAssigned_to().getId().equals(btr.getManager().getId())) {
				btr.setAssigned_from((User) user.get());
				if (btr.getManager().getIdManager() == null){
				    btr.setAssigned_from((User) user.get());
				    btr.setAssigned_to(btr.getSupplier());
				}
				else {
					btr.setAssigned_from((User) user.get());
					Optional<User> managerNDoi = userService.getUserWithAuthoritiesByLogin(btr.getManager().getIdManager());
					btr.setAssigned_to((User) managerNDoi.get());
				}	
			}
			else {
				btr.setAssigned_from((User) user.get());
				btr.setLast_modified_date(ZonedDateTime.now());
			}
		}

		Btr result = btrRepository.save(btr);
		btrSearchRepository.save(result);
		return result;

	}

	/**
	 * OLD get all the btrs.
	 * 
	 * @return the list of entities
	 * 
	 * @Transactional(readOnly = true) public Page<Btr> findAll(Pageable
	 *                         pageable) { log.debug("Request to get all Btrs");
	 *                         Page<Btr> result =
	 *                         btrRepository.findAll(pageable); return result; }
	 */

	/**
	 * NEW get all the btrs.
	 * 
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<Btr> findAll(Pageable pageable) {
		log.debug("Request to get all Btrs");
		Page<Btr> result = btrRepository.finByAssigned_toOrEmployeeIsCurrentUser(pageable);
		return result;
	}

	/**
	 * NEW get all the btrs in status "Initiated".
	 * 
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<Btr> findAllBtrInitiated(Pageable pageable) {
		log.debug("Request to get all Btrs");
		Page<Btr> result = btrRepository.findAllBtrInitiated(pageable);
		return result;
	}
	
	/*
	 * get all btrs for admin
	 */
	@Transactional(readOnly = true)
	public Page<Btr> findAllBtrAdmin(Pageable pageable) {
		log.debug("Request to get all Btrs");
		Page<Btr> result = btrRepository.findAllBtrAdmin(pageable);
		return result;
	}
	
	/**
	 * get one btr by id.
	 * 
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public Btr findOne(Long id) {
		log.debug("Request to get Btr : {}", id);
		Btr btr = btrRepository.findOne(id);
		btr.getExpenses().size();
		return btr;
	}

	/**
	 * delete the btr by id.
	 */
	public void delete(Long id) {
		log.debug("Request to delete Btr : {}", id);
		btrRepository.delete(id);
		btrSearchRepository.delete(id);
	}

	/**
	 * search for the btr corresponding to the query.
	 */
	@Transactional(readOnly = true)
	public List<Btr> search(String query) {

		log.debug("REST request to search Btrs for query {}", query);
		return (List<Btr>) StreamSupport
				.stream(btrSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());

	}

}
