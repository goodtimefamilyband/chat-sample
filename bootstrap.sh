#!/usr/bin/env bash

db='asapp'
db_usr='asapp'
db_pw='vjauir934vrj'

db_su='root'
db_su_pw='vagrant'

apt-get update
apt-get upgrade

debconf-set-selections <<< "mysql-server-5.5 mysql-server/root_password password $db_su_pw"
debconf-set-selections <<< "mysql-server-5.5 mysql-server/root_password_again password $db_su_pw"
    
#
# Ubuntu Equip 
#  Java 8 Equip
# Licence: MIT
# see http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html
# http://stackoverflow.com/questions/13018626/add-apt-repository-not-found
sudo echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
wget --no-check-certificate https://github.com/aglover/ubuntu-equip/raw/master/equip_base.sh && bash equip_base.sh

sudo add-apt-repository ppa:webupd8team/java -y
sudo apt-get update -y
sudo apt-get install oracle-java8-installer -y
sudo apt-get install ant -y

apt-get install --yes git \
    mysql-server-5.6 \

echo "CREATE USER '$db_usr' IDENTIFIED BY '$db_pw'" | mysql --user=$db_su --password=$db_su_pw
echo "CREATE DATABASE $db" | mysql --user=$db_su --password=$db_su_pw
echo "GRANT ALL PRIVILEGES ON ${db}.* TO '$db_usr'@'%'" | mysql --user=$db_su --password=$db_su_pw

cd /vagrant
mysql --user=$db_usr --password=$db_pw $db < asapp.sql
sudo sed -i "s/^bind-address/# bind-address/g" /etc/mysql/my.cnf
sudo service mysql restart
