package arithmaticcoding;

public class Symbol {
    
    
    private char symbol = ' ' ;
	private double probability = 0;
	private double lowRange = 0;
	private double highRange = 0 ;
	private double frequncy = 0 ;
	
	public Symbol(char symbol, double frequncy, double probability, double lowRange, double highange) {
		this.symbol = symbol ;
		this.frequncy = frequncy ;
		this.probability = probability ;
		this.lowRange = lowRange ;
		this.highRange = highange ;
	}

	public char getSymbol() {
		return symbol;
	}

	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public double getLowRange() {
		return lowRange;
	}

	public void setLowRange(double lowRange) {
		this.lowRange = lowRange;
	}

	public double getHighRange() {
		return highRange;
	}

	public void setHighRange(double highRange) {
		this.highRange = highRange;
	}
	
	public void printSymbol() {
		System.out.println("Symbol is : "  + this.getSymbol() +
							"\nFrequncy is " + this.getFrequncy() + 
							"\nPorbability is :" + this.getProbability() +
							"\nLow-range is : " + this.getLowRange() +
							"\nHigh-range is : " + this.getHighRange());
	}

	public double getFrequncy() {
		return frequncy;
	}

	public void setFrequncy(double frequncy) {
		this.frequncy = frequncy;
	}
    
}
