package com.btapp.repository.search;

import com.btapp.domain.Btr;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data ElasticSearch repository for the Btr entity.
 */
public interface BtrSearchRepository extends MyElasticsearchRepository {
	
	/*@Query("select btr from Btr btr where "
    		+ " btr.status != 'Closed'"
    		+ " and ( btr.assigned_to.login = ?#{principal.username}"
    		+ " or btr.user.login = ?#{principal.username}"
    		+ " or btr.assigned_from.login = ?#{principal.username}"
    		+ " or btr.manager.login = ?#{principal.username}"
    		+ " or btr.supplier.login = ?#{principal.username} )")
    Page<Btr> search(Pageable pageable);
	*/
}
