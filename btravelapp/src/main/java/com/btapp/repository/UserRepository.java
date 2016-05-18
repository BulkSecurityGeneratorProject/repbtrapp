package com.btapp.repository;

import com.btapp.domain.Authority;
import com.btapp.domain.Btr;
import com.btapp.domain.User;

import java.time.ZonedDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByLogin(String login);

    Optional<User> findOneById(Long userId);
    
    Optional<User> findAllByAuthorities(Authority authority);  // adaugat 11.03.2016

    // find all the managers
    //@Query("SELECT u.LOGIN FROM JHI_USER u join JHI_USER_AUTHORITY ua on u.ID = ua.USER_ID where ua.AUTHORITY_NAME = 'ROLE_MANAGER' ")
    /*@Query("SELECT user FROM jhi_user user join user.jhi_user_authority authorities "
    		+ "where authorities.AUTHORITY_NAME = 'ROLE_MANAGER' ")
    Optional<User> findManagers();
    */
    @Override
    void delete(User t);

}
