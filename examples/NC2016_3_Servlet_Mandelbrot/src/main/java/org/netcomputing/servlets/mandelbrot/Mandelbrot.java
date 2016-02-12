package org.netcomputing.servlets.mandelbrot;

import java.awt.image.BufferedImage;

public class Mandelbrot {
	
	public void drawMandelbrot(BufferedImage bi, double minx, double miny,
			double maxx, double maxy, int iterations) {

		int w=bi.getWidth();
		int h=bi.getHeight();
		
		Complex z = new Complex(0, 0);
		Complex mu = new Complex(0, 0);
		Complex z0 = new Complex(0, 0);

        // the colors of the pixels are defined using a predefined series, alias palette
		MPalette colors = new MPalette(iterations + 1);
		colors.definePalette(0, iterations / 2, 0, 0, 0, 255, 0, 0);
		colors.definePalette(iterations / 2, iterations, 255, 0, 0, 255, 255, 0);
		
		//scaling factors
		double factorp = (maxx - minx) / (double)w;
		double factorq = (maxy - miny) / (double)h;

		double y = miny;
		double x;
		int q= 0;
		while (y < maxy) {
			x = minx;
			int p = 0;
				while (x < maxx) {
				z.re = z0.re;
				z.im = z0.im;
				mu.re = x;
				mu.im = y;
				// do escape routine for this point z
				int i = 0;
				while (((z.im * z.im + z.re * z.re) < 4) && (i < iterations)) {
					Complex.msqr(z);
					Complex.madd(z, mu);
					i++;
				}
				if (i > 300)
					i = 300;
				// check, because p and q might be prone to rounding issues factorp and factorq
				if (p<w && q<h)
					bi.setRGB(p,q,colors.getColor(i));
				p++;
				x += factorp;
			}
			y += factorq;
			q++;
		}

	}

}
