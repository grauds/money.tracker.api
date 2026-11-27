package org.clematis.mt.rest;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.clematis.storage.client.dto.FileMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tngtech.keycloakmock.api.TokenConfig;

/**
 * REST API tests for StorageController endpoints covering branch conditions.
 * Uses @MockBean StorageApiClient from base class.
 */
@SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:MultipleStringLiterals"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StorageControllerTests extends HateoasApiTests {

    private static final String BASE_PATH = "/api/storage/mt/commodities/1";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    /**
     * Sets up MockMvc for multipart requests.
     */
    private MockMvc getMockMvc() {
        if (mockMvc == null) {
            mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        }
        return mockMvc;
    }

    /**
     * Verifies downloading a file when no files exist returns 404 Not Found.
     * Covers the else branch: files.isEmpty() -> ResponseEntity.notFound().build().
     */
    @Test
    public void testDownloadFileNotFound() {
        when(storageApiClient.getFilesByPath(anyString())).thenReturn(Collections.emptyList());

        TestRestTemplate rt = getRestTemplateWithHalMessageConverter();
        ResponseEntity<byte[]> response = rt.exchange(
            BASE_PATH,
            HttpMethod.GET,
            new HttpEntity<>(createHeaders()),
            byte[].class
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Verifies listing files with extraPath parameter.
     * Covers the buildDirectory branch: extraPath != null && !blank.
     */
    @Test
    public void testListFilesWithExtraPath() {
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setDownloadUrl("/mt/commodities/1/subdir/file.png");
        when(storageApiClient.getFilesByPath(anyString())).thenReturn(
            Collections.singletonList(fileMetadata)
        );

        TestRestTemplate rt = getRestTemplateWithHalMessageConverter();

        ResponseEntity<List<FileMetadata>> response = rt.exchange(
            BASE_PATH + "/files?extraPath=subdir",
            HttpMethod.GET,
            new HttpEntity<>(createHeaders()),
            new ParameterizedTypeReference<>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().size());
    }

    /**
     * Verifies listing files without extraPath parameter.
     * Covers the buildDirectory else branch: extraPath is null.
     */
    @Test
    public void testListFilesWithoutExtraPath() {
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setDownloadUrl("/mt/commodities/1/file.png");
        when(storageApiClient.getFilesByPath(anyString())).thenReturn(
            Collections.singletonList(fileMetadata)
        );

        TestRestTemplate rt = getRestTemplateWithHalMessageConverter();

        ResponseEntity<List<FileMetadata>> response = rt.exchange(
            BASE_PATH + "/files",
            HttpMethod.GET,
            new HttpEntity<>(createHeaders()),
            new ParameterizedTypeReference<>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().size());
    }

    /**
     * Verifies uploading a file with extraPath that does not start with '/'.
     * Covers the buildDirectory branch: !extraPath.startsWith("/").
     */
    @Test
    public void testUploadFileWithExtraPathNoLeadingSlash() throws Exception {
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setDownloadUrl("/mt/commodities/1/subdir/old.png");
        when(storageApiClient.getFilesByPath(anyString())).thenReturn(
            Collections.singletonList(fileMetadata)
        );
        when(storageApiClient.uploadFile(
            org.mockito.ArgumentMatchers.any(org.springframework.web.multipart.MultipartFile.class),
            anyString()
        )).thenReturn(null);

        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.png",
            MediaType.IMAGE_PNG_VALUE,
            "test content".getBytes(StandardCharsets.UTF_8)
        );

        getMockMvc().perform(
            multipart(BASE_PATH + "/files?extraPath=subdir")
                .file(file)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mock.getAccessToken(
                    TokenConfig.aTokenConfig().build()
                ))
        ).andExpect(status().isCreated());
    }

    /**
     * Verifies uploading a file with extraPath that starts with '/'.
     * Covers the buildDirectory branch: extraPath.startsWith("/") -> no extra separator.
     */
    @Test
    public void testUploadFileWithExtraPathLeadingSlash() throws Exception {
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setDownloadUrl("/mt/commodities/1/subdir/old.png");
        when(storageApiClient.getFilesByPath(anyString())).thenReturn(
            Collections.singletonList(fileMetadata)
        );
        when(storageApiClient.uploadFile(
            org.mockito.ArgumentMatchers.any(org.springframework.web.multipart.MultipartFile.class),
            anyString()
        )).thenReturn(null);

        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.png",
            MediaType.IMAGE_PNG_VALUE,
            "test content".getBytes(StandardCharsets.UTF_8)
        );

        getMockMvc().perform(
            multipart(BASE_PATH + "/files?extraPath=/subdir")
                .file(file)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mock.getAccessToken(
                    TokenConfig.aTokenConfig().build()
                ))
        ).andExpect(status().isCreated());
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(mock.getAccessToken(
            TokenConfig.aTokenConfig().build()
        ));
        return headers;
    }
}
