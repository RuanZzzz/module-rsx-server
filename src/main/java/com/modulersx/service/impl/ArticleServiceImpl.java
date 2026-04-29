package com.modulersx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.modulersx.domain.dto.ArticleSaveDTO;
import com.modulersx.domain.po.ArticlePO;
import com.modulersx.domain.vo.ArticleVO;
import com.modulersx.exception.BizException;
import com.modulersx.repository.ArticleMapper;
import com.modulersx.service.ArticleService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    public List<ArticleVO> listArticles() {
        return articleMapper.selectList(new LambdaQueryWrapper<ArticlePO>()
                        .orderByDesc(ArticlePO::getId))
                .stream()
                .map(this::toVO)
                .toList();
    }

    @Override
    public List<ArticleVO> listPublishedArticles() {
        return articleMapper.selectList(new LambdaQueryWrapper<ArticlePO>()
                        // 前台门户只展示已发布文章，避免草稿内容被公开访问。
                        .eq(ArticlePO::getStatus, "published")
                        .orderByDesc(ArticlePO::getId))
                .stream()
                .map(this::toVO)
                .toList();
    }

    @Override
    public ArticleVO getArticle(Long id) {
        return toVO(requireArticle(id));
    }

    @Override
    public ArticleVO getPublishedArticle(Long id) {
        ArticlePO article = requireArticle(id);
        if (!"published".equals(article.getStatus())) {
            throw new BizException(404, "article not found");
        }
        return toVO(article);
    }

    @Override
    public ArticleVO createArticle(ArticleSaveDTO dto) {
        validate(dto);
        ArticlePO article = toPO(dto, null);
        articleMapper.insert(article);
        return toVO(article);
    }

    @Override
    public ArticleVO updateArticle(Long id, ArticleSaveDTO dto) {
        validate(dto);
        ArticlePO article = requireArticle(id);
        article.setTitle(dto.getTitle());
        article.setSummary(dto.getSummary());
        article.setContent(dto.getContent());
        article.setStatus(dto.getStatus());
        articleMapper.updateById(article);
        return toVO(article);
    }

    @Override
    public void deleteArticle(Long id) {
        ArticlePO article = requireArticle(id);
        articleMapper.deleteById(article.getId());
    }

    private void validate(ArticleSaveDTO dto) {
        if (!StringUtils.hasText(dto.getTitle())) {
            throw new BizException(400, "article title cannot be blank");
        }
        if (!StringUtils.hasText(dto.getContent())) {
            throw new BizException(400, "article content cannot be blank");
        }
        if (!StringUtils.hasText(dto.getStatus())) {
            throw new BizException(400, "article status cannot be blank");
        }
    }

    private ArticlePO requireArticle(Long id) {
        ArticlePO article = articleMapper.selectById(id);
        if (article == null) {
            throw new BizException(404, "article not found");
        }
        return article;
    }

    private ArticlePO toPO(ArticleSaveDTO dto, Long id) {
        ArticlePO article = new ArticlePO();
        article.setId(id);
        article.setTitle(dto.getTitle());
        article.setSummary(dto.getSummary());
        article.setContent(dto.getContent());
        article.setStatus(dto.getStatus());
        return article;
    }

    private ArticleVO toVO(ArticlePO po) {
        return new ArticleVO(po.getId(), po.getTitle(), po.getSummary(), po.getContent(), po.getStatus());
    }
}
