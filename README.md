# KMP Project Template

Template **Kotlin Multiplatform** réutilisable avec Compose Multiplatform, Clean Architecture et pattern MVI.

> Ce template contient toute l'infrastructure commune à une application mobile professionnelle.
> Tu n'as qu'à brancher ton API et coder les features métier spécifiques à ton projet.

---

## Table des matières

1. [Ce que le template contient](#-ce-que-le-template-contient)
2. [Architecture](#-architecture)
3. [Lancer un projet depuis le template](#-lancer-un-projet-depuis-le-template)
   - [Prérequis](#prérequis)
   - [Étape 1 — Cloner et initialiser](#étape-1--cloner-et-initialiser)
   - [Étape 2 — Setup Wizard](#étape-2--lancer-le-setup-wizard)
   - [Étape 3 — Ce que le wizard applique](#étape-3--ce-que-le-wizard-applique-automatiquement)
   - [Mode Ktor uniquement](#mode-1--ktor-uniquement-api-externe)
   - [Mode Spring Boot Server](#mode-2--spring-boot-server-inclus)
   - [Mode Supabase](#mode-3--supabase-client-direct)
   - [Étape 4 — Ouvrir dans Android Studio](#étape-4--ouvrir-dans-android-studio)
4. [Configurer les cibles](#-configurer-les-cibles-plateforme)
5. [Ajouter une feature métier](#-ajouter-une-feature-métier)
6. [Commandes Gradle](#-commandes-gradle)
7. [Scénarios d'utilisation](#-scénarios-dutilisation)
8. [Ce que tu dois personnaliser](#-ce-que-tu-dois-personnaliser-par-projet)
9. [Stack technique](#-stack-technique)
10. [Structure du projet](#-structure-du-projet)

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

## 🚀 Lancer un projet depuis le template

### Prérequis

| Outil | Version minimum | Obligatoire |
|---|---|---|
| **JDK** | 17+ | Toujours |
| **Android Studio** | Hedgehog (2023.1.1)+ | Toujours |
| **Plugin KMP** | Kotlin Multiplatform Mobile | Toujours |
| **Xcode** | 15+ | Si cible iOS |
| **Supabase CLI** | Dernière version | Si mode Supabase + config BD |
| **Docker** | — | Requis par Supabase CLI |

---

### Étape 1 — Cloner et initialiser

```bash
git clone https://github.com/ton-compte/kmp-template.git MonProjet
cd MonProjet

# Repartir d'un historique git propre
rm -rf .git
git init
git add .
git commit -m "chore: initial commit from KMP template"
```

---

### Étape 2 — Lancer le Setup Wizard

```bash
chmod +x setup.sh
./setup.sh
```

Le wizard pose **4 questions** interactives :

#### Question 1 — App identity

```
? App name [MyApp]: MonApp
? Package name [com.mycompany.monapp]: com.monentreprise.monapp
? Component prefix [MonApp]: Mon
```

- **App name** → nom affiché dans Android Studio, Xcode, et l'icône de l'app
- **Package name** → remplace `empire.digiprem.kmptemplate` dans tout le code source
- **Component prefix** → renomme les composants UI : `AppButton` → `MonButton`, `AppImage` → `MonImage`, etc.

#### Question 2 — Targets (Y/n pour chacun)

```
? Android? (Y/n): Y
? iOS? (Y/n): Y
? Desktop (JVM)? (Y/n): n
? Web (WASM)? (Y/n): n
```

Les convention plugins Gradle lisent ces choix depuis `gradle.properties` et configurent automatiquement les source sets KMP. Aucune modification de code nécessaire.

#### Question 3 — Backend

```
  1) Ktor only  — client calls your own external API
  2) Ktor + Spring Boot Server — backend included in this repo (Supabase PostgreSQL)
  3) Supabase — BaaS via supabase-kt SDK (direct client access)
? Choice [1]:
```

Voir la section [Configuration selon le backend](#configuration-selon-le-backend) pour le détail de chaque mode.

#### Question 4 — Push notifications

```
  1) Firebase FCM (Android + iOS)
  2) APNs only (iOS)
  3) None
? Choice [3]:
```

---

### Étape 3 — Ce que le wizard applique automatiquement

| Action | Détail |
|---|---|
| `rename-project.sh` | Remplace package, nom d'app et préfixe composants dans tous les `.kt`, `.kts`, `.xml`, `.swift`, `.xcconfig`, `.toml` |
| `gradle.properties` | Active les targets : `kmp.target.android`, `kmp.target.ios`, `kmp.target.desktop`, `kmp.target.web` |
| `gradle.properties` | Définit `backend.type=ktor\|ktor-server\|supabase` |
| `gradle.properties` | Écrit les credentials backend (URL DB, JWT secret, Supabase URL/key) |
| `local.properties` | Créé uniquement si mode Supabase + config BD — contient les clés debug/release |
| `create-feature.sh` | Rendu exécutable automatiquement |

---

### Configuration selon le backend

#### Mode 1 — Ktor uniquement (API externe)

C'est le mode par défaut. Le wizard ne demande rien de plus. Après le setup :

```kotlin
// core/data/src/commonMain/.../networking/HttpConstants.kt
const val BASE_URL = "https://api.tonprojet.com/"
```

Puis :

```bash
./gradlew :androidApp:assembleDebug
```

---

#### Mode 2 — Spring Boot Server inclus

Le repo contient un serveur Spring Boot complet (`/server/`) connecté à une base PostgreSQL hébergée sur Supabase cloud. Le wizard te demande :

```
? Supabase DB URL: jdbc:postgresql://db.xxxx.supabase.co:5432/postgres
? DB user: postgres
? DB password: ***
? JWT secret (base64, min 32 chars): ***
```

Ces valeurs sont écrites dans `gradle.properties` (gitignored).

**Lancer le serveur :**

```bash
./gradlew :server:app:bootRun
# Démarre sur http://localhost:8080
```

**Connecter le client KMP au serveur local :**

```kotlin
// core/data/src/commonMain/.../networking/HttpConstants.kt
const val BASE_URL = "http://10.0.2.2:8080/"   // depuis émulateur Android
// const val BASE_URL = "http://localhost:8080/" // depuis Desktop ou Web
```

**Appliquer les migrations SQL** sur ton projet Supabase cloud (via le Dashboard Supabase ou Supabase CLI) avant le premier démarrage du serveur.

---

#### Mode 3 — Supabase (client direct)

Le wizard demande l'URL et la clé anon de ton projet Supabase :

```
? Supabase project URL: https://xxxx.supabase.co
? Supabase Anon Key: eyJhbGci...
? Inclure la configuration BD Supabase (migrations SQL + Supabase CLI) ? (Y/n):
```

**Option A — Sans config BD** (connexion à un projet Supabase cloud existant) :

BuildKonfig génère automatiquement `BuildConfig.SUPABASE_URL` et `BuildConfig.SUPABASE_KEY` depuis `gradle.properties`. Aucune étape supplémentaire.

**Option B — Avec config BD** (migrations SQL + développement local via Supabase CLI) :

Le wizard crée `local.properties` avec 4 clés (debug ↔ release switching automatique) :

```properties
# local.properties — NE PAS COMMITTER (déjà dans .gitignore)
SUPABASE_URL_DEV=http://127.0.0.1:54321      # Supabase CLI local
SUPABASE_KEY_DEV=<anon-key-locale>
SUPABASE_URL_PROD=https://xxxx.supabase.co   # Supabase cloud
SUPABASE_KEY_PROD=<anon-key-prod>
```

**Démarrer l'environnement local Supabase :**

```bash
# 1. Installer Supabase CLI (macOS)
brew install supabase/tap/supabase

# 2. Démarrer Supabase local (PostgreSQL + Auth + API — nécessite Docker)
supabase start

# 3. Récupérer la clé anon locale
supabase status
# → copier "anon key" dans local.properties > SUPABASE_KEY_DEV

# 4. Appliquer les 4 migrations
#    (profiles, user_settings, notifications, storage avatars)
supabase db push

# 5. Injecter les données de test
#    (alice@test.com / bob@test.com — mot de passe: Test1234!)
supabase db seed
```

**BuildKonfig — switching debug/release automatique :**

```bash
# Debug → utilise SUPABASE_URL_DEV + SUPABASE_KEY_DEV (Supabase CLI local)
./gradlew :androidApp:assembleDebug

# Release → utilise SUPABASE_URL_PROD + SUPABASE_KEY_PROD (Supabase cloud)
./gradlew :androidApp:assembleRelease -Prelease
```

---

### Étape 4 — Ouvrir dans Android Studio

```
File > Open > sélectionner le dossier MonProjet
```

La première sync Gradle télécharge les dépendances (~2-5 min). Une fois terminée :

- **Android** : Run → sélectionner `androidApp` → choisir un émulateur ou device
- **Desktop** : `./gradlew :desktopApp:run`
- **Web** : `./gradlew :webApp:wasmJsBrowserDevelopmentRun`
- **iOS** : `open iosApp/iosApp.xcodeproj` puis Run dans Xcode

L'application démarre avec login, dashboard, profil, notifications et paramètres fonctionnels.

---

### Étape 5 — Personnaliser le thème

```kotlin
// core/design_system/src/commonMain/.../theme/Color.kt
val Primary   = Color(0xFF1565C0)   // couleur principale du projet
val Secondary = Color(0xFFFF6F00)   // couleur secondaire
```

---

### Étape 6 — Créer la première feature métier

```bash
./create-feature.sh commandes
```

Génère `feature/commandes/` avec domain, data, presentation et config Koin entièrement scaffoldés. Voir section [Ajouter une feature métier](#-ajouter-une-feature-métier).

---

## 🎯 Configurer les cibles plateforme

Les targets KMP sont contrôlées par `gradle.properties` — aucune modification de code nécessaire :

```properties
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
