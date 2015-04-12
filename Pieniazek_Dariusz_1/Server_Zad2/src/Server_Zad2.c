#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>
#include <unistd.h>


#define BUFLEN 10000

int main(int argc, char **argv) {

	int sock_fd, cli_fd, len;
	socklen_t cli_len;
	struct sockaddr_in serv_addr, cli_addr;
	char recvline[BUFLEN];

	if (argc != 2) {
		printf("usage: %s <TCP port>\n", argv[0]);  //2000
		exit(1);
	}

	if ((sock_fd = socket(AF_INET, SOCK_STREAM, 0)) == -1) { // create the socket (add missing arguments)
		perror("socket");
		exit(1);
	}

	bzero((char*)&serv_addr, sizeof(serv_addr)); // fill in the socket family, address and port
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	serv_addr.sin_port= htons(atoi(argv[1]));

	int so_reuseaddr = 1; // set SO_REUSEADDR socket option (please explain the option's meaning)
	if ((setsockopt(sock_fd,SOL_SOCKET,SO_REUSEADDR,&so_reuseaddr, sizeof so_reuseaddr) == -1)) {
		perror("setsockopt");
		exit(1);
	}

	if(bind(sock_fd, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) == -1) { // bind with the use of bind procedure
		perror("bind");
		exit(1);
	}

	if(listen(sock_fd, 5) == -1){ // start listening with the use of listen procedure
		perror("listen");
		exit(1);
	}

	if ((cli_fd = accept(sock_fd, (struct sockaddr*)&cli_addr, &cli_len)) == -1) { // accept the connection and assign descriptor to cli_fd
		perror("accept");
		exit(1);
	}

	while(1){
		len = recv(cli_fd, recvline, BUFLEN, 0); // receive data to recvline buffer with the "recv" system call and assign number of received bytes to len
		printf("Filename: %s\n", recvline);
		break;
	}
	FILE *fd = fopen(recvline,"w");
	if(!fd){
		perror("File coulnd not open");
		exit(1);
	}

	while(1){
		len = recv(cli_fd, recvline, BUFLEN, 0);//
		if(len<=0)
			break;
		if((len == -1)){
			perror("recv");
			exit(0);
		}
		printf("received bytes: %d\n", len);
		recvline[len] = 0;
		printf("received: %s\n", recvline);
		if(fwrite(recvline, 1, len, fd)== -1){
			perror("Coulnd not write to file");
			exit(1);
		}
	}
	if((close(cli_fd) == -1)){
		perror("close");
		exit(1);
	}
	close(sock_fd);
	fclose(fd);
	return 0;
}

