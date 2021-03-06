package controller

import java.time.Clock
import javax.inject.{ Inject, Singleton }

import model.Todo
import model.Todo.TodoId
import org.slf4j.{ Logger, LoggerFactory }
import play.api.libs.circe.Circe
import play.api.mvc.{ AbstractController, Action, AnyContent, ControllerComponents }

import scala.collection.concurrent.TrieMap
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class ApplicationController @Inject() (
  cc: ControllerComponents
)(implicit ec: ExecutionContext) extends AbstractController(cc) with Circe {

  private val logger: Logger = LoggerFactory.getLogger(getClass)

  private val todoStorage: TrieMap[TodoId, Todo] = new TrieMap[TodoId, Todo]()

  private val clock = Clock.systemUTC()
  Set(
    Todo(TodoId.newTodoId, "Hit the gym", isCompleted = false, clock.instant()),
    Todo(TodoId.newTodoId, "Pay bills", isCompleted = true, clock.instant()),
    Todo(TodoId.newTodoId, "Meet George", isCompleted = false, clock.instant()),
    Todo(TodoId.newTodoId, "Buy eggs", isCompleted = false, clock.instant()),
    Todo(TodoId.newTodoId, "Read a book", isCompleted = false, clock.instant()),
    Todo(TodoId.newTodoId, "Organize office", isCompleted = false, clock.instant())
  ).foreach(v => todoStorage.put(v.id, v))

  val healthcheck = Action {
    Ok("Ok")
  }

  val index = Action {
    logger.info(s"Got 'index' request. Returning HTML...")
    Ok(views.html.index())
  }

  def createOrUpdate: Action[Todo] = Action.async(circe.json[Todo]) { request =>
    val todo = request.body
    logger.info("Got 'createOrUpdate' request for Todo " + todo)
    Future(todoStorage.put(todo.id, todo)).map(_ => Ok(todo))
  }

  def delete(id: String): Action[AnyContent] = Action.async { _ =>
    logger.info("Got 'delete' request for todo id " + id)
    Future(todoStorage.remove(TodoId(id))).map(_ => Ok)
  }

  def list: Action[AnyContent] = Action.async { request =>
    logger.info("Got 'list' request")
    Future(todoStorage.readOnlySnapshot().values).map(Ok(_))
  }
}
