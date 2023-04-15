package com.upday.controller

import com.upday.entity.Article
import com.upday.service.ArticleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/articles")
class ArticleController(private val articleService: ArticleService) {

    @Operation(summary = "Create an article")
    @ApiResponse(responseCode = "201", description = "Article created successfully")
    @PostMapping
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
    fun updateArticle(
        @Parameter(
            description = "ID of the article to update",
            required = true,
            schema = Schema(type = "integer", format = "int64")
        )
        @PathVariable id: Long,
        @RequestBody article: Article
    ): ResponseEntity<Article> {
        val updatedArticle = articleService.updateArticle(id, article)
        return ResponseEntity(updatedArticle, HttpStatus.OK)
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
    fun deleteArticle(
        @Parameter(
            description = "ID of the article to delete",
            required = true,
            schema = Schema(type = "integer", format = "int64")
        )
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        articleService.deleteArticle(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
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
    fun getArticleById(
        @Parameter(
            description = "ID of the article to get",
            required = true,
            schema = Schema(type = "integer", format = "int64")
        )
        @PathVariable id: Long
    ): ResponseEntity<Article> {
        val article = articleService.getArticleById(id)
        return if (article != null) {
            ResponseEntity(article, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
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
        val articles = articleService.getArticlesByPeriod(fromDate, toDate)
        return resultResponseEntity(articles)
    }

//    @GetMapping("/articles/author/{authorName}")
//    @Operation(summary = "Get all articles for a given author name")
//    @ApiResponses(
//        value = [
//            ApiResponse(responseCode = "200", description = "Found the articles for the author"),
//            ApiResponse(responseCode = "404", description = "Articles not found for the author")
//        ]
//    )
//    fun getArticlesByAuthorName(
//        @Parameter(
//            description = "Name of the author to search for",
//            required = true,
//            example = "John Smith"
//        )
//        @PathVariable authorName: String
//    ): ResponseEntity<List<Article>> {
//        val articles = articleService.getArticlesByAuthorName(authorName)
//
//        return resultResponseEntity(articles)
//    }

    @GetMapping("/keyword/{keyword}")
    @Operation(
        summary = "Get articles by keyword",
        description = "Retrieves articles that contain the specified keyword in their keywords field"
    )
    @ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Articles not found for the keyword")
    fun getArticlesByKeyword(
        @Parameter(description = "Keyword to search for", required = true)
        @PathVariable keyword: String
    ): ResponseEntity<List<Article>> {
        val articles = articleService.getArticlesByKeyword(keyword)
        return resultResponseEntity(articles)
    }

    private fun resultResponseEntity(articles: List<Article>): ResponseEntity<List<Article>> {
        return if (articles.isNotEmpty()) {
            ResponseEntity.ok(articles)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}