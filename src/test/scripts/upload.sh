#!/bin/bash

readonly this=$(readlink -f $0)

curl -v -k localhost:8080/upload -F creator=konopski -F issue=BUG-997 -F file1=@${this}

