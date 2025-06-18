package tech.zerofiltre.blog.infra.entrypoints.rest.video;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.web.bind.annotation.*;
import tech.zerofiltre.blog.domain.error.*;
import tech.zerofiltre.blog.infra.providers.api.vimeo.*;

@Slf4j
@RestController
@RequestMapping("/vimeo")
@RequiredArgsConstructor
public class VimeoController {

    private final VimeoProvider vimeoService;

    @PostMapping("/init")
    public String init(@RequestParam long size, @RequestParam String name) throws VideoUploadFailedException {
        return vimeoService.init(size, name);
    }
    @PostMapping("/delete")
    public String deleteVideo(@RequestParam String video) throws ZerofiltreException {
        return vimeoService.delete(video);
    }
}
