package com.upday.controller

import com.upday.entity.Article
import com.upday.service.ArticleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/articles")
class ArticleController(private val articleService: ArticleService) {
    private val logger: Logger = LoggerFactory.getLogger(ArticleController::class.java)

    @Operation(summary = "Create an article")
    @ApiResponse(responseCode = "201", description = "Article created successfully")
    @PostMapping
    @PreAuthorize("hasRole('Admin') or hasRole('SuperAdmin')")
    fun createArticle(@RequestBody article: Article): ResponseEntity<Article> {
        val createdArticle = articleService.createArticle(article)
        return ResponseEntity(createdArticle, HttpStatus.CREATED)
    }

    @Operation(summary = "Update an article")
    @ApiResponse(responseCode = "200", description = "Article updated successfully")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "404", description = "Article not found"),
            ApiResponse(responseCode = "400", description = "Invalid article ID supplied")
        ]
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('SuperAdmin')")
    fun updateArticle(
        @Parameter(
            description = "ID of the article to update",
            required = true,
            schema = Schema(type = "integer", format = "int64")
        )
        @PathVariable id: Long,
        @RequestBody article: Article
    ): ResponseEntity<Article> {
        logger.info("Updating article with ID $id")
        val updatedArticle = articleService.updateArticle(id, article)
        return if (updatedArticle != null) {
            logger.info("Updated article with ID $id")
            ResponseEntity(updatedArticle, HttpStatus.OK)
        } else {
            logger.warn("Article with ID $id not found")
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @Operation(summary = "Delete an article")
    @ApiResponse(responseCode = "204", description = "Article deleted successfully")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "404", description = "Article not found"),
            ApiResponse(responseCode = "400", description = "Invalid article ID supplied")
        ]
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('SuperAdmin')")
    fun deleteArticle(
        @Parameter(
            description = "ID of the article to delete",
            required = true,
            schema = Schema(type = "integer", format = "int64")
        )
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        return try {
            articleService.deleteArticle(id)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null)
        }
    }

    @Operation(summary = "Get an article by ID")
    @ApiResponse(responseCode = "200", description = "Found the article")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "404", description = "Article not found"),
            ApiResponse(responseCode = "400", description = "Invalid article ID supplied")
        ]
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('SuperAdmin')")
    fun getArticleById(
        @Parameter(
            description = "ID of the article to get",
            required = true,
            schema = Schema(type = "integer", format = "int64")
        )
        @PathVariable id: Long,
    ): ResponseEntity<Article> {
        return try {
            val article = articleService.getArticleById(id)
            if (article != null) {
                ResponseEntity(article, HttpStatus.OK)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null)
        }
    }

    @GetMapping("/articles/period")
    @Operation(summary = "Get all articles between a given period")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Found the articles"),
            ApiResponse(responseCode = "404", description = "Articles not found for the given period")
        ]
    )
    fun getArticlesByPeriod(
        @Parameter(
            description = "Start date of the period to search for",
            required = true,
            example = "2022-01-01"
        )
        @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) fromDate: LocalDate,

        @Parameter(
            description = "End date of the period to search for",
            required = true,
            example = "2022-12-31"
        )
        @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) toDate: LocalDate
    ): ResponseEntity<List<Article>> {
        return try {
            val articles = articleService.getArticlesByPeriod(fromDate, toDate)
            resultResponseEntity(articles)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null)
        }
    }

    @GetMapping("/articles/author/{authorName}")
    @Operation(summary = "Get all articles for a given author name")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Found the articles for the author"),
            ApiResponse(responseCode = "404", description = "Articles not found for the author")
        ]
    )
    fun getArticlesByAuthorName(
        @Parameter(
            description = "Name of the author to search for",
            required = true,
            example = "John Smith"
        )
        @PathVariable authorName: String
    ): ResponseEntity<List<Article>> {
        return try {
            val articles = articleService.getArticlesByAuthorName(authorName)
            resultResponseEntity(articles)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null)
        }
    }

    @GetMapping("/keyword/{keyword}")
    @Operation(
        summary = "Get articles by keyword",
        description = "Get all articles containing a given keyword"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Found the articles containing the keyword"),
            ApiResponse(responseCode = "404", description = "Articles not found containing the keyword")
        ]
    )
    fun getArticlesByKeyword(
        @Parameter(
            description = "Keyword to search for in the articles",
            required = true,
            example = "technology"
        )
        @PathVariable keyword: String
    ): ResponseEntity<List<Article>> {
        return try {
            val articles = articleService.getArticlesByKeyword(keyword)
            resultResponseEntity(articles)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null)
        }
    }

    private fun resultResponseEntity(articles: List<Article>): ResponseEntity<List<Article>> {
        return if (articles.isNotEmpty()) {
            ResponseEntity.ok(articles)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}