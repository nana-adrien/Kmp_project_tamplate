package empire.digiprem.kmptemplate.contracts.validation

import empire.digiprem.kmptemplate.contracts.settings.UpdateSettingsRequest
import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.notBlank

val validateUpdateSettingsRequest = Validation<UpdateSettingsRequest> {
    UpdateSettingsRequest::language ifPresent {
        notBlank()
        minLength(2)
        maxLength(10)
    }
    UpdateSettingsRequest::theme ifPresent {
        notBlank()
    }
}
