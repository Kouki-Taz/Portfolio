//15816053
//�c�V�q��

#include <stdio.h>
#include <stdlib.h>
#include<string.h>
#include <time.h>


int row, col;
int degree;
int mode = 0;


/*�ċA�I�Ɏ�����J��*/
void RecursivelyOpen(int **data, int **open, int **mark, int i, int j, int *remain) {

	/*�����}�[�N����Ă����牽�����Ȃ�*/
	if (mark[i][j] == 1)
		return;
	/*���̃Z�����J��*/
	open[i][j] = 1;
	*remain -= 1;

	/*�������̃Z����0��������*/
	if (data[i][j] == 0) {
		/*�オ���݂���ꍇ*/
		if (i - 1 >= 0)
			if (open[i - 1][j] == 0) RecursivelyOpen(data, open, mark, i - 1, j, remain);//2
																						 /*�������݂���ꍇ*/
		if (i + 1 <= row - 1)
			if (open[i + 1][j] == 0) RecursivelyOpen(data, open, mark, i + 1, j, remain);//8
																						 /*�������݂���ꍇ*/
		if (j - 1 >= 0)
			if (open[i][j - 1] == 0) RecursivelyOpen(data, open, mark, i, j - 1, remain);//4
																						 /*�E�����݂���ꍇ*/
		if (j + 1 <= col - 1)
			if (open[i][j + 1] == 0) RecursivelyOpen(data, open, mark, i, j + 1, remain);//6
																						 /*���オ���݂���ꍇ*/
		if (i - 1 >= 0 && j - 1 >= 0)
			if (open[i - 1][j - 1] == 0) RecursivelyOpen(data, open, mark, i - 1, j - 1, remain);//1
																								 /*�E�オ���݂���ꍇ*/
		if (i - 1 >= 0 && j + 1 <= col - 1)
			if (open[i - 1][j + 1] == 0) RecursivelyOpen(data, open, mark, i - 1, j + 1, remain);//3
																								 /*���������݂���ꍇ*/
		if (i + 1 <= row - 1 && j - 1 >= 0)
			if (open[i + 1][j - 1] == 0) RecursivelyOpen(data, open, mark, i + 1, j - 1, remain);//7
																								 /*�E�������݂���ꍇ*/
		if (i + 1 <= row - 1 && j + 1 <= col - 1)
			if (open[i + 1][j + 1] == 0) RecursivelyOpen(data, open, mark, i + 1, j + 1, remain);//9
	}
}


/*����̒n���̐��𐔂��ăZ�b�g����*/
void SetNum(int **data) {
	int i, j;
	int cnt;

	for (i = 0; i<row; i++) {
		for (j = 0; j<col; j++) {
			if (data[i][j] != -1) {//���̃Z�����n���łȂ��ꍇ
				cnt = 0;
				/*�オ���݂���ꍇ*/
				if (i - 1 >= 0)
					if (data[i - 1][j] == -1) cnt++;//2
													/*�������݂���ꍇ*/
				if (i + 1 <= row - 1)
					if (data[i + 1][j] == -1) cnt++;//8
													/*�������݂���ꍇ*/
				if (j - 1 >= 0)
					if (data[i][j - 1] == -1) cnt++;//4
													/*�E�����݂���ꍇ*/
				if (j + 1 <= col - 1)
					if (data[i][j + 1] == -1) cnt++;//6
													/*���オ���݂���ꍇ*/
				if (i - 1 >= 0 && j - 1 >= 0)
					if (data[i - 1][j - 1] == -1) cnt++;//1
														/*�E�オ���݂���ꍇ*/
				if (i - 1 >= 0 && j + 1 <= col - 1)
					if (data[i - 1][j + 1] == -1) cnt++;//3
														/*���������݂���ꍇ*/
				if (i + 1 <= row - 1 && j - 1 >= 0)
					if (data[i + 1][j - 1] == -1) cnt++;//7
														/*�E�������݂���ꍇ*/
				if (i + 1 <= row - 1 && j + 1 <= col - 1)
					if (data[i + 1][j + 1] == -1) cnt++;//9

				data[i][j] = cnt;
			}
		}
	}
}

/*�Ֆʂ��o��*/
void OutputTable(int **data, int **open, int **mark) {
	int i, j;

	printf("  ");
	for (i = 0; i<col; i++)
		printf("|%3d", i);
	printf("\n");
	printf("--");
	for (i = 0; i<col; i++)
		printf("+---");
	printf("\n");

	for (i = 0; i<row; i++) {
		printf("%2d", i);
		for (j = 0; j<col; j++) {
			if (mark[i][j] == 1) {
				printf("| M ");
			}
			else if (mark[i][j] == 0) {
				switch (open[i][j]) {
				case 1:
					if (data[i][j] == -1)
						printf("| X ");
					else
						printf("| %d ", data[i][j]);
					break;
				case 0:
					printf("| ? ");
					break;
				}
			}
		}
		printf("\n");
	}
}

/*�ŏ��ɒn���𓥂񂾎��̏���*/
void replace(int **data, int n1, int n2) {
	int i, j;
	int flag = 0;

	for (i = 0; i < row; i++) {
		for (j = 0; j < col; j++)
			if (data[i][j] != -1) {
				data[i][j] = -1;
				flag++;
				break;
			}
		if (flag == 1)
			break;
	}
	data[n1][n2] = 0;
	SetNum(data);
	//printf("�n���̍Đݒu���� [%d][%d] -> [%d][%d]\n",n1,n2,i,j);
}

/*�����ǂݍ��݂̊֐�*/
void scan_1(int *variable) {
	char buf[10];
	*variable = -1;
	fgets(buf, sizeof(buf), stdin);
	if (strlen(buf) == 2 || strlen(buf) == 3)
		sscanf(buf, "%d\n", variable);
	//printf("�ǂݍ��݊��� %d\n",*variable);
}
void scan_2(int *variable1, int *variable2) {
	char buf[10];
	*variable1 = -1;
	*variable2 = -1;
	fgets(buf, sizeof(buf), stdin);
	if (strlen(buf) >= 4 && strlen(buf) <= 6 && (buf[1] == ' ' || buf[2] == ' ') && buf[3] != ' ')
		sscanf(buf, "%d %d\n", variable1, variable2);
	//printf("�ǂݍ��݊��� %d %d\n", *variable1, *variable2);
}

int main() {
	int i, j;
	int n1, n2;
	int mined = 0;
	int remain = 0;
	int mark_num = 0;
	int not_opened = 0;
	int **data;
	int **open;
	int **mark;

	/*�T�C�Y����́��������m��*/
	while (1) {
		printf("Enter the number of rows (an integer within the range from 5 to 15):");
		scan_1(&row);
		if (row >= 5 && row <= 15)
			break;
		printf("Invalid value! Enter an integer within the range from 5 to 15.\n");
	}
	while (1) {
		printf("Enter the number of colums (an integer within the range from 5 to 15):");
		scan_1(&col);
		if (col >= 5 && col <= 15)
			break;
		printf("Invalid value! Enter an integer within the range from 5 to 15.\n");
	}
	data = (int**)malloc(row * sizeof(int*));
	for (i = 0; i<row; i++)
		data[i] = (int*)malloc(col * sizeof(int));
	open = (int**)malloc(row * sizeof(int*));
	for (i = 0; i<row; i++)
		open[i] = (int*)malloc(col * sizeof(int));
	mark = (int**)malloc(row * sizeof(int*));
	for (i = 0; i<row; i++)
		mark[i] = (int*)calloc(col, sizeof(int));


	/*��Փx�I��*/
	while (1) {
		printf("Select the degree of difficulty of the game(1: novice. 2: middle. 3: advance.):");
		scan_1(&degree);
		if (degree >= 1 && degree <= 3)
			break;
		printf("Invalid value! Enter either 1. 2. or 3.\n");
	}

	/*�������(close)*/
	for (i = 0; i<row; i++)
		for (j = 0; j<col; j++)
			open[i][j] = 0;

	/*�n�����m�������_���ɐݒu*/
	srand((unsigned int)time(NULL));
	for (i = 0; i<row; i++) {
		for (j = 0; j<col; j++) {
			if (rand() % (10 / degree) == 0)//�m�� degree = 1:0.1, 2:0.2, 3:0.333
				data[i][j] = -1;
			else
				data[i][j] = 0;
		}
	}

	/*����ɒn���������邩���Z�b�g*/
	SetNum(data);

	/*�Ֆʂ��o��*/
	OutputTable(data, open, mark);

	/*�n���̐���\��*/
	for (i = 0; i<row; i++)
		for (j = 0; j<col; j++)
			if (data[i][j] == -1)
				mined++;
	printf("The number of mined cells: %d\n", mined);

	/*�c���Ă���Z���̐���������*/
	remain = col * row - mined;




	/*�Q�[���̃��C�����[�v*/
	while (1) {

		/*��ʂ�?���Ȃ��Ȃ����ꍇ�A�����I��remove mode�ֈڍs*/
		not_opened = 0;
		for (i = 0; i<row; i++)
			for (j = 0; j<col; j++)
				if (open[i][j] == 1 || mark[i][j] == 1)//�J����Ă���or�}�[�N�ς�
					not_opened++;
		if (not_opened == col * row)
			mode = 3;

		/*���[�h�I��*/
		mark_num = 0;/*M�����邩�m�F*/
		for (i = 0; i<row; i++)
			for (j = 0; j<col; j++)
				if (mark[i][j] == 1)
					mark_num++;
		if (mode == 0) {
			while (1) {
				printf("Select the operation (1: open, 2: mark");
				if (mark_num > 0)
					printf(", 3: remove marking");
				printf(")");
				scan_1(&mode);
				if (mode == 1 || mode == 2 || (mark_num>0 && mode == 3))
					break;
				printf("Invalid Value!\n");
			}
		}



		/*open���[�h*/
		if (mode == 1) {
			printf("Enter the coordinate of a cell to open\n");
			printf("(row and column numbers divide by an space):");
			scan_2(&n1, &n2);
			/*index���s���ȏꍇ*/
			if (n1<0 || n1>(row - 1) || n2<0 || n2>(col - 1))
				printf("Incorrect coordinate!\n");
			/*�Z�������ɊJ����Ă����ꍇ*/
			else if (open[n1][n2] == 1)
				printf("The cell is already open.\n");
			/*���C������*/
			else {
				/*���̃Z�����J��(mark����ĂȂ����)*/
				if (mark[n1][n2] == 1)
					printf("To open the cell, remove its marking first.\n");
				else {
					/*�������߂ɑI�񂾃Z�������e��������*/
					if (remain == col * row - mined && data[n1][n2] == -1)
						replace(data, n1, n2);

					RecursivelyOpen(data, open, mark, n1, n2, &remain);
					/*���̍��W���n���Ȃ�GAME OVER*/
					if (data[n1][n2] == -1) {
						for (i = 0; i<row; i++)
							for (j = 0; j<col; j++) {
								mark[i][j] = 0;
								open[i][j] = 1;
							}
						OutputTable(data, open, mark);
						printf("GAME OVER!\n");
						break;
					}
					mode = 0;
				}
			}
			/*�c��̃Z����0�ɂȂ����ꍇ�A�Q�[���I��*/
			if (remain == 0 || mined == 0) {
				for (i = 0; i<row; i++)
					for (j = 0; j<col; j++) {
						mark[i][j] = 0;
						open[i][j] = 1;
					}
				OutputTable(data, open, mark);
				printf("Congratulations!\n");
				break;
			}
		}


		/*mark���[�h*/
		else if (mode == 2) {
			printf("Enter the coordinate of a cell to mark\n");
			printf("(row and column numbers divide by an space):");
			scan_2(&n1, &n2);
			/*index���s���ȏꍇ*/
			if (n1<0 || n1>(row - 1) || n2<0 || n2>(col - 1))
				printf("Incorrect coordinate!\n");
			/*�Z�������ɊJ����Ă����ꍇ*/
			else if (open[n1][n2] == 1)
				printf("The cell is already open.\n");
			/*�Z�������Ƀ}�[�N����Ă����ꍇ*/
			else if (mark[n1][n2] == 1)
				printf("This cell is already marked.\n");
			/*���C������*/
			else {
				mark[n1][n2] = 1;

				mode = 0;
			}
		}


		/*remove mark���[�h*/
		else if (mode == 3) {
			printf("Enter the coordinate of a cell to remove marking\n");
			printf("(row and column numbers divide by an space):");
			scan_2(&n1, &n2);
			/*index���s���ȏꍇ*/
			if (n1<0 || n1>(row - 1) || n2<0 || n2>(col - 1))
				printf("Incorrect coordinate!\n");
			/*�Z�������ɊJ����Ă����ꍇ*/
			else if (open[n1][n2] == 1)
				printf("The cell is already open.\n");
			/*�Z�����}�[�N����Ă��Ȃ��ꍇ*/
			else if (mark[n1][n2] == 0)
				printf("This cell is not marked.\n");
			else {
				mark[n1][n2] = 0;

				mode = 0;
			}
		}


		/*�Z�����J�������ʂ�\��*/
		OutputTable(data, open, mark);
		printf("The number of remaining cells to open: %d\n", remain);
	}
	return 0;
}