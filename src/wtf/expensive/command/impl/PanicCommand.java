package wtf.expensive.command.impl;

import com.mojang.datafixers.types.Func;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.util.ClientUtil;

@CommandInfo(name = "panic", description = "��������� ��� ������� ����")

public class PanicCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        if (args.length == 1) {
            Managment.FUNCTION_MANAGER.getFunctions().stream().filter(function -> function.state).forEach(function -> function.setState(false));
            ClientUtil.sendMesage("�������� ��� ������!");
        } else error();
    }

    @Override
    public void error() {

    }
}
