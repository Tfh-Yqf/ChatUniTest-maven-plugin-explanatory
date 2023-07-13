package com.example.spring.Service;

import com.example.spring.pojo.Entity.ProjectEntity;
import com.example.spring.pojo.Entity.ReturnCode;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ProjectService {

    public ReturnCode start(ProjectEntity projectEntity) throws IOException;
    public ReturnCode getStatus(ProjectEntity projectEntity) throws IOException;
}
