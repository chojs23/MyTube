package com.mytube.upload;

import com.mytube.domain.Member;
import com.mytube.domain.MemberImage;
import com.mytube.domain.Video;
import com.mytube.domain.VideoFile;
import com.mytube.service.MemberService;
import com.mytube.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UploadVideo {
    private final VideoService videoService;

    @Value("${custom.path.videos}")
    private String videoDir;

    public String getFullPath(String filename) {
        return videoDir + filename;
    }

    public VideoFile uploadVideoFile(MultipartFile multipartFile) throws IOException
    {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));


        return new VideoFile(originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}