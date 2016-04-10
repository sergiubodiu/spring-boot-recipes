package io.pivotal.apac;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

    Collection<UserPreference> findByUserId(String userId);

}