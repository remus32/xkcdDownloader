#!/usr/bin/env bash
tar -cvzf "xkcdLib/src/main/resources/xkcd/cache.tar.gz" $( find "xkcdCache" -name "*.json" )