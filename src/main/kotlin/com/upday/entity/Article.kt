package com.upday.entity

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import jakarta.persistence.*

@Entity
@Table(name = "articles")
data class Article(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Article ID", example = "1", name = "id")
    var id: Long? = null,

    @Schema(description = "Header of the article", example = "Ukraine government changing")
    var header: String,

    @Schema(description = "Short description of article", example = "It was a new election in Ukraine")
    var shortDescription: String,

    @Schema(description = "Main article context", example = "The last election was in...")
    var text: String,

    @Schema(description = "Article publication date", example = "2023-16-4")
    var publishDate: LocalDate,

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "article_keywords",
        joinColumns = [JoinColumn(name = "article_id")],
        inverseJoinColumns = [JoinColumn(name = "keyword_id")],
    )
    @Schema(description = "Article keywords for search", example = "Ukraine, election")
    var keywords: MutableSet<Keyword> = HashSet(),

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "article_authors",
        joinColumns = [JoinColumn(name = "article_id")],
        inverseJoinColumns = [JoinColumn(name = "author_id")]
    )
    @Schema(description = "Authors of the article", example = "Ilya Veduta")
    var authors: MutableSet<Author> = HashSet(),
)