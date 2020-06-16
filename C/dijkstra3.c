//16816053
//田澤航樹

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

	/*ファイル1行目読み込み*/
	fgets(buf,sizeof(buf), fin);
	size = atoi(buf);
	//printf("ノードの数%d\n",size);

	/*メモリ確保*/
	c = (int**)calloc(size,sizeof(int*));
	for(i=0;i<size;i++)
		c[i] = (int*)calloc(size,sizeof(int));
	d = (int*)calloc(size,sizeof(int));
	fix = (int*)calloc(size,sizeof(int));

	/*配列初期化*/
	for(i=0; i<size; i++)
		d[i] = MAXLENGTH;

	/*2行目以降*/
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
		/*固定されていない点の中で*/
		if(fix[i] == 0){
			/*最小値を探す*/
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
		/*隣接していれば*/
		if(c[node][i] != 0)
			/*固定されていなければ*/
			if(fix[i] == 0)
				/*暫定最短距離が更新できるなら*/
				if(d[i] > c[node][i]+d[node])
					d[i] = c[node][i]+d[node];
	}
}


/*デバッグ用*/
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

/*再帰的に経路を求める*/
void FindPath(int index){
	int i;
	
	if(index != start){
		for(i=0; i<size; i++){
			/*隣接していれば*/
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

	/*データ入力*/
	printf("Input file: "); scanf("%s",filename);
	printf("Starting node: "); scanf("%d",&start);
	printf("Goal node: "); scanf("%d",&goal);
	
	printf("\n【Excercise6】\n");

	/*ファイル読み込み*/
	readValue(filename);

	/*各点の最短距離を求める*/
	d[start] = 0;//始点の暫定距離を0とする
	while(1){

		/*固定されていない点の中から最短を探し、固定*/
		index = findMin();
		fix[index] = 1;
		printf("Fixed node: %d, Distance from the starting node: %d\n",index,d[index]);

		 /*隣接点の暫定距離を更新*/
		 updateDistance(index);

		/*終了条件*/
		if(index == goal) break;
	}

	/*経路を調べる*/
	printf("\n【Excercise7】\n");
	printf("Input file: %s\n",argv[1]);
	printf("Shortest distance from node[%d] to node[%d]: %d\n",start, goal, d[goal]);
	FindPath(goal);

	return 0;
}