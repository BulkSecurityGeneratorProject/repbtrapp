package com.btapp.repository;

import com.btapp.domain.Comments;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Comments entity.
 */
public interface CommentsRepository extends JpaRepository<Comments,Long> {

}
