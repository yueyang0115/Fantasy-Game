#!/bin/bash
mkdir coverage
chmod u+x coverage
#docker run --rm -v `pwd`/coverage:/coverage-out  citest scripts/test.sh
docker-compose run server