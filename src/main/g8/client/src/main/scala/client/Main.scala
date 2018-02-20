package client

import java.time.Clock

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.{ JSExportTopLevel, JSImport }
import scalacss.ProdDefaults._
import scalacss.ScalaCssReact._

@JSImport("elemental", JSImport.Namespace, "Elemental")
@js.native
object Elemental extends js.Object

@JSExportTopLevel("Main")
object Main {
  GlobalStyles.addToDocument()
  // This is needed to mount elemental library into `elemental` JavaScript global variable (elemental = require("elemental"))
  js.Dynamic.global.elemental = Elemental

  private val service: ApplicationService = new ApplicationService
  private val clock: Clock = Clock.systemUTC()

  MainComponent(MainComponent.Props(
    loadList = () => service.list(),
    createOrUpdate = service.createOrUpdate,
    delete = service.delete,
    clock = clock
  )).renderIntoDOM(dom.document.getElementById("root"))
}
