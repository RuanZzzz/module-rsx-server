package com.modulersx.controller;

import com.modulersx.common.response.ApiResponse;
import com.modulersx.domain.vo.ArticleVO;
import com.modulersx.domain.vo.ToolVO;
import com.modulersx.service.ArticleService;
import com.modulersx.service.ToolService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicContentController {

    private final ToolService toolService;
    private final ArticleService articleService;

    public PublicContentController(ToolService toolService, ArticleService articleService) {
        this.toolService = toolService;
        this.articleService = articleService;
    }

    @GetMapping("/tools")
    public ApiResponse<List<ToolVO>> listTools() {
        return ApiResponse.success(toolService.listEnabledTools());
    }

    @GetMapping("/articles")
    public ApiResponse<List<ArticleVO>> listArticles() {
        return ApiResponse.success(articleService.listPublishedArticles());
    }

    @GetMapping("/articles/detail")
    public ApiResponse<ArticleVO> getArticle(@RequestParam Long id) {
        // 公开详情仍然只允许访问 published 状态的文章，避免草稿绕过管理端暴露。
        return ApiResponse.success(articleService.getPublishedArticle(id));
    }
}
