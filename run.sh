#!/bin/bash
docker build -t my-redis-image .
docker-compose up -d
