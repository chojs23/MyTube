package com.mytube.Controller;


import com.mytube.Controller.form.VideoCreateForm;
import com.mytube.domain.Member;
import com.mytube.domain.Post;
import com.mytube.domain.Video;
import com.mytube.domain.VideoFile;
import com.mytube.dto.postDto;
import com.mytube.dto.videoDto;
import com.mytube.service.VideoService;
import com.mytube.upload.UploadVideo;
import com.mytube.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class VideoController {
    private final VideoService videoService;
    private final UploadVideo uploadVideo;

    @GetMapping("/videos")
    public String videos(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model,
                         @RequestParam Optional<Integer> page, @RequestParam Optional<String> sortBy) {
        model.addAttribute("loginMember", loginMember);
        Page<Video> videoPage = videoService.getVideoPage(PageRequest.of(
                page.orElse(0),
                10,
                Sort.Direction.DESC, sortBy.orElse("createdDate")
        ));
        Page<videoDto> videos = videoPage.map(v -> new videoDto(v.getId(), v.getTitle(),v.getAttachFile(),v.getMember(), v.getCreatedDate(), v.getLastModifiedDate()));

        log.info("videos = " + videos);
        model.addAttribute("videos", videos);

        return "/videos/videos";
    }

    @GetMapping("/videos/search")
    public String search(String keyword, Model model,
                         @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                         @RequestParam Optional<Integer> page,@RequestParam Optional<String> sortBy) {
        Page<Video> searchList = videoService.search(keyword, PageRequest.of(
                page.orElse(0),
                10,
                Sort.Direction.DESC,sortBy.orElse("createdDate")));
        Page<videoDto> searchListDto = searchList.map(v -> new videoDto(v.getId(), v.getTitle(), v.getAttachFile(), v.getMember(), v.getCreatedDate(), v.getLastModifiedDate()));
        model.addAttribute("searchList", searchListDto);
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("keyword", keyword);
        return "videos/videos-search";
    }


    @GetMapping("/videos/new")
    public String createVideo(Model model){
        model.addAttribute("form", new VideoCreateForm());

        return "videos/uploadVideoForm";
    }


    @PostMapping("/videos/new")
    public String createVideo(@Valid @ModelAttribute("form") VideoCreateForm form, BindingResult result, Model model,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) throws IOException {
        if (loginMember==null){
            return "redirect:/home";
        }

        if (form.getAttachFile().isEmpty()) {
            result.addError(new FieldError("form","attachFile",null,false,null,null,"파일 필수"));
        }

        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "videos/uploadVideoForm";
        }

        VideoFile videoFile = uploadVideo.uploadVideoFile(form.getAttachFile());

        Video video = new Video(form.getTitle(), videoFile, loginMember);
        videoService.uploadVideo(video);

        return "redirect:/videos";

    }

    @GetMapping("/videos/{id}/detail")
    public String videoDetail(@PathVariable Long id, Model model,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Video video = videoService.getVideo(id);


        videoDto videoDto = new videoDto(video);
        model.addAttribute("videoDto", videoDto);
        model.addAttribute("loginMember", loginMember);

        return "/videos/videoDetail";

    }

    @ResponseBody
    @GetMapping("/videos/{filename}")
    public Resource showVideo(@PathVariable String filename) throws
            MalformedURLException {
        log.info("dasdsadsadsadsadasdasdasdasdas");
        return new UrlResource("file:" + uploadVideo.getFullPath(filename));
    }
}
