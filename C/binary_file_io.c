#include <stdio.h>

/*学生一人のデータは168バイト*/
typedef struct {
    int year;
    short month;
    short day;
} Date;

typedef struct {
    char name[40];
    char university[40];
    char department[40];
    Date birthday;
    char hobby[40];
} Student;

int add_student(Student *data);
void clean_string(char *string, int length);
void print_data(Student data);


int main(void)
{
	FILE *fp;
	int flag=0,i=0,seitosuu=1,n,check;
	Student data;
	
	if(!(fp=fopen("bininfo","r+"))){
		fprintf(stderr,"ファイルが見つかりませんでした。");
	}
	fseek(fp,0,SEEK_SET);
	fread(&seitosuu,sizeof(int),1,fp);
	//printf("%d\n",seitosuu);
	
	while(1){
		printf("Next choice, please: ");
		if(!(scanf("%d",&flag)))
			return 0;
		
		switch(flag){
			
			case 0://完成
				fclose(fp);
				return 0;
				
			case 1://完成した気がする
				for(i=0;i<seitosuu;i++){
					fseek(fp,4+(sizeof(Student)*i),SEEK_SET);
					if(!(fread(&data,sizeof(Student),1,fp)))//これでファイルから構造体に一人分読み取れた
						printf("fread失敗1\n");
					//fread(&data,sizeof(Student),1,fp);
					print_data(data);
				}
				break;
				
			case 2://完成
				printf("Enter student number: ");
				scanf("%d",&n);
				fseek(fp,4+168*(n-1),SEEK_SET);//開始部分の人数を考慮
				if(!(fread(&data,sizeof(Student),1,fp)))
					printf("fread失敗2\n");
				//fread(&data,sizeof(Student),1,fp)
				print_data(data);
				break;
			
			case 3://完成
				check=add_student(&data);
				if(check){
					fseek(fp,0,SEEK_END);//末尾に移動
					fwrite(&data,sizeof(Student),1,fp);//ファイルに書き込む
					seitosuu++;
					fseek(fp,0,SEEK_SET);//先頭に移動
					fwrite(&seitosuu,sizeof(int),1,fp);//生徒数書き出し
				}
				break;
		}
	}
	
	fclose(fp);
	return 0;
}

void print_data(Student data)
{
	printf("Name: %s\n",data.name);
	printf("University: %s\n",data.university);
	printf("Department: %s\n",data.department);
	printf("Birthday: %d/%hd/%hd\n",data.birthday.year,data.birthday.month,data.birthday.day);
	printf("Hobby: %s\n",data.hobby);
	printf("\n");
}


int add_student(Student *data)
{
	char c;
	
	printf("Input  next student, please:\n");//標準入力から読み込む
	c=getchar();
	
	printf("Name:");
	if(!(fgets(data->name,40,stdin)))
		return -1;

	printf("University:");
	if(!(fgets(data->university,40,stdin)))
		return -1;

	printf("Department:");
	if(!(fgets(data->department,40,stdin)))
		return -1;

	printf("Birthday:");
	if(!(scanf("%d/%hd/%hd",&data->birthday.year,&data->birthday.month,&data->birthday.day)))
		return -1;
	c=getchar();

	printf("Hobby:");
	if(!(fgets(data->hobby,40,stdin)))
		return -1;
	
	clean_string(data->name,sizeof(data->name));
	clean_string(data->university,40);
	clean_string(data->department,40);
	clean_string(data->hobby,40);
	
	return 1;
}

void clean_string(char *string, int length)
{
    int clean_flag = 0;
    char *end_string = string + length - 1;
    
    for ( ; string<end_string; string++) {
        if (*string=='\n' || *string=='\0')
            clean_flag = 1;
        if (clean_flag)
            *string = '\0';
    }
    *string = '\0';
}
