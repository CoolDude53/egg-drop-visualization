package algs;

import javax.swing.*;
import java.awt.*;

public class Controller extends JFrame
{
    private Drop drop;
    private JTextField floor;
    private JTextField egg;
    private JLabel caseInfo;
    private JButton button;
    private int calcCount = 0;
    boolean highlighted = false;
    private int max10 = -1, max11 = -1, max20 = -1, max21 = -1;

    public Controller(Drop drop)
    {
        this.drop = drop;

        initialize();
        register();
    }

    private void initialize()
    {
        Container cp = getContentPane();
        cp.setLayout(new FlowLayout());

        cp.add(new JLabel("Floor"));
        floor = new JTextField("" + drop.getTempFloors());
        floor.setPreferredSize(new Dimension(35, 20));
        floor.setEditable(false);
        cp.add(floor);

        cp.add(new JLabel("Eggs"));
        egg = new JTextField("" + drop.getTempEggs());
        egg.setPreferredSize(new Dimension(35, 20));
        egg.setEditable(false);
        cp.add(egg);

        button = new JButton("Next Step");
        cp.add(button);

        caseInfo = new JLabel("");
        cp.add(caseInfo);

        EventQueue.invokeLater(() -> {
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
            {
            }

            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setTitle("Controller");
            setSize(350, 150);
            setVisible(true);
        });
    }

    private void register()
    {
        button.addActionListener(evt -> {
            if (drop.getTempFloors() > drop.getFloors())
            {
                button.setEnabled(false);

                return;
            }

            DropData dropData = drop.getDropDataArray()[drop.getTempFloors()][drop.getTempEggs()];
            CaseData currentCase = dropData.getCurrentCase();

            if (currentCase == null)
            {
                drop.getGrid().getCellPanes()[drop.getTempFloors() + 1][drop.getTempEggs()].setBackground(Color.GREEN);
                drop.getGrid().getCellPanes()[drop.getTempFloors() + 1][drop.getTempEggs()].setNum(dropData.getDrop());

                if (!highlighted)
                {
                    for (int cell = 0; cell < calcCount; cell++)
                    {
                        if (drop.getGrid().getCalcPanes()[cell].getNum() == dropData.getDrop())
                            drop.getGrid().getCalcPanes()[cell].setBackground(Color.GREEN);
                    }

                    highlighted = true;
                    return;
                }

                for (int cell = 0; cell < calcCount; cell++)
                    drop.getGrid().getCalcPanes()[cell].clear();
                calcCount = 0;
                highlighted = false;
                drop.getGrid().getCellPanes()[drop.getTempFloors() + 1][drop.getTempEggs()].deactivate();

                drop.incrementTempEggs();
                egg.setText("" + drop.getTempEggs());
                floor.setText("" + drop.getTempFloors());

                if (drop.getTempFloors() > drop.getFloors())
                {
                    if (max11 > -1)
                    {
                        drop.getGrid().getCellPanes()[max10][max11].deactivate();
                        drop.getGrid().getCellPanes()[max20][max21].deactivate();
                    }

                    button.setEnabled(false);
                    return;
                }

                drop.getGrid().getCellPanes()[drop.getTempFloors() + 1][drop.getTempEggs()].activate();
            }
            else
            {
                if (max11 > -1)
                {
                    drop.getGrid().getCellPanes()[max10][max11].deactivate();
                    drop.getGrid().getCellPanes()[max20][max21].deactivate();
                }

                max10 = currentCase.getMax10() + 1;
                max11 = currentCase.getMax11();
                max20 = currentCase.getMax20() + 1;
                max21 = currentCase.getMax21();

                if (max11 > -1)
                {
                    drop.getGrid().getCellPanes()[max10][max11].setBackground(Color.YELLOW);
                    drop.getGrid().getCellPanes()[max20][max21].setBackground(Color.YELLOW);
                }

                caseInfo.setText("<html><div style='text-align: center;'>" + currentCase.getText() + "</div></html>");
                drop.getGrid().getCalcPanes()[calcCount++].setNum(currentCase.getDrops());
            }
        });
    }
}

