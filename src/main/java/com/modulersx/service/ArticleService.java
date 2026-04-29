package com.modulersx.service;

import com.modulersx.domain.dto.ArticleSaveDTO;
import com.modulersx.domain.vo.ArticleVO;
import java.util.List;

public interface ArticleService {

    List<ArticleVO> listArticles();

    List<ArticleVO> listPublishedArticles();

    ArticleVO getArticle(Long id);

    ArticleVO getPublishedArticle(Long id);

    ArticleVO createArticle(ArticleSaveDTO dto);

    ArticleVO updateArticle(Long id, ArticleSaveDTO dto);

    void deleteArticle(Long id);
}
