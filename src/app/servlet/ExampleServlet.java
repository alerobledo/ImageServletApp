package app.servlet;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

public class ExampleServlet extends HttpServlet implements Servlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2848145966045241873L;

	private final BufferedImage originalImage;
	private final BufferedImage watermarkImage;
    	
	public ExampleServlet() throws IOException {
		super();
		//HERE IT SHOULD BE CHANGED THE PATH
		originalImage = ImageIO.read(new File("C:\\Users\\Administrator\\workspace\\ImageServletApp\\src\\app\\servlet\\hinchadacanaya.jpg"));
		watermarkImage = ImageIO.read(new File("C:\\Users\\Administrator\\workspace\\ImageServletApp\\src\\app\\servlet\\rc_marc.png"));
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		//System.out should be replaced by logback/log4j or some other log framework
		System.out.println("ExampleServlet - doGet - Enter");
		
		//It should be added some parameters validation (null and type)
		int newHeight = Integer.parseInt(request.getParameter("h"));
		int newWidth = Integer.parseInt(request.getParameter("w"));
		String format = request.getParameter("f");
		System.out.println("height:"+newHeight+" / width:"+newWidth+" / format:"+format);
		
				
		BufferedImage returnImage = new BufferedImage(newWidth,newHeight, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D graphicsReturnImage = (Graphics2D) returnImage.getGraphics();	 
		graphicsReturnImage.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        graphicsReturnImage.drawImage(Scalr.resize(originalImage, Method.QUALITY, Mode.FIT_EXACT, newWidth, newHeight), 0, 0, null);
        
        int newWatermarcImageWidth = (int)Math.floor(watermarkImage.getWidth()*newWidth/originalImage.getWidth());
        int newWatermarcImageHeight = (int)Math.floor(watermarkImage.getHeight()*newHeight/originalImage.getHeight());
		int waterMarkXPosition = newWidth-newWatermarcImageWidth;
		int waterMarkYPosition = newHeight-newWatermarcImageHeight;
        graphicsReturnImage.drawImage(Scalr.resize(watermarkImage, Method.SPEED, Mode.FIT_EXACT, newWatermarcImageWidth, newWatermarcImageHeight), waterMarkXPosition, waterMarkYPosition, null);
 
		resp.setContentType("image/"+format);
        ServletOutputStream outputStream = resp.getOutputStream();
		ImageIO.write(returnImage, format, outputStream);
        outputStream.close();
        
		System.out.println("ExampleServlet - doGet - End");
	}
	
	
}
