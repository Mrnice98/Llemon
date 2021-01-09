package net.runelite.client.plugins.averagetime;


import lombok.Getter;
import net.runelite.api.Client;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.List;

import static net.runelite.client.RuneLite.RUNELITE_DIR;

public class FileReadWriter
{

    KphPlugin plugin;

    Client client;

    KphPanel panel;


    @Inject
    private FileReadWriter(KphPlugin plugin, Client client)
    {
        this.plugin = plugin;
        this.client = client;
    }


    File file;
    String boss;
    String fileName;
    Path path;

    int oldTotalTime;
    int oldTotalKills;

    int timeOffset;
    int newTotalTime;

    int virtualTimeOffset;
    int oldTotalVirtualTime;
    int newTotalVirtualTime;

    int oldFastestKill;
    int newFastestKill;



    int lastKillTimeActual;
    int lastKillTimeVirtual;


    int newTotalKills;



    public void tests()
    {
        createDirectory();
        createFileForBoss();
        replaceAndUpdate();
        statConverter();
    }


    public void createDirectory()
    {

        //should create a directoy for each login profile
        File mainFolder = new File(RUNELITE_DIR,"bossing-info");
        file = new File(mainFolder,client.getUsername());

        if(!file.exists())
        {
            file.mkdir();
            System.out.println("creating directory");
        }
        else
        {
            System.out.println("directory in place");
        }

    }

    public void createFileForBoss()
    {
        boss = plugin.currentBoss;
        fileName = boss + ".txt";

        //this finds the file at the directed path
        path = Paths.get(file.getPath(), fileName);


        try
        {

            List<String> list = Files.readAllLines(path);
            list.forEach(line -> list.toArray());

            oldTotalTime = Integer.parseInt(list.get(0).replaceAll("[^0-9]", ""));
            oldTotalVirtualTime = Integer.parseInt(list.get(1).replaceAll("[^0-9]", ""));
            oldTotalKills = Integer.parseInt(list.get(2).replaceAll("[^0-9]", ""));
            oldFastestKill = Integer.parseInt(list.get(3).replaceAll("[^0-9]", ""));

        }

        catch (IOException e)
        {
            System.out.println("File being created");
            try
            {

                StringBuilder contentBuilder = new StringBuilder();

                contentBuilder.append(plugin.totalTime);
                contentBuilder.append(" Total Time Actual\n");
                contentBuilder.append(plugin.totalTime);
                contentBuilder.append(" Total Time Virtual\n");
                contentBuilder.append(plugin.killsThisSession);
                contentBuilder.append(" Kills Tracked\n");
                contentBuilder.append(plugin.fastestKill);
                contentBuilder.append(" Fastest Kill\n");

                String content = contentBuilder.toString();
                Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);


                nullValuesForFileCreation();


            }
            catch (IOException ioException)
            {
                // exception handling
            }
            //this return keeps it from makeing odd errors eg. adding an extra 0 when file is created

        }

    }

    public void nullValuesForFileCreation()
    {
        boss = plugin.currentBoss;
        fileName = boss + ".txt";

        oldTotalTime = 0;
        oldTotalKills = 0;
        timeOffset = 0;
        newTotalTime = 0;
        lastKillTimeActual = 0;

        virtualTimeOffset = 0;
        oldTotalVirtualTime = 0;
        newTotalVirtualTime = 0;

        virtualAverageKillTime = 0;
        virtualKillsPerHour = 0;

        oldFastestKill = 99999999;
        newFastestKill = 99999999;
        lastKillTimeVirtual = 0;



    }


    public void replaceAndUpdate()
    {

        //issue is that they are 2x'ing up when being sent on kill calc. need to find a way to only send once
        System.out.println(path.toString());
        System.out.println(oldTotalTime + " old");
        System.out.println(plugin.lastKillTotalTime_1 + " last kill total time");
        System.out.println(newTotalTime + " new total time");
        System.out.println(timeOffset + " old time offset");

        if(plugin.isBossChatDisplay())
        {


            if(plugin.killsThisSession == 1)
            {
                newTotalVirtualTime = oldTotalVirtualTime + plugin.totalBossKillTime;
                newTotalTime = oldTotalTime + plugin.lastKillTotalTime_1;
            }
            else
            {

                virtualTimeOffset = plugin.totalBossKillTime - lastKillTimeVirtual;
                timeOffset = plugin.lastKillTotalTime_1 - lastKillTimeActual;

                newTotalTime = newTotalTime + timeOffset;
                newTotalVirtualTime = newTotalVirtualTime + virtualTimeOffset;
            }

            newTotalKills = oldTotalKills + 1;

            lastKillTimeActual = plugin.lastKillTotalTime_1;
            lastKillTimeVirtual = plugin.totalBossKillTime;

        }
        else
        {
            if(plugin.killsThisSession == 1)
            {

                newTotalVirtualTime = oldTotalVirtualTime + plugin.totalKillTime;
                newTotalTime = oldTotalTime + plugin.lastKillTotalTime_0;

            }
            else
            {
                virtualTimeOffset = plugin.totalKillTime - lastKillTimeVirtual;
                timeOffset = plugin.lastKillTotalTime_0 - lastKillTimeActual;

                newTotalTime = newTotalTime + timeOffset;
                newTotalVirtualTime = newTotalVirtualTime + virtualTimeOffset;

            }
            newTotalKills = oldTotalKills + 1;
            lastKillTimeActual = plugin.lastKillTotalTime_0;
            lastKillTimeVirtual = plugin.totalKillTime;
        }


        System.out.println(oldFastestKill);
        System.out.println(newFastestKill);
        System.out.println(plugin.fastestKill);

        if(plugin.fastestKill < oldFastestKill && oldFastestKill != 0)
        {
            newFastestKill = plugin.fastestKill;
        }
        else
        {
            newFastestKill = oldFastestKill;
        }

        System.out.println("---------------------------------------------------");
        System.out.println(oldTotalTime + " old new");
        System.out.println(plugin.lastKillTotalTime_1 + " last kill total time new");
        System.out.println(newTotalTime + " new total time new");
        System.out.println(timeOffset + " new time offset fun fun");
        System.out.println("testsdsdsdsd");


        try
        {
            Files.delete(path);

            StringBuilder contentBuilder = new StringBuilder();

            contentBuilder.append(newTotalTime);
            contentBuilder.append(" Total Time Actual\n");
            contentBuilder.append(newTotalVirtualTime);
            contentBuilder.append(" Total Time Virtual\n");
            contentBuilder.append(newTotalKills);
            contentBuilder.append(" Kills Tracked\n");
            contentBuilder.append(newFastestKill);
            contentBuilder.append(" Fastest Kill\n");

            String content = contentBuilder.toString();
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE,StandardOpenOption.APPEND);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }



    }



    int averageKillTime;
    double killsPerHour;

    int virtualAverageKillTime;
    double virtualKillsPerHour;

    int newkills;

    public void statConverter()
    {

        averageKillTime = newTotalTime / newTotalKills;
        virtualAverageKillTime =  newTotalVirtualTime / newTotalKills;

        if(averageKillTime == 0)
        {
            killsPerHour = 0;
        }
        if(virtualAverageKillTime == 0)
        {
            virtualKillsPerHour = 0;
        }

        else
        {
            virtualKillsPerHour = 3600D / virtualAverageKillTime;
            killsPerHour = 3600D / averageKillTime;
        }

        DecimalFormat df = new DecimalFormat("#.#");
        killsPerHour = Double.parseDouble(df.format(killsPerHour));
        virtualKillsPerHour = Double.parseDouble(df.format(virtualKillsPerHour));

    }


    public void fileReader()
    {

    }


}





