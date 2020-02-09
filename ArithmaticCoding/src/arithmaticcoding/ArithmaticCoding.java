package arithmaticcoding;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ArithmaticCoding {

    public static void main(String[] args) {
		start();
	}

	public static void start() {
		
		JFrame frame = new JFrame("Arithmetic Coding");
		frame.setBounds(450, 50, 0, 0);
		frame.setSize(1000, 1100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JTextField data = new JTextField();
		data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		data.setBounds(250, 120, 500, 50);
		frame.getContentPane().add(data);
		data.setColumns(100);

		JTextField dataToBeCompressed = new JTextField();
		dataToBeCompressed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		dataToBeCompressed.setBounds(250, 300, 500, 50);
		frame.getContentPane().add(dataToBeCompressed);
		dataToBeCompressed.setColumns(100);

		JLabel lblNewLabel = new JLabel("Enter the sequence of the characters to compute the  ");
		JLabel lblNewLabe2 = new JLabel("probaibilties");
		JLabel lblNewLabe3 = new JLabel("Enter the sequence of the characters to compress");
		lblNewLabel.setFont(new Font("", 0, 35));
		lblNewLabel.setBounds(90, 10, 900, 50);
		lblNewLabe2.setFont(new Font("", 0, 35));
		lblNewLabe2.setBounds(400, 50, 900, 50);
		lblNewLabe3.setFont(new Font("", 0, 35));
		lblNewLabe3.setBounds(110, 200, 900, 50);
		frame.getContentPane().add(lblNewLabel);
		frame.getContentPane().add(lblNewLabe2);
		frame.getContentPane().add(lblNewLabe3);
		JButton Compress = new JButton("Compression");
                
                //======================Compression===========================
		
                Compress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				compression(data.getText());
				str = dataToBeCompressed.getText();
				compressSymbols(str);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Compression has been Successfully");
			}

		});
		JButton deCompress = new JButton("Decompression");
              

                //======================Decompression===========================

		deCompress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deCompression();
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Decompression has been Successfully");
			}
		});

		deCompress.setBounds(250, 500, 500, 50);
		deCompress.setSize(500, 50);
		frame.getContentPane().add(deCompress);
		Compress.setBounds(250, 400, 500, 50);
		Compress.setSize(500, 50);
		frame.getContentPane().add(Compress);
		frame.setVisible(true);
	}
	public static void compression(String data) {
		calculateSymbosDetails(data);
	}

	private static double getHighRange(char ch) {
		double highRang = 0;
		for (Symbol symbol : symbolsDetails) {
			if (symbol.getSymbol() == ch) {
				highRang = symbol.getHighRange();
				break;
			}
		}
		return highRang;
	}

	private static double getLowRange(char ch) {
		double lowRange = 0;
		for (Symbol symbol : symbolsDetails) {
			if (symbol.getSymbol() == ch) {
				lowRange = symbol.getLowRange();
				break;
			}
		}
		return lowRange;
	}

	private static void compressSymbols(String str) {
		char ch = ' ';
		double lowRang = 0, highRange = 0, lower = 0, upper = 0;
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			if (i == 0) {
				for (Symbol symbol : symbolsDetails) {
					if (symbol.getSymbol() == ch) {
						lowRang = symbol.getLowRange();
						highRange = symbol.getHighRange();
						compressedSymbols.add(new CompressedSymbol(ch, lowRang, highRange));
						
						break;
					}
				}
				lower = compressedSymbols.get(0).getLower();
				highRange = compressedSymbols.get(0).getUpper();
				lowRang = compressedSymbols.get(0).getLower();
				for (Symbol symbols : symbolsDetails) {
					ch = symbols.getSymbol();
					lower = lowRang + (highRange - lowRang) * getLowRange(ch);
					upper = lowRang + (highRange - lowRang) * getHighRange(ch);
					tempCompressedSymbols.add(new CompressedSymbol(ch, lower, upper));
				}
			} else {
				for (CompressedSymbol symbol : tempCompressedSymbols) {
					if(symbol.getSymbol() == ch ) {
						lower = symbol.getLower();
						lowRang = symbol.getLower();
						highRange = symbol.getUpper();
						compressedSymbols.add(new CompressedSymbol(ch ,lowRang ,highRange ));
						break;
					}
				}
				
				for(CompressedSymbol symbols : tempCompressedSymbols ) {
					ch = symbols.getSymbol();
					if(symbols.getSymbol() == ch) {
						ch = symbols.getSymbol();
						symbols.setLower(lower + (highRange - lowRang) * getLowRange(ch));
						symbols.setUpper(lower + (highRange - lowRang) * getHighRange(ch));
					}
				}
			}
		}
		double comp = 0 ;
		int last = compressedSymbols.size() - 1 ;
		CompressedSymbol symbol = compressedSymbols.get(last) ;
		comp = (symbol.getLower() + symbol.getUpper() ) / 2 ;
		saveReslut(comp);
	}

	private static void saveReslut(double comp) {
		File file = new File("reslut.txt");
		try {
		      FileWriter writer = new FileWriter("reslut.txt");
		      String reslut = Double.toString(comp);
		      writer.write(reslut);
		      writer.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	}

	private static void calculateSymbosDetails(String data) {
		calculateFrequncy(data);
		calculateProbaility(data.length());
		sortSymbols();
		caculateHighAndLowRange();
	} 

	private static void calculateProbaility(int lenghtOfTheSymbols) {
		for (Symbol symbol : symbolsDetails)
			symbol.setProbability(symbol.getFrequncy() / lenghtOfTheSymbols);
	}

	private static void calculateFrequncy(String data) {
		char ch = ' ';
		symbolsDetails.add(new Symbol(data.charAt(0), 1.0, 0, 0, 0));
		for (int i = 1; i < data.length(); i++) {
			ch = data.charAt(i);
			if (!isNew(ch))
				increaseFrequncy(ch);
			else
				symbolsDetails.add(new Symbol(data.charAt(i), 1.0 , 0, 0, 0));
		}
	}

	private static void caculateHighAndLowRange() {
		double low = 0, high = 0;
		for (int i = 0; i < symbolsDetails.size(); i++) {
			symbolsDetails.get(i).setLowRange(low);
			high = symbolsDetails.get(i).getProbability();
			symbolsDetails.get(i).setHighRange(low + high);
			low = symbolsDetails.get(i).getHighRange();
		}
	}

	private static void sortSymbols() {
		Collections.sort(symbolsDetails, (Object a, Object b) -> {
			return (((Symbol) a).getSymbol() > ((Symbol) b).getSymbol()) ? 1 : -1;
		});
	}

	private static boolean isNew(char ch) {
		boolean flag = true;
		for (Symbol symbol : symbolsDetails)
			if (symbol.getSymbol() == ch)
				flag = false;
		return flag;
	}

	private static void increaseFrequncy(char ch) {
		for (Symbol symbol : symbolsDetails)
			if (symbol.getSymbol() == ch)
				symbol.setFrequncy(symbol.getFrequncy() + 1);
	}

	public static void deCompression() {
		double copressed = loadData();
		String reslut = decompresseCode(copressed);
		savereslut(reslut);
	}

	private static void savereslut(String reslut) {
		File file = new File("dereslut.txt");
		try {
		      FileWriter writer = new FileWriter("dereslut.txt");
		      writer.write(reslut);
		      writer.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		
	}

	private static String decompresseCode(double copressed) {
		String reslut = "" ;
		
		while (!compressedSymbols.isEmpty()) {
			CompressedSymbol symbol = compressedSymbols.get(0);
			if(getLowRange(symbol.getSymbol()) < copressed && copressed < getHighRange(symbol.getSymbol())  ) {
				reslut += symbol.getSymbol();
				double lowRange = getLowRange(symbol.getSymbol());
				double highRange = getHighRange(symbol.getSymbol());
				copressed = ((copressed - lowRange) / (highRange - lowRange ));
				//break;
			}
			compressedSymbols.remove(symbol);
		}
		return reslut;
	}
	
	

	private static double loadData() {
		double reslut = 0 ;
		File file ;
		Scanner	scan = null ;
		try{
			file = new File("reslut.txt");
			scan = new Scanner(file);
		}
		catch(Exception e){
		   System.exit(0);
		}
		while(scan.hasNext()) {
			reslut = scan.nextDouble();
		}
		return reslut;
	}

	static List<CompressedSymbol> compressedSymbols = new ArrayList<>();
	static List<CompressedSymbol> tempCompressedSymbols = new ArrayList<>();
	static List<Symbol> symbolsDetails = new ArrayList<>();
	private static String str = "";
}
