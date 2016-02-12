package org.netcomputing.servlets.mandelbrot;

public class MPalette {
  // blue-white
  //colors.definePalette(0, ITERATIONS/2, 0, 0, 0, 0, 255, 255);
  //colors.definePalette(ITERATIONS/2,ITERATIONS,0,255,255,200,255,255);
  // yellow-red
  //colors.definePalette(0, ITERATIONS / 2, 0, 0, 0, 255, 0, 0);
  //colors.definePalette(ITERATIONS / 2, ITERATIONS, 255, 0, 0, 255, 255, 0);

  private int coltable[];

  public MPalette(int maxcolors) {
    coltable= new int[maxcolors];
  }
  /**
   * Palette creating
   * pal - palette table[0..255]
   * s   - start
   * e   - end
   * r1r2- red
   * g1g2- green
   * b1b2- blue
   */
  public void definePalette(int s, int e, int r1, int g1, int b1, int r2, int g2, int b2) {
    int i;
    float k;
    for (i= 0; i <= e - s; i++) {
      k= (float) i / (float) (e - s);
      coltable[s + i]=
        (255 << 24)
          + ((int) (r1 + (r2 - r1) * k) << 16)
          + ((int) (g1 + (g2 - g1) * k) << 8)
          + ((int) (b1 + (b2 - b1) * k));
    }
  }

  public int getColor(int i) {
    try {
      return coltable[i];
    } catch (ArrayIndexOutOfBoundsException e) {
      return 0;
    }
  }

  public void setColor(int i, int argb) {
    coltable[i]= argb;
  }

  public int[] getColtable() {
    return coltable;
  }

}
