#!/bin/bash
# post_starting_data.sh
# This script posts all JSON files matching:
#   - "attributeset_*.json" to /v1/admin/attributesets
#
# Place this script in the directory containing your JSON files.

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Enable nullglob so that if no files match the pattern, the loop won't iterate over a literal pattern.
shopt -s nullglob

# Post attributeset JSON files
ATTR_TARGET_URL="http://localhost:8080/v1/admin/attributesets"
attr_files=("$SCRIPT_DIR"/attributeset_*.json)

if [ ${#attr_files[@]} -eq 0 ]; then
    echo "No attributeset JSON files found in $SCRIPT_DIR"
else
    echo "Posting attributeset JSON files:"
    for jsonFile in "${attr_files[@]}"; do
        echo "Posting file: $jsonFile to $ATTR_TARGET_URL"
        curl -X POST -H "Content-Type: application/json" -d @"$jsonFile" "$ATTR_TARGET_URL"
        echo -e "\n"
    done
fi

# Disable nullglob to restore default behavior
shopt -u nullglob

echo "Finished posting starting data."
