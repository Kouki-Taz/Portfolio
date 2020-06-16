#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define studentCount 10

typedef struct {
    char studentNumber[9];
    char familyName[12]; /* 姓 */
    char givenName[12]; /* 名 */
    int englishMark;
    int mathMark;
} Student;

void printStudentList (Student *students[], int l)
{
    int i;

    for ( i = 0; i < l; i++ ) {
        printf("%8s: %-10s %-8s: E=%3d, M=%3d, Av=%5.1f\n",
            students[i]->studentNumber,
            students[i]->familyName,
            students[i]->givenName,
            students[i]->englishMark,
            students[i]->mathMark,
            (students[i]->englishMark + students[i]->mathMark) / 2.0);
    }
}



/* 比較部分 */
int compareNumbersI (const void* a, const void* b)
{
    Student *studentPa = *((Student **) a);
    Student *studentPb = *((Student **) b);

    return strcmp (studentPa->studentNumber,
                   studentPb->studentNumber);
}
int compareNamesI (const void* a, const void* b)//(2)
{
    Student *studentPa = *((Student **) a);
    Student *studentPb = *((Student **) b);

    return strcmp (studentPa->givenName,
                   studentPb->givenName);
}
int compareFamilyNamesI (const void* a, const void* b)//(3)
{
    Student *studentPa = *((Student **) a);
    Student *studentPb = *((Student **) b);

    return strcmp (studentPa->familyName,
                   studentPb->familyName);
}
int compareenglishMarkD (const void* a, const void* b)//(4)
{
	int n;
    Student *studentPa = *((Student **) a);
    Student *studentPb = *((Student **) b);
	
	if(studentPb->englishMark > studentPa->englishMark) n=1;
	else if(studentPb->englishMark < studentPa->englishMark) n=-1;
	else n=0;

    return n;
}
int comparemathMarkD (const void* a, const void* b)//(5)
{
	int n;
    Student *studentPa = *((Student **) a);
    Student *studentPb = *((Student **) b);
	
	if(studentPb->mathMark > studentPa->mathMark) n=1;
	else if(studentPb->mathMark < studentPa->mathMark) n=-1;
	else n=0;

    return n;
}
int compareAverageD (const void* a, const void* b)//(6)
{
	int n,Avea=0,Aveb=0;
    Student *studentPa = *((Student **) a);
    Student *studentPb = *((Student **) b);
	
	Avea=(studentPa->mathMark+studentPa->englishMark)/2;
	Aveb=(studentPb->mathMark+studentPb->englishMark)/2;
	
	if(Aveb > Avea) n=1;
	else if(Aveb < Avea) n=-1;
	else n=0;

    return n;
}
int compareenglishMarkI (const void* a, const void* b)//(7)
{
	int n;
    Student *studentPa = *((Student **) a);
    Student *studentPb = *((Student **) b);
	
	if(studentPa->englishMark > studentPb->englishMark) n=1;
	else if(studentPa->englishMark < studentPb->englishMark) n=-1;
	else n=0;

    return n;
}
int comparemathMarkI (const void* a, const void* b)//(8)
{
	int n;
    Student *studentPa = *((Student **) a);
    Student *studentPb = *((Student **) b);
	
	if(studentPa->mathMark > studentPb->mathMark) n=1;
	else if(studentPa->mathMark < studentPb->mathMark) n=-1;
	else n=0;

    return n;
}
int compareAverageI (const void* a, const void* b)//(9)
{
	int n,Avea=0,Aveb=0;
    Student *studentPa = *((Student **) a);
    Student *studentPb = *((Student **) b);
	
	Avea=(studentPa->mathMark+studentPa->englishMark)/2;
	Aveb=(studentPb->mathMark+studentPb->englishMark)/2;
	
	if(Avea > Aveb) n=1;
	else if(Avea < Aveb) n=-1;
	else n=0;

    return n;
}
int compareNumbersD (const void* a, const void* b)//(10)
{
    Student *studentPa = *((Student **) a);
    Student *studentPb = *((Student **) b);

    return strcmp (studentPb->studentNumber,
                   studentPa->studentNumber);
}
int compareNamesD (const void* a, const void* b)//(11)
{
    Student *studentPa = *((Student **) a);
    Student *studentPb = *((Student **) b);

    return strcmp (studentPb->givenName,
                   studentPa->givenName);
}
int compareFamilyNamesD (const void* a, const void* b)//(12)
{
    Student *studentPa = *((Student **) a);
    Student *studentPb = *((Student **) b);

    return strcmp (studentPb->familyName,
                   studentPa->familyName);
}



int main (void)
{
    int i;
    int select;

    static Student students[] = {
        { "12345678", "Reigai",     "Taro",     80,  62 }, /* 例外 　　太郎 */
        { "12349875", "Reidai",     "Hanako",   76,  65 }, /* 例題 　　花子 */
        { "12349458", "Gambare",    "Manabu",   56,  66 }, /* 頑張 　　学 */
        { "12342584", "Sample",     "Tatoe",    34,  70 }, /* サンプル 例恵 */
        { "12348347", "Sugaku",     "Tokeko",   55, 100 }, /* 数学 　　解子 */
        { "12341948", "Girigiri",   "Tariyasu", 60,  60 }, /* ぎりぎり 足康 */
        { "12348463", "English",    "Perfect", 100,  56 }, /* 英語 　　完璧 */
        { "12347628", "Asobi",      "Saboro",   20,  25 }, /* 遊日 　　サボ郎 */
        { "12344924", "Kurikaeshi", "Mawaroh",  77,  30 }, /* 繰返 　　回郎 */
        { "12341369", "Seiretu",    "Junko",    69,  80 }  /* 整列 　　順子 */
    };

    /* 配列の定義 */
	Student *original_order[studentCount];//(0)
    Student *byNumber[studentCount];
	Student *nibann[studentCount];
	Student *sannbann[studentCount];
	Student *yonnbann[studentCount];
	Student *gobann[studentCount];
	Student *rokubann[studentCount];
	Student *nanabann[studentCount];
	Student *hatibann[studentCount];
	Student *kyuubann[studentCount];
	Student *juubann[studentCount];
	Student *juuitibann[studentCount];
	Student *juunibann[studentCount];

    /* 初期化 */
    for (i=0; i<studentCount; i++){
		original_order[i] = &students[i];//(0)
        byNumber[i] = &students[i];
		nibann[i] = &students[i];
		sannbann[i] = &students[i];
		yonnbann[i] = &students[i];
		gobann[i] = &students[i];
		rokubann[i] = &students[i];
		nanabann[i] = &students[i];
		hatibann[i] = &students[i];
		kyuubann[i] = &students[i];
		juubann[i] = &students[i];
		juuitibann[i] = &students[i];
		juunibann[i] = &students[i];
	}

    /* ソートする */
    qsort(byNumber, studentCount, sizeof(Student*), compareNumbersI);
	qsort(nibann, studentCount, sizeof(Student*), compareNamesI);
	qsort(sannbann, studentCount, sizeof(Student*), compareFamilyNamesI);
	qsort(yonnbann, studentCount, sizeof(Student*), compareenglishMarkD);
	qsort(gobann, studentCount, sizeof(Student*), comparemathMarkD);
	qsort(rokubann, studentCount, sizeof(Student*), compareAverageD);
	qsort(nanabann, studentCount, sizeof(Student*), compareenglishMarkI);
	qsort(hatibann, studentCount, sizeof(Student*), comparemathMarkI);
	qsort(kyuubann, studentCount, sizeof(Student*), compareAverageI);
	qsort(juubann, studentCount, sizeof(Student*), compareNumbersD);
	qsort(juuitibann, studentCount, sizeof(Student*), compareNamesD);
	qsort(juunibann, studentCount, sizeof(Student*), compareFamilyNamesD);

    /* 本体 */
    while (1) {
        printf ("Select order (0=original order, 1=by number (inc), 2=by given name (inc),\n"
                "    3=by family name (inc), 4=by English (dec), 5=by Math (dec), 6=by average (dec),\n"
                "    7=by English (inc), 8=by Math (inc), 9=by average (inc), 10=by number (dec),\n"
                "    11=by given name (dec), 12=by family name (dec)): ");

        if (scanf("%d", &select) != 1)
            return 0;
        putchar ('\n');

		if (select == 0)
            printStudentList(original_order, studentCount);
        else if (select == 1)
            printStudentList(byNumber, studentCount);
		else if (select == 2)
            printStudentList(nibann, studentCount);
		else if (select == 3)
            printStudentList(sannbann, studentCount);
		else if (select == 4)
            printStudentList(yonnbann, studentCount);
		else if (select == 5)
            printStudentList(gobann, studentCount);
		else if (select == 6)
            printStudentList(rokubann, studentCount);
		else if (select == 7)
            printStudentList(nanabann, studentCount);
		else if (select == 8)
            printStudentList(hatibann, studentCount);
		else if (select == 9)
            printStudentList(kyuubann, studentCount);
		else if (select == 10)
            printStudentList(juubann, studentCount);
		else if (select == 11)
            printStudentList(juuitibann, studentCount);
		else if (select == 12)
            printStudentList(juunibann, studentCount);
        else
            return 0;
    }
}
