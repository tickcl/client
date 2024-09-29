package wtf.expensive.command.impl;

import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;

import java.util.ArrayList;

@CommandInfo(name = "xray", description = "��������� ��������� ���� � XRay")
public class XRayCommand extends Command {

    public final ArrayList<Integer> blocksID = new ArrayList<>();

    @Override
    public void run(String[] args) throws Exception {
        if (args.length > 2) {
            final int blockId = Integer.parseInt(args[2]);

            switch (args[1].toLowerCase()) {
                case "add" -> {
                    if (!blocksID.contains(blockId)) {
                        blocksID.add(blockId);
                        sendMessage("������� " + blockId + " � ������ ������!");
                    } else {
                        sendMessage("������ ���� ��� ���� � ������!");
                    }
                }
                case "remove" -> {
                    if (blocksID.contains(blockId)) {
                        blocksID.remove(blockId);
                        sendMessage("������ " + blockId + " �� ������ ������!");
                    } else {
                        sendMessage("������� ����� ���� � ������!");
                    }
                }
                default -> error();
            }
        } else error();
    }

    @Override
    public void error() {
        sendMessage("������ � ������������� �������!");
        sendMessage("���������� �������������:");
        sendMessage(".xray add <blockID>");
        sendMessage(".xray remove <blockID>");
    }
}
