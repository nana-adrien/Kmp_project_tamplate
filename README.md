# KMP Project Template

Template **Kotlin Multiplatform** réutilisable avec Compose Multiplatform, Clean Architecture et pattern MVI.

> Ce template contient toute l'infrastructure commune à une application mobile professionnelle.
> Tu n'as qu'à brancher ton API et coder les features métier spécifiques à ton projet.

---

## Table des matières

1. [Ce que le template contient](#-ce-que-le-template-contient)
2. [Architecture](#-architecture)
3. [Démarrage rapide](#-démarrage-rapide)
4. [Configurer les cibles](#-configurer-les-cibles-plateforme)
5. [Créer un nouveau projet](#-créer-un-nouveau-projet)
6. [Ajouter une feature métier](#-ajouter-une-feature-métier)
7. [Commandes Gradle](#-commandes-gradle)
8. [Scénarios d'utilisation](#-scénarios-dutilisation)
9. [Ce que tu dois personnaliser](#-ce-que-tu-dois-personnaliser-par-projet)
10. [Stack technique](#-stack-technique)
11. [Structure du projet](#-structure-du-projet)

---

## ✅ Ce que le template contient

Tout ce qui est **identique dans chaque application** est déjà codé et prêt à l'emploi :

### Features intégrées

| Feature | Écrans | Statut |
|---|---|---|
| **Authentification** | Login, Mot de passe oublié, Réinitialisation, Vérification identité, Création de profil | ✅ Prêt |
| **Profil utilisateur** | Consultation, Modification | ✅ Prêt |
| **Notifications** | Liste, Marquer comme lu, Filtres, Suppression | ✅ Prêt |
| **Paramètres** | Langue, Thème clair/sombre, Notifications actives | ✅ Prêt |
| **Dashboard adaptatif** | BottomBar (mobile) / NavigationRail (tablette) / Sidebar (desktop/web) | ✅ Prêt |

### Infrastructure intégrée

| Composant | Détail |
|---|---|
| **Réseau** | Ktor Client + intercepteur automatique de headers + gestion des erreurs |
| **Authentification HTTP** | Bearer token auto-injecté, refresh token, endpoints anonymes |
| **Stockage sécurisé** | AES-256/GCM + AndroidKeyStore (Android) / Keychain (iOS) |
| **DI** | Koin configuré, modules par couche, platform-specific |
| **Result<T,E>** | Pattern fonctionnel pour la gestion d'erreurs sans try/catch |
| **DataError** | Hiérarchie complète d'erreurs réseau et locales |
| **Composants UI** | Button, TextField, PasswordField, OtpField, Image async, Dialog, TopBar, etc. |
| **Thème** | Material3 complet, clair/sombre, couleurs/typo personnalisables |
| **Navigation** | 3 graphes type-safe (PreAuth / Auth / App), transitions propres |
| **Images async** | Coil 3 avec shimmer de chargement et gestion des erreurs |
| **Logging** | Kermit multiplateforme |

---

## 🏗 Architecture

Le template suit la **Clean Architecture** avec le pattern **MVI** (Model-View-Intent).

```
shared/                          ← Shell de l'app (navigation, DI, App.kt)
│                                   dépend de tous les modules core/ et feature/
├── commonMain/                  ← Code partagé toutes plateformes
├── androidMain/                 ← expect/actual Android
├── iosMain/                     ← MainViewController + expect/actual iOS
├── jvmMain/                     ← expect/actual Desktop
└── wasmJsMain/                  ← expect/actual Web

androidApp/  ─── depend de :shared ──→  MainActivity
desktopApp/  ─── depend de :shared ──→  main() Window
webApp/      ─── depend de :shared ──→  main() ComposeViewport
iosApp/      ─── utilise Shared.framework (Swift/Xcode)

core/
├── domain/          ← Result<T,E>, DataError, UiText, interfaces de service
├── data/            ← Ktor, DataStore, CryptoManager, SecureStorage
├── presentation/    ← Extensions Compose, ObservableEvent, rememberScreenType
├── design_system/   ← Composants UI réutilisables (AppButton, AppImage, etc.)
└── config/          ← Module Koin core

feature/{nom}/
├── domain/          ← IDataSource, IRepository, UseCases, modèles, erreurs
├── data/            ← KtorDataSource (impl), Repository (impl), DTOs
├── presentation/    ← State, Action, Event, ViewModel, Screen, Content
└── config/          ← Module Koin feature
```

### Flux de données (MVI)

```
Composable → onAction() → ViewModel → UseCase → Repository → DataSource
                                                                    ↓
                                                              API (Ktor)
                                                                    ↓
ViewModel ← _state.update { } ← Result<DomainModel, DomainError> ←─┘
    ↓
StateFlow → Composable (recomposition automatique)

Events one-shot (navigation, toast) → Channel<Event> → ObservableEvent
```

---

## 🚀 Démarrage rapide

### Prérequis

- Android Studio Hedgehog ou supérieur
- JDK 17+
- Xcode 15+ (pour iOS)
- Kotlin Multiplatform Mobile plugin

### Cloner et lancer

```bash
# 1. Cloner le template
git clone https://github.com/ton-compte/kmp-template.git MonProjet
cd MonProjet

# 2. Ouvrir dans Android Studio
# File > Open > sélectionner le dossier MonProjet

# 3. Lancer l'app Android directement
./gradlew :androidApp:assembleDebug
```

L'application tourne avec des écrans de login, dashboard, profil, notifications et paramètres fonctionnels (avec des données de test).

---

## 🎯 Configurer les cibles plateforme

Ouvre `gradle.properties` et active les plateformes dont tu as besoin :

```properties
# Cibles actives — true ou false
kmp.target.android=true
kmp.target.ios=true
kmp.target.desktop=false
kmp.target.web=false
```

| Combinaison | Configuration |
|---|---|
| Mobile uniquement | `android=true` `ios=true` `desktop=false` `web=false` |
| Mobile + Desktop | `android=true` `ios=true` `desktop=true` `web=false` |
| Toutes plateformes | `android=true` `ios=true` `desktop=true` `web=true` |
| Web + Desktop | `android=false` `ios=false` `desktop=true` `web=true` |

> Aucune modification de code n'est nécessaire. Les convention plugins Gradle lisent ces flags et configurent automatiquement les targets KMP.

---

## 🆕 Créer un nouveau projet

### Étape 1 — Cloner le template

```bash
git clone https://github.com/ton-compte/kmp-template.git NomDuProjet
cd NomDuProjet
rm -rf .git
git init
```

### Étape 2 — Renommer le projet

```bash
./rename-project.sh \
  --package com.tonentreprise.tonapp \
  --name TonApp \
  --prefix Ton
```

Ce script remplace dans tous les fichiers `.kt`, `.kts`, `.xml`, `.swift`, `.xcconfig`, `.toml` :
- Le package `empire.digiprem.kmptemplate` → `com.tonentreprise.tonapp`
- Le nom `KmpTemplate` → `TonApp`
- Le préfixe des composants UI `App` → `Ton` (ex: `AppButton` → `TonButton`)

### Étape 3 — Configurer les cibles

```properties
# gradle.properties
kmp.target.android=true
kmp.target.ios=true
kmp.target.desktop=false
kmp.target.web=false
```

### Étape 4 — Brancher l'API

```kotlin
// core/data/networking/HttpConstants.kt
const val BASE_URL = "https://api.tonprojet.com/"
const val API_KEY  = BuildConfig.API_KEY   // via buildKonfig
```

### Étape 5 — Personnaliser le thème

```kotlin
// core/design_system/theme/Color.kt
val Primary   = Color(0xFF1565C0)   // ta couleur principale
val Secondary = Color(0xFFFF6F00)   // ta couleur secondaire
```

**C'est tout.** L'app est opérationnelle avec le login, le dashboard, le profil, les notifications et les paramètres.

---

## ➕ Ajouter une feature métier

```bash
# Exemple : feature "commandes"
./create-feature.sh commandes
```

Le script génère automatiquement :

```
feature/commandes/
├── domain/
│   ├── datasource/ICommandeDataSource.kt   ← interface HTTP (à ne pas modifier)
│   ├── repository/ICommandeRepository.kt
│   ├── usecase/GetCommandesUseCase.kt      ← logique métier (à compléter)
│   ├── usecase/UpdateCommandeUseCase.kt
│   └── model/Commande.kt                   ← TON modèle métier
├── data/
│   ├── datasource/CommandeKtorDataSource.kt ← appels API (routes à remplir)
│   ├── repository/CommandeRepository.kt
│   └── dto/CommandeResponse.kt              ← structure JSON de l'API
├── presentation/
│   ├── list/CommandeListState.kt            ← champs de l'UI
│   ├── list/CommandeListAction.kt           ← intentions utilisateur
│   ├── list/CommandeListEvent.kt            ← navigation/toast one-shot
│   ├── list/CommandeListViewModel.kt        ← logique MVI câblée
│   └── list/CommandeListScreen.kt           ← UI à construire
└── config/KoinCommandesModule.kt            ← DI auto-configurée
```

### Ce que tu dois remplir

| Fichier | Action |
|---|---|
| `Commande.kt` | Définir les champs du modèle métier |
| `CommandeResponse.kt` | Mapper la structure JSON de l'API |
| `CommandeKtorDataSource.kt` | Ajouter les routes API (`v1/commandes/...`) |
| `CommandeListState.kt` | Définir les données affichées |
| `CommandeListScreen.kt` | Construire l'UI avec les composants du design system |

### Ce qui est déjà câblé

- `Result<T,E>` et gestion d'erreurs dans tous les use cases
- Injection Koin dans `KoinCommandesModule.kt`
- `StateFlow` + `Channel<Event>` dans le ViewModel
- Connexion au design system (tous les composants disponibles)

### Ajouter la route dans le Dashboard

```kotlin
// shared/src/commonMain/.../navigation/app/AppNavGraph.kt
composable<AppNavGraphRoute.Commandes> {
    CommandeListRoot(onBack = { navController.popBackStack() })
}

// AppNavGraphRoute.kt
@Serializable object Commandes : AppNavGraphRoute
```

---

## 📦 Commandes Gradle

### Build

```bash
# Android (APK debug)
./gradlew :androidApp:assembleDebug

# Android (APK release)
./gradlew :androidApp:assembleRelease

# Desktop (lancer)
./gradlew :desktopApp:run

# Desktop (hot reload)
./gradlew :desktopApp:hotRun --auto

# Web — WASM (navigateurs modernes)
./gradlew :webApp:wasmJsBrowserDevelopmentRun

# Web — JS (compatibilité étendue)
./gradlew :webApp:jsBrowserDevelopmentRun

# iOS — ouvrir Xcode puis Run
open iosApp/iosApp.xcodeproj
```

### Tests

```bash
# Tests Android
./gradlew :shared:testAndroidHostTest

# Tests Desktop (JVM)
./gradlew :shared:jvmTest

# Tests iOS simulateur
./gradlew :shared:iosSimulatorArm64Test

# Tests Web WASM
./gradlew :shared:wasmJsTest

# Tests Web JS
./gradlew :shared:jsTest

# Tous les tests
./gradlew test
```

### Utilitaires

```bash
# Créer un nouveau projet depuis le template
./rename-project.sh --package com.entreprise.app --name MonApp --prefix Mon

# Scaffolder une nouvelle feature
./create-feature.sh nom_de_la_feature

# Vérifier les dépendances obsolètes
./gradlew dependencyUpdates

# Nettoyer le build
./gradlew clean
```

---

## 📖 Scénarios d'utilisation

### Scénario 1 — App mobile standard (Android + iOS)

> "Je dois créer une app de gestion de livraisons pour un client logistique."

```bash
git clone ... GestionLivraisons
./rename-project.sh --package com.client.livraisons --name GestionLivraisons --prefix GL

# gradle.properties
kmp.target.android=true
kmp.target.ios=true
kmp.target.desktop=false
kmp.target.web=false

# Brancher l'API du client
# HttpConstants.kt → BASE_URL = "https://api.livraisons.com/"

# Créer les features métier
./create-feature.sh livraisons
./create-feature.sh chauffeurs
./create-feature.sh tournees
```

**Résultat :** Login, profil, notifications, settings déjà fonctionnels. Tu codes uniquement Livraisons, Chauffeurs et Tournées.

---

### Scénario 2 — App multi-plateforme (Mobile + Desktop)

> "Une app de caisse pour tablette Android et PC Windows."

```bash
./rename-project.sh --package com.magasin.caisse --name CaisseApp --prefix Caisse

# gradle.properties
kmp.target.android=true
kmp.target.ios=false
kmp.target.desktop=true
kmp.target.web=false

./create-feature.sh produits
./create-feature.sh transactions
./create-feature.sh rapports
```

Le Dashboard adaptatif affiche automatiquement une **Sidebar** sur Desktop et une **BottomBar** sur tablette Android.

---

### Scénario 3 — App web + desktop (SaaS backoffice)

> "Un tableau de bord web pour les managers, accessible aussi depuis l'app Desktop."

```bash
./rename-project.sh --package com.entreprise.backoffice --name BackOffice --prefix BO

# gradle.properties
kmp.target.android=false
kmp.target.ios=false
kmp.target.desktop=true
kmp.target.web=true

./create-feature.sh analytics
./create-feature.sh utilisateurs
./create-feature.sh rapports
```

---

### Scénario 4 — Suite d'applications (même base, projets différents)

> "Je développe 3 apps différentes pour le même client (Driver, Manager, Admin)."

```bash
# App Driver (mobile)
git clone ... AppDriver
./rename-project.sh --package com.client.driver --name DriverApp --prefix Driver
# kmp.target.android=true, ios=true

# App Manager (mobile + desktop)
git clone ... AppManager
./rename-project.sh --package com.client.manager --name ManagerApp --prefix Manager
# kmp.target.android=true, ios=true, desktop=true

# App Admin (web)
git clone ... AppAdmin
./rename-project.sh --package com.client.admin --name AdminApp --prefix Admin
# kmp.target.web=true, desktop=true
```

Les 3 apps partagent la même base (auth, profil, notifications, settings, design system). Seules les features métier diffèrent.

---

## 🎨 Ce que tu dois personnaliser par projet

| Élément | Fichier | Description |
|---|---|---|
| **URL de l'API** | `core/data/networking/HttpConstants.kt` | `BASE_URL` de ton backend |
| **Clé API** | `gradle.properties` + `BuildConfig` | via buildKonfig plugin |
| **Couleurs** | `core/design_system/theme/Color.kt` | Palette de ton client |
| **Typographie** | `core/design_system/theme/Type.kt` | Polices du projet |
| **Logo / icônes** | `core/design_system/src/.../composeResources/drawable/` | Assets visuels |
| **Nom de l'app** | `rename-project.sh` | Renomme partout automatiquement |
| **Package** | `rename-project.sh` | Renomme partout automatiquement |
| **Features métier** | `./create-feature.sh` | Scaffold automatique |
| **Strings/i18n** | `shared/src/commonMain/composeResources/values/` | Textes de l'app |

### Ce que tu ne touches PAS (géré par le template)

- `Result<T,E>`, `DataError` et tous les opérateurs
- `HttpClientFactory`, intercepteur de headers, bearer token
- `AndroidCryptoManager` et `IosCryptoManager`
- `SecureStorageService` (chiffrement DataStore)
- Tous les composants UI (`AppButton`, `AppTextField`, `AppImage`, etc.)
- Navigation type-safe et gestion de la back stack
- Koin DI (core + features auto-enregistrés)
- `AbstractViewModel` avec `safeCallOperation`
- Dashboard adaptatif (détection automatique de la surface d'affichage)

---

## 📚 Stack technique

| Domaine | Bibliothèque | Version |
|---|---|---|
| UI | Compose Multiplatform | 1.11.1 |
| Design | Material3 | 1.11.0-alpha07 |
| Navigation | Navigation Compose | 2.9.0 |
| Navigation adaptive | Material3 Adaptive Suite | 1.1.0 |
| Réseau | Ktor Client | 3.2.3 |
| DI | Koin | 4.1.0 |
| Sérialisation | Kotlinx Serialization | 1.9.0 |
| Base de données | Room | 2.7.2 |
| Préférences | DataStore | 1.1.7 |
| Async | Coroutines + Flow | 1.11.0 |
| Images | Coil | 3.3.0 |
| Logging | Kermit | 2.0.6 |
| Config build | BuildKonfig | 0.17.1 |
| Kotlin | Kotlin | 2.4.0 |
| Android Gradle Plugin | AGP | 9.0.1 |

---

## 📁 Structure du projet

```
KmpTemplate/
│
├── build-logic/                         # Convention plugins Gradle
│   └── convention/src/main/kotlin/
│       ├── ext/TargetExt.kt             # Lit gradle.properties → active les targets
│       ├── KmpLibraryPlugin.kt          # Module KMP sans Compose (domain, data)
│       ├── CmpLibraryPlugin.kt          # Module KMP avec Compose (presentation, design_system)
│       ├── FeaturePlugin.kt             # Module feature complet
│       ├── RoomPlugin.kt                # Module avec Room + KSP
│       └── AppPlugin.kt                 # Module shared (app shell)
│
├── shared/                              # Shell de l'application
│   └── src/
│       ├── commonMain/kotlin/
│       │   ├── App.kt                   # Composable racine
│       │   ├── di/KoinConfig.kt         # initKoin() — agrège tous les modules
│       │   └── navigation/              # NavHost + 3 graphes type-safe
│       ├── androidMain/kotlin/          # expect/actual Android
│       ├── iosMain/kotlin/              # MainViewController + expect/actual iOS
│       ├── jvmMain/kotlin/              # expect/actual Desktop
│       └── wasmJsMain/kotlin/           # expect/actual Web
│
├── androidApp/                          # Entry point Android
│   └── src/main/
│       ├── kotlin/.../MainActivity.kt
│       └── AndroidManifest.xml
│
├── desktopApp/                          # Entry point Desktop (JVM)
│   └── src/main/kotlin/.../main.kt
│
├── webApp/                              # Entry point Web (Wasm + JS)
│   └── src/webMain/kotlin/.../main.kt
│
├── iosApp/                              # Entry point iOS (Swift/Xcode)
│   └── iosApp/
│       ├── iOSApp.swift
│       └── ContentView.swift
│
├── core/
│   ├── domain/                          # Result<T,E>, DataError, UiText, interfaces
│   ├── data/                            # Ktor, DataStore, CryptoManager
│   ├── presentation/                    # Extensions Compose, ObservableEvent
│   ├── design_system/                   # Composants UI, thème, ressources
│   └── config/                          # KoinCoreModuleData
│
├── feature/
│   ├── auth/                            # Login, ForgotPwd, ResetPwd, Verify, CreateProfile
│   ├── profile/                         # GetProfile, UpdateProfile
│   ├── notifications/                   # List, MarkAsRead, Filter, Delete
│   ├── settings/                        # Language, Theme, NotifToggle
│   └── dashboard/                       # NavigationSuiteScaffold adaptatif
│
├── gradle/
│   └── libs.versions.toml              # Catalogue centralisé des dépendances
│
├── gradle.properties                    # Flags kmp.target.*
├── settings.gradle.kts                  # Includes conditionnels selon targets
│
├── rename-project.sh                    # Script de renommage (package + nom + prefix)
├── create-feature.sh                    # Scaffold d'une nouvelle feature MVI
└── README.md                            # Cette documentation
```

---

## 💡 Bonnes pratiques

### Nommage (Conventions Commits)

```
feat(auth): ajouter la validation du formulaire de login
fix(notifications): corriger le crash au swipe-to-delete
chore(deps): mettre à jour Ktor 3.2.3
refactor(profile): extraire la logique de validation dans un UseCase
```

### Règles d'architecture

- Les modules `:domain` n'ont **aucune dépendance externe** (ni Ktor, ni Room, ni Koin)
- Les features sont **isolées** — aucune dépendance cross-feature
- `DataError` ne remonte **jamais** dans le ViewModel — traduit en erreur domaine par le UseCase
- Aucun appel réseau directement dans un Composable — tout passe par `onAction` → ViewModel
- `Result<T,E>` partout — jamais de `try/catch` dans les UseCases et Repositories

---

## 🤝 Contribuer au template

Quand tu identifies un pattern récurrent dans tes projets qui n'est pas dans le template :

1. Implémente-le dans un projet existant d'abord
2. Si ça se révèle utile et générique → ouvre une PR sur le template
3. Assure-toi que ça ne casse aucune des cibles existantes (`./gradlew test`)

---

*Template maintenu par l'équipe Digiprem — Architecture KMP / Compose Multiplatform*
