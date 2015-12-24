FROM mhart/alpine-node

MAINTAINER Oleksii Fedorov <waterlink000@gmail.com>

RUN npm install -g foundation-cli bower gulp

RUN apk add --update ruby git
RUN gem install --no-doc bundler sass
RUN apk add --update python
RUN apk add --update build-base

RUN npm install -g browserify

EXPOSE 8080

ENTRYPOINT []
