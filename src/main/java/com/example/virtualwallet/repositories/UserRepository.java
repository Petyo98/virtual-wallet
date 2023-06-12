package com.example.virtualwallet.repositories;

import com.example.virtualwallet.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User getById(int id);

    List<User> getByUsername(String username);

    Page<User> findAllByEmailLikeOrPhoneNumberLikeOrUsernameLike(String searchByEmail,
                                                                 String searchByNumber,
                                                                 String searchByUsername,
                                                                 Pageable pageable);
    boolean existsByUsername(String username);

    Page<User> findAll(Pageable pageable);

    Page<User> findAllByIdentifiedIs(boolean identified, Pageable pageable);

    Page<User> findAllByFirstNameLikeOrLastNameLike(String firstName, String lastName, Pageable pageable);

}
