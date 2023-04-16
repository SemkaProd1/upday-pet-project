package com.upday.service

import com.upday.entity.Article
import com.upday.repository.ArticleRepository
import com.upday.repository.AuthorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ArticleService(
    @Autowired private val articleRepository: ArticleRepository,
    @Autowired private val authorRepository: AuthorRepository
) {

    fun createArticle(article: Article): Article {
        return articleRepository.save(article)
    }

    fun updateArticle(id: Long, updatedArticle: Article): Article? {
        val article = getArticleById(id)
        article?.header = updatedArticle.header
        article?.shortDescription = updatedArticle.shortDescription
        article?.text = updatedArticle.text
        article?.publishDate = updatedArticle.publishDate
        article?.authors = updatedArticle.authors
        article?.keywords = updatedArticle.keywords
        if (article != null) {
            articleRepository.save(article)
        }
        return article
    }

    fun deleteArticle(id: Long) {
        articleRepository.deleteById(id)
    }

    fun getArticleById(id: Long): Article? {
        return articleRepository.findById(id)
            .orElseThrow { throw NoSuchElementException("Article with ID $id not found") }
    }

    fun getArticlesByAuthorName(author: String): List<Article> {
        return articleRepository.findByAuthorsContaining(author)
    }

    fun getArticlesByPeriod(fromDate: LocalDate, toDate: LocalDate): List<Article> {
        return articleRepository.findByPublishDateBetween(fromDate, toDate)
    }

    fun getArticlesByKeyword(keyword: String): List<Article> {
        return articleRepository.findByKeywordsContaining(keyword)
    }
}
