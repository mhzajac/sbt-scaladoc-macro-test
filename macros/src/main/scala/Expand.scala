import scala.annotation.{ StaticAnnotation, compileTimeOnly }
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

@compileTimeOnly("You must enable the macro paradise plugin.")
class expand extends StaticAnnotation {
    def macroTransform(annottees: Any*): Any = macro Impl.impl
}

object Impl {

  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    val result = annottees map (_.tree) match {
      case (classDef @
        q"""
          $mods class $tpname[..$tparams] $ctorMods(...$paramss) extends { ..$earlydefns } with ..$parents {
            $self => ..$stats
          }
        """) :: _ =>

        val copies = for {
            q"def $tname[..$tparams](...$paramss): $tpt = $expr" <- stats
            ident = TermName(tname.toString + "Copy")
        } yield {
            val paramSymbols = paramss.map(_.map(_.name))
            q"def $ident[..$tparams](...$paramss): $tpt = $tname(...$paramSymbols)"
        }
        q"""
            $mods class $tpname[..$tparams] $ctorMods(...$paramss) extends { ..$earlydefns } with ..$parents { $self =>
                ..$stats
                ..$copies
            }
        """
        case _ => c.abort(c.enclosingPosition, "Invalid annotation target: not a class")
    }

    c.Expr[Any](result)
  }

}
