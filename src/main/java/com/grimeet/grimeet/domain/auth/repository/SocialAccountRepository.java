package com.grimeet.grimeet.domain.auth.repository;

import com.grimeet.grimeet.domain.auth.entity.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {

}
