package wtf.expensive.command.impl;

import net.minecraft.util.text.TextFormatting;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.util.ClientUtil;

@CommandInfo(name = "t", description = "��������/��������� ������.")
public class ToggleCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        if (args.length == 2) {
            Function func = Managment.FUNCTION_MANAGER.get(args[1]);
            func.setState(!func.isState());

            if (func.isState()) ClientUtil.sendMesage(TextFormatting.GREEN + "������ " + func.name + " �������.");
            else ClientUtil.sendMesage(TextFormatting.RED + "������ " + func.name + " ��������.");
        } else {
            ClientUtil.sendMesage(TextFormatting.RED + "�� ������� �������� �������� ������!");
        }
    }

    @Override
    public void error() {

    }
}
