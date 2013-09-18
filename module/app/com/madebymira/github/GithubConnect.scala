package com.madebymira.github

import play.api.{ Logger, Application, Plugin, Routes }
import play.api.libs._
import play.api.libs.ws.WS
import scala.concurrent._
import scala.concurrent.duration._
import play.api.libs.json.JsValue

trait GithubConnect {
    self: GithubConfig =>

    def getGithubAuthorizeUrl: String = {
        return "https://github.com/login/oauth/authorize?scope=" + ghScope + "&client_id=" + ghId + "&redirect_uri=" + ghCallbackURL
    }

    def getGithubAccessToken(code: String): String = {
        val duration = Duration(10, SECONDS)
        val future: Future[play.api.libs.ws.Response] = WS.url("https://github.com/login/oauth/access_token").withHeaders("Accept" -> "application/json").post(Map("client_id" -> Seq(ghId), "client_secret" -> Seq(ghSecret), "code" -> Seq(code)))
        val response = Await.result(future, duration)

        val accessJson = response.json
        val accessToken = (accessJson \ "access_token").toString.filter(char => char != '\"')
        return accessToken
    }

    /**
     * Gets the github user.
     *
     * @param accessToken the access token
     * @return the github user
     */
    def getGithubUser(accessToken: String): JsValue = {
        val duration = Duration(10, SECONDS)
        val future: Future[play.api.libs.ws.Response] = WS.url("https://api.github.com/user?access_token=" + accessToken).get
        val response = Await.result(future, duration)
        return response.json
    }
}