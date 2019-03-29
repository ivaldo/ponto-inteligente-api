@echo off
docker run --name mysql -d -p 3306:3306 -v c:/mysql-data-backup:/mysql-data-backup -e MYSQL_ROOT_PASSWORD=root mariadb
docker run --name myadmin -d --link mysql:db -p 8888:80 -e MYSQL_ROOT_PASSWORD=root phpmyadmin/phpmyadmin