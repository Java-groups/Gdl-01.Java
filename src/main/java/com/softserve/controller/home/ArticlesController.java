package com.softserve.controller.home;




//import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;


import com.softserve.service.CategoryService;
import com.softserve.service.SubCategoryService;
import com.softserve.model.Constants;


@Controller
public class ArticlesController {

    private CategoryService categoryService;
    private SubCategoryService subCategoryService;
    
    public ArticlesController(CategoryService categoryService, SubCategoryService subCategoryService){
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
    }
        
    @GetMapping("/article")
    public String getArticles(Model model){

        //model.addAttribute("categories",Constants.CATEGORIES);
        this.categoryService.loadCategories(model);
        this.subCategoryService.loadSubcategories(model);
        model.addAttribute("teams",Constants.TEAM);
        
        return "configurations/mostcommented"; 
    }

}
