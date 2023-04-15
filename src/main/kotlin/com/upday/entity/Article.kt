package com.upday.entity

import java.time.LocalDate
import jakarta.persistence.*

@Entity
@Table(name = "articles")
data class Article(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var header: String,
    var shortDescription: String,
    var text: String,
    var publishDate: LocalDate,

    @ManyToMany
    @JoinTable(
        name = "article_keywords",
        joinColumns = [JoinColumn(name = "article_id")],
        inverseJoinColumns = [JoinColumn(name = "keyword_id")]
    )
    var keywords: MutableSet<Keyword> = HashSet(),

    @ManyToMany
    @JoinTable(
        name = "article_authors",
        joinColumns = [JoinColumn(name = "article_id")],
        inverseJoinColumns = [JoinColumn(name = "author_id")]
    )
    var authors: MutableSet<Author> = HashSet(),

)