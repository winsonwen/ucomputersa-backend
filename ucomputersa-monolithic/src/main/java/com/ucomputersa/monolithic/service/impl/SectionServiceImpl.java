package com.ucomputersa.monolithic.service.impl;

import com.ucomputersa.monolithic.domain.Section;
import com.ucomputersa.monolithic.repository.SectionRepository;
import com.ucomputersa.monolithic.service.SectionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SectionServiceImpl implements SectionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SectionServiceImpl.class);

    private final SectionRepository sectionRepository;

    private final MongoTemplate mongoTemplate;

    public void saveSection(Section section) {
        try {
            sectionRepository.save(section);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Section> findAllSection() {
        List<Section> sections = mongoTemplate.findAll(Section.class);
        return sections;
    }

    public Section getSectionById(String sectionId) {
        Optional<Section> sections = sectionRepository.findById(sectionId);
        return sections.orElse(null);
    }

    public void deleteSection(String sectionId) {
        try {
            sectionRepository.deleteById(sectionId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
