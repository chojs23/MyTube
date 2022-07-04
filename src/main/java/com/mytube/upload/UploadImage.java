package com.mytube.upload;


import com.mytube.domain.Member;
import com.mytube.domain.MemberImage;
import com.mytube.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UploadImage {
    private final MemberService memberService;
    @Value("${custom.path.memberImage}")
    private String memberImageDir;

    public String getFullPath(String filename) {
        log.info("fullPath = " + memberImageDir + filename);
        return memberImageDir + filename;
    }

    public MemberImage uploadMemberImage(MultipartFile multipartFile,Long id) throws IOException
    {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        log.info("storeFileName = "+ storeFileName);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        Member member = memberService.findMember(id);

        return new MemberImage(member,originalFilename, storeFileName,getFullPath(storeFileName));
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
