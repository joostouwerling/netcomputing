package org.netcomputing.servlets.mandelbrot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Wico Mulder 20 - 08 -2003
 */

public class MandelbrotServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("image/png");

		int width = checkParamAsInt("width", req, 200);
		int height = checkParamAsInt("height", req, 200);
		double minx = checkParamAsDouble("minx", req, -2);
		double miny = checkParamAsDouble("miny", req, -1);
		double maxx = checkParamAsDouble("maxx", req, 0.5);
		double maxy = checkParamAsDouble("maxy", req, 1);

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Mandelbrot mandelbrot = new Mandelbrot();
		mandelbrot.drawMandelbrot(bi, minx, miny, maxx, maxy, 50);
		String showname = req.getParameter("showname");
		if (showname != null) {
			Graphics2D g = (Graphics2D) bi.getGraphics();
			g.setColor(Color.red);
			g.drawString("Hello " + showname, 20, 20);
		}
		// Encode the image as a PNG image-data and send this to the
		// servlet-output stream
		PngEncoder png = new PngEncoder(bi);
		ServletOutputStream sos = resp.getOutputStream();
		sos.write(png.pngEncode());
		sos.close();
	}

	/**
	 * conveniece method to extract a parameter from a servlet request object.
	 * if it does not exist, the given default value is returned
	 * 
	 * @param name
	 * @param req
	 * @param defaultvalue
	 * @return
	 */
	private int checkParamAsInt(String name, HttpServletRequest req,
			int defaultvalue) {
		if (!(req.getParameter(name) == null))
			return Integer.parseInt(req.getParameter(name));
		else
			return defaultvalue;
	}

	/**
	 * conveniece method to extract a parameter from a servlet request object.
	 * if it does not exist, the given default value is returned
	 * 
	 * @param name
	 * @param req
	 * @param defaultvalue
	 * @return
	 */
	private double checkParamAsDouble(String name, HttpServletRequest req,
			double defaultvalue) {
		if (!(req.getParameter(name) == null))
			return Double.parseDouble(req.getParameter(name));
		else
			return defaultvalue;
	}

}
