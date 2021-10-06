package com.artineer.spring_study_week2.service;

import com.artineer.spring_study_week2.domain.Article;
import com.artineer.spring_study_week2.domain.ArticleRepository;
import com.artineer.spring_study_week2.exception.ApiException;
import com.artineer.spring_study_week2.exception.Asserts;
import com.artineer.spring_study_week2.vo.ApiCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

//비지니스 로직을 담는 곳.
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Long save(Article request) {
        return articleRepository.save(request).getId();
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiCode.DATA_IS_NOT_FOUND, "article is not found"));
    }

    @Transactional
    public Article update(Article request) {
        Article article = this.findById(request.getId());

        article.update(request.getTitle(), request.getContent());

        return article;
    }

    public void delete(Long id) {
        Article article = this.findById(id);
        articleRepository.deleteById(id); // = articleRepository.delete(article);
    }
}
