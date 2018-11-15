package com.zandraa.ppmtool.web;

import com.zandraa.ppmtool.domain.Project;
import com.zandraa.ppmtool.services.MapValidationService;
import com.zandraa.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private ProjectService projectService;
    private MapValidationService mapValidationService;

    @Autowired
    public ProjectController(ProjectService projectService, MapValidationService mapValidationService) {
        this.projectService = projectService;
        this.mapValidationService = mapValidationService;
    }

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {

        ResponseEntity<?> errorMap = mapValidationService.validateBindingResult(result);
        if(errorMap != null) return errorMap;

        Project saved = projectService.saveOrUpdate(project);
        return new ResponseEntity<Project>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId) {
        Project project = projectService.findProjectByIdentifier(projectId);
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects() {
        return projectService.findAllProjects();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId) {
        projectService.delete(projectId);

        return new ResponseEntity<String>(projectId + " deleted", HttpStatus.OK);
    }
}
