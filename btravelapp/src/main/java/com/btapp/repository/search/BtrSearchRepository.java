package com.btapp.repository.search;

import com.btapp.domain.Btr;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data ElasticSearch repository for the Btr entity.
 */
public interface BtrSearchRepository extends MyElasticsearchRepository {
	
    //Page<Btr> mySearch(Pageable pageable);

	Page<Btr> search(QueryStringQueryBuilder queryStringQuery);
	
}
