package com.softserve.controller.article.api;

import com.softserve.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/article")
public class ArticleRestController {

    @Autowired
    private ArticleService articleService;

    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveArticle(@RequestParam Map<String, Object> json, @RequestParam("articleImage") MultipartFile articleImage){
        return this.articleService.saveArticle(json, articleImage);
    }

    @GetMapping("/load")
    public ResponseEntity<?> readAllSubCategory(){
        return this.articleService.loadNewArticleContentApi();
    }
}
