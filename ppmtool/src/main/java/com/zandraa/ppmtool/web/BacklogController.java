package com.zandraa.ppmtool.web;

import com.zandraa.ppmtool.domain.Project;
import com.zandraa.ppmtool.domain.ProjectTask;
import com.zandraa.ppmtool.services.MapValidationService;
import com.zandraa.ppmtool.services.ProjectTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    private ProjectTaskService projectTaskService;
    private MapValidationService mapValidationService;

    public BacklogController(ProjectTaskService projectTaskService, MapValidationService mapValidationService) {
        this.projectTaskService = projectTaskService;
        this.mapValidationService = mapValidationService;
    }

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask,
                                            BindingResult result, @PathVariable String backlog_id) {

        ResponseEntity<?> errorMap = mapValidationService.validateBindingResult(result);
        if(errorMap != null) return errorMap;

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask);

        return new ResponseEntity<>(projectTask1, HttpStatus.CREATED);
    }

}
