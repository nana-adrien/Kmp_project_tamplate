#!/usr/bin/env bash
# Usage: ./create-feature.sh <feature_name>
# Generates the full scaffold for a new feature: domain, data, presentation, config
# Example: ./create-feature.sh orders

set -e

FEATURE="$1"

if [[ -z "$FEATURE" ]]; then
    echo "Usage: $0 <feature_name>"
    echo "  Example: $0 orders"
    exit 1
fi

# Read current app package from gradle.properties
APP_PACKAGE=$(grep "^app.package=" gradle.properties | cut -d'=' -f2 | tr -d '[:space:]')
if [[ -z "$APP_PACKAGE" ]]; then
    APP_PACKAGE="empire.digiprem.kmptemplate"
fi

FEATURE_LOWER=$(echo "$FEATURE" | tr '[:upper:]' '[:lower:]')
FEATURE_UPPER="$(tr '[:lower:]' '[:upper:]' <<< "${FEATURE:0:1}")${FEATURE:1}"

FEATURE_DIR="feature/${FEATURE_LOWER}"
BASE_PKG="${APP_PACKAGE}.feature.${FEATURE_LOWER}"
SRC_COMMON="src/commonMain/kotlin/$(echo "${BASE_PKG}" | tr '.' '/')"

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  Creating feature: $FEATURE_UPPER"
echo "  Directory: $FEATURE_DIR"
echo "  Package  : $BASE_PKG"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# ── Helper: write file ────────────────────────────────────────────────────────
write_file() {
    local path="$1"
    local content="$2"
    mkdir -p "$(dirname "$path")"
    echo "$content" > "$path"
    echo "  ✓ $path"
}

# ────────────────────────────────────────────────────────────────────────────
# 1. DOMAIN
# ────────────────────────────────────────────────────────────────────────────
DOMAIN_SRC="${FEATURE_DIR}/domain/${SRC_COMMON}"

write_file "${FEATURE_DIR}/domain/build.gradle.kts" \
"plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(\":core:domain\"))
        }
    }
}"

write_file "${DOMAIN_SRC}/error/${FEATURE_UPPER}Error.kt" \
"package ${BASE_PKG}.error

import ${APP_PACKAGE}.core.domain.util.ResultError

sealed interface ${FEATURE_UPPER}Error : ResultError {
    data object NotFound : ${FEATURE_UPPER}Error
    data object Unknown  : ${FEATURE_UPPER}Error
}"

write_file "${DOMAIN_SRC}/model/${FEATURE_UPPER}.kt" \
"package ${BASE_PKG}.model

data class ${FEATURE_UPPER}(
    val id   : String,
    val name : String,
)"

write_file "${DOMAIN_SRC}/datasource/I${FEATURE_UPPER}DataSource.kt" \
"package ${BASE_PKG}.datasource

import ${APP_PACKAGE}.core.domain.network.DataError
import ${APP_PACKAGE}.core.domain.util.Result
import ${BASE_PKG}.model.${FEATURE_UPPER}

interface I${FEATURE_UPPER}DataSource {
    suspend fun getAll(): Result<List<${FEATURE_UPPER}>?, DataError.Remote>
    suspend fun getById(id: String): Result<${FEATURE_UPPER}?, DataError.Remote>
}"

write_file "${DOMAIN_SRC}/repository/I${FEATURE_UPPER}Repository.kt" \
"package ${BASE_PKG}.repository

import ${APP_PACKAGE}.core.domain.network.DataError
import ${APP_PACKAGE}.core.domain.util.Result
import ${BASE_PKG}.model.${FEATURE_UPPER}

interface I${FEATURE_UPPER}Repository {
    suspend fun getAll(): Result<List<${FEATURE_UPPER}>?, DataError.Remote>
    suspend fun getById(id: String): Result<${FEATURE_UPPER}?, DataError.Remote>
}"

write_file "${DOMAIN_SRC}/usecase/Get${FEATURE_UPPER}ListUseCase.kt" \
"package ${BASE_PKG}.usecase

import ${APP_PACKAGE}.core.domain.util.Result
import ${APP_PACKAGE}.core.domain.util.mapFailure
import ${BASE_PKG}.error.${FEATURE_UPPER}Error
import ${BASE_PKG}.model.${FEATURE_UPPER}
import ${BASE_PKG}.repository.I${FEATURE_UPPER}Repository

class Get${FEATURE_UPPER}ListUseCase(private val repository: I${FEATURE_UPPER}Repository) {
    suspend operator fun invoke(): Result<List<${FEATURE_UPPER}>?, ${FEATURE_UPPER}Error> =
        repository.getAll().mapFailure { ${FEATURE_UPPER}Error.Unknown }
}"

write_file "${DOMAIN_SRC}/usecase/Get${FEATURE_UPPER}ByIdUseCase.kt" \
"package ${BASE_PKG}.usecase

import ${APP_PACKAGE}.core.domain.util.Result
import ${APP_PACKAGE}.core.domain.util.mapFailure
import ${BASE_PKG}.error.${FEATURE_UPPER}Error
import ${BASE_PKG}.model.${FEATURE_UPPER}
import ${BASE_PKG}.repository.I${FEATURE_UPPER}Repository

class Get${FEATURE_UPPER}ByIdUseCase(private val repository: I${FEATURE_UPPER}Repository) {
    suspend operator fun invoke(id: String): Result<${FEATURE_UPPER}?, ${FEATURE_UPPER}Error> =
        repository.getById(id).mapFailure { ${FEATURE_UPPER}Error.Unknown }
}"

# ────────────────────────────────────────────────────────────────────────────
# 2. DATA
# ────────────────────────────────────────────────────────────────────────────
DATA_SRC="${FEATURE_DIR}/data/${SRC_COMMON}"

write_file "${FEATURE_DIR}/data/build.gradle.kts" \
"plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(\":core:domain\"))
            implementation(project(\":core:data\"))
            implementation(project(\":feature:${FEATURE_LOWER}:domain\"))
            implementation(libs.kotlinx.serialization.json)
        }
    }
}"

write_file "${DATA_SRC}/dto/${FEATURE_UPPER}Dto.kt" \
"package ${BASE_PKG}.dto

import kotlinx.serialization.Serializable

@Serializable
data class ${FEATURE_UPPER}Dto(
    val id   : String,
    val name : String,
)"

write_file "${DATA_SRC}/mapper/${FEATURE_UPPER}Mapper.kt" \
"package ${BASE_PKG}.mapper

import ${BASE_PKG}.dto.${FEATURE_UPPER}Dto
import ${BASE_PKG}.model.${FEATURE_UPPER}

fun ${FEATURE_UPPER}Dto.toDomain(): ${FEATURE_UPPER} = ${FEATURE_UPPER}(
    id   = id,
    name = name,
)"

write_file "${DATA_SRC}/datasource/${FEATURE_UPPER}KtorDataSource.kt" \
"package ${BASE_PKG}.datasource

import ${APP_PACKAGE}.core.data.dto.ApiResponseWithPayload
import ${APP_PACKAGE}.core.domain.network.DataError
import ${APP_PACKAGE}.core.domain.util.Result
import ${APP_PACKAGE}.core.domain.util.map
import ${BASE_PKG}.dto.${FEATURE_UPPER}Dto
import ${BASE_PKG}.mapper.toDomain
import ${BASE_PKG}.model.${FEATURE_UPPER}
import io.ktor.client.HttpClient

class ${FEATURE_UPPER}KtorDataSource(
    private val client: HttpClient,
) : I${FEATURE_UPPER}DataSource {

    override suspend fun getAll(): Result<List<${FEATURE_UPPER}>?, DataError.Remote> =
        client.get<ApiResponseWithPayload<List<${FEATURE_UPPER}Dto>>>(route = \"v1/${FEATURE_LOWER}s\")
            .map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun getById(id: String): Result<${FEATURE_UPPER}?, DataError.Remote> =
        client.get<ApiResponseWithPayload<${FEATURE_UPPER}Dto>>(route = \"v1/${FEATURE_LOWER}s/\$id\")
            .map { it.payload?.toDomain() }
}"

write_file "${DATA_SRC}/repository/${FEATURE_UPPER}Repository.kt" \
"package ${BASE_PKG}.repository

import ${APP_PACKAGE}.core.domain.network.DataError
import ${APP_PACKAGE}.core.domain.util.Result
import ${BASE_PKG}.datasource.I${FEATURE_UPPER}DataSource
import ${BASE_PKG}.model.${FEATURE_UPPER}

class ${FEATURE_UPPER}Repository(
    private val dataSource: I${FEATURE_UPPER}DataSource,
) : I${FEATURE_UPPER}Repository {
    override suspend fun getAll(): Result<List<${FEATURE_UPPER}>?, DataError.Remote> = dataSource.getAll()
    override suspend fun getById(id: String): Result<${FEATURE_UPPER}?, DataError.Remote> = dataSource.getById(id)
}"

# ────────────────────────────────────────────────────────────────────────────
# 3. PRESENTATION
# ────────────────────────────────────────────────────────────────────────────
PRES_SRC="${FEATURE_DIR}/presentation/${SRC_COMMON}/${FEATURE_LOWER}"

write_file "${FEATURE_DIR}/presentation/build.gradle.kts" \
"plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(\":core:domain\"))
            implementation(project(\":core:presentation\"))
            implementation(project(\":core:design_system\"))
            implementation(project(\":feature:${FEATURE_LOWER}:domain\"))
        }
    }
}"

write_file "${PRES_SRC}/${FEATURE_UPPER}State.kt" \
"package ${BASE_PKG}.${FEATURE_LOWER}

import ${APP_PACKAGE}.core.presentation.extension.UiText
import ${BASE_PKG}.model.${FEATURE_UPPER}

data class ${FEATURE_UPPER}State(
    val items        : List<${FEATURE_UPPER}> = emptyList(),
    val isLoading    : Boolean                = false,
    val errorMessage : UiText?                = null,
)"

write_file "${PRES_SRC}/${FEATURE_UPPER}Action.kt" \
"package ${BASE_PKG}.${FEATURE_LOWER}

sealed interface ${FEATURE_UPPER}Action {
    data object OnRefresh                        : ${FEATURE_UPPER}Action
    data class OnItemClick(val id: String)       : ${FEATURE_UPPER}Action
}"

write_file "${PRES_SRC}/${FEATURE_UPPER}Event.kt" \
"package ${BASE_PKG}.${FEATURE_LOWER}

sealed interface ${FEATURE_UPPER}Event {
    data class OnNavigateToDetail(val id: String) : ${FEATURE_UPPER}Event
}"

write_file "${PRES_SRC}/${FEATURE_UPPER}ViewModel.kt" \
"package ${BASE_PKG}.${FEATURE_LOWER}

import ${APP_PACKAGE}.core.domain.util.onFailure
import ${APP_PACKAGE}.core.domain.util.onSuccess
import ${APP_PACKAGE}.core.presentation.extension.UiText
import ${APP_PACKAGE}.core.presentation.util.AbstractViewModel
import ${BASE_PKG}.usecase.Get${FEATURE_UPPER}ListUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ${FEATURE_UPPER}ViewModel(
    private val get${FEATURE_UPPER}ListUseCase: Get${FEATURE_UPPER}ListUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(${FEATURE_UPPER}State())
    val state = _state.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), ${FEATURE_UPPER}State())

    private val _events = Channel<${FEATURE_UPPER}Event>()
    val events = _events.receiveAsFlow()

    init { load() }

    fun onAction(action: ${FEATURE_UPPER}Action) {
        when (action) {
            ${FEATURE_UPPER}Action.OnRefresh         -> load()
            is ${FEATURE_UPPER}Action.OnItemClick    -> viewModelScope.launch {
                _events.send(${FEATURE_UPPER}Event.OnNavigateToDetail(action.id))
            }
        }
    }

    private fun load() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            get${FEATURE_UPPER}ListUseCase()
                .onSuccess { list ->
                    _state.update { it.copy(isLoading = false, items = list ?: emptyList()) }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = UiText.DynamicString(error.toString()))
                    }
                }
        }
    }
}"

write_file "${PRES_SRC}/${FEATURE_UPPER}Screen.kt" \
"package ${BASE_PKG}.${FEATURE_LOWER}

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ${APP_PACKAGE}.core.design_system.components.topbar.AppTopBar
import ${APP_PACKAGE}.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ${FEATURE_UPPER}Root(
    onNavigateToDetail: (String) -> Unit = {},
    viewModel         : ${FEATURE_UPPER}ViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            is ${FEATURE_UPPER}Event.OnNavigateToDetail -> onNavigateToDetail(event.id)
        }
    }

    ${FEATURE_UPPER}Screen(state = state, onAction = viewModel::onAction)
}

@Composable
fun ${FEATURE_UPPER}Screen(
    state   : ${FEATURE_UPPER}State,
    onAction: (${FEATURE_UPPER}Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar   = { AppTopBar(title = \"${FEATURE_UPPER}\") },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                state.errorMessage != null -> Text(
                    text     = state.errorMessage.toString(),
                    color    = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center),
                )
                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items = state.items, key = { it.id }) { item ->
                        ListItem(
                            headlineContent = { Text(item.name) },
                        )
                    }
                }
            }
        }
    }
}"

# ────────────────────────────────────────────────────────────────────────────
# 4. CONFIG
# ────────────────────────────────────────────────────────────────────────────
CONFIG_SRC="${FEATURE_DIR}/config/${SRC_COMMON}/config"

write_file "${FEATURE_DIR}/config/build.gradle.kts" \
"plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(\":feature:${FEATURE_LOWER}:domain\"))
            api(project(\":feature:${FEATURE_LOWER}:data\"))
            api(project(\":feature:${FEATURE_LOWER}:presentation\"))
            implementation(libs.koin.core)
        }
    }
}"

write_file "${CONFIG_SRC}/Koin${FEATURE_UPPER}Module.kt" \
"package ${BASE_PKG}.config

import ${BASE_PKG}.datasource.I${FEATURE_UPPER}DataSource
import ${BASE_PKG}.datasource.${FEATURE_UPPER}KtorDataSource
import ${BASE_PKG}.${FEATURE_LOWER}.${FEATURE_UPPER}ViewModel
import ${BASE_PKG}.repository.I${FEATURE_UPPER}Repository
import ${BASE_PKG}.repository.${FEATURE_UPPER}Repository
import ${BASE_PKG}.usecase.Get${FEATURE_UPPER}ByIdUseCase
import ${BASE_PKG}.usecase.Get${FEATURE_UPPER}ListUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koin${FEATURE_UPPER}Module = module {
    singleOf(::${FEATURE_UPPER}KtorDataSource) bind I${FEATURE_UPPER}DataSource::class
    singleOf(::${FEATURE_UPPER}Repository)     bind I${FEATURE_UPPER}Repository::class
    singleOf(::Get${FEATURE_UPPER}ListUseCase)
    singleOf(::Get${FEATURE_UPPER}ByIdUseCase)
    viewModelOf(::${FEATURE_UPPER}ViewModel)
}"

# ────────────────────────────────────────────────────────────────────────────
# 5. Register in settings.gradle.kts
# ────────────────────────────────────────────────────────────────────────────
SETTINGS_FILE="settings.gradle.kts"

if grep -q "feature:${FEATURE_LOWER}:domain" "$SETTINGS_FILE"; then
    echo ""
    echo "⚠️  Feature ':feature:${FEATURE_LOWER}' already registered in $SETTINGS_FILE — skipping."
else
    # Append after the last include block
    printf '\n// %s feature\ninclude(\n    ":feature:%s:domain",\n    ":feature:%s:data",\n    ":feature:%s:presentation",\n    ":feature:%s:config",\n)\n' \
        "$FEATURE_UPPER" "$FEATURE_LOWER" "$FEATURE_LOWER" "$FEATURE_LOWER" "$FEATURE_LOWER" \
        >> "$SETTINGS_FILE"
    echo "  ✓ Registered in settings.gradle.kts"
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  ✅ Feature '${FEATURE_UPPER}' created!"
echo ""
echo "  Next steps:"
echo "  1. Add 'koin${FEATURE_UPPER}Module' to initKoin() in shared/di/KoinConfig.kt"
echo "  2. Add a route in navigation/app/AppNavGraphRoute.kt"
echo "  3. Wire ${FEATURE_UPPER}Root in AppNavGraph.kt"
echo "  4. Adjust API routes in ${FEATURE_UPPER}KtorDataSource.kt"
echo "  5. Sync Gradle"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
