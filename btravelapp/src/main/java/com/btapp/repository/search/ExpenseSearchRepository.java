package com.btapp.repository.search;

import com.btapp.domain.Expense;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Expense entity.
 */
public interface ExpenseSearchRepository extends ElasticsearchRepository<Expense, Long> {
}
