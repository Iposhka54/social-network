package com.iposhka.socialnetworkapi.repository;

import com.iposhka.socialnetworkapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
