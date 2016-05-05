package com.btapp.repository.search;

import com.btapp.domain.Expense_type;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Expense_type entity.
 */
public interface Expense_typeSearchRepository extends ElasticsearchRepository<Expense_type, Long> {
}
