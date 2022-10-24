# requires dockerhub login
sudo docker build . -t javashop.instruments
sudo docker tag javashop.instruments shabushabu/javashop.instruments
sudo docker push shabushabu/javashop.instruments

