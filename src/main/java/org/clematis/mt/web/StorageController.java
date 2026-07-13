package org.clematis.mt.web;

import java.nio.file.Paths;
import java.util.List;

import org.clematis.storage.client.StorageApiClient;
import org.clematis.storage.client.dto.FileMetadata;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

    private static final String PATH_SEPARATOR = "/";
    private static final char PATH_SEPARATOR_CHAR = '/';

    private final StorageApiClient storageApiClient;

    public StorageController(StorageApiClient storageApiClient) {
        this.storageApiClient = storageApiClient;
    }

    /**
     * Upload a file to: mt/{entityName}[/{extraPath}]
     * @param entityName - the name of the entity to save files for, for instance, 'commodities' or 'accounts'
     * @param entityId - the id of the entity to save files for, for instance, the id of the commodity or account.
     * @param file - the file to upload
     * @param extraPath - an optional path to append to the entity root directory
     */
    @PostMapping("/{root}/{entityName}/{entityId}/files")
    public ResponseEntity<Void> uploadFile(
        @PathVariable("root") String root,
        @PathVariable("entityName") String entityName,
        @PathVariable("entityId") String entityId,
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "extraPath", required = false) String extraPath
    ) {

        String directory = buildDirectory(root, entityName, entityId, extraPath);
        List<FileMetadata> files = storageApiClient.getFilesByPath(directory);
        for (FileMetadata old : files) {
            String id = extractId(old);
            storageApiClient.deleteFile(id);
        }
        storageApiClient.uploadFile(file, directory);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Get the list of file names for a project under: mt/{entityName}[/{extraPath}] recursively
     * @param entityName - the name of the entity to get files for, for instance, 'commodities' or 'accounts'
     * @param entityId - the id of the entity to get files for, for instance, the id of the commodity or account.
     * @param extraPath - an optional path to append to the entity root directory
     */
    @GetMapping("/{root}/{entityName}/{entityId}/files")
    public ResponseEntity<List<FileMetadata>> listFiles(
        @PathVariable("root") String root,
        @PathVariable("entityName") String entityName,
        @PathVariable("entityId") String entityId,
        @RequestParam(value = "extraPath", required = false) String extraPath
    ) {
        String directory = buildDirectory(root, entityName, entityId, extraPath);
        List<FileMetadata> files = storageApiClient.getFilesByPath(directory);
        return ResponseEntity.ok(files);
    }

    /**
     * Download the main file for an entity under: mt/{entityName}/{entityId}
     * @param entityName - the name of the entity to get files for, for instance, 'commodities' or 'accounts'
     * @param entityId - the id of the entity to get files for, for instance, the id of the commodity or account.
     */
    @GetMapping("/{root}/{entityName}/{entityId}")
    public ResponseEntity<byte[]> downloadFile(
        @PathVariable("root") String root,
        @PathVariable("entityName") String entityName,
        @PathVariable("entityId") String entityId,
        @RequestParam(value = "extraPath", required = false) String extraPath
    ) {
        String directory = buildDirectory(root, entityName, entityId, extraPath);
        List<FileMetadata> files = storageApiClient.getFilesByPath(directory);
        // todo: for money tracker support many files in one directory, i.e. galleries
        if (!files.isEmpty()) {
            String id = extractId(files.get(0));
            byte[] body = storageApiClient.getFile(id).getBody();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{root}/{entityName}/{entityId}")
    public ResponseEntity<byte[]> deleteFile(
        @PathVariable("root") String root,
        @PathVariable("entityName") String entityName,
        @PathVariable("entityId") String entityId,
        @RequestParam(value = "extraPath", required = false) String extraPath
    ) {
        String directory = buildDirectory(root, entityName, entityId, extraPath);
        List<FileMetadata> files = storageApiClient.getFilesByPath(directory);
        if (!files.isEmpty()) {
            String id = extractId(files.get(0));
            storageApiClient.deleteFile(id);
        }
        return ResponseEntity.ok().build();
    }

    private static String extractId(FileMetadata fileMetadata) {
        String downloadUrl = fileMetadata.getDownloadUrl();
        return downloadUrl.substring(downloadUrl.lastIndexOf(PATH_SEPARATOR) + 1);
    }

    private String buildDirectory(String root, String entityName, String entityId, String extraPath) {
        StringBuilder sb = new StringBuilder();
        sb.append(root)
            .append(PATH_SEPARATOR_CHAR)
            .append(entityName)
            .append(PATH_SEPARATOR_CHAR)
            .append(entityId);
        if (extraPath != null && !extraPath.isBlank()) {
            if (!extraPath.startsWith(PATH_SEPARATOR)) {
                sb.append(PATH_SEPARATOR_CHAR);
            }
            sb.append(extraPath);
        }
        return Paths.get(sb.toString()).normalize().toString();
    }
}
