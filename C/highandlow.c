#include <stdio.h>
#include <stdlib.h> //乱数を発生させる関数 rand() を使うために必要
#include <time.h>   //実行開始時刻で乱数を変えるためにこれも必要となる
#define CARD_NUM 13 //カード枚数

//High, Lowを判定する関数
int hanteiHL(int p,int a[],int n);

//結果を表示する関数
void hyouji(int sengen[],int card[],int seikai[],int kekka[],int miss_sum);


void quicksort(int array[],int card[], int begin, int end)
{
 int i = begin;
 int j = end;
 int pivot;
 int *swap;
   
 pivot = array[ ( begin + end ) / 2 ];
  
 while( 1 )
 {
  while( array[i] < pivot ){ ++i; }
  while( array[j] > pivot ){ --j; }
  if( i >= j )break;
   
  
  //card配列の入れ替え
	&swap=card[j];
	&card[j]=&card[j+1];
	&card[j+1]=&swap;

	//ransuu配列の入れ替え
	unsigned int *swap_ransuu;
	&swap_ransuu=&array[j];
	&array[j]=&array[j+1];
	&array[j+1]=&swap_ransuu;
  i++;
  j--;
 }
  
 // 軸の左側をソートする
 if( begin < i - 1 ){ quicksort( array,card, begin, i - 1 ); }
 // 軸の右側をソートする
 if( j + 1 < end ){ quicksort( array, card,j + 1, end ); }
}


//main関数はここから
int main(void)
{
	int i,j;
	int card[CARD_NUM];
	int no_open[CARD_NUM];
	unsigned int ransuu[CARD_NUM];

	//カードの番号を格納する配列にカード番号を代入する
	for(i=0;i<CARD_NUM;i++)
	{
		card[i]=i+1;
	}

	//カード枚数だけ乱数を発生させる
    srand((unsigned)time(NULL));        //乱数の初期化はこう書く
    
    /*
     * ☆以上までは「カードを切る」ための準備。
     * 　以降から『A 地点』までが実際にカードの並び替えを行うプログラム
     */

	for(i=0;i<CARD_NUM;i++)
	{
		ransuu[i]=(unsigned)rand();
	}
	
	quicksort(ransuu,card,ransuu[0],ransuu[CARD_NUM-1]);
	
	
	/*
	//13枚のカードを「乱数」の小さい順に並べかえる(バブル・ソート)
	for(i=0;i<CARD_NUM-1;i++)
	{
		int j;
		for(j=0;j<(CARD_NUM-1)-i;j++)
		{
			if(ransuu[j]>ransuu[j+1])
			{
				//card配列の入れ替え
				int swap;
				swap=card[j];
				card[j]=card[j+1];
				card[j+1]=swap;

				//ransuu配列の入れ替え
				unsigned int swap_ransuu;
				swap_ransuu=ransuu[j];
				ransuu[j]=ransuu[j+1];
				ransuu[j+1]=swap_ransuu;
/*-----  A地点  -----*//*
			}
		}
	}*/

	//乱数が小さい順にソートされているかを確認するプログラム
	for(i=0;i<CARD_NUM;i++)
	{
		printf("%10ld\n",ransuu[i]);	// %ld で表示するのがコツ
	}

/*-----  B地点  -----*/

	/*
	 * ☆以降では実際のゲームを進めていく。
	 * 　p. 1-9 の概要における [2] から [5] までが行われる
	 */

	for(i=0;i<CARD_NUM;i++)
	{
		no_open[i]=1;	//まだ出ていなければ 1 である。
	}

	//「正解」データの作成
	int seikai[CARD_NUM-1];	//ゲームは12回行う(≠13回)ので、配列も12個で充分
	for(i=0;i<(CARD_NUM-1);i++)
	{
		if(card[i]<card[i+1])
			seikai[i]=1;
		else
			seikai[i]=0;
	}

	//「宣言」データの作成および外れたか当たったかの「結果」を記録する。
	int sengen[CARD_NUM-1];
	int kekka[CARD_NUM-1];
	for(i=0;i<CARD_NUM-1;i++)
	{
		no_open[ card[i]-1 ]=0;
		//sengen[i]=hanteiHL(card[i],no_open,CARD_NUM);
		
		if(i<CARD_NUM-2){                                                               //書き加えた部分
			printf("めくったカード : %d\n",card[i]);
			printf("High(1) か Low(0) かを宣言 : ");
			scanf("%d",&sengen[i]);
		}
		else printf("めくったカード : %d\n",card[i]);
		
		if(sengen[i]==seikai[i]){
			kekka[i]=0;
			printf("当たり\n");
		}
		else{
			kekka[i]=1;
			printf("はずれ\n");
		}
		printf("\n");
	}
	
	
/*
	//外れたか当たったかの「結果」を記録する。
	int kekka[CARD_NUM-1];
	for(i=0;i<(CARD_NUM-1);i++)
	{
		if(sengen[i]==seikai[i])
			kekka[i]=0;
		else
			kekka[i]=1;
	}*/
	
	
/*-----  C地点  -----*/

	//全部で何回ミスしたかを計算する
	int miss_sum=0;
	for(i=0;i<(CARD_NUM-1);i++)
	{
		miss_sum+=kekka[i];
	}

	//表示関数で結果を表示する
	hyouji(sengen,card,seikai,kekka,miss_sum);
}

/*
 * ☆main 関数は以上。
 * 　以降では重要なサブルーチンの実装を行っている
 */


//HighかLowかを判定する関数はここから
int hanteiHL(int p,int a[],int n)
{
	int i,small=0,big=0;

	for(i=0;i<p;i++)
		small+=a[i];

	for(i=p+1;i<n;i++)
		big+=a[i];

	if(big>small) return 1;
	else return 0;
}

//結果を表示する関数はここから
void hyouji(int sengen[],int card[],int seikai[],int kekka[],int miss_sum)
{
	int i;
	printf("%8s%8s%8s%8s%12s\n","回数","宣言","カード","正解","ミスか？");
	printf("\n%24d\n",card[0]);
	printf("-----------------------------------------------\n");
	for(i=0;i<(CARD_NUM-1);i++)
	{
		printf("%4d回目・・",i+1);
		printf("%4d",sengen[i]);
		printf("%8d",card[i+1]);
		printf("%8d",seikai[i]);
		printf("%12d\n",kekka[i]);
	}
	printf("-----------------------------------------------\n");
	printf("%33sミス合計%3d回\n","",miss_sum);
}
 

