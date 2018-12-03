package algs;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class Grid
{
    private Drop drop;
    private CellPane[][] cellPanes;
    private CellPane[] calcPanes;

    public Grid(Drop drop)
    {
        this.drop = drop;
        this.cellPanes = new CellPane[drop.getFloors() + 2][drop.getFloors()];
        this.calcPanes = new CellPane[drop.getFloors()];

        initialize();
    }

    private void initialize()
    {
        EventQueue.invokeLater(() -> {
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
            {
            }

            JFrame frame = new JFrame("Grid Structure");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(new TestPane(drop));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public class TestPane extends JPanel
    {
        private Drop drop;

        public TestPane(Drop drop)
        {
            this.drop = drop;

            initialize();
        }

        private void initialize()
        {
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            for (int row = 0; row < drop.getFloors() + 2; row++)
            {
                for (int col = 0; col < drop.getFloors(); col++)
                {
                    gbc.gridx = col;
                    gbc.gridy = row;

                    CellPane cellPane = new CellPane();
                    cellPane.setBorder(new MatteBorder(1, 1, 0, 0, Color.GRAY));
                    add(cellPane, gbc);
                    cellPanes[row][col] = cellPane;
                }
            }

            for (int col = 0; col < drop.getFloors(); col++)
            {
                gbc.gridx = col;
                gbc.gridy = drop.getFloors() + 2;

                CellPane cellPane = new CellPane();
                cellPane.setBorder(new MatteBorder(1, 1, 0, 0, Color.GRAY));

                add(cellPane, gbc);
                calcPanes[col] = cellPane;
            }

            for (int row = 1; row <= drop.getFloors() + 1; row++)
            {
                cellPanes[row][0].setBackground(Color.LIGHT_GRAY);
                cellPanes[row][0].setNum(row - 1);
            }

            for (int col = 0; col <= drop.getEggs(); col++)
            {
                cellPanes[0][col].setBackground(Color.LIGHT_GRAY);
                cellPanes[0][col].setNum(col);

                cellPanes[1][col].setNum(0);
            }

            cellPanes[0][0].setText("<html><div style='text-align: center;'>Eggs→<br>Floors↓</div></html>");
            cellPanes[1][1].activate();
        }
    }

    public class CellPane extends JPanel
    {
        private Color defaultBackground;
        private JLabel text;
        private int num;

        public CellPane()
        {
            defaultBackground = getBackground();
            text = new JLabel();
            this.add(text);
        }

        public void activate()
        {
            setBackground(Color.BLUE);
        }

        public void deactivate()
        {
            setBackground(defaultBackground);
        }

        public void clear()
        {
            deactivate();
            this.text.setText("");
            this.num = 0;
        }


        public void setNum(int num)
        {
            this.num = num;
            setText("" + num);
        }

        public void setText(String text)
        {
            this.text.setText(text);
            this.updateUI();
        }

        public int getNum()
        {
            return num;
        }

        @Override
        public Dimension getPreferredSize()
        {
            return new Dimension(50, 50);
        }

    }

    public CellPane[][] getCellPanes()
    {
        return cellPanes;
    }

    public CellPane[] getCalcPanes()
    {
        return calcPanes;
    }
}
