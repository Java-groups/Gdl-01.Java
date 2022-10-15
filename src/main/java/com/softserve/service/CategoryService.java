package com.softserve.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.softserve.model.Category;
import com.softserve.repository.ICategoryRepository;

@Service
public class CategoryService {

	private ICategoryRepository categoryRepository;
	
	public CategoryService(ICategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	//Searching parent categories
	public List<Category> findAll(){
		return this.categoryRepository.findByIdParentCategoryIsNull();
	}
	//Searching for subCategories
	public List<Category> findByCategoryParent(Integer idCategoruParent){
		return this.categoryRepository.findByIdParentCategory(idCategoruParent);
	}
	
}
