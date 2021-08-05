package com.lister.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lister.errorhandling.ServiceException;
import com.lister.model.api.dto.GitCLICommitDto;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.lister.errorhandling.ErrorCodeImpl.NOT_FOUND_ERROR;
import static com.lister.errorhandling.ErrorCodeImpl.SERVICE_UNAVAILABLE_ERROR;

@Slf4j
@ApplicationScoped
public class GitHubCLIClient {
    private final char START_TEXT_CHAR = 2;
    private final char END_TEXT_CHAR = 3;
    private static final String TEMP_FOLDER_PREFIX = "commit-lister";

    @Inject
    ObjectMapper objectMapper;
    public List<GitCLICommitDto> getCommitHistory(final String repositoryOwner, final String repositoryName)
        throws IOException {
        String[] command = getCloneCommand(getRepositoryUrl(repositoryOwner,
                                                            repositoryName));

        Path tempDirPath = Files.createTempDirectory(TEMP_FOLDER_PREFIX);
        File tempDirFile = new File(tempDirPath.toString());

        ProcessBuilder pb = new ProcessBuilder(command).directory(tempDirFile)
                                                       .redirectErrorStream(true);
        Process process = pb.start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            log.error("Get clone interrupted. Possible timeout?", e);
        }

        File tempRepoDirFile = new File(tempDirPath + "/" + repositoryName);
        tempRepoDirFile.deleteOnExit();

        if (!tempRepoDirFile.exists()) {
            tempDirFile.delete();
            throw new ServiceException(NOT_FOUND_ERROR);
        }

        // match hard-coded maxCount used in the GitHubApi
        String[] logCommand = getLogCommand(500);
        pb = new ProcessBuilder(logCommand)
            .directory(tempRepoDirFile)
            .redirectErrorStream(true);

        try {
            process = pb.directory(tempRepoDirFile)
                        .start();
        } catch (IOException ex) {
            log.error("Exception during git log:", ex);
            tempDirFile.delete();
            throw new ServiceException(SERVICE_UNAVAILABLE_ERROR);
        }

        StringBuilder result = new StringBuilder(80);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream())))
        {
            result.append("[");
            while (true) {
                String line = in.readLine();
                if (line == null)
                    break;
                result.append(line);
            }
        } finally {
            tempDirFile.delete();
        }

        List<GitCLICommitDto> gitCLICommitDtoList = new ArrayList<>();
        if (result.length() > 1) {
            // Make result a valid json format so we can parse it with jackson
            result.replace(result.length() - 1, result.length(), "]");
            final String escapedResult = escapeCommitMessageQuotationMarks(result.toString().toCharArray());
            try {
                objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
                gitCLICommitDtoList = objectMapper.readValue(escapedResult, new TypeReference<>() {});
            } catch (JsonParseException ex) {
                log.error("Exception: ", ex);
                throw new ServiceException(SERVICE_UNAVAILABLE_ERROR);
            }
        } else {
            gitCLICommitDtoList = Collections.emptyList();
        }

        return gitCLICommitDtoList;
    }

    protected String[] getCloneCommand(final String repositoryUrl) {
        return new String[] {"git", "clone", "-n", repositoryUrl};
    }

    protected String[] getLogCommand(final int maxCount) {
        return new String[] {"git", "log", "-" + maxCount, "--pretty=format:{\"sha\": \"%H\"," +
                                                           "\"author\": \"%an <%ae>\"," +
                                                           "\"date\":   \"%aD\"," +
                                                           "\"message\": \"" + START_TEXT_CHAR + "%B"
                                                           + END_TEXT_CHAR + "\"},"};
    }

    protected String escapeCommitMessageQuotationMarks(final char[] result) {
        boolean isReadingTextSequence = false;
        for (int i = 0; i < result.length; i++) {
            if (isReadingTextSequence && result[i] == '"') {
                result[i] = '\'';
                continue;
            }
            if (result[i] == START_TEXT_CHAR) {
                isReadingTextSequence = true;
                result[i] = ' ';
                continue;
            }
            if (result[i] == END_TEXT_CHAR) {
                isReadingTextSequence = false;
                result[i] = ' ';
            }
        }
        return String.valueOf(result);
    }

    protected String getRepositoryUrl(final String owner, final String name) {
        String GITHUB_URL_PREFIX = "git@github.com:";
        return GITHUB_URL_PREFIX + owner + "/" + name;
    }
}
