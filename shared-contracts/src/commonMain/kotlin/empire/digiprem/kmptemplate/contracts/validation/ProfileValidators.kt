package empire.digiprem.kmptemplate.contracts.validation

import empire.digiprem.kmptemplate.contracts.profile.UpdateProfileRequest
import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.notBlank

val validateUpdateProfileRequest = Validation<UpdateProfileRequest> {
    UpdateProfileRequest::displayName ifPresent {
        notBlank()
        minLength(1)
        maxLength(50)
    }
    UpdateProfileRequest::bio ifPresent {
        maxLength(500)
    }
}
