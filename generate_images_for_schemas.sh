#!/bin/bash

PLANTUML_CMD="plantuml"

# shellcheck disable=SC2207
CHANGED_FILES=($(git diff --name-only | grep "^docs/schemas/.*\.puml$"))

if [ ${#CHANGED_FILES[@]} -eq 0 ]; then
    echo "No .puml files changed."
    exit 0
fi

for file in $CHANGED_FILES
do
    OUTPUT_FILE="$(basename "$file" .puml).svg"
    echo "$OUTPUT_FILE"
    echo "Generating SVG for $file -> docs/images/$OUTPUT_FILE"
    $PLANTUML_CMD -tsvg "$file" -o "../images" || exit 1
done

echo "PlantUML schemas are up to date"
exit 0
