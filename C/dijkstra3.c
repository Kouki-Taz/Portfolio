//16816053
//�c�V�q��

#include <stdio.h>
#include<string.h>
#include <stdlib.h>
#include <limits.h>

#define DELIMITER " "
#define MAXLENGTH 512

int **c;
int *d, *fix;
int size;
int start;
int goal;


void readValue(char *fname){
	int i;
	int col, row, v;
	char *token;
	char buf[1024];
	FILE *fin;
	if( (fin = fopen(fname,"r")) == NULL){
		printf("Can't open file: %s\n", fname);
		exit(1);
	}

	/*�t�@�C��1�s�ړǂݍ���*/
	fgets(buf,sizeof(buf), fin);
	size = atoi(buf);
	//printf("�m�[�h�̐�%d\n",size);

	/*�������m��*/
	c = (int**)calloc(size,sizeof(int*));
	for(i=0;i<size;i++)
		c[i] = (int*)calloc(size,sizeof(int));
	d = (int*)calloc(size,sizeof(int));
	fix = (int*)calloc(size,sizeof(int));

	/*�z�񏉊���*/
	for(i=0; i<size; i++)
		d[i] = MAXLENGTH;

	/*2�s�ڈȍ~*/
	while( fgets(buf,sizeof(buf), fin) != NULL){
		i=0;
		token = strtok(buf, DELIMITER);
		while( token!=NULL ){
			if(i==0) row = atoi(token);
			else if(i==1) col = atoi(token);
			else if(i==2) v = atoi(token);
			else break;
			token = strtok(NULL, DELIMITER);
			i++;
		}
		c[row][col] = v;
		c[col][row] = v;
		//printf("(i,j,v) = (%d, %d, %d)\n",row,col,v);
	}
}
int findMin(){
	int i, j;
	int min = MAXLENGTH, min_index = 0;

	for(i=0; i<size; i++){
		/*�Œ肳��Ă��Ȃ��_�̒���*/
		if(fix[i] == 0){
			/*�ŏ��l��T��*/
			if(min > d[i]){
				min = d[i];
				min_index = i;
			}
		}
	}
	return min_index;
}
void updateDistance(int node){
	int i;
	for(i=0; i<size; i++){
		/*�אڂ��Ă����*/
		if(c[node][i] != 0)
			/*�Œ肳��Ă��Ȃ����*/
			if(fix[i] == 0)
				/*�b��ŒZ�������X�V�ł���Ȃ�*/
				if(d[i] > c[node][i]+d[node])
					d[i] = c[node][i]+d[node];
	}
}


/*�f�o�b�O�p*/
void print_c(){
	int i, j;

	printf("  ");
	for (i = 0; i<size; i++)
		printf("|%3d", i);
	printf("\n");
	printf("--");
	for (i = 0; i<size; i++)
		printf("+---");
	printf("\n");

	for (i = 0; i<size; i++) {
		printf("%2d|", i);
		for (j = 0; j<size; j++) {
			if(c[i][j] == MAXLENGTH)
				printf("  X ");
			else
				printf("%3d ",c[i][j]);
		}
		printf("\n");
	}
}

/*�ċA�I�Ɍo�H�����߂�*/
void FindPath(int index){
	int i;
	
	if(index != start){
		for(i=0; i<size; i++){
			/*�אڂ��Ă����*/
			if(c[index][i] != 0)
				if(d[index] - c[index][i] == d[i])
					FindPath(i);
		}
		printf("-> (%d) -> node[%d]\n", d[index], index);
	}
	else
		printf("node[%d]\n",start);
}


int main(int argc, char *argv[]){
	int i, j;
	int index;
	char filename[256];

	/*�f�[�^����*/
	printf("Input file: "); scanf("%s",filename);
	printf("Starting node: "); scanf("%d",&start);
	printf("Goal node: "); scanf("%d",&goal);
	
	printf("\n�yExcercise6�z\n");

	/*�t�@�C���ǂݍ���*/
	readValue(filename);

	/*�e�_�̍ŒZ���������߂�*/
	d[start] = 0;//�n�_�̎b�苗����0�Ƃ���
	while(1){

		/*�Œ肳��Ă��Ȃ��_�̒�����ŒZ��T���A�Œ�*/
		index = findMin();
		fix[index] = 1;
		printf("Fixed node: %d, Distance from the starting node: %d\n",index,d[index]);

		 /*�אړ_�̎b�苗�����X�V*/
		 updateDistance(index);

		/*�I������*/
		if(index == goal) break;
	}

	/*�o�H�𒲂ׂ�*/
	printf("\n�yExcercise7�z\n");
	printf("Input file: %s\n",argv[1]);
	printf("Shortest distance from node[%d] to node[%d]: %d\n",start, goal, d[goal]);
	FindPath(goal);

	return 0;
}