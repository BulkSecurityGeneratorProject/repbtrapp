package com.btapp.repository.search;

import com.btapp.domain.Historybtr;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Historybtr entity.
 */
public interface HistorybtrSearchRepository extends ElasticsearchRepository<Historybtr, Long> {
}
