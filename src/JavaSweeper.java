import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import sweeperr.Box;
import sweeperr.Coord;
import sweeperr.Game;
import sweeperr.Ranges;

public class JavaSweeper extends JFrame {
    private Game game;
    private JPanel panel;
    private JLabel label;
    private final int COLS =9;
    private final int ROWS = 9;
    private final int BOMBS = 10;
    private final int IMAGE_SIZE = 50;
    public static void main(String[] args)
    {
        new JavaSweeper();

    }
    private JavaSweeper()
    {
        game = new Game(COLS, ROWS, BOMBS);
        game.start();
        setImages();
        initLabel();
        initPanel();
        initFrame();
    }
    private void initLabel()
    {
        label = new JLabel("Welcom");
        add(label, BorderLayout.SOUTH);
    }
    private void initFrame()
    {
     setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
     setTitle("JavaSweeper");
     setResizable(false);
     setVisible(true);
     pack();
     setLocationRelativeTo(null);
     setIconImage(getImage("icon"));
    }
    private void setImages()
    {
        for (Box box : Box.values())
            box.image = getImage(box.name().toLowerCase());
    }
    private void initPanel()
    {
        panel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Coord coord : Ranges.getAllCooords())
                    g.drawImage((Image) game.getBox(coord).image,
                            coord.x*IMAGE_SIZE,coord.y*IMAGE_SIZE,this);
            }
        };
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e)
            {
                int x = e.getX()/ IMAGE_SIZE;
                int y = e.getY()/ IMAGE_SIZE;
                Coord coord = new Coord(x,y);
                if(e.getButton() == MouseEvent.BUTTON1)
                    game.pressLeftButton(coord);
                if(e.getButton() == MouseEvent.BUTTON3)
                    game.pressRightButton(coord);
                if(e.getButton() == MouseEvent.BUTTON2)
                    game.start();
                label.setText(getMessage());
                panel.repaint();
            }
        });
        panel.setPreferredSize(new Dimension(
                Ranges.getSize().x * IMAGE_SIZE,
                Ranges.getSize().y * IMAGE_SIZE));
        add(panel);
    }

    private String getMessage()
    {
        switch (game.getState())
        {
            case PLAYED : return "Think twice!";
            case BOMBED : return "You Lose!";
            case WINNER : return "Winner!";
            default: return "Welcome!";
        }
    }

    private Image getImage(String name)
    {
        String filename = "img/"+name.toLowerCase()+".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }

}
