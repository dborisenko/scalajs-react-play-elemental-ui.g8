package client

import chandu0101.scalajs.react.components.elementalui._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ BackendScope, Callback, ReactEventFromHtml, ReactEventFromInput, ScalaComponent }
import model.Todo

object TodoEditorComponent {

  final case class Props(
    snapshot: StateSnapshot[Todo],
    submit: Todo => Callback,
    close: Callback
  )

  final class Backend(scope: BackendScope[Props, Unit]) {

    private val close: Callback = scope.props.flatMap(_.close)

    private def submit(todo: Todo): Callback = scope.props.flatMap(_.submit(todo))

    private def modState(f: Todo => Todo): Callback = scope.props.flatMap(_.snapshot.modState(f))

    private val handleDialogCancel: ReactEventFromHtml => Callback = _ => close

    private def handleDialogSubmit(todo: Todo): ReactEventFromHtml => Callback = _ => submit(todo) >> close

    private def onInputChange(f: (Todo, String) => Todo) = (event: ReactEventFromInput) =>
      modState(f(_, event.target.value))

    private val onIsCompletedSwitch: ReactEventFromHtml => Callback =
      _ => modState(v => v.copy(isCompleted = !v.isCompleted))

    def render(props: Props): VdomElement = {
      val todo = props.snapshot.value
      <.div(
        Modal(
          isOpen = true,
          backdropClosesModal = true,
          onCancel = handleDialogCancel
        )(
          ModalHeader(text = "To Do")(),
          ModalBody()(
            <.div(
              ^.display.flex,
              ^.flexDirection.column,
              <.div(
                FormField(label = "ID")(
                  FormInput(
                    label = "ID",
                    `type` = "text",
                    name = "id",
                    disabled = true,
                    value = todo.id.toString
                  )()
                )(),
                FormField(label = "Description")(
                  FormInput(
                    label = "Description",
                    `type` = "text",
                    name = "description",
                    value = todo.description,
                    autoFocus = true,
                    placeholder = "Description",
                    multiline = true,
                    onChange = onInputChange((s, n) => s.copy(description = n))
                  )()
                )(),
                FormField()(
                  CheckBox(label = "Is Completed", onclick = onIsCompletedSwitch)()
                )
              )
            )
          ),
          ModalFooter()(
            Button(onClick = handleDialogSubmit(todo), `type` = ButtonType.PRIMARY, submit = true)("Submit"),
            Button(onClick = handleDialogCancel, `type` = ButtonType.LINK_CANCEL, submit = false)("Cancel")
          )
        )
      )
    }
  }

  private val component = ScalaComponent
    .builder[Props]("TodoEditorComponent")
    .renderBackend[Backend]
    .build

  def apply(props: Props): Unmounted[Props, Unit, Backend] = component(props)
}
