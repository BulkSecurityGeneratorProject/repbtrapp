package com.btapp.repository.search;

import com.btapp.domain.Btr;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Btr entity.
 */
public interface BtrSearchRepository extends ElasticsearchRepository<Btr, Long> {
}
