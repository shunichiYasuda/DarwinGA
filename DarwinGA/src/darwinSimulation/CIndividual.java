package darwinSimulation;

public class CIndividual {
	// 配列の長さは CHeader で決められている
	char[] chrom; // 染色体の長さは記憶の長さである。3回記憶ならば3x2=6なので2^6=64
	char[] memRec; // 記憶領域の長さは、記憶回数x2。3回記憶ならば 3x2=6
	int adr;// 染色体上での「次の手」の位置。記憶領域の10進数
	char myChoice; // 次に出す「手」
	// 各種利得
	double payoff, scaledPayoff, cumPayoff, avePayoff;
	// 繰り返しゲームのカウント
	int gameCount = 0;

	// コンストラクタ
	public CIndividual() {
		chrom = new char[CHeader.LENGTH];
		memRec = new char[CHeader.MEM];
		initBinary(chrom);
		initBinary(memRec);
		// 記憶が指し示すアドレス 0から数えるので、配列のアドレスになる（ビットを数えるときに注意）
		String tmp = new String(this.memRec);
		this.adr = Integer.parseInt(tmp, 2);
		// そのアドレスにある行動（0:協力'C', 1：裏切り'D'）。this.adr が示すのは戦略領域のビット数。
		this.myChoice = this.chrom[this.adr];
		// 利得関係初期化
		payoff = scaledPayoff = cumPayoff = avePayoff = 0.0;
		// ゲームカウント。個体によってゲーム回数が異なるので
		gameCount = 0;
	}

	//
	// 自分自身の初期化
	public void initialize() {
		// 一つの実験が終わって、次の実験に入るときに初期化する
		initBinary(this.memRec);
		initBinary(this.chrom);
		// 記憶が指し示すアドレス 0から数えるので、配列のアドレスになる（ビットを数えるときに注意）
		String tmp = new String(this.memRec);
		this.adr = Integer.parseInt(tmp, 2);
		// そのアドレスにある行動（0:協力'C', 1：裏切り'D'）
		this.myChoice = this.chrom[this.adr];
		// 利得関係初期化
		payoff = scaledPayoff = cumPayoff = avePayoff = 0.0;
		// ゲームカウント。個体によってゲーム回数が異なるので
		gameCount = 0;
	}

	// replace : 染色体を新しくして自分自身を上書きする。記憶については初期化される。
	public void replace(char[] in) {
		// 染色体の置き換え
		for (int i = 0; i < CHeader.LENGTH; i++) {
			this.chrom[i] = in[i];
		}
		// 記憶の初期化
		initBinary(this.memRec);
		// 記憶が指し示すアドレス 0から数えるので、配列のアドレスになる（ビットを数えるときに注意）
		String tmp = new String(this.memRec);
		this.adr = Integer.parseInt(tmp, 2);
		// そのアドレスにある行動（0:協力'C', 1：裏切り'D'）
		this.myChoice = this.chrom[this.adr];
		// 利得関係初期化
		payoff = scaledPayoff = cumPayoff = avePayoff = 0.0;
		// ゲームカウント。個体によってゲーム回数が異なるので
		gameCount = 0;
	}

	//
	// 対戦履歴の更新。一回の対戦ごとに更新される。受け入れるのは対戦相手のchoice
	public void reMem(char in) {
		// 記憶領域のビット長
		int L = this.memRec.length;
		char[] tmp = new char[L];
		// はじめに最初の2ビットを捨てて、以降を詰める
		for (int i = 0; i < tmp.length - 2; i++) {
			tmp[i] = this.memRec[2 + i];
		}
		// 終わりから2ビット目 [L-2] は自分が出した choice である。
		tmp[L - 2] = this.getChoice();
		// 最後のビットは in である。
		tmp[L - 1] = in;
		// tmp で memRec を更新する。
		for (int i = 0; i < L; i++) {
			this.memRec[i] = tmp[i];
		}
		// 記憶が更新されたら、adr が変わり、 myChoice が変わる。
		// 記憶の更新（ゲームのプレイ）がトリガーになるということだ。
		String str = new String(this.memRec);
		this.adr = Integer.parseInt(str, 2);
		this.myChoice = this.chrom[this.adr];
	}

	// setter
	public void setPayoff(double p) {
		// ゲームの利得がはいってくる、ということはゲームが1回終わったということなので
		// このメソッドの中でgameCount をすすめ、平均利得も計算しておく
		this.gameCount++;
		this.payoff = p;
		this.cumPayoff += p;
		this.avePayoff = cumPayoff / gameCount;
	}

	// getter
	public int getAdr() {
		return this.adr;
	}

	public char[] getChrom() {
		return this.chrom;
	}

	public char[] getMemory() {
		return this.memRec;
	}

	public double getPayoff() {
		return this.payoff;
	}

	public double getCumPayoff() {
		return this.cumPayoff;
	}

	public double getAvePayoff() {
		return this.avePayoff;
	}

	public double getScaledPayoff() {
		return this.scaledPayoff;
	}

	public char getChoice() {
		return this.myChoice;
	}

	//
	// 文字列初期化
	public void initBinary(char[] in) {
		double d;
		for (int i = 0; i < in.length; i++) {
			d = Math.random();
			if (d >= 0.5) {
				in[i] = '1';
			} else {
				in[i] = '0';
			}
		} // end of for
	} // end of void initBinary()
}
