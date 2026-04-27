package com.modulersx.controller;

import com.modulersx.common.response.ApiResponse;
import com.modulersx.domain.dto.ArticleSaveDTO;
import com.modulersx.domain.vo.ArticleVO;
import com.modulersx.service.ArticleService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ApiResponse<List<ArticleVO>> listArticles() {
        return ApiResponse.success(articleService.listArticles());
    }

    @GetMapping("/detail")
    public ApiResponse<ArticleVO> getArticle(@RequestParam Long id) {
        return ApiResponse.success(articleService.getArticle(id));
    }

    @PostMapping("/create")
    public ApiResponse<ArticleVO> createArticle(@RequestBody ArticleSaveDTO dto) {
        return ApiResponse.success(articleService.createArticle(dto));
    }

    @PostMapping("/update")
    public ApiResponse<ArticleVO> updateArticle(@RequestParam Long id, @RequestBody ArticleSaveDTO dto) {
        return ApiResponse.success(articleService.updateArticle(id, dto));
    }

    @PostMapping("/delete")
    public ApiResponse<Void> deleteArticle(@RequestParam Long id) {
        articleService.deleteArticle(id);
        return ApiResponse.success(null);
    }
}
