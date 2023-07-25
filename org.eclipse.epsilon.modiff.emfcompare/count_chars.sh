#!/usr/bin/env bash
find output -iname "*plain.diff" -type f -exec sh -c 'echo -n "$(basename "{}"), "; wc -m < "{}"' \; | sort
find output -iname "*munidiff.diff" -type f -exec sh -c 'echo -n "$(basename "{}"), "; wc -m < "{}"' \; | sort
