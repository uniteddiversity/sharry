package sharry.restserver

import cats.implicits._
import sharry.common.pureconfig.Implicits._
import sharry.common.SignupMode
import _root_.pureconfig._
import _root_.pureconfig.generic.auto._
import emil.MailAddress
import emil.javamail.syntax._
import emil.SSLType
import yamusca.imports._

object ConfigFile {
  import Implicits._

  def loadConfig: Config =
    ConfigSource.default.at("sharry.restserver").loadOrThrow[Config]

  object Implicits {
    implicit val signupModeReader: ConfigReader[SignupMode] =
      ConfigReader[String].emap(reason(SignupMode.fromString))

    implicit val mailAddressReader: ConfigReader[Option[MailAddress]] =
      ConfigReader[String].emap(
        reason(s => if (s.trim.isEmpty) Right(None) else MailAddress.parse(s).map(m => Some(m)))
      )

    implicit val mailSslTypeReader: ConfigReader[SSLType] =
      ConfigReader[String].emap(
        reason(
          s =>
            s.toLowerCase match {
              case "none"     => Right(SSLType.NoEncryption)
              case "starttls" => Right(SSLType.StartTLS)
              case "ssl"      => Right(SSLType.SSL)
              case _          => Left(s"Invalid ssl type '$s'. Use one of none, ssl or starttls.")
            }
        )
      )

    implicit val templateReader: ConfigReader[Template] =
      ConfigReader[String].emap(
        reason(s => mustache.parse(s).leftMap(err => s"Error parsing template at ${err._1.pos}"))
      )
  }
}
