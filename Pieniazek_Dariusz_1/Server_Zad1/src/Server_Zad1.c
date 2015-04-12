#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>
#include <unistd.h>


#define BUFLEN 10000

int pi_numbers(int a) {
   int result = a%15;
   int piTable[15] = {2, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9};
   return piTable[result];
}

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

	while (1) {  	// By removing the while loop the server will turn off after receving a single 'ask'
					// Otherwise to turn it off use CTR+C  /  Terminate
		if ((cli_fd = accept(sock_fd, (struct sockaddr*)&cli_addr, &cli_len)) == -1) { // accept the connection and assign descriptor to cli_fd
				perror("accept");
				exit(1);
			}

		// receive data to recvline buffer with the "recv" system call and assign number of received bytes to len
		len = recv(cli_fd, recvline, BUFLEN, 0);//
		if((len == -1)){
			perror("recv");
			exit(0);
		}
		printf("received bytes: %d\n", len);
		recvline[len] = 0;
		printf("received: %s\n", recvline);

		int a = atoi(recvline);

		a = pi_numbers(a);
		char sendline[9];
		sendline[0] = '0' + a;
		if((len = send(cli_fd, sendline, strlen(sendline), 0))== -1){
			perror("send");
			exit(1);
		}
		printf("sent bytes: %d\n", len);
		printf("sent: %s\n", sendline);

		if((close(cli_fd) == -1)){
			perror("close");
			exit(1);
		}
	}
	close(sock_fd);
	return 0;
}

