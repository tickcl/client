package wtf.expensive.command.impl;

import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;
import wtf.expensive.command.macro.Macro;
import wtf.expensive.managment.Managment;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.math.KeyMappings;

/**
 * @author dedinside
 * @since 25.06.2023
 */
@CommandInfo(name = "macro", description = "��������� ��������� ������� �� ������� ������")
public class MacroCommand extends Command {

    @Override
    public void run(String[] args) throws Exception {
        if (args.length > 1) {


            switch (args[1]) {
                case "add" -> {
                    String buttonName = args[2].toUpperCase();
                    Integer keycode = null;

                    try {
                        keycode = KeyMappings.keyMap.get(buttonName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (keycode != null) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 3; i < args.length; i++) {
                            sb.append(args[i]).append(" ");
                        }
                        Managment.MACRO_MANAGER.addMacros(new Macro(sb.toString(), keycode));
                        sendMessage(TextFormatting.GREEN + "�������� ������ ��� ������" + TextFormatting.RED + " \""
                                + args[2].toUpperCase() + TextFormatting.RED + "\" " + TextFormatting.WHITE + "� �������� "
                                + TextFormatting.RED + sb);
                    } else {
                        sendMessage("�� ������� ������ � ��������� " + buttonName);
                    }
                }
                case "clear" -> {

                    if (Managment.MACRO_MANAGER.getMacros().isEmpty()) {
                        sendMessage(TextFormatting.RED + "������ �������� ����");
                    } else {
                        sendMessage(TextFormatting.GREEN + "������ �������� " + TextFormatting.WHITE + "������� ������");
                        Managment.MACRO_MANAGER.getMacros().clear();
                        Managment.MACRO_MANAGER.updateFile();
                    }
                }
                case "remove" -> {
                    String buttonName = args[2].toUpperCase();
                    Integer keycode = null;

                    try {
                        keycode = KeyMappings.keyMap.get(buttonName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (keycode != null) {
                        sendMessage(TextFormatting.GREEN + "������ " + TextFormatting.WHITE + "��� ������ � ������ "
                                + TextFormatting.RED + "\"" + args[2] + "\"");

                        Managment.MACRO_MANAGER.deleteMacro(keycode);
                    } else {
                        sendMessage("�� ������� ������ � ��������� " + buttonName);

                    }
                }
                case "list" -> {
                    if (Managment.MACRO_MANAGER.getMacros().isEmpty()) {
                        sendMessage("������ �������� ����");
                    } else {
                        sendMessage(TextFormatting.GREEN + "������ ��������: ");
                        Managment.MACRO_MANAGER.getMacros()
                                .forEach(macro -> sendMessage(TextFormatting.WHITE + "�������: " + TextFormatting.RED
                                        + macro.getMessage() + TextFormatting.WHITE + ", ������: " + TextFormatting.RED
                                        + macro.getKey()));
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
        sendMessage(TextFormatting.WHITE + "." + "macro add " + TextFormatting.GRAY + "<"
                + TextFormatting.RED + "key" + TextFormatting.GRAY + ">" + TextFormatting.GRAY + " message");
        sendMessage(TextFormatting.WHITE + "." + "macro remove " + TextFormatting.GRAY + "<"
                + TextFormatting.RED + "key" + TextFormatting.GRAY + ">");
        sendMessage(TextFormatting.WHITE + "." + "macro list");
        sendMessage(TextFormatting.WHITE + "." + "macro clear");
    }
}
