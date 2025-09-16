cd accounts
(docker build . -f Dockerfile -t thedeno/accounts -t thedeno/accounts:0.0.1 && docker image push thedeno/accounts -a) > /dev/null 2>&1 &
cd ..
cd cards
(docker build . -f Dockerfile -t thedeno/cards -t thedeno/cards:0.0.1 && docker image push thedeno/cards -a) > /dev/null 2>&1 &
cd ..
cd configserver
(docker build . -f Dockerfile -t thedeno/configserver -t thedeno/configserver:0.0.1 && docker image push thedeno/configserver -a) > /dev/null 2>&1 &
cd ..
cd discoveryserver
(docker build . -f Dockerfile -t thedeno/discoveryserver -t thedeno/discoveryserver:0.0.1 && docker image push thedeno/discoveryserver -a) > /dev/null 2>&1 &
cd ..
echo "Waiting..."
wait
echo "Done!"