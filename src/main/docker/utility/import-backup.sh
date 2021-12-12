cat backup.sql | docker exec -i docker_fluidqrserver-mysql_1 /usr/bin/mysql -u fluidqruser --password=fluidqruserpw fluidqrserver
