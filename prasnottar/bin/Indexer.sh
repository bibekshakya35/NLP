#!/usr/bin/env bash
#/bin/sh
export MEM_ARGS="-Xms512m -Xmx512m"
RUN_CMD="java -cp ../target/prasnottar.jar com.prasnottar.indexing.FeedData"
echo $RUN_CMD
exec $RUN_CMD

