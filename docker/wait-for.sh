#!/usr/bin/env bash
set -e

HOST="$1"
PORT="$2"
shift 2

until (echo > /dev/tcp/${HOST}/${PORT}) >/dev/null 2>&1; do
  echo "Waiting for ${HOST}:${PORT}..."
  sleep 1
done

echo "${HOST}:${PORT} is available — starting app"
exec "$@"
