package com.upday.repository

import com.upday.entity.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ArticleRepository : JpaRepository<Article, Long> {
    fun findByKeywordsContaining(keyword: String): List<Article>

    fun findByAuthorsContaining(author: String): List<Article>

    fun findByPublishDateBetween(fromDate: LocalDate, toDate: LocalDate): List<Article>

}