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
    
    //Optional<User>
    List<User> findAllByAuthorities(Authority authority);  // adaugat 11.03.2016

    // find all employees for a manager
    @Query("SELECT u FROM User u where u.idManager = ?#{principal.username}")
    Optional<User> findAllEmployeesForManager(String idManager);
    
    @Override
    void delete(User t);

}
