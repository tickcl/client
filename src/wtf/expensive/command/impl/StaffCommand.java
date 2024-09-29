package wtf.expensive.command.impl;

import net.minecraft.util.text.TextFormatting;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;
import wtf.expensive.managment.Managment;
import wtf.expensive.managment.StaffManager;

/**
 * @author dedinside
 * @since 06.07.2023
 */
@CommandInfo(name = "staff", description = "��������� ������ � Staff List")
public class StaffCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        if (args.length >= 2) {
            switch (args[1].toLowerCase()) {
                case "add" -> addStaffName(args[2]);
                case "remove" -> removeStaffName(args[2]);
                case "clear" -> clearList();
                case "list" -> outputList();
            }
        } else {
            error();
        }
    }

    private void addStaffName(String name) {
        StaffManager manager = Managment.STAFF_MANAGER;

        if (manager.getStaffNames().contains(name)) {
            sendMessage(TextFormatting.RED + "���� ����� ��� � Staff List!");
        } else {
            manager.addStaff(name);
            sendMessage(TextFormatting.GREEN + "��� " + TextFormatting.WHITE + name + TextFormatting.GREEN + " �������� � Staff List");
        }
    }

    private void removeStaffName(String name) {
        StaffManager manager = Managment.STAFF_MANAGER;

        if (manager.getStaffNames().contains(name)) {
            manager.removeStaff(name);
            sendMessage(TextFormatting.GREEN + "��� " + TextFormatting.WHITE + name + TextFormatting.GREEN + " ������ �� Staff List");
        } else {
            sendMessage(TextFormatting.RED + "����� ������ ��� � Staff List!");
        }
    }

    private void clearList() {
        StaffManager manager = Managment.STAFF_MANAGER;

        if (manager.getStaffNames().isEmpty()) {
            sendMessage(TextFormatting.RED + "Staff List ����!");
        } else {
            manager.clearStaffs();
            sendMessage(TextFormatting.GREEN + "Staff List ������");
        }
    }

    private void outputList() {
        StaffManager manager = Managment.STAFF_MANAGER;

        sendMessage(TextFormatting.GRAY + "������ Staff:");

        for (String name : manager.getStaffNames()) {
            sendMessage(TextFormatting.WHITE + name);
        }
    }

    @Override
    public void error() {
        sendMessage(TextFormatting.GRAY + "������ � �������������" + TextFormatting.WHITE + ":");
        sendMessage(TextFormatting.WHITE + ".staff " + TextFormatting.GRAY + "<"
                + TextFormatting.RED + "add; remove; clear; list." + TextFormatting.GRAY + ">");
    }
}
