package com.nagarro.bankappbackend.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nagarro.bankappbackend.dto.UserDto;
import com.nagarro.bankappbackend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.valid = false")
    List<User> findUsersWithInvalidStatus();
	
	default List<UserDto> findInvalidUsersAsDto() {
        return findUsersWithInvalidStatus().stream()
            .map(user -> new UserDto(
                user.getUserId(),
                user.getFirstName(),
                user.getEmail()
                ))
            .collect(Collectors.toList());
    }
	
    Optional<User> findByEmail(String email);
}
