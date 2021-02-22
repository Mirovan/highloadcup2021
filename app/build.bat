docker build -t mirovan/hl2021 .
docker tag mirovan/hl2021 stor.highloadcup.ru/rally/unique_alligator
docker push stor.highloadcup.ru/rally/unique_alligator

docker run -it -e ADDRESS=192.168.1.176 mirovan/hl2021