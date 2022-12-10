#!/usr/bin/env bash

docker run \
 --env MYSQL_ROOT_PASSWORD=password \
 --env MYSQL_DATABASE=Shakespeare \
 --name mysql \
 --publish 3306:3306 \
 -rm \
 mysql:8