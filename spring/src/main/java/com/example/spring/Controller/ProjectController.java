package com.example.spring.Controller;

import com.example.spring.Service.ProjectService;
import com.example.spring.pojo.Entity.ProjectEntity;
import com.example.spring.pojo.Entity.ReturnCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    ProjectService projectService;
    @PostMapping("/start")
    public ReturnCode start(@RequestBody ProjectEntity projectEntity) throws IOException {
        ReturnCode res = new ReturnCode();

        if(projectEntity.email==null){
            res.excuteFail(11,"未找到用户");
        }else if(projectEntity.name==null||projectEntity.name.length()==0){
            res.excuteFail(12,"请填写工程名");
        }else if(projectEntity.dirName==null||projectEntity.dirName.length()==0){
            res.excuteFail(12,"上传的文件名为空");
        }else {
            projectEntity.setStatus("processing");
            res = projectService.start(projectEntity);
        }

        return res;
    }

    @PostMapping("/getStatus")
    public ReturnCode getStatus(@RequestBody ProjectEntity projectEntity) throws IOException {
        ReturnCode res = new ReturnCode();

        if(projectEntity.email==null||projectEntity.email.length()==0){
            res.excuteFail(13,"email not found");
        }else{
            res = projectService.getStatus(projectEntity);
        }

        return res;
    }
}
