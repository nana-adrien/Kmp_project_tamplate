package empire.digiprem.kmptemplate.core.design_system.extension

import empire.digiprem.kmptemplate.core.design_system.generated.resources.Res
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_bad_request
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_disk_full
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_forbidden
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_local_not_found
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_no_internet
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_not_found
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_payload_too_large
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_request_timeout
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_serialization
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_server
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_server_unavailable
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_too_many_requests
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_unauthorized
import empire.digiprem.kmptemplate.core.design_system.generated.resources.error_unknown
import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.presentation.extension.UiText

fun DataError.toUiText(): UiText = when (this) {
    DataError.Remote.NoInternet        -> UiText.Resource(Res.string.error_no_internet)
    DataError.Remote.RequestTimeout    -> UiText.Resource(Res.string.error_request_timeout)
    DataError.Remote.Unauthorized      -> UiText.Resource(Res.string.error_unauthorized)
    DataError.Remote.Forbidden         -> UiText.Resource(Res.string.error_forbidden)
    DataError.Remote.NotFound          -> UiText.Resource(Res.string.error_not_found)
    DataError.Remote.BadRequest        -> UiText.Resource(Res.string.error_bad_request)
    DataError.Remote.TooManyRequests   -> UiText.Resource(Res.string.error_too_many_requests)
    DataError.Remote.ServerError       -> UiText.Resource(Res.string.error_server)
    DataError.Remote.ServerUnavailable -> UiText.Resource(Res.string.error_server_unavailable)
    DataError.Remote.PayloadTooLarge   -> UiText.Resource(Res.string.error_payload_too_large)
    DataError.Remote.Serialization     -> UiText.Resource(Res.string.error_serialization)
    DataError.Remote.Conflict          -> UiText.Resource(Res.string.error_unknown)
    DataError.Remote.Unknown           -> UiText.Resource(Res.string.error_unknown)
    is DataError.Remote.ServerProcessError -> UiText.DynamicString(message)
    DataError.Local.DiskFull           -> UiText.Resource(Res.string.error_disk_full)
    DataError.Local.NotFound           -> UiText.Resource(Res.string.error_local_not_found)
    DataError.Local.Unknown            -> UiText.Resource(Res.string.error_unknown)
}
