package com.edu.proyecto.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final static String UPLOADS_FOLDER = "uploads";
	private String pathComercio;

	@Override
	public Resource load(String filename, Long id) throws MalformedURLException {
		if (id != null) {
			pathComercio = "\\" + id;
		} else {
			pathComercio="\\imagenes";
		}


		Path path = getPath(filename);

		log.info("pathFoto: " + path);

		Resource resouce = new UrlResource(path.toUri());
		if (!resouce.exists() || !resouce.isReadable()) {
			throw new RuntimeException("Error al cargar" + path.toString());
		}

		return resouce;
	}

	private Path getPath(String filename) {
		// TODO Auto-generated method stub

		if (pathComercio != null) {
			return Paths.get(UPLOADS_FOLDER + pathComercio).resolve(filename).toAbsolutePath();
		}
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public String copy(MultipartFile file, Long idComercio) throws IOException {
		String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

		if (idComercio != null) {
			pathComercio = "\\" + idComercio;
		} else {
			pathComercio="\\imagenes";
		}

		Path rootPath = getPath(uniqueFilename);

		log.info("rootPath: " + rootPath);

		Files.copy(file.getInputStream(), rootPath);

		return uniqueFilename;
	}

	public boolean delete(String filename) {
		if (filename != null && filename.length() > 0) {
			Path rootPath = getPath(filename);
			File archivo = rootPath.toFile();

			if (archivo.exists() && archivo.canRead()) {
				if (archivo.delete()) {
					return true;
				}
			}
		}
		return false;
	}
}
