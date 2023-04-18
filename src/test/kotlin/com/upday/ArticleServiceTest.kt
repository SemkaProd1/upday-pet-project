package com.upday.service

import com.upday.entity.Article
import com.upday.entity.Author
import com.upday.entity.Keyword
import com.upday.repository.ArticleRepository
import com.upday.repository.AuthorRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.util.*

@SpringBootTest
@ExtendWith(MockitoExtension::class)
class ArticleServiceTests {

    @Autowired
    private lateinit var articleService: ArticleService

    @Mock
    private lateinit var articleRepository: ArticleRepository

    @Mock
    private lateinit var authorRepository: AuthorRepository

    @Test
    fun testCreateArticle() {
        val article = Article(
            header = "Test header",
            shortDescription = "Test short description",
            text = "Test text",
            publishDate = LocalDate.now(),
            authors = mutableSetOf(Author(name = "Test author")),
            keywords = mutableSetOf(Keyword(name = "Test keyword"))
        )
        `when`(articleRepository.save(article)).thenReturn(article)

        val createdArticle = articleService.createArticle(article)

        assertNotNull(createdArticle.id)
        assertEquals(article.header, createdArticle.header)
        assertEquals(article.shortDescription, createdArticle.shortDescription)
        assertEquals(article.text, createdArticle.text)
        assertEquals(article.publishDate, createdArticle.publishDate)
        assertEquals(article.authors, createdArticle.authors)
        assertEquals(article.keywords, createdArticle.keywords)
    }

    @Test
    fun testUpdateArticle() {
        val article = Article(
            id = 1,
            header = "Test header",
            shortDescription = "Test short description",
            text = "Test text",
            publishDate = LocalDate.now(),
            authors = mutableSetOf(Author(name = "Test author")),
            keywords = mutableSetOf(Keyword(name = "Test keyword"))
        )
        `when`(articleRepository.findById(1)).thenReturn(Optional.of(article))

        val updatedArticle = Article(
            id = 1,
            header = "New header",
            shortDescription = "New short description",
            text = "New text",
            publishDate = LocalDate.now(),
            authors = mutableSetOf(Author(name = "New author")),
            keywords = mutableSetOf(Keyword(name = "New keyword"))
        )
        `when`(articleRepository.save(article)).thenReturn(updatedArticle)

        val result = articleService.updateArticle(1, updatedArticle)

        assertNotNull(result)
        assertEquals(updatedArticle.header, result?.header)
        assertEquals(updatedArticle.shortDescription, result?.shortDescription)
        assertEquals(updatedArticle.text, result?.text)
        assertEquals(updatedArticle.publishDate, result?.publishDate)
        assertEquals(updatedArticle.authors, result?.authors)
        assertEquals(updatedArticle.keywords, result?.keywords)
    }

    @Test
    fun testDeleteArticle() {
        articleService.deleteArticle(1)
    }

    @Test
    fun testGetArticleById() {
        val article = Article(
            id = 1,
            header = "Test header",
            shortDescription = "Test short description",
            text = "Test text",
            publishDate = LocalDate.now(),
            authors = mutableSetOf(Author(name = "Test author")),
            keywords = mutableSetOf(Keyword(name = "Test keyword"))
        )
        `when`(articleRepository.findById(1)).thenReturn(Optional.of(article))

        val result = articleService.getArticleById(1)

        assertNotNull(result)
        assertEquals(article.id, result?.id)
        assertEquals(article.header, result?.header)
        assertEquals(article.shortDescription, result?.shortDescription)
        assertEquals(article.text, result?.text)
        assertEquals(article.publishDate, result?.publishDate)
        assertEquals(article.authors, result?.authors)
        assertEquals(article.keywords, result?.keywords)

        verify(articleRepository, times(1)).findById(1)
    }

    @Test
    fun testGetArticlesByAuthorName() {
        val authorName = "Test author"
        val article1 = Article(
            id = 1,
            header = "Test header 1",
            shortDescription = "Test short description 1",
            text = "Test text 1",
            publishDate = LocalDate.now(),
            authors = mutableSetOf(Author(name = authorName)),
            keywords = mutableSetOf(Keyword(name = "Test keyword 1"))
        )
        val article2 = Article(
            id = 2,
            header = "Test header 2",
            shortDescription = "Test short description 2",
            text = "Test text 2",
            publishDate = LocalDate.now(),
            authors = mutableSetOf(Author(name = authorName)),
            keywords = mutableSetOf(Keyword(name = "Test keyword 2"))
        )
        `when`(articleRepository.findByAuthorsContaining(authorName)).thenReturn(listOf(article1, article2))

        val result = articleService.getArticlesByAuthorName(authorName)

        assertEquals(listOf(article1, article2), result)
    }

    @Test
    fun testGetArticlesByPeriod() {
        val fromDate = LocalDate.of(2023, 1, 1)
        val toDate = LocalDate.of(2023, 4, 16)
        val article1 = Article(
            id = 1,
            header = "Test header 1",
            shortDescription = "Test short description 1",
            text = "Test text 1",
            publishDate = LocalDate.of(2023, 2, 1),
            authors = mutableSetOf(Author(name = "Test author 1")),
            keywords = mutableSetOf(Keyword(name = "Test keyword 1"))
        )
        val article2 = Article(
            id = 2,
            header = "Test header 2",
            shortDescription = "Test short description 2",
            text = "Test text 2",
            publishDate = LocalDate.of(2023, 3, 1),
            authors = mutableSetOf(Author(name = "Test author 2")),
            keywords = mutableSetOf(Keyword(name = "Test keyword 2"))
        )
        `when`(articleRepository.findByPublishDateBetween(fromDate, toDate)).thenReturn(listOf(article1, article2))

        val result = articleService.getArticlesByPeriod(fromDate, toDate)

        assertEquals(listOf(article1, article2), result)
    }

    @Test
    fun testGetArticlesByKeyword() {
        val keyword = "Test keyword"
        val article1 = Article(
            id = 1,
            header = "Test header 1",
            shortDescription = "Test short description 1",
            text = "Test text 1",
            publishDate = LocalDate.now(),
            authors = mutableSetOf(Author(name = "Test author 1")),
            keywords = mutableSetOf(Keyword(name = keyword))
        )
        val article2 = Article(
            id = 2,
            header = "Test header 2",
            shortDescription = "Test short description 2",
            text = "Test text 2",
            publishDate = LocalDate.now(),
            authors = mutableSetOf(Author(name = "Test author 2")),
            keywords = mutableSetOf(Keyword(name = keyword))
        )
        `when`(articleRepository.findByKeywordsContaining(keyword)).thenReturn(listOf(article1, article2))

        val result = articleService.getArticlesByKeyword(keyword)

        assertEquals(listOf(article1, article2), result)
    }
}

