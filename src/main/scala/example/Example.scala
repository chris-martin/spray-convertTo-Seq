package example

import spray.json.DefaultJsonProtocol._
import spray.json._

object Example {

  def main(args: Array[String]) {

    case class Foo(a: Int, b: Int)

    object FooJsonProtocol extends DefaultJsonProtocol {

      implicit object FooJsonReader extends RootJsonReader[Foo] {

        override def read(json: JsValue): Foo =
          json.asJsObject.getFields("a", "b") match {
            case Seq(JsNumber(a), JsNumber(b)) => Foo(a.toIntExact, b.toIntExact)
            case _ => throw deserializationError(s"Foo expected")
          }
      }
    }

    import FooJsonProtocol._

    print("""{"a": 1, "b": 2}""".parseJson.convertTo[Foo])

    print("""[{"a": 1, "b": 2}, {"a": 1, "b": 2}]""".parseJson.convertTo[Seq[Foo]])
  }
}
