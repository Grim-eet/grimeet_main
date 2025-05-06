FROM ubuntu:latest
LABEL authors="jgone2"

ENTRYPOINT ["top", "-b"]