package qv;
import java.io.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * Version : 1.1
 * @author ALI GAD
 */
public class QV {

    static int width, height, new_width, new_height;

    static int height_of_bloack, width_of_bloack, bookLength;

    static String path = "oo.jpg";

    public static void main(String[] args) {

        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter your Block Hieght");
            height_of_bloack = input.nextInt();
            System.out.println("Enter your Block Width");
            width_of_bloack = input.nextInt();
            System.out.println("Enter your CodeBookLength");
            bookLength = input.nextInt();

            Compress(path);
            Decompress(path.substring(0, path.lastIndexOf('.')) + ".txt");

            JOptionPane.showMessageDialog(null, "CONVERSION has been Successfully");

        } catch (IOException e1) {
            System.out.println("There is an error occured");
        } catch (ClassNotFoundException e2) {
            System.out.println("There is somthing occured");
        }
    }

//   public static void start()
//    {
//        JFrame frame = new JFrame("QV");
//		frame.setBounds(450, 50, 0, 0);
//		frame.setSize(1000, 1100);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().setLayout(null);
//
//		JTextField data = new JTextField();
//		data.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//			}
//		});
//		data.setBounds(250, 120, 500, 50);
//		frame.getContentPane().add(data);
//		data.setColumns(100);
//                
//                JTextField dataToBeCompressed = new JTextField();
//		dataToBeCompressed.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//			}
//		});
//		dataToBeCompressed.setBounds(250, 300, 500, 50);
//		frame.getContentPane().add(dataToBeCompressed);
//		dataToBeCompressed.setColumns(100);
//
//		JLabel lblNewLabel = new JLabel("Enter your Block Hieght  ");
//		JLabel lblNewLabe2 = new JLabel("Enter your CodeBookLength");
//		JLabel lblNewLabe3 = new JLabel("Enter your Block Width");
//		lblNewLabel.setFont(new Font("", 0, 35));
//		lblNewLabel.setBounds(90, 10, 900, 50);
//		lblNewLabe2.setFont(new Font("", 0, 35));
//		lblNewLabe2.setBounds(400, 50, 900, 50);
//		lblNewLabe3.setFont(new Font("", 0, 35));
//		lblNewLabe3.setBounds(110, 200, 900, 50);
//		frame.getContentPane().add(lblNewLabel);
//		frame.getContentPane().add(lblNewLabe2);
//		frame.getContentPane().add(lblNewLabe3);
//		JButton CONVERSION = new JButton("Compression");
//    }
    static void Compress(String Path) throws IOException {

        int[][] image = readImage(Path);

        int[][] SquaredImage = CreateSquared(image);

        Vector<Vector<Integer>> Vectors = CreateBlocks(SquaredImage);

        Vector<Vector<Integer>> QBlocks = new Vector<>();

        Vector<Integer> VectorsIndices = Quantize(bookLength, Vectors, QBlocks);

        System.out.println("Code Books : last Iteration : " + QBlocks);

        WriteToFile(QBlocks, VectorsIndices);
    }

    public static int[][] readImage(String filePath) {

        File f = new File(filePath);

        int[][] imageMAtrix = null;

        try {
            BufferedImage img = ImageIO.read(f);
            height = img.getWidth();
            width = img.getHeight();

            imageMAtrix = new int[width][height];

            for (int y = 0; y < width; y++) {

                for (int x = 0; x < height; x++) {

                    int p = img.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;

                    imageMAtrix[y][x] = r;

                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    img.setRGB(x, y, p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageMAtrix;

    }

    static int[][] CreateSquared(int[][] image) {

        if ((new_height = width) % height_of_bloack != 0) {
            new_height = ((width / height_of_bloack) + 1) * height_of_bloack;
        }
        if ((new_width = height) % width_of_bloack != 0) {
            new_width = ((height / width_of_bloack) + 1) * width_of_bloack;
        }

        //Scale by adding padding
        int col, wid;
        int[][] SquaredImage = new int[new_height][new_width];
        for (int i = 0; i < new_height; i++) {
            col = i;

            for (int j = 0; j < new_width; j++) {
                wid = j;

                if (i + 1 > width || j + 1 > height) {
                    SquaredImage[i][j] = 0;
                } else {
                    SquaredImage[i][j] = image[col][wid];
                }
            }
        }
        return SquaredImage;
    }

    static Vector<Integer> Quantize(int L, Vector<Vector<Integer>> Vectors, Vector<Vector<Integer>> QBlocks) {

        if (L == 1) {
            if (Vectors.size() > 0) {
                QBlocks.add(getMean(Vectors));
            }
            return rearrange(Vectors, QBlocks);
        }
        Vector<Vector<Integer>> Lefts = new Vector();
        Vector<Vector<Integer>> Rights = new Vector();

        Vector<Integer> mean = getMean(Vectors);

        for (Vector vec : Vectors) {
            int left = minDistance(vec, mean, 1);  //5
            int right = minDistance(vec, mean, -1);  //6

            if (left > right) {
                Lefts.add(vec);
            } else {
                Rights.add(vec);
            }
        }

        Quantize(L / 2, Rights, QBlocks);
        Quantize(L / 2, Lefts, QBlocks);

        return rearrange(Vectors, QBlocks);
    }

    static Vector<Vector<Integer>> CreateBlocks(int[][] SquaredImage) {

        Vector<Vector<Integer>> Vectors = new Vector<>();

        for (int i = 0; i < new_height; i += height_of_bloack) {
            for (int j = 0; j < new_width; j += width_of_bloack) {
                Vector v = new Vector();
                for (int x = i; x < i + height_of_bloack; x++) {
                    for (int y = j; y < j + width_of_bloack; y++) {
                        v.add(SquaredImage[x][y]);
                    }
                }
                Vectors.add(v);
            }
        }

        return Vectors;

    }

    static Vector<Integer> getMean(Vector<Vector<Integer>> Vectors) {

        int[] cum = new int[Vectors.elementAt(0).size()];

        int nOfBlocks = Vectors.size();
        Vector res = new Vector<Integer>();

        for (Vector<Integer> block : Vectors) {
            for (int i = 0; i < block.size(); i++) {
                cum[i] += block.elementAt(i);
            }
        }

        for (int i = 0; i < cum.length; i++) {
            res.add(cum[i] / nOfBlocks);
        }
        return res;
    }

    static int minDistance(Vector<Integer> x, Vector<Integer> y, int PlusOrMinus) {
        int dist = 0;
        for (int i = 0; i < x.size(); i++) {
            dist += Math.abs(x.get(i) - y.get(i) + PlusOrMinus);
        }

        return dist;
    }

    private static Vector<Integer> rearrange(Vector<Vector<Integer>> Vectors, Vector<Vector<Integer>> QBlocks) {

        Vector<Integer> res = new Vector<>();
        Vector<String> res_binary = new Vector<>();

        for (Vector<Integer> vector : Vectors) {
            int min = 1000000, idx = -1, temp;

            for (int i = 1; i < QBlocks.size(); i++) {
                if ((temp = minDistance(vector, QBlocks.get(i), 0)) < min) {
                    min = temp;
                    idx = i;
                }
            }

            String binary = Integer.toBinaryString(idx);
            res_binary.add(binary);

            res.add(idx);
        }

        System.out.println("Compressed Codes : " + res_binary);
        return res;

    }

    static void WriteToFile(Vector<Vector<Integer>> QBlocks, Vector<Integer> VectorsIndices) throws IOException {

        ObjectOutputStream HelperFile = new ObjectOutputStream(new FileOutputStream(path.substring(0, path.lastIndexOf('.')) + ".txt"));
        HelperFile.writeInt(height);
        HelperFile.writeInt(width);
        HelperFile.writeInt(new_width);
        HelperFile.writeInt(new_height);
        HelperFile.writeInt(width_of_bloack);
        HelperFile.writeInt(height_of_bloack);
        HelperFile.writeObject(VectorsIndices);
        HelperFile.writeObject(QBlocks);
        HelperFile.close();
    }

    static void Decompress(String Path) throws IOException, ClassNotFoundException {

        System.out.println(Path);
        InputStream file = new FileInputStream(Path);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);
        System.out.println(Path);

        height = input.readInt();
        width = input.readInt();

        int NEWidth = input.readInt();
        int NEWoldH = input.readInt();
        int BlockW = input.readInt();
        int BlockH = input.readInt();

        Vector<Integer> VectorsIndices = (Vector<Integer>) input.readObject();
        Vector<Vector<Integer>> QBlocks = (Vector<Vector<Integer>>) input.readObject();

        int[][] newImg = new int[NEWoldH][NEWidth];
        for (int i = 0; i < VectorsIndices.size(); i++) {

            int x = i / (NEWidth / BlockW);
            int y = i % (NEWidth / BlockW);
            x *= BlockH;
            y *= BlockW;
            int v = 0;
            for (int j = x; j < x + BlockH; j++) {
                for (int k = y; k < y + BlockW; k++) {
                    newImg[j][k] = QBlocks.get(VectorsIndices.get(i)).get(v++);
                }
            }
        }

        writeImage(newImg, (Path.substring(0, path.lastIndexOf('.')) + "_Reconstructed.jpg"));
    }

    public static void writeImage(int[][] imagePixels, String outPath) {
        int oldH = imagePixels.length;
        int oldW = imagePixels[0].length;
        BufferedImage img = new BufferedImage(oldW, oldH, BufferedImage.TYPE_3BYTE_BGR);

        for (int y = 0; y < oldH; y++) {
            for (int x = 0; x < oldW; x++) {

                int a = 255;
                int pix = imagePixels[y][x];
                int p = (a << 24) | (pix << 16) | (pix << 8) | pix;

                img.setRGB(x, y, p);

            }
        }

        File f = new File(outPath);

        try {
            ImageIO.write(img, "jpg", f);
        } catch (IOException e) {
        }
    }
}
