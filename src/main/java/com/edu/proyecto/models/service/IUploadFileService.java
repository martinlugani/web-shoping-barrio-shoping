package com.edu.proyecto.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {

	public Resource load(String filename, Long id) throws MalformedURLException;

	public String copy(MultipartFile file, Long comercioId) throws IOException;
	
	public boolean delete(String filename);
}
