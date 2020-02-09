package arithmaticcoding;

/**
 * @author ALI Gad
 */
public class CompressedSymbol {
    
    public CompressedSymbol(char symbol, double lower, double upper) {

		this.setSymbol(symbol);
		this.setLower(lower);
		this.setUpper(upper);
	}
	public char getSymbol() {
		return symbol;
	}
	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}
	public double getLower() {
		return lower;
	}
	public void setLower(double lower) {
		this.lower = lower;
	}
	public double getUpper() {
		return upper;
	}
	public void setUpper(double upper) {
		this.upper = upper;
	}
	
	private char symbol = ' ' ;
	private double lower = 0 ;
	private double upper = 0 ;
	public void prints() {
		System.out.println("Symbol is : " + this.getSymbol() +
							"\nLower is : " + this.getLower() +
							"\nUpper is : " + this.getUpper());
	}
    
}
