call mvn clean package
call docker build -t mirovan/hl2021 .
call docker tag mirovan/hl2021 stor.highloadcup.ru/rally/hamster_flyer
call docker push stor.highloadcup.ru/rally/hamster_flyer

@REM docker run -it -e ADDRESS=192.168.1.176 mirovan/hl2021