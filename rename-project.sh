#!/usr/bin/env bash
# Usage: ./rename-project.sh --package com.company.myapp --name MyApp --prefix My
# Renames all package references, app name, and component prefix across the project.

set -e

usage() {
    echo "Usage: $0 --package <new.package.name> --name <AppName> --prefix <Prefix>"
    echo "  --package  New package (e.g. com.mycompany.myapp)"
    echo "  --name     App name    (e.g. MyApp)"
    echo "  --prefix   Component prefix (e.g. My)"
    exit 1
}

NEW_PACKAGE=""
NEW_NAME=""
NEW_PREFIX=""

while [[ $# -gt 0 ]]; do
    case "$1" in
        --package) NEW_PACKAGE="$2"; shift 2 ;;
        --name)    NEW_NAME="$2";    shift 2 ;;
        --prefix)  NEW_PREFIX="$2";  shift 2 ;;
        *) usage ;;
    esac
done

[[ -z "$NEW_PACKAGE" || -z "$NEW_NAME" || -z "$NEW_PREFIX" ]] && usage

OLD_PACKAGE="empire.digiprem.kmptemplate"
OLD_PACKAGE_SHARED="empire.digiprem.kmp_project_tamplate"
OLD_NAME="KmpTemplate"
OLD_NAME_TITLE="Kmp_project_tamplate"
OLD_PREFIX="App"

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  Renaming project"
echo "  Package : $OLD_PACKAGE  →  $NEW_PACKAGE"
echo "  Name    : $OLD_NAME     →  $NEW_NAME"
echo "  Prefix  : $OLD_PREFIX   →  $NEW_PREFIX"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# ── 1. Replace in source files ────────────────────────────────────────────────
NEW_PACKAGE_SHARED="${NEW_PACKAGE//./_}"
NEW_PACKAGE_SHARED=$(echo "$NEW_PACKAGE" | tr '.' '_')

find . -type f \( \
    -name "*.kt" -o -name "*.kts" -o -name "*.xml" \
    -o -name "*.swift" -o -name "*.xcconfig" \
    -o -name "*.properties" -o -name "*.toml" \
    -o -name "*.html" -o -name "*.css" \
\) \
    -not -path "./.git/*" \
    -not -path "*/build/*" \
    -not -path "*/node_modules/*" \
    | while read -r file; do
        sed -i '' \
            -e "s/${OLD_PACKAGE}/${NEW_PACKAGE}/g" \
            -e "s/${OLD_PACKAGE_SHARED}/${NEW_PACKAGE}/g" \
            -e "s/${OLD_NAME_TITLE}/${NEW_NAME}/g" \
            -e "s/${OLD_NAME}/${NEW_NAME}/g" \
            "$file"

        # Rename component prefix (only if different)
        if [[ "$OLD_PREFIX" != "$NEW_PREFIX" ]]; then
            sed -i '' \
                -e "s/${OLD_PREFIX}Button/${NEW_PREFIX}Button/g" \
                -e "s/${OLD_PREFIX}TextField/${NEW_PREFIX}TextField/g" \
                -e "s/${OLD_PREFIX}PasswordTextField/${NEW_PREFIX}PasswordTextField/g" \
                -e "s/${OLD_PREFIX}OtpTextField/${NEW_PREFIX}OtpTextField/g" \
                -e "s/${OLD_PREFIX}Image/${NEW_PREFIX}Image/g" \
                -e "s/${OLD_PREFIX}Icon/${NEW_PREFIX}Icon/g" \
                -e "s/${OLD_PREFIX}Theme/${NEW_PREFIX}Theme/g" \
                -e "s/${OLD_PREFIX}TopBar/${NEW_PREFIX}TopBar/g" \
                -e "s/${OLD_PREFIX}Divider/${NEW_PREFIX}Divider/g" \
                -e "s/${OLD_PREFIX}CardWrapper/${NEW_PREFIX}CardWrapper/g" \
                -e "s/${OLD_PREFIX}PageWrapper/${NEW_PREFIX}PageWrapper/g" \
                -e "s/${OLD_PREFIX}PageState/${NEW_PREFIX}PageState/g" \
                -e "s/${OLD_PREFIX}LoadingSpinner/${NEW_PREFIX}LoadingSpinner/g" \
                -e "s/${OLD_PREFIX}StateContent/${NEW_PREFIX}StateContent/g" \
                -e "s/${OLD_PREFIX}EmptyContent/${NEW_PREFIX}EmptyContent/g" \
                -e "s/${OLD_PREFIX}AnimatedVisibility/${NEW_PREFIX}AnimatedVisibility/g" \
                -e "s/${OLD_PREFIX}AlertDialog/${NEW_PREFIX}AlertDialog/g" \
                -e "s/${OLD_PREFIX}ConfirmDialog/${NEW_PREFIX}ConfirmDialog/g" \
                -e "s/${OLD_PREFIX}ErrorDialog/${NEW_PREFIX}ErrorDialog/g" \
                -e "s/${OLD_PREFIX}ButtonType/${NEW_PREFIX}ButtonType/g" \
                -e "s/${OLD_PREFIX}ImageSource/${NEW_PREFIX}ImageSource/g" \
                -e "s/${OLD_PREFIX}IconResource/${NEW_PREFIX}IconResource/g" \
                -e "s/${OLD_PREFIX}Settings/${NEW_PREFIX}Settings/g" \
                "$file"
        fi
    done

# ── 2. Rename source directories (package path) ───────────────────────────────
OLD_PATH=$(echo "$OLD_PACKAGE" | tr '.' '/')
NEW_PATH=$(echo "$NEW_PACKAGE" | tr '.' '/')

find . -type d -name "$(basename "$OLD_PATH")" -not -path "./.git/*" -not -path "*/build/*" \
    | sort -r \
    | while read -r dir; do
        NEW_DIR="${dir/$OLD_PATH/$NEW_PATH}"
        if [[ "$dir" != "$NEW_DIR" ]]; then
            mkdir -p "$(dirname "$NEW_DIR")"
            mv "$dir" "$NEW_DIR" 2>/dev/null || true
        fi
    done

# ── 3. Update gradle.properties ───────────────────────────────────────────────
sed -i '' \
    -e "s/app.package=.*/app.package=${NEW_PACKAGE}/" \
    -e "s/app.name=.*/app.name=${NEW_NAME}/" \
    -e "s/app.component.prefix=.*/app.component.prefix=${NEW_PREFIX}/" \
    gradle.properties

echo ""
echo "✅ Done! Open the project in Android Studio and sync Gradle."
