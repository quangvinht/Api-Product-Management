package com.springproject.myjavaspringproject.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IsStorageService {
    public String storeFile ( MultipartFile file );
    public Stream<Path> loadAll() ; // load all file inside a folder
    public byte[] readFileContent(String fileName); // a array about file detail can watch on browser
    public void deleteAllFiles();




}
