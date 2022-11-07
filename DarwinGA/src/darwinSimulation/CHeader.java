package darwinSimulation;

public class CHeader {
	static int PRE= 3; //ゲームの履歴回数
	static int MEM = PRE*2;//記憶の長さ
	static int LENGTH = (int)Math.pow(2.0, (double)MEM); //染色体の長さ記憶は遺伝しない
	public static final double crossProb = 0.25; //交叉確率
	public static final double mutProb = 0.01; //突然変異確率
}
