package com.springproject.myjavaspringproject.controller;

import com.springproject.myjavaspringproject.model.ResponseObject;
import com.springproject.myjavaspringproject.repository.ProductRepository;
import com.springproject.myjavaspringproject.services.ImageStorageService;
import com.springproject.myjavaspringproject.services.IsStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

//Recive file from client
@RestController
@RequestMapping("api/v1/fileUpload/")
public class FileUploadController {
    //Inject Storage Image service
    private final IsStorageService isStorageService;

    public FileUploadController(IsStorageService isStorageService) {
        this.isStorageService = isStorageService;
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> upLoadFile(@RequestParam MultipartFile file) {
        try {
            // save file to a folder by use the service
            String generatedFileName = isStorageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "upload image success", generatedFileName)
            );
            // when have image in folder upload => how to open image in web  browser
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }

    @GetMapping("files/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable("fileName") String fileName) {
        try {
            byte[] bytes = isStorageService.readFileContent(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG).body(bytes);
        } catch (Exception exception) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("files")
    public ResponseEntity<ResponseObject> getDetailFiles(){
        try {
            List<String> urls = isStorageService.loadAll().map( path -> {
                //convert file name to url( send request readDetailFile)
                String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class , "readDetailFile",
                        path.getFileName().toString()).build().toUri().toString();
                return  urlPath ;
            }).collect(Collectors.toList());

           return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok" , "List file successly",urls)
            );
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed" , "List file failed",new String[] {})
            );
        }


    }
}
