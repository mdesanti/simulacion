package ss.gui.out;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * Clase con m??todos ??tiles para el manejo de im??genes.
 */
public class ImageUtils {

	/**
	 * Carga una imagen y retorna una instancia de la misma.
	 * Si hay algun problema al leer el archivo lanza una excepcion.
	 */
	public static Image loadImage(String fileName) throws IOException {
		InputStream stream = ClassLoader.getSystemResourceAsStream(fileName);
		if (stream == null) {
			return ImageIO.read(new File(fileName));
		} else {
			return ImageIO.read(stream);
		}
	}

	/**
	 * Dibuja un texto en el centro de la imagen, con el color indicado.
	 * Retorna una imagen nueva con los cambios, la imagen original no se modifica.
	 */
	public static Image drawString(Image img, String text, Color color) {
		BufferedImage result = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);
		g.drawImage(img, 0, 0, null);
		g.setFont(font);
		g.setColor(color);
		Rectangle2D r = font.getStringBounds(text, g.getFontRenderContext());
		g.drawString(text, img.getWidth(null) / 2 - (int) r.getWidth() / 2, img.getHeight(null) / 2 + (int) r.getHeight() / 2 - 3);
		return result;
	}
	
	/**
	 * Dada una imagen en escala de grises, la pinta con el color indicado.
	 * Retorna una imagen nueva con los cambios, la imagen original no se modifica.
	 */
	public static Image colorize(Image image, Color color) {
		BufferedImage result = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		result.getGraphics().drawImage(image, 0, 0, null);

		for (int x = 0; x < image.getWidth(null); x++) {
			for (int y = 0; y < image.getHeight(null); y++) {
				Color c = new Color(result.getRGB(x, y));

				if (result.getRGB(x, y) >> 24 != 0) {
					double r = c.getGreen() / 255.0;
					Color c2 = new Color((int) (r * color.getRed()), (int) (r * color.getGreen()), (int) (r * color.getBlue()));
					result.setRGB(x, y, c2.getRGB());
				}
			}
		}
		return result;
	}
}