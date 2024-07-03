#!/usr/bin/env bash
rm ~/Desktop/modiff.timeline.zip
zip -r ~/Desktop/modiff.timeline.zip . -x "target/*" -x ".git/*"
