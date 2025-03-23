package com.ucomputersa.monolithic.controller;

import com.ucomputersa.monolithic.domain.Section;
import com.ucomputersa.monolithic.service.impl.SectionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/0/section")
public class SectionController {
    private final SectionServiceImpl sectionService;

    @Autowired
    public SectionController(SectionServiceImpl sectionService) {
        this.sectionService = sectionService;
    }

    // Create a new section
    @PostMapping
    public ResponseEntity<Section> createSection(@RequestBody Section section) {
        sectionService.saveSection(section);
        return ResponseEntity.ok(section);
    }

    // Get all sections
    @GetMapping
    public ResponseEntity<List<Section>> getSections() {
        List<Section> sections = null;
        try {
            sections = sectionService.findAllSection();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        if (Objects.isNull(sections)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sections);
    }

    @GetMapping("/{sectionId}")
    public ResponseEntity<Section> getSectionById(@PathVariable("sectionId") String sectionId) {
        Section section = sectionService.getSectionById(sectionId);
        if (Objects.nonNull(section)) {
            return ResponseEntity.ok(section);
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<HttpStatus> deleteSection(@PathVariable("sectionId") String sectionId) {
        if (Objects.nonNull(sectionId)) {
            sectionService.deleteSection(sectionId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
