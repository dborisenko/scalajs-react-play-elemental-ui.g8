package client

import java.time.Clock

import chandu0101.scalajs.react.components.elementalui._
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.vdom.html_<^._
import model.Todo
import model.Todo.TodoId

object TodoListComponent {

  final case class Props(
    snapshot: StateSnapshot[List[Todo]],
    createOrUpdate: Todo => Callback,
    delete: TodoId => Callback,
    clock: Clock
  )

  final case class State(
    createOrUpdate: Option[Todo]
  )

  final class Backend(scope: BackendScope[Props, State]) {

    private val colNames = Row()(
      Col(xs = "1/5")(Card()("ID")),
      Col(xs = "1/5")(Card()("Description")),
      Col(xs = "1/5")(Card()("Is Completed")),
      Col(xs = "1/5")(Card()("Created At")),
      Col(xs = "1/5")()
    )

    private def withSpanTitle(string: String) = <.span(^.title := string, string)

    private def renderRow(todo: Todo): VdomNode = Row(key = todo.id.toString)(
      Col(xs = "1/5")(withSpanTitle(todo.id.toString)),
      Col(xs = "1/5")(withSpanTitle(todo.description)),
      Col(xs = "1/5")(withSpanTitle(todo.isCompleted.toString)),
      Col(xs = "1/5")(withSpanTitle(todo.createdAt.toString)),
      Col(xs = "1/5")(
        Button(onClick = openDialog(todo))("Update"),
        Button(onClick = handleDelete(todo.id))("Delete")
      )
    )

    private def open(todo: Todo) = scope.modState(_.copy(createOrUpdate = Some(todo)))

    private def handleDelete(todoId: TodoId): ReactEvent => Callback = _ => scope.props.flatMap(_.delete(todoId))

    private def openDialog(todo: Todo): ReactEvent => Callback = _ => open(todo)

    private def renderRows(todos: List[Todo]): List[VdomNode] = todos.sortBy(_.createdAt).map(renderRow)

    def render(p: Props, s: State): VdomElement = <.div(
      <.div(
        colNames,
        <.div(renderRows(p.snapshot.value): _*)
      ),
      Button(onClick = openDialog(Todo.newTodo(p.clock)))("Create"),
      s.createOrUpdate.fold[VdomElement](<.div())(todo => TodoEditorComponent(TodoEditorComponent.Props(
        snapshot = StateSnapshot(todo)(s => scope.modState(_.copy(createOrUpdate = Some(s)))),
        submit = s => scope.props.flatMap(_.createOrUpdate(s)),
        close = scope.modState(_.copy(createOrUpdate = None))
      )))
    )
  }

  private val component = ScalaComponent
    .builder[Props]("TodoListComponent")
    .initialState(State(None))
    .renderBackend[Backend]
    .build

  def apply(props: Props): Unmounted[Props, State, Backend] = component(props)
}
