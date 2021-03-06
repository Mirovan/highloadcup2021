docker build -t mirovan/hl2021 .
docker tag mirovan/hl2021 stor.highloadcup.ru/rally/hamster_flyer
docker push stor.highloadcup.ru/rally/hamster_flyer

#docker run -it -e ADDRESS=192.168.1.176 mirovan/hl2021