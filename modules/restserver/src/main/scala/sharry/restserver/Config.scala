package sharry.restserver

import sharry.backend.{Config => BackendConfig}
import sharry.common._

case class Config(
    baseUrl: LenientUri,
    bind: Config.Bind,
    webapp: Config.Webapp,
    backend: BackendConfig
)

object Config {

  case class Bind(address: String, port: Int)

  case class Webapp(appName: String, chunkSize: ByteSize, retryDelays: Seq[Duration])

}
