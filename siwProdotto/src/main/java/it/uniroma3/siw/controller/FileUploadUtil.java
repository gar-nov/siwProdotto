package it.uniroma3.siw.controller;

import java.io.*;
import java.nio.file.*;

import org.springframework.web.multipart.MultipartFile;

// classe ausiliaria per l'upload delle foto.
public class FileUploadUtil {

	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		//Path directory dove salvare le foto (vedi addAnimale() di animaleController)
		Path uploadPath = Paths.get(uploadDir);

		// se non esiste, crea una cartella dove mettere le foto
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + fileName, ioe);
		}
	}
}
