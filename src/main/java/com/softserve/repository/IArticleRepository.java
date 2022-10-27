package com.softserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.softserve.model.Article;

import java.util.List;

/**
 * 
 * @author José Castellanos
 * Repository for Article data retrieving
 */
@Repository
public interface IArticleRepository extends JpaRepository<Article, Integer>{

    /*
        Custom query for selecting Top 3 most commented articles
     */
    @Query(nativeQuery = true, value = "select a.id_article, title, description, image, a.id_app_user, is_commentable, a.status, a.creation_date,a.modification_date, description_html, id_team  from article a join \"comment\"  c on a.id_article = c.id_article group by a.id_article order by count(a.id_article) desc limit 3")
    List<Article> findByTop3MostCommented();

    /*
        Custom query for selecting Top 3 most liked articles
     */
    @Query(nativeQuery = true, value = "select a.id_article, title, description, image, a.id_app_user, is_commentable, a.status, a.creation_date,a.modification_date, description_html, id_team from article a join app_like al on a.id_article = al.id_article group by a.id_article order by count(a.id_article) desc limit 3")
    List<Article> findByTop3MostLiked();
}
