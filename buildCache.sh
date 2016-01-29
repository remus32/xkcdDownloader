#!/usr/bin/env bash
cd src/main/resources/xkcd
tar -cvzf "cache.tar.gz" $( find "../../../../cache" -name "*.json" )