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

    private val colNames = <.thead(
      <.tr(
        <.th("ID"),
        <.th("Description"),
        <.th("Is Completed"),
        <.th("Created At"),
        <.th()
      )
    )

    private def withSpanTitle(string: String) = <.span(^.title := string, string)

    private def renderRow(todo: Todo): VdomNode = <.tr(
      <.td(withSpanTitle(todo.id.toString)),
      <.td(withSpanTitle(todo.description)),
      <.td(withSpanTitle(todo.isCompleted.toString)),
      <.td(withSpanTitle(todo.createdAt.toString)),
      <.td(
        Button(onClick = openDialog(todo), `type` = ButtonType.LINK)("Update"),
        Button(onClick = handleDelete(todo.id), `type` = ButtonType.LINK_DELETE)("Delete")
      )
    )

    private def open(todo: Todo) = scope.modState(_.copy(createOrUpdate = Some(todo)))

    private def handleDelete(todoId: TodoId): ReactEvent => Callback = _ => scope.props.flatMap(_.delete(todoId))

    private def openDialog(todo: Todo): ReactEvent => Callback = _ => open(todo)

    private def renderRows(todos: List[Todo]): VdomNode = <.tbody(todos.sortBy(_.createdAt).map(renderRow): _*)

    def render(p: Props, s: State): VdomElement = <.div(
      Table()(
        colNames,
        renderRows(p.snapshot.value)
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
