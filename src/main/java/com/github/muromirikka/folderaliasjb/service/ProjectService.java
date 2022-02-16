package com.github.muromirikka.folderaliasjb.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProjectService {

    private final Project myProject;
    private Map<String,Map<String,Object>> config = new HashMap<>();
    public ProjectService(Project project) throws IOException {
        myProject = project;
        init(project);
    }

    private void init(Project project) throws IOException {
        String path = String.valueOf(Paths.get(project.getBasePath()+"/","folder-alias.json"));
        File file = new File(path);
        if(!file.exists()){
            try{
                file.createNewFile();
                FileWriter write = new FileWriter(path,false);
                write.write("{}",0,"{}".length());
                write.flush();
                write.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
           byte[] allBytes = Files.readAllBytes(file.toPath());
           String stringJson = new String(allBytes, StandardCharsets.UTF_8);
            HashMap map = new Gson().fromJson(stringJson, HashMap.class);
           setConfig(map);
        }

    }

    public Project getProject(){
        return myProject;
    }

    public void setOnceConfig(String key,Map<String,Object> config) throws IOException {
        this.config.put(key,config);
        this.saveFile();

    }

    private void saveFile() throws IOException {
        String stringJson = new GsonBuilder().setPrettyPrinting().create().toJson(this.config);
        String path = String.valueOf(Paths.get(this.myProject.getBasePath()+"/","folder-alias.json"));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path,false), StandardCharsets.UTF_8));

        writer.write(stringJson,0,stringJson.length());
        writer.flush();
        writer.close();

    }

    public void setConfig(Map<String,Map<String,Object>> config){
        this.config = config;
    }

    public Map<String,Map<String,Object>> getConfig(){
        return this.config;
    }

}
