package controllers

import javax.inject._

import play.api.{Environment, Mode}
import play.api.http.HttpEntity
import play.api.libs.ws.WSClient
import play.api.mvc._

@Singleton
class StaticResourcesController @Inject()(
    cc: ControllerComponents,
    wsClient: WSClient,
    assets: Assets,
    environment: Environment) extends AbstractController(cc) {

  // This is the controller method being called from the outside.
  def serve(file: String): Action[AnyContent] = predefinedAssetServingFunction(file)

  // This is a val. Therefore we determine the way we serve the static javascript (eg preact ) app only ONCE at startup
  // of the application.
  // It's a tiny optimization, but otherwise we'd have to do if .. else on every request in the controller method.
  private val predefinedAssetServingFunction = if (environment.mode == Mode.Dev) {
    reverseProxyResourcesFromDevServerRunningOnOtherPort
  } else {
    serveFilesFromAssetsDirectoryStatically
  }

  private def reverseProxyResourcesFromDevServerRunningOnOtherPort = (file: String) => Action.async(parse.anyContent) {
    // That's basically a reverse proxy for the preact server. Simply start the preact devserver. Then start
    // the play app. And enjoy some productivity.
    import scala.concurrent.ExecutionContext.Implicits._
    request: Request[AnyContent] =>
      wsClient
        .url("http://localhost:8080" + request.path)
        .withFollowRedirects(false)
        .withMethod(request.method)
        .withVirtualHost("localhost:9000")
        .addHttpHeaders(flattenMultiMap(request.headers.toMap): _*)
        .addQueryStringParameters(request.queryString.mapValues(_.head).toSeq: _*)
        .stream().map { response =>
        // If there's a content length, send that, otherwise return the body chunked
          response.headers.get("Content-Length") match {
            case Some(Seq(length)) =>
              Status(response.status)
                .sendEntity(HttpEntity.Streamed(response.bodyAsSource, Some(length.toLong), Some(response.contentType)))
                .withHeaders(flattenMultiMap(response.headers): _*)
            case _ =>
              Status(response.status)
                .chunked(response.bodyAsSource)
                .as(response.contentType)
                .withHeaders(flattenMultiMap(response.headers): _*)
          }
      }.recover {
        case exception => InternalServerError(
          s"""
             <h1>Oops. Error while serving assets in development mode.</h1>
             <pre>$exception</pre>
             <p><strong>Why?</strong> During development we are expecting a server serving the javascript assets at localhost:8080.
             Make sure this server is up and running. </p>
             <p>For preact the command is: <strong>preact watch</strong> in the frontend directory. This then starts up the preact
             development server. And we can then reverse proxy everything to make development a breeze.</p>
             <p>Note: This error happens only in development mode.</p>
          """).as("text/html")
      }
  }

  // This is used in production. Stuff is deployed to directory '/public' and delivered by the default assets
  // controller of Play.
  private def serveFilesFromAssetsDirectoryStatically = (file: String) => {
    // If the requested resource exists then stream it out... otherwise return index.html.
    // This is needed for html5 history support. Preact, Angular and all modern frameworks handle this automatically.
    if (environment.resource("public/" + file).isDefined) {
      assets.at(file)
    } else {
      assets.at("index.html")
    }

  }

  private def flattenMultiMap(headers: Map[String, Seq[String]]): Seq[(String, String)] = for {
    (name, values) <- headers.toSeq
    value <- values
  } yield (name, value)

}