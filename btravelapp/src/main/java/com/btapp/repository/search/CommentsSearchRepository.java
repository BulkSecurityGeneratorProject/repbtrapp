package com.btapp.repository.search;

import com.btapp.domain.Comments;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Comments entity.
 */
public interface CommentsSearchRepository extends ElasticsearchRepository<Comments, Long> {
}
