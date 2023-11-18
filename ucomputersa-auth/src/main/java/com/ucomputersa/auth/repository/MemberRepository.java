package com.ucomputersa.auth.repository;

import com.ucomputersa.auth.domain.entity.MemberEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository<MemberEntity, Integer> {

    MemberEntity findByEmail(String email);
}
