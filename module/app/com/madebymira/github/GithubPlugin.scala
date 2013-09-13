package com.madebymira.github

import play.api.{ Logger, Application, Plugin, Routes }
import play.api.libs._
import play.api.libs.ws.WS
import scala.concurrent._
import scala.concurrent.duration._
import play.api.libs.json.JsValue

/**
 * The Class GithubPlugin.
 */
class GithubPlugin(application: Application) extends Plugin {
    val GITHUB_ID: String = "github.id";
    val GITHUB_SECRET: String = "github.secret";
    val GITHUB_CALLBACK_URL: String = "github.callbackURL"
    val GITHUB_SCOPE: String = "github.scope"

    lazy val id: String = application.configuration.getString(GITHUB_ID).getOrElse(null);
    lazy val secret: String = application.configuration.getString(GITHUB_SECRET).getOrElse(null);
    lazy val callbackURL: String = application.configuration.getString(GITHUB_CALLBACK_URL).getOrElse(null);
    lazy val scope: String = application.configuration.getString(GITHUB_SCOPE).getOrElse(null);

    /* (non-Javadoc)
     * @see play.api.Plugin#onStart()
     */
    override def onStart() {
        val configuration = application.configuration;
        // you can now access the application.conf settings, including any custom ones you have added

        Logger.info("GithubPlugin has started");
    }

    /**
     * Gets the login url.
     *
     * @param scope the scope
     * @return the login url
     */
    def getLoginUrl: String = {
        return "https://github.com/login/oauth/authorize?scope=" + scope + "&client_id=" + id + "&redirect_uri=" + callbackURL
    }

    /**
     * Gets the access token.
     *
     * @param code the code
     * @return the access token
     */
    def getAccessToken(code: String): String = {
        val duration = Duration(10, SECONDS)
        val future: Future[play.api.libs.ws.Response] = WS.url("https://github.com/login/oauth/access_token").withHeaders("Accept" -> "application/json").post(Map("client_id" -> Seq(id), "client_secret" -> Seq(secret), "code" -> Seq(code)))
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