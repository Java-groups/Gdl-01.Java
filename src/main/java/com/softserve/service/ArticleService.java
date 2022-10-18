package com.softserve.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.softserve.model.Article;
import com.softserve.repository.IArticleRepository;

@Service
public class ArticleService {

	private IArticleRepository articleRepository;
	
	private SubCategoryService subCategoryService;
	
	public ArticleService(IArticleRepository articleRepository, SubCategoryService subCategoryService) {
	
		this.articleRepository = articleRepository;
		this.subCategoryService = subCategoryService;
	}
	
	public void loadArticleDescription(Model model, Integer idArticle) {
		this.subCategoryService.loadContentMain(model);
		final Optional<Article> articleOptional = this.articleRepository.findById(idArticle);
		
		if(articleOptional.isPresent()) {
			Article article = articleOptional.get();
			article.setImage(String.format("https://firebasestorage.googleapis.com/v0/b/sport-hub-11400.appspot.com/o/%s", article.getImage()));
			model.addAttribute("article", article);	
		}
		
		
	}
    
}
