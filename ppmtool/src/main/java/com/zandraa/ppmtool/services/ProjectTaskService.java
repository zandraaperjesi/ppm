package com.zandraa.ppmtool.services;

import com.zandraa.ppmtool.domain.Backlog;
import com.zandraa.ppmtool.domain.Project;
import com.zandraa.ppmtool.domain.ProjectTask;
import com.zandraa.ppmtool.exceptions.ProjectNotFoundException;
import com.zandraa.ppmtool.repositories.BacklogRepository;
import com.zandraa.ppmtool.repositories.ProjectRepository;
import com.zandraa.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    private BacklogRepository backlogRepository;
    private ProjectTaskRepository projectTaskRepository;
    private ProjectRepository projectRepository;

    @Autowired
    public ProjectTaskService(BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository, ProjectRepository projectRepository) {
        this.backlogRepository = backlogRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectRepository = projectRepository;
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
        try {

            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;

            projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3);
            }

            if (projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
                projectTask.setStatus("TO DO");
            }

            return projectTaskRepository.save(projectTask);
        } catch (Exception e) {
            throw new ProjectNotFoundException("Project not found");
        }
    }

    public Iterable<ProjectTask> findBacklogById(String id) {

        Project project = projectRepository.findByProjectIdentifier(id);

        if(project == null) {
            throw new ProjectNotFoundException("Project with id " + id + " does not exist");
        }

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);

    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {

         Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
         if(backlog == null) {
             throw new ProjectNotFoundException("Project doesn't exist");
         }

         ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
         if(projectTask == null) {
             throw new ProjectNotFoundException("Project task does not exist");
         }

         if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
             throw  new ProjectNotFoundException("Project task does not exist on this project");
         }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id) {

        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id) {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
        projectTaskRepository.delete(projectTask);
    }
}
