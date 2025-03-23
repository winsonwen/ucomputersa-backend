package com.ucomputersa.monolithic.repository;

import com.ucomputersa.monolithic.domain.Section;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SectionRepository extends MongoRepository<Section, String> {



}
