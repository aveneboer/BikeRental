package nl.anouk.bikerental.controllers;


import nl.anouk.bikerental.models.File;
import nl.anouk.bikerental.repositories.FileRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {
    private final FileRepository fileRepository;

    public FileController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostMapping("/single/uploadDb")
    public ResponseEntity<String> singleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadfile = new File();
        //file.getOriginalFilename();
        uploadfile.setFilename("newfile"); //omzetten naar Dto of filename get original filename
        uploadfile.setDocfile(file.getBytes());

        fileRepository.save(uploadfile);

        return ResponseEntity.ok("Your upload was succesful");
    }

    @GetMapping("/downloadFromDb/{fileId}")
    public ResponseEntity<byte[]> downloadSingleFile(@PathVariable Long fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new RuntimeException());
        byte[] docFile = file.getDocfile();
        if (docFile == null) {
            throw new RuntimeException("there is no file yet.");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("attachment", "file" + file.getFilename() + ".png");
        headers.setContentLength(docFile.length);
        return new ResponseEntity<>(docFile, headers, HttpStatus.OK);
    }
}

