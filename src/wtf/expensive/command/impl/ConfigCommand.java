package wtf.expensive.command.impl;

import net.minecraft.util.text.TextFormatting;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;
import wtf.expensive.config.Config;
import wtf.expensive.config.ConfigManager;
import wtf.expensive.managment.Managment;

import java.io.IOException;

/**
 * @author dedinside
 * @since 26.06.2023
 */
@CommandInfo(name = "cfg", description = "����� ��� ������� ����� ��������� ���������")
public class ConfigCommand extends Command {

    @Override
    public void run(String[] args) throws Exception {
        if (args.length > 1) {
            ConfigManager configManager = Managment.CONFIG_MANAGER;
            switch (args[1]) {
                case "save" -> {
                    String configName = args[2];
                    configManager.saveConfiguration(configName);
                    sendMessage("������������ " + TextFormatting.GRAY + args[2] + TextFormatting.RESET + " ������� ���������.");
                }
                case "load" -> {
                    String configName = args[2];
                    configManager.loadConfiguration(configName, false);
                }
                case "remove" -> {
                    String configName = args[2];
                    try {
                        configManager.deleteConfig(configName);
                        sendMessage("������������ " + TextFormatting.GRAY + args[2] + TextFormatting.RESET + " ������� �������.");
                    } catch (Exception e) {
                        sendMessage("�� ������� ������� ������������ � ������ " + configName + " �������� � ������ ����.");
                    }
                }
                case "list" -> {
                    if (configManager.getAllConfigurations().isEmpty()) {
                        sendMessage("������ �������� ����.");
                        return;
                    }
                    for (String s : configManager.getAllConfigurations()) {
                        sendMessage(s.replace(".cfg", ""));
                    }
                }
                case "dir" -> {
                    try {
                        Runtime.getRuntime().exec("explorer " + ConfigManager.CONFIG_DIR.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            error();
        }
    }

    @Override
    public void error() {
        sendMessage(TextFormatting.GRAY + "������ � �������������" + TextFormatting.WHITE + ":");
        sendMessage(TextFormatting.WHITE + "." + "cfg load " + TextFormatting.GRAY + "<name>");
        sendMessage(TextFormatting.WHITE + "." + "cfg save " + TextFormatting.GRAY + "<name>");
        sendMessage(TextFormatting.WHITE + "." + "cfg remove " + TextFormatting.GRAY + "<name>");
        sendMessage(TextFormatting.WHITE + "." + "cfg list" + TextFormatting.GRAY
                + " - �������� ������ ��������");
        sendMessage(TextFormatting.WHITE + "." + "cfg dir" + TextFormatting.GRAY
                + " - ������� ����� � ���������");
    }
}
