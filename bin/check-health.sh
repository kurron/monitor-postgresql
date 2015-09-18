#!/bin/bash

curl --verbose localhost:8400/operations/health | python -m json.tool

