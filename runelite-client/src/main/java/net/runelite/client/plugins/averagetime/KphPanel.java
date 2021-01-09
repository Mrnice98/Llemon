package net.runelite.client.plugins.averagetime;



import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

class KphPanel extends PluginPanel {
    final JLabel currentBossNameLabel = new JLabel("Unknown Boss Name");

    private final JLabel currentBossNameLabelTwo = new JLabel();
    private final JLabel totalTrackedTime = new JLabel();
    private final JLabel totalTrackedKills = new JLabel();
    private final JLabel actualTotalAverageKillTime = new JLabel();
    private final JLabel actualTotalKph = new JLabel();
    private final JLabel overallFastestKill = new JLabel();


    private final JLabel sessionTimeLabel = new JLabel("Session Time: N/A");
    private final JLabel totalBossKillsLabel = new JLabel("Kills: N/A");
    private final JLabel averageKillTimeLabel = new JLabel("Average Kill: N/A");
    private final JLabel fastestKillTimeLabel = new JLabel("Fastest Kill: N/A");
    private final JLabel killsPerHourLabel = new JLabel("KPH: N/A");
    private final JLabel idleTimeLabel = new JLabel("Idle Time: N/A");


    private final JLabel picLabel = new JLabel();

    private final JPanel dropdownIcon;

    private final JPanel icon;
    private final JPanel sidePanel;
    private final JPanel titlePanel;
    private final JPanel bossInfoPanel;

    private final JPanel bossInfoPanelTwo;

    private final JPanel pauseAndResumeButtons;
    private final JPanel sessionEndButton;
    private final JPanel supportButtons;

    private static final ImageIcon DROPDOWN_ICON;
    private static final ImageIcon DROPDOWN_HOVER;
    private static final ImageIcon DROPDOWN_FLIPPED_ICON;
    private static final ImageIcon DROPDOWN_FLIPPED_HOVER;
    private static final ImageIcon DISCORD_ICON;
    private static final ImageIcon  DISCORD_HOVER;
    private static final ImageIcon GITHUB_ICON;
    private static final ImageIcon  GITHUB_HOVER;

    private final KphConfig config;

    private final KphPlugin plugin;

    private final FileReadWriter fileReadWriter;

    @Inject
    private ItemManager itemManager;

    JButton pauseResumeButton = new JButton();
    JButton switchModeButton = new JButton();

    @Inject
    KphPanel(KphPlugin plugin, KphConfig config, FileReadWriter fileReadWriter)
    {
        this.sessionEndButton = new JPanel();
        this.pauseAndResumeButtons = new JPanel();
        this.supportButtons = new JPanel();
        this.sidePanel = new JPanel();
        this.titlePanel = new JPanel();
        this.bossInfoPanel = new JPanel();

        this.bossInfoPanelTwo = new JPanel();

        this.dropdownIcon = new JPanel();

        this.icon = new JPanel();
        this.plugin = plugin;
        this.config = config;
        this.fileReadWriter = fileReadWriter;
    }

    void sidePanelInitializer()
    {
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.sidePanel.setLayout(new BoxLayout(this.sidePanel,BoxLayout.Y_AXIS));
        this.sidePanel.add(this.buildTitlePanel());
        this.sidePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        this.sidePanel.add(this.buildBossInfoPanel());


        this.sidePanel.add(this.buildBossInfoPanelTwo());

        this.sidePanel.add(this.buildDropDownButton());



        this.sidePanel.add(this.buildPauseAndResumebuttons());
        this.sidePanel.add(this.buildSessionEndButton());
        this.sidePanel.add(this.buildSupportbuttons());
        this.add(sidePanel, "North");
    }


    JPanel overallInfoSection = new JPanel(new GridBagLayout());

    public void openCurrentHistoricalData()
    {
        bossInfoPanelTwo.setBorder(new EmptyBorder(0, 10, 8, 10));
        bossInfoPanelTwo.setBorder(new MatteBorder(1, 0, 0, 0, new Color(37, 125, 141)));
        overallInfoSection.setLayout(new GridLayout(6, 0, 0, 10));
        overallInfoSection.setBorder(new EmptyBorder(10, 5, 4, 0));

        currentBossNameLabelTwo.setText("Historical Information");
        totalTrackedTime.setText("Time Tracked: " + plugin.timeConverter(plugin.totalTrackedTime));
        totalTrackedKills.setText("Kills Tracked: " + plugin.totalTrackedKills);
        actualTotalAverageKillTime.setText("Average Kill: " + plugin.timeConverter(plugin.actualTotalAverageKillTime));
        actualTotalKph.setText("Average Kphs: " + plugin.overallKph);
        overallFastestKill.setText("Fastest Kill: " + plugin.timeConverter(plugin.overallFastestKill));





    }

    public void closeCurrentHistoricalData()
    {
        bossInfoPanelTwo.setBorder(new EmptyBorder(0, 0, 0, 0));
        bossInfoPanelTwo.setBorder(new MatteBorder(0, 0, 0, 0, new Color(37, 125, 141)));
        overallInfoSection.setLayout(new GridLayout(6, 0, 0, 0));
        overallInfoSection.setBorder(new EmptyBorder(0, 0, 0, 0));

        currentBossNameLabelTwo.setText("");
        totalTrackedTime.setText("");
        totalTrackedKills.setText("");
        actualTotalAverageKillTime.setText("");
        actualTotalKph.setText("");
        overallFastestKill.setText("");

         //this will work to remove and replace panels
//        sidePanel.remove(bossInfoPanel);
//        sidePanel.revalidate();



    }



    private JPanel buildBossInfoPanelTwo()
    {
        bossInfoPanelTwo.setLayout(new BorderLayout());

        currentBossNameLabelTwo.setFont(FontManager.getRunescapeBoldFont());

        //sets the continer panel to opaque or not, false = transparent, this will only affect the assingned panel. it will not affect any other content or panel within said panel.
        overallInfoSection.setOpaque(false);

        //adds the lables to the respective panel in the order they are added
        overallInfoSection.add(currentBossNameLabelTwo);
        overallInfoSection.add(totalTrackedTime);
        overallInfoSection.add(totalTrackedKills);
        overallInfoSection.add(actualTotalAverageKillTime);
        overallInfoSection.add(actualTotalKph);
        overallInfoSection.add(overallFastestKill);

        bossInfoPanelTwo.add(overallInfoSection, "West");

        return bossInfoPanelTwo;
    }

    public void setHistoricalInfo()
    {
        if(dropdownButton.isSelected())
        {
            System.out.println(fileReadWriter.averageKillTime);
            currentBossNameLabelTwo.setText("Historical Information");
            totalTrackedTime.setText("Time Tracked: " + plugin.timeConverter(plugin.totalTrackedTime));
            totalTrackedKills.setText("Kills Tracked: " + plugin.totalTrackedKills);
            actualTotalAverageKillTime.setText("Average Kill: " + plugin.timeConverter(plugin.actualTotalAverageKillTime));
            actualTotalKph.setText("Average Kph: " + plugin.overallKph);
            overallFastestKill.setText("Fastest Kill: " + plugin.timeConverter(plugin.overallFastestKill));

        }
    }



    private JPanel buildTitlePanel()
    {
        titlePanel.setBorder(new CompoundBorder(new EmptyBorder(5, 0, 0, 0), new MatteBorder(0, 0, 1, 0, new Color(37, 125, 141))));
        titlePanel.setLayout(new BorderLayout());
        PluginErrorPanel errorPanel = new PluginErrorPanel();
        errorPanel.setBorder(new EmptyBorder(2, 0, 3, 0));
        errorPanel.setContent("KPH Tracker", "Tracks your KPH at various bosses");
        titlePanel.add(errorPanel, "Center");
        return titlePanel;
    }


    private JPanel buildBossInfoPanel()
    {
        bossInfoPanel.setLayout(new BorderLayout());

        bossInfoPanel.setBorder(new EmptyBorder(0, 10, 0, 10));

        bossInfoPanel.setBorder(new MatteBorder(0, 0, 0, 0, new Color(37, 125, 141)));

        JPanel sessionInfoSection = new JPanel(new GridBagLayout());

        sessionInfoSection.setLayout(new GridLayout(7, 1, 0, 10));

        //this controls the offset of the current boss name, useful for alinging the icon
        sessionInfoSection.setBorder(new EmptyBorder(10, 5, 10, 0));

        currentBossNameLabel.setFont(FontManager.getRunescapeBoldFont());
//
//        currentBossNameLabel.setFont((new Font(FontManager.getRunescapeFont().toString(),Font.BOLD,12)));
//
//        killsPerHourLabel.setFont(new Font(FontManager.getRunescapeFont().toString(),Font.BOLD,11));
//
//        averageKillTimeLabel.setFont(new Font(FontManager.getRunescapeFont().toString(),Font.BOLD,11));
//
//        totalBossKillsLabel.setFont(new Font(FontManager.getRunescapeFont().toString(),Font.BOLD,11));
//
//        sessionTimeLabel.setFont(new Font(FontManager.getRunescapeFont().toString(),Font.BOLD,11));
//
//        idleTimeLabel.setFont(new Font(FontManager.getRunescapeFont().toString(),Font.BOLD,11));




        icon.setLayout(new GridLayout(0, 2, 0, 0));

        icon.setBorder(new EmptyBorder(0, 0, 180, 150));




        //sets the continer panel to opaque or not, false = transparent, this will only affect the assingned panel. it will not affect any other content or panel within said panel.
        icon.setOpaque(false);
        sessionInfoSection.setOpaque(false);



        //adds the lables to the respective panel in the order they are added
        icon.add(picLabel);
        sessionInfoSection.add(currentBossNameLabel);
        sessionInfoSection.add(killsPerHourLabel);
        sessionInfoSection.add(totalBossKillsLabel);
        sessionInfoSection.add(averageKillTimeLabel);
        sessionInfoSection.add(fastestKillTimeLabel);
        sessionInfoSection.add(idleTimeLabel);
        sessionInfoSection.add(sessionTimeLabel);

        //sessionInfoSection.add(testLabel);



        bossInfoPanel.add(icon,"East");
        bossInfoPanel.add(sessionInfoSection, "West");



        return bossInfoPanel;
    }


    public void setBossIcon()
    {
        if(plugin.sessionNpc != null)
        {
            KphBossInfo kphBossInfo = KphBossInfo.find(plugin.sessionNpc);
            AsyncBufferedImage bossSprite = itemManager.getImage(kphBossInfo.getIcon());
            //this is how the icon is positioned
            int offset = 150 - ((plugin.sessionNpc.length() * 8) + 6) ;
                                                            //if i change the bottom offset i can compensate when changeing the row number of the session info
            icon.setBorder(new EmptyBorder(0, 0, 153,offset));

            //use this method when applying icons.
            bossSprite.addTo(picLabel);
        }
    }


    JToggleButton dropdownButton = new JToggleButton();


    private JPanel buildDropDownButton()
    {

        //sets what happens when the botton is hovered over


        dropdownButton.setIcon(DROPDOWN_ICON);
        dropdownButton.setRolloverIcon(DROPDOWN_HOVER);

        dropdownButton.setSelectedIcon(DROPDOWN_FLIPPED_ICON);
        dropdownButton.setRolloverSelectedIcon(DROPDOWN_FLIPPED_HOVER);


        //sets the buttons prefered size (can be finkiky)
        dropdownButton.setPreferredSize(new Dimension(35, 20));
        dropdownButton.setToolTipText("Opens Historical Info");

        //removes the box around the button
        SwingUtil.removeButtonDecorations(dropdownButton);

        //links button press to method call
        dropdownButton.addActionListener((e) -> {
            if (dropdownButton.isSelected())
            {
                openCurrentHistoricalData();
            }
            else if (!dropdownButton.isSelected())
            {
                closeCurrentHistoricalData();
            }

        });


        //adds buttons to JPanel
        dropdownIcon.add(dropdownButton);

        dropdownIcon.setLayout(new GridLayout(0, 12, 0, 0));
        dropdownIcon.setBorder(new EmptyBorder(0, 0, 0, 150));
        dropdownIcon.setBorder(new MatteBorder(0, 0, 1, 0, new Color(37, 125, 141)));

        dropdownIcon.setOpaque(false);

        dropdownIcon.add(dropdownButton,"South");


        return dropdownIcon;
    }












    private JPanel buildPauseAndResumebuttons()
    {
        pauseAndResumeButtons.setLayout(new BorderLayout());

        pauseAndResumeButtons.setBorder(new EmptyBorder(4, 5, 0, 10));

        JPanel myButtons = new JPanel(new GridBagLayout());

        myButtons.setLayout(new GridLayout(1, 2, 5, 0));

        myButtons.setBorder(new EmptyBorder(5, 5, 0, 0));

        switchModeButton = new JButton("     Actual     ");

        switchModeButton.setToolTipText("Switches your information display mode");
        pauseResumeButton = new JButton("      Pause     ");


        pauseResumeButton.addActionListener((e) ->
        {
            if (plugin.paused)
            {
                plugin.sessionResume();
            }
            else
            {
                plugin.sessionPause();
            }
        });

        switchModeButton.addActionListener((e) ->
        {

            switch (plugin.getCalcMode())
            {
                case 0:
                    plugin.setCalcMode(1);
                    switchModeButton.setText("    Virtual     ");
                    plugin.calcKillsPerHour();
                    break;
                case 1:
                    plugin.setCalcMode(0);
                    switchModeButton.setText("     Actual     ");
                    plugin.calcKillsPerHour();
                    break;
            }

        });


        myButtons.add(pauseResumeButton);
        myButtons.add(switchModeButton);

        pauseAndResumeButtons.add(myButtons, "West");


        return pauseAndResumeButtons;
    }




    //uses the default browser on the machine to open the attached link (my discord for support & my github)
    public void discordLink()
    {
        try { Desktop.getDesktop().browse(new URI("https://discord.gg/ATXSsbbXQV")); }
        catch (IOException | URISyntaxException e1) { e1.printStackTrace(); }
    }

    public void githubLink()
    {
        try { Desktop.getDesktop().browse(new URI("https://github.com/Mrnice98/KillsPerHour")); }
        catch (IOException | URISyntaxException e1) { e1.printStackTrace(); }
    }



    private JPanel buildSessionEndButton()
    {
        this.sessionEndButton.setLayout(new BorderLayout());

        this.sessionEndButton.setBorder(new EmptyBorder(0, 5, 8, 10));

        //adds a matte border
        this.sessionEndButton.setBorder(new MatteBorder(0, 0, 1, 0, new Color(37, 125, 141)));

        JPanel myButton = new JPanel(new GridBagLayout());

        myButton.setLayout(new GridLayout(1, 2, 5, 5));

        myButton.setBorder(new EmptyBorder(3, 10, 8, 0));

        JButton endButton = new JButton("                 End Session                  ");

        endButton.addActionListener(e -> plugin.sessionEnd());

        myButton.add(endButton);

        this.sessionEndButton.add(myButton, "West");

        return this.sessionEndButton;
    }



    private JPanel buildSupportbuttons()
    {
        //sets the main panles layout
        supportButtons.setLayout(new BorderLayout());
        //sets the main panels border
        supportButtons.setBorder(new EmptyBorder(4, 5, 0, 10));

        //creates the sub panel which the buttons are contained in
        JPanel myButtons = new JPanel(new GridBagLayout());
        myButtons.setLayout(new GridLayout(1, 2, 8, 0));
        myButtons.setBorder(new EmptyBorder(10, 5, 0, 0));

        //creates the individual buttons and assings there text or icon ect...
        JButton discordButton = new JButton(DISCORD_ICON);
        JButton githubButton = new JButton(GITHUB_ICON);

        //sets what happens when the botton is hovered over
        discordButton.setRolloverIcon(DISCORD_HOVER);
        githubButton.setRolloverIcon(GITHUB_HOVER);

        //sets the buttons prefered size (can be finkiky)
        discordButton.setPreferredSize(new Dimension(23, 25));
        githubButton.setPreferredSize(new Dimension(20, 23));

        //removes the box around the button
        SwingUtil.removeButtonDecorations(githubButton);
        SwingUtil.removeButtonDecorations(discordButton);

        //links button press to method call
        githubButton.addActionListener(e -> githubLink());
        discordButton.addActionListener(e -> discordLink());

        //adds buttons to JPanel
        myButtons.add(githubButton);
        myButtons.add(discordButton);

        //adds Panel to master/main panel
        supportButtons.add(myButtons, "East");

        return supportButtons;
    }




    public void setSessionTimeLabel()
    {
        sessionTimeLabel.setText("Session time: " + plugin.timeConverter(plugin.totalSessionTime));
    }

    public void setSessionInfo()
    {
        if(plugin.sessionNpc != null)
        {
            killsPerHourLabel.setText("KPH: " + plugin.formatKPH());
            averageKillTimeLabel.setText("Average Kill Time: " + plugin.avgKillTimeConverter());
            totalBossKillsLabel.setText("Kills: " + plugin.killsThisSession);
            idleTimeLabel.setText("Idle Time: " + plugin.timeConverter(plugin.timeSpentIdle));
            currentBossNameLabel.setText(plugin.sessionNpc);
            fastestKillTimeLabel.setText("Fastest Kill: " + plugin.timeConverter(plugin.fastestKill));

            if(!plugin.paused)
            {
                currentBossNameLabel.setForeground(new Color(71, 226, 12));
            }

        }

        if(plugin.sessionNpc == null && plugin.cacheHasInfo)
        {
            killsPerHourLabel.setText("KPH: " + plugin.cachedKPH);
            averageKillTimeLabel.setText("Average Kill Time: " + plugin.cachedAvgKillTime);
            totalBossKillsLabel.setText("Kills: " + plugin.cachedSessionKills);
            idleTimeLabel.setText("Idle Time: " + plugin.cachedIdleTime);
            currentBossNameLabel.setText(plugin.cachedSessionNpc);
            fastestKillTimeLabel.setText("Fastest Kill: " + plugin.cachedFastestKill);
            currentBossNameLabel.setForeground(new Color(187, 187, 187));
        }

    }



    public void updateKphMethod()
    {
        killsPerHourLabel.setText("KPH: " + plugin.formatKPH());
    }

    public void setBossNameColor()
    {
        currentBossNameLabel.setForeground(new Color(227, 160, 27));
    }


    static
    {
        BufferedImage dropdownFlippedPNG = ImageUtil.getResourceStreamFromClass(KphPlugin.class, "dropdown_flipped_icon.png");
        BufferedImage dropdownPNG = ImageUtil.getResourceStreamFromClass(KphPlugin.class, "dropdown_icon.png");
        BufferedImage discordPNG = ImageUtil.getResourceStreamFromClass(KphPlugin.class, "discord_icon.png");
        BufferedImage githubPNG = ImageUtil.getResourceStreamFromClass(KphPlugin.class, "github_icon.png");

        DROPDOWN_FLIPPED_ICON = new ImageIcon(dropdownFlippedPNG);
        DROPDOWN_FLIPPED_HOVER = new ImageIcon(ImageUtil.luminanceOffset(dropdownFlippedPNG, -80));

        DROPDOWN_ICON = new ImageIcon(dropdownPNG);
        DROPDOWN_HOVER = new ImageIcon(ImageUtil.luminanceOffset(dropdownPNG, -80));

        DISCORD_ICON = new ImageIcon(discordPNG);
        DISCORD_HOVER = new ImageIcon(ImageUtil.luminanceOffset(discordPNG, -80));

        GITHUB_ICON = new ImageIcon(githubPNG);
        GITHUB_HOVER = new ImageIcon(ImageUtil.luminanceOffset(githubPNG, -80));

    }

}
