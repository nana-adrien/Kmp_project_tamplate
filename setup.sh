#!/usr/bin/env bash
# KMP Template — Interactive Setup Wizard
# Run after cloning: ./setup.sh

set -e

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

ask() {
    local prompt="$1"
    local default="$2"
    local result
    if [[ -n "$default" ]]; then
        read -r -p "$(echo -e "${BLUE}?${NC} $prompt [${default}]: ")" result
        echo "${result:-$default}"
    else
        read -r -p "$(echo -e "${BLUE}?${NC} $prompt: ")" result
        echo "$result"
    fi
}

ask_yn() {
    local prompt="$1"
    local default="${2:-y}"
    local result
    read -r -p "$(echo -e "${BLUE}?${NC} $prompt (Y/n) [${default}]: ")" result
    result="${result:-$default}"
    [[ "${result,,}" == "y" ]]
}

echo ""
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}  KMP Template — Setup Wizard${NC}"
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

# ── 1. App metadata ───────────────────────────────────────────────────────────
echo -e "${YELLOW}▸ App identity${NC}"
APP_NAME=$(ask "App name" "MyApp")
APP_PACKAGE=$(ask "Package name" "com.mycompany.${APP_NAME,,}")
COMPONENT_PREFIX=$(ask "Component prefix (used in design system)" "${APP_NAME}")

# ── 2. Targets ────────────────────────────────────────────────────────────────
echo ""
echo -e "${YELLOW}▸ Deployment targets${NC}"
TARGET_ANDROID=$(ask_yn "Android?" "y" && echo "true" || echo "false")
TARGET_IOS=$(ask_yn "iOS?" "y" && echo "true" || echo "false")
TARGET_DESKTOP=$(ask_yn "Desktop (JVM)?" "n" && echo "true" || echo "false")
TARGET_WEB=$(ask_yn "Web (WASM)?" "n" && echo "true" || echo "false")

# ── 3. Backend ────────────────────────────────────────────────────────────────
echo ""
echo -e "${YELLOW}▸ Backend${NC}"
echo "  1) Ktor only  — client calls your own external API"
echo "  2) Ktor + Spring Boot Server — backend included in this repo (Supabase PostgreSQL)"
echo "  3) Supabase — BaaS via supabase-kt SDK (direct client access)"
read -r -p "$(echo -e "${BLUE}?${NC} Choice [1]: ")" BACKEND_CHOICE
BACKEND_CHOICE="${BACKEND_CHOICE:-1}"

BACKEND_TYPE="ktor"
SUPABASE_URL=""
SUPABASE_KEY=""
SUPABASE_URL_DEV=""
SUPABASE_KEY_DEV=""
SETUP_SUPABASE_DB="false"
SERVER_DB_URL=""
SERVER_DB_USER="postgres"
SERVER_DB_PASSWORD=""
SERVER_JWT_SECRET=""

if [[ "$BACKEND_CHOICE" == "2" ]]; then
    BACKEND_TYPE="ktor-server"
    echo ""
    echo -e "${YELLOW}  ▸ Supabase PostgreSQL (used by the Spring Boot server via JDBC)${NC}"
    SERVER_DB_URL=$(ask "Supabase DB URL" "jdbc:postgresql://db.xxxx.supabase.co:5432/postgres")
    SERVER_DB_USER=$(ask "DB user" "postgres")
    SERVER_DB_PASSWORD=$(ask "DB password")
    SERVER_JWT_SECRET=$(ask "JWT secret (base64, min 32 chars)")
elif [[ "$BACKEND_CHOICE" == "3" ]]; then
    BACKEND_TYPE="supabase"
    SUPABASE_URL=$(ask "Supabase project URL" "https://xyzxyz.supabase.co")
    SUPABASE_KEY=$(ask "Supabase Anon Key")
    echo ""
    if ask_yn "Inclure la configuration BD Supabase (migrations SQL + Supabase CLI) ?" "y"; then
        SETUP_SUPABASE_DB="true"
        echo -e "${YELLOW}  ▸ URLs pour debug/release (stockées dans local.properties)${NC}"
        SUPABASE_URL_DEV=$(ask "URL Supabase locale debug (Supabase CLI)" "http://127.0.0.1:54321")
        SUPABASE_KEY_DEV=$(ask "Anon key locale debug (depuis: supabase status)")
    fi
fi

# ── 4. Push notifications ─────────────────────────────────────────────────────
echo ""
echo -e "${YELLOW}▸ Push notifications${NC}"
echo "  1) Firebase FCM (Android + iOS)"
echo "  2) APNs only (iOS)"
echo "  3) None"
read -r -p "$(echo -e "${BLUE}?${NC} Choice [3]: ")" PUSH_CHOICE
PUSH_CHOICE="${PUSH_CHOICE:-3}"

case "$PUSH_CHOICE" in
    1) PUSH_PROVIDER="firebase" ;;
    2) PUSH_PROVIDER="apns" ;;
    *) PUSH_PROVIDER="none" ;;
esac

# ── 5. Summary ────────────────────────────────────────────────────────────────
echo ""
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}  Summary${NC}"
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo "  App name    : $APP_NAME"
echo "  Package     : $APP_PACKAGE"
echo "  Prefix      : $COMPONENT_PREFIX"
echo "  Android     : $TARGET_ANDROID"
echo "  iOS         : $TARGET_IOS"
echo "  Desktop     : $TARGET_DESKTOP"
echo "  Web         : $TARGET_WEB"
echo "  Backend     : $BACKEND_TYPE$([ "$BACKEND_TYPE" = "ktor-server" ] && echo " (Spring Boot included)")"
echo "  Push        : $PUSH_PROVIDER"
echo ""
read -r -p "$(echo -e "${BLUE}?${NC} Proceed? (Y/n): ")" CONFIRM
CONFIRM="${CONFIRM:-y}"
if [[ "${CONFIRM,,}" != "y" ]]; then
    echo "Aborted."
    exit 0
fi

# ── 6. Apply configuration ────────────────────────────────────────────────────
echo ""
echo "  Applying configuration..."

# 6a. Rename project (package + name + prefix)
chmod +x rename-project.sh
./rename-project.sh \
    --package "$APP_PACKAGE" \
    --name    "$APP_NAME" \
    --prefix  "$COMPONENT_PREFIX"

# 6b. Update gradle.properties targets
sed -i '' \
    -e "s/^kmp.target.android=.*/kmp.target.android=${TARGET_ANDROID}/" \
    -e "s/^kmp.target.ios=.*/kmp.target.ios=${TARGET_IOS}/" \
    -e "s/^kmp.target.desktop=.*/kmp.target.desktop=${TARGET_DESKTOP}/" \
    -e "s/^kmp.target.web=.*/kmp.target.web=${TARGET_WEB}/" \
    gradle.properties

# 6c. Backend
sed -i '' \
    -e "s/^backend.type=.*/backend.type=${BACKEND_TYPE}/" \
    gradle.properties

if [[ "$BACKEND_TYPE" == "supabase" ]]; then
    sed -i '' \
        -e "s|^supabase.url=.*|supabase.url=${SUPABASE_URL}|" \
        -e "s|^supabase.anon.key=.*|supabase.anon.key=${SUPABASE_KEY}|" \
        gradle.properties
fi

if [[ "$BACKEND_TYPE" == "ktor-server" ]]; then
    sed -i '' \
        -e "s|^server.supabase.db.url=.*|server.supabase.db.url=${SERVER_DB_URL}|" \
        -e "s|^server.supabase.db.user=.*|server.supabase.db.user=${SERVER_DB_USER}|" \
        -e "s|^server.supabase.db.password=.*|server.supabase.db.password=${SERVER_DB_PASSWORD}|" \
        -e "s|^server.jwt.secret=.*|server.jwt.secret=${SERVER_JWT_SECRET}|" \
        gradle.properties
fi

# 6d. Push notifications
sed -i '' \
    -e "s/^push.provider=.*/push.provider=${PUSH_PROVIDER}/" \
    gradle.properties

# 6e. Make helper scripts executable
chmod +x create-feature.sh

# 6f. Supabase BD config
if [[ "$SETUP_SUPABASE_DB" == "true" ]]; then
    cat > local.properties <<EOF
SUPABASE_URL_DEV=${SUPABASE_URL_DEV}
SUPABASE_KEY_DEV=${SUPABASE_KEY_DEV}
SUPABASE_URL_PROD=${SUPABASE_URL}
SUPABASE_KEY_PROD=${SUPABASE_KEY}
EOF
    echo "  ✅ local.properties créé"
fi

# 7. Cleanup template artifacts from generated project
echo "  Cleaning up template artifacts..."

# 7a. Resolve isTargetEnabled() blocks in core/config/build.gradle.kts
python3 - <<'PYEOF'
import re

props = {}
with open('gradle.properties') as f:
    for line in f:
        line = line.strip()
        if '=' in line and not line.startswith('#'):
            k, v = line.split('=', 1)
            props[k.strip()] = v.strip()

def is_enabled(key):
    return props.get(key, 'false') == 'true'

with open('core/config/build.gradle.kts') as f:
    lines = f.read().split('\n')

result = []
i = 0
while i < len(lines):
    line = lines[i]
    m = re.match(r'^(\s*)if \(isTargetEnabled\("([^"]+)"\)\) \{$', line)
    if m:
        key = m.group(2)
        depth = 1
        block_lines = []
        i += 1
        while i < len(lines):
            l = lines[i]
            depth += l.count('{') - l.count('}')
            if depth == 0:
                i += 1
                break
            block_lines.append(l)
            i += 1
        if is_enabled(key):
            for bl in block_lines:
                result.append(bl[4:] if bl.startswith('    ') else bl)
        continue
    elif re.match(r'^fun isTargetEnabled\(', line):
        i += 1
        continue
    else:
        result.append(line)
    i += 1

cleaned = re.sub(r'\n{3,}', '\n\n', '\n'.join(result))
with open('core/config/build.gradle.kts', 'w') as f:
    f.write(cleaned)
PYEOF
echo "  ✅ core/config/build.gradle.kts nettoyé"

# 7b. Clean build-logic/convention/build.gradle.kts
if [[ "$BACKEND_TYPE" != "ktor-server" ]]; then
    sed -i '' '/\/\/ \[template:spring-only-start\]/,/\/\/ \[template:spring-only-end\]/d' build-logic/convention/build.gradle.kts
fi
if [[ "$BACKEND_TYPE" != "supabase" ]]; then
    sed -i '' '/\/\/ \[template:supabase-only-start\]/,/\/\/ \[template:supabase-only-end\]/d' build-logic/convention/build.gradle.kts
fi
sed -i '' '/\/\/ \[template:/d' build-logic/convention/build.gradle.kts
echo "  ✅ build-logic/convention/build.gradle.kts nettoyé"

# 7c. Clean root build.gradle.kts
if [[ "$BACKEND_TYPE" != "ktor-server" ]]; then
    sed -i '' '/\[template:spring-only\]/d' build.gradle.kts
else
    sed -i '' 's/ *\/\/ \[template:spring-only\]//' build.gradle.kts
fi
echo "  ✅ build.gradle.kts nettoyé"

# 7d. Resolve settings.gradle.kts backend block
python3 - <<'PYEOF'
import re

props = {}
with open('gradle.properties') as f:
    for line in f:
        line = line.strip()
        if '=' in line and not line.startswith('#'):
            k, v = line.split('=', 1)
            props[k.strip()] = v.strip()

backend_type = props.get('backend.type', 'ktor')

with open('settings.gradle.kts') as f:
    lines = f.read().split('\n')

result = []
i = 0
while i < len(lines):
    line = lines[i]
    m = re.match(r'^if \(strProp\("backend\.type"\) == "ktor-server"\) \{$', line)
    if m:
        depth = 1
        block_lines = []
        i += 1
        while i < len(lines):
            l = lines[i]
            depth += l.count('{') - l.count('}')
            if depth == 0:
                i += 1
                break
            block_lines.append(l)
            i += 1
        if backend_type == 'ktor-server':
            for bl in block_lines:
                result.append(bl[4:] if bl.startswith('    ') else bl)
        continue
    else:
        result.append(line)
    i += 1

cleaned = re.sub(r'\n{3,}', '\n\n', '\n'.join(result))
with open('settings.gradle.kts', 'w') as f:
    f.write(cleaned)
PYEOF
echo "  ✅ settings.gradle.kts nettoyé"

# 7e. Supprimer les fichiers convention plugins inutilisés
PLUGIN_DIR="build-logic/convention/src/main/kotlin"

if [[ "$BACKEND_TYPE" != "ktor-server" ]]; then
    rm -f "$PLUGIN_DIR/SpringCommonPlugin.kt"
    rm -f "$PLUGIN_DIR/SpringServicePlugin.kt"
    rm -f "$PLUGIN_DIR/convention/SpringTargetServer.kt"
fi

if [[ "$TARGET_DESKTOP" != "true" ]]; then
    rm -f "$PLUGIN_DIR/convention/DesktopTargetKmp.kt"
fi

if [[ "$TARGET_WEB" != "true" ]]; then
    rm -f "$PLUGIN_DIR/convention/WebTargetKmp.kt"
fi

if [[ "$BACKEND_TYPE" != "supabase" ]]; then
    rm -f "$PLUGIN_DIR/BuildConfigPlugin.kt"
    sed -i '' '/alias.*conventionBuildConfig/d' core/data/build.gradle.kts
fi
echo "  ✅ Fichiers convention plugins inutilisés supprimés"

# 7f. Retirer :shared-contracts de settings.gradle.kts si pas de serveur
if [[ "$BACKEND_TYPE" != "ktor-server" ]]; then
    sed -i '' '/include.*":shared-contracts"/d' settings.gradle.kts
    echo "  ✅ :shared-contracts exclu (pas de serveur)"
fi

echo ""
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}  ✅ Setup complete!${NC}"
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""
echo "  Next steps:"

if [[ "$PUSH_PROVIDER" == "firebase" ]]; then
    echo "  • Add google-services.json to androidApp/"
    echo "  • Add GoogleService-Info.plist to iosApp/"
fi

if [[ "$BACKEND_TYPE" == "ktor-server" ]]; then
    echo "  • Run the server: ./gradlew :server:app:bootRun"
    echo "  • Set BASE_URL in core/data to http://localhost:8080"
    echo "  • Apply DB migrations to your Supabase PostgreSQL instance"
else
    echo "  • Set BASE_URL in core/data/src/commonMain/.../networking/HttpConstants.kt"
fi

if [[ "$SETUP_SUPABASE_DB" == "true" ]]; then
    echo "  • Installer Supabase CLI : https://supabase.com/docs/guides/cli"
    echo "  • Démarrer Supabase local : supabase start"
    echo "  • Appliquer les migrations : supabase db push"
    echo "  • Peupler les données de test : supabase db seed"
    echo "  • Récupérer les clés locales : supabase status"
fi
echo "  • Customize colors in core/design_system/src/.../theme/Color.kt"
echo "  • Open the project in Android Studio and sync Gradle"
echo "  • Run: ./gradlew :androidApp:assembleDebug"
echo ""
echo "  To create a new feature:"
echo "    ./create-feature.sh <feature_name>"
echo ""
