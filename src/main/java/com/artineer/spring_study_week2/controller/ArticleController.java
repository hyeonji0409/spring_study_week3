package com.artineer.spring_study_week2.controller;

import com.artineer.spring_study_week2.domain.Article;
import com.artineer.spring_study_week2.dto.ArticleDto;
import com.artineer.spring_study_week2.dto.Response;
import com.artineer.spring_study_week2.exception.ApiException;
import com.artineer.spring_study_week2.service.ArticleService;
import com.artineer.spring_study_week2.vo.ApiCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping
    public Response<Long> post(@RequestBody ArticleDto.ReqPost request) {
        return Response.ok(articleService.save(Article.of(request)));
    }

    @GetMapping("/{id}")
    public Response<ArticleDto.Res> get(@PathVariable Long id) {
        return Response.ok(ArticleDto.Res.of(articleService.findById(id)));
    }

    @PutMapping("/{id}")
    public Response<Object> put(@PathVariable Long id, @RequestBody ArticleDto.ReqPut request) {
        return Response.ok(ArticleDto.Res.of(articleService.update(Article.of(request, id))));
    }

    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        return Response.ok();
    }
}