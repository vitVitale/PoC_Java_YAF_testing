#!/usr/bin/env bash
set -e

exited_containers=$(docker ps -q --filter "status=exited")

if [ -z "${exited_containers}" ]; then
  echo "All run..."
else
  echo "Has stopped containers..."
  docker start $exited_containers
  sleep 3
fi