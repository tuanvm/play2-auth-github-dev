This is your new Play 2.1 application
=====================================

This file will be packaged with your application, when using `play dist`.

How to use this module
1, run: play publish-local => to public this module to local

2, add "play2-auth-github-module" % "play2-auth-github-module_2.10" % "1.0-SNAPSHOT" to Build.scala in your project (with 2.10 is your play version => re-check your play version if needed)

3, create your github app, config callbackURL to your callback URL (example: http://localhost:9000/callback/github)

4, add these lines to application.conf (and test.conf if needed), change these value properly
github.id=your app id
github.secret=your app secret
github.callbackURL="http://localhost:9000/callback/github"
github.scope=(for example:user)

5, create social config trait (must extends Controller)
trait SocialConfig extends Controller with GithubConfig {
    //Github configuration
    val ghId: String = Play.current.configuration.getString("github.id").getOrElse("")
    val ghSecret: String = Play.current.configuration.getString("github.secret").getOrElse("")
    val ghCallbackURL: String = Play.current.configuration.getString("github.callbackURL").getOrElse("")
    val ghScope: String = Play.current.configuration.getString("github.scope").getOrElse("")
  }

6, let your controller which handle login/register with github connect extends SocialConfig, and with GithubConnect (because SocialConfig extends Controller, so you no need to extends Controller anymore)
object SocialConnect extends SocialConfig with GithubConnect{...}


7, click login/register with github button, redirect user to Redirect(getGithubAuthorizeUrl)

8, implement a callback function to handle callbackURL, github will return a code or error (example: user denied), exchange code to get access token
val accessToken = getGithubAccessToken(code)

9. use this access token to get user info
val githubUser = getGithubUser(accessToken) will return JsValue

10. do your business logic and logged user in
