package template;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.core.utils.DrawingUtils;
import br.com.davidbuzatto.jsge.image.Image;
import java.util.ArrayList;

/**
 * Modelo de projeto básico da JSGE.
 * 
 * JSGE basic project template.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Main extends EngineFrame {
  private Image logo;
  int[][] mazeGrid;
  int[][] visitados;
  ArrayList<int[][]> passos;
  int inicioL;
  int inicioC;
  int fimL;
  int fimC;
  int pos;
  
  
  private double tempoParaMudar;
  private double contadorTempo;

  public Main() {

    super(
        500, // largura / width
        500, // algura / height
        "Window Title", // título / title
        60, // quadros por segundo desejado / target FPS
        true, // suavização / antialiasing
        false, // redimensionável / resizable
        false, // tela cheia / full screen
        false, // sem decoração / undecorated
        false, // sempre no topo / always on top
        false // fundo invisível / invisible background
    );

  }

  /**
   * Cria o mundo do jogo.
   * Esse método executa apenas uma vez durante a inicialização da engine.
   * 
   * Creates the game world.
   * This method runs just one time during engine initialization.
   */
  @Override
  public void create() {
    passos = new ArrayList<>();
    logo = DrawingUtils.createLogo();
    logo.resize((int) (logo.getWidth() * 0.1), (int) (logo.getWidth() * 0.1));
    setWindowIcon(logo);

    mazeGrid = new int[][] {
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    {1, 2, 0, 0, 1, 0, 0, 0, 0, 1},
    {1, 1, 0, 0, 1, 0, 1, 1, 0, 1},
    {1, 0, 0, 0, 1, 0, 1, 3, 0, 1},
    {1, 0, 0, 1, 1, 0, 0, 1, 0, 1},
    {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    {1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
    {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
    {1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
};
    visitados = new int[][] {
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    for (int i = 0; i < mazeGrid.length; i++) {
        for (int j = 0; j < mazeGrid[i].length; j++) {
            if (mazeGrid[i][j] == 2) { // 2 é o caractere de inicio definido por mim
                inicioL = i;
                inicioC = j;
            }
            if (mazeGrid[i][j] == 3) { // 3 é o caractere de fim definido por mim
                fimL = i;
                fimC = j;
            }
        }
    }

    mover(inicioL, inicioC, fimL, fimC);
    pos = 0;
    tempoParaMudar = 1;
    
  }

  /**
   * Lê a entrada do usuário e atualiza o mundo do jogo.
   * Os métodos de entrada devem ser usados aqui.
   * Atenção: Você NÃO DEVE usar nenhum dos métodos de desenho da engine aqui.
   * 
   * 
   * Reads user input and update game world.
   * Input methods should be used here.
   * Warning: You MUST NOT use any of the engine drawing methods here.
   * 
   * @param delta O tempo passado, em segundos, de um quadro para o outro.
   *              Time passed, in seconds, between frames.
   */
  @Override
  public void update(double delta) {
    contadorTempo += delta;
    
    if (contadorTempo > tempoParaMudar) {
        contadorTempo = 0;
        pos++;
    }
    if (pos >= passos.size()) {
        pos = 0;
        
    }
  }

  /**
   * Desenha o mundo do jogo.
   * Todas as operações de desenho DEVEM ser feitas aqui.
   * 
   * Draws the game world.
   * All drawing related operations MUST be performed here.
   */
  @Override
  public void draw() {
    clearBackground(WHITE);
    for (int i = 0; i < mazeGrid.length; i++) {
      for (int j = 0; j < mazeGrid[i].length; j++) {
        switch (mazeGrid[i][j]) {
          case 0 -> drawRectangle(50 * j, 50 * i, 50, 50, BLACK);
          case 1 -> fillRectangle(50 * j, 50 * i, 50, 50, BLACK);
          case 2 -> {
              drawRectangle(50 * j, 50 * i, 50, 50, BLACK);
              drawText("I", 50 * j + 25, 50 * i + 25, 12, BLACK);
              }
          case 3 -> {
              drawRectangle(50 * j, 50 * i, 50, 50, BLACK);
              drawText("F", 50 * j + 25, 50 * i + 25, 12, BLACK);
              }
        }
      }
    }
    
    desenharAcessados(passos.get(pos));
  }
  
  private boolean mover( int linhaFonte, int colunaFonte, int linhaDest, int colunaDest ) {
      if (valido(linhaFonte, colunaFonte)) {
          visitados[linhaFonte][colunaFonte] = 1;
          passos.add(copyMatrix(visitados));

          // fim
          if (linhaFonte == linhaDest && colunaFonte == colunaDest) {
              return true;
          }
          
          // cima
          if (mover(linhaFonte-1, colunaFonte, linhaDest, colunaDest)){
              return true;
          }
          
          // direita
          if (mover(linhaFonte, colunaFonte+1, linhaDest, colunaDest)) {
             return true;
          }
          
          // baixo
          if (mover(linhaFonte+1, colunaFonte, linhaDest, colunaDest)) {
              return true;
          }
          
          // esquerda
          if (mover(linhaFonte, colunaFonte-1, linhaDest, colunaDest)) {
              return true;
          }
          
      }
      return false;
  }
  
   private boolean valido( int line, int column ) {
        return line >= 0 &&
               line < mazeGrid.length &&
               column >= 0 &&
               column < mazeGrid[line].length &&
               mazeGrid[line][column] != 1 &&
               visitados[line][column] != 1;
    }
   
    private int[][] copyMatrix(int[][] original) {
        int rows = original.length;
        int cols = original[0].length;
        int[][] copy = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, cols);
        }

        return copy;
    }
   
   private void desenharAcessados(int[][] acessado) {
       for (int i = 0; i < acessado.length; i++) {
            for (int j = 0; j < acessado[i].length; j++){
                switch (acessado[i][j]) {
                    case 1 -> fillRectangle(50 * j, 50 * i, 50, 50, BLUE);
                }
            }
        }
   }


  /**
   * Instancia a engine e a inicia.
   * 
   * Instantiates the engine and starts it.
   */
  public static void main(String[] args) {
    new Main();
  }

}
