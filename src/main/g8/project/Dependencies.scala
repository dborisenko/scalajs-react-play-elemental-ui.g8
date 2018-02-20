import org.scalajs.sbtplugin.impl.DependencyBuilders
import sbt._

object Dependencies extends DependencyBuilders {
  type NpmDependency = (String, String)

  object Versions {
    val scala = "2.12.4"

    val paradise = "2.1.1"
    val scalatest = "3.0.3"
    val circe = "0.9.0"
    val `scala-java-time` = "2.0.0-M12"
    val `play-circe` = "2609.0"
    val `scala-guice` = "4.1.1"
    val ficus = "1.4.3"
    val `scalajs-react` = "1.1.1"
    val scalacss = "0.5.4"
    val `scalajs-dom` = "0.9.4"
    val `scalajs-react-components` = "0.8.0"
    val react = "15.6.1"
    val elemental = "0.6.1"
    val `react-addons-css-transition-group` = "15.6.2"
  }

  val paradise = "org.scalamacros" % "paradise" % Versions.paradise

  val scalatest = Def.setting("org.scalatest" %% "scalatest" % Versions.scalatest)
  val `circe-generic` = Def.setting("io.circe" %%% "circe-generic" % Versions.circe)
  val `circe-java8` = Def.setting("io.circe" %%% "circe-java8" % Versions.circe)
  val `circe-parser` = Def.setting("io.circe" %%% "circe-parser" % Versions.circe)
  val `scala-java-time` = Def.setting("io.github.cquiroz" %%% "scala-java-time" % Versions.`scala-java-time`)
  val `play-circe` = Def.setting("com.dripower" %% "play-circe" % Versions.`play-circe`)
  val `scala-guice` = Def.setting("net.codingwell" %% "scala-guice" % Versions.`scala-guice`)
  val ficus = Def.setting("com.iheart" %% "ficus" % Versions.ficus)
  val `scalajs-react-core` = Def.setting("com.github.japgolly.scalajs-react" %%% "core" % Versions.`scalajs-react`)
  val `scalajs-react-extra` = Def.setting("com.github.japgolly.scalajs-react" %%% "extra" % Versions.`scalajs-react`)
  val `scalacss-ext-react` = Def.setting("com.github.japgolly.scalacss" %%% "ext-react" % Versions.scalacss)
  val `scalajs-dom` = Def.setting("org.scala-js" %%% "scalajs-dom" % Versions.`scalajs-dom`)
  val `scalajs-react-components` = Def.setting("com.olvind" %%% "scalajs-react-components" % Versions.`scalajs-react-components`)

  val react: NpmDependency = "react" -> Versions.react
  val `react-dom`: NpmDependency = "react-dom" -> Versions.react
  val elemental: NpmDependency = "elemental" -> Versions.elemental
  val `react-addons-css-transition-group`: NpmDependency = "react-addons-css-transition-group" -> Versions.`react-addons-css-transition-group`
}
