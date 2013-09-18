package com.madebymira.github

import play.api.mvc._

trait GithubConfig {
    self: Controller =>

    val ghId: String
    val ghSecret: String
    val ghCallbackURL: String
    val ghScope: String
}