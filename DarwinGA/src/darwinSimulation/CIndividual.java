package darwinSimulation;

public class CIndividual {
	char[] chrom; //染色体の長さは記憶の長さである。3回記憶ならば3x2=6なので2^6=64
	char[] memRec; //記憶領域の長さは、記憶回数x2。3回記憶ならば 3x2=6
	int adr;//染色体上での「次の手」の位置。記憶領域の10進数
	char myChoice; //次に出す「手」
	//各種利得
	double payoff, scaledPayoff, cumPayoff, avePayoff;
	//繰り返しゲームのカウント
	int gameCount = 0;
}
