#include <stdio.h>

typedef struct{//自販機内のお金の数
	int money;
	int number;
}STOCK;

typedef struct{//自販機のメニュー
	char name[20];
	int price;
}MENU;

/**/
void print_menu();
int pay_money(int pay[],STOCK *stock);
void judge(int sum,int price,int oturi[],int pay[],STOCK *stock);
int cul(int sum,int price,int oturi[],int pay[],STOCK *stock);
void print_result(int pay[],int oturi[],STOCK *stock);
void print_result2(int buffer[]);
void print_result3(int x,STOCK *stock);
void initialize(int a[]);

int main(void)
{
	int n,sum=0;
	int pay[5]={0},oturi[5]={0};
	STOCK stock[5]={{1000,0},{500,10},{100,10},{50,10},{10,10}};//自販機内のお金と数の初期化
	MENU menu[5]={{"Cola",110},{"Milk tea",140},{"Coffee",210},{"Tomato juice",190},{"Shimijimi",120}};
	
	print_menu(menu);//メニュー出力
	while((scanf("%d",&n))==1){//買うジュース決定
		printf("\n");
		printf("You are buying a %s (price: %d Yen). Please insert your money.\n",menu[n-1].name,menu[n-1].price);
		sum=pay_money(pay,stock);//入れるお金の数をpay[]に入力、ついでに払うお金も記録
		
		judge(sum,menu[n-1].price,oturi,pay,stock);//払ったお金に対して買えるかどうかを判定し、出力
		print_result(pay,oturi,stock);//自販機の残りのお金を出力
		
		/*一周終わった時の処理*/
		initialize(oturi);//お釣りの配列初期化
		initialize(pay);//支払の配列を初期化
		print_menu(menu);//メニュー出力
	}
		
	return 0;
}

void print_menu(MENU menu[])
{
	int i;
	for(i=0;i<5;i++)
		printf("%d: %-13s%d Yen\n",i+1,menu[i].name,menu[i].price);
	printf("Enter the number of the drink you want to buy (or Ctrl+D to finish): ");
}
int pay_money(int pay[],STOCK *stock)
{
	int i,sum=0;
	for(i=0;i<5;i++){
		printf("%4d Yen: How many? ",stock[i].money);
		scanf("%d",&pay[i]);
		sum+=pay[i]*stock[i].money;
	}
	printf("\n");
	return sum;
}


void judge(int sum,int price,int oturi[],int pay[],STOCK *stock)
{
	int i,flag=0;
	flag=cul(sum,price,oturi,pay,stock);
	if(flag==1){
		printf("Price: %d Yen, not enough money.\n",price);//支払いがたりない→お金を返す
		for(i=0;i<5;i++)//支払いをそのまま返す
			oturi[i] = pay[i];
	}
	else if(flag==2){
		printf("Price: %d Yen, not enough change.\n",price);//自販機内のお金がたりない→お金を返す
		for(i=0;i<5;i++)//支払いをそのまま返す
			oturi[i] = pay[i];
	}
	else if(flag==3){
		printf("Price: %d Yen, no change needed.\n",price);//ぴったり払った
		for(i=0;i<5;i++)//支払いをそのまま返す
			stock[i].number += pay[i];
	}
	
	else if(flag==4){
		printf("Price: %d Yen, change available.\n",price);//お釣りを出す
		for(i=0;i<5;i++)//自販機ストック更新(お釣りの分引いてる)
			stock[i].number -= oturi[i];
	}
}

int cul(int pay,int price,int oturi[],int paybuf[],STOCK *stock)
{
	int i,j,k,flag=0,sa=pay-price;
	int stock2[5];
	/*
	for(i=0;i<5;i++){//自販機ストックに支払分を足してる
		stock[i].number+=paybuf[i];
		stock2[i]=stock[i].number;
	}*/
	/*
	printf("1\n");
	for(i=0;i<5;i++){
		printf("stock2[%d]=%d\n",i,stock2[i]);
		printf("stock[%d].number=%d\n",i,stock[i].number);
	}*/
	
	if(sa<0)
		return 1;
	else if(sa==0)
		return 3;
	else{
		for(i=0;i<5;i++){//自販機ストックに支払分を足してる
			stock[i].number+=paybuf[i];
			stock2[i]=stock[i].number;
		}
			
		for(i=0;i<5;i++){
			k=sa;
			j=0;
			while(k>=0){
				k-=stock[i].money;
				j++;//j-1がその金額単位で何回引けるか
			}
			if(j>=2){
				sa-=stock[i].money*(j-1);
				oturi[i]=j-1;
				stock2[i]-=(j-1);
			}
		}
	}
	/*
	printf("2\n");
	for(i=0;i<5;i++){
		printf("stock2[%d]=%d\n",i,stock2[i]);
		printf("stock[%d].number=%d\n",i,stock[i].number);
	}
	*/
	
	/*より小さい硬貨で支払えるか考える*/
	if(stock2[0]<0 || stock2[1]<0 || stock2[2]<0 || stock2[3]<0 || stock2[4]<0){//もし数がマイナスのものがあるなら
		for(i=0;i<5;i++){
			if(stock2[i]<0){//1000~10円のうち個数がマイナスだったら
				for(j=i;j<4;j++){//判定は50円まででok
					if((stock2[i]*(-1))*stock[i].money/stock[j+1].money <= stock2[j+1]){
						oturi[i]-=(stock2[i]*(-1));
						oturi[j+1]+=(stock2[i]*(-1))*stock[i].money/stock[j+1].money;
						stock2[j+1]-=(stock2[i]*(-1))*stock[i].money/stock[j+1].money;
						stock2[i]=0;
					}
				}
			}
		}
	}
	/*
	printf("3\n");
	for(i=0;i<5;i++){
		printf("stock2[%d]=%d\n",i,stock2[i]);
		printf("stock[%d]=%d\n",i,stock[i].number);
	}*/
	
	
	if(stock2[0]>=0 && stock2[1]>=0 && stock2[2]>=0 && stock2[3]>=0 && stock2[4]>=0){//お釣りが払える場合
		return 4;
	}
	else{//お釣りが足りない場合
		for(i=0;i<5;i++)//自販機ストックに支払分を引いている
			stock[i].number-=paybuf[i];
		return 2;
	}
}


void print_result(int pay[],int oturi[],STOCK *stock)//最後の結果を出力
{
	int i;
	printf("Denominations (Yen):");//お金の種類
	print_result3(1,stock);
	printf("Money paid:         ");//支払い
	print_result2(pay);
	printf("Change given back:  ");//おつり
	print_result2(oturi);
	printf("Remaining stock:     ");//残り
	print_result3(2,stock);
	
	printf("\n");
}
void print_result2(int buffer[])
{
	int i;
	for(i=0;i<5;i++)
		printf("%5d",buffer[i]);
	printf("\n");
}
void print_result3(int x,STOCK *stock)
{
	int i;
	for(i=0;i<5;i++)
		printf("%5d",(x==1)? stock[i].money:stock[i].number);
	printf("\n");
}


void initialize(int a[])
{
	int i;
	for(i=0;i<5;i++)
		a[i]=0;
}
