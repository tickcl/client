package wtf.expensive.modules.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.network.play.client.CUseEntityPacket;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventCloseWindow;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.events.impl.player.EventWindowClick;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.util.world.InventoryUtil;

/**
 * @author dedinside
 * @since 04.06.2023
 */
@FunctionAnnotation(name = "InventoryMove", type = Type.Player)
public class InventoryMoveFunction extends Function {


    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventUpdate) {
            handleEventUpdate();
        }
    }

    /**
     * ������������ ������� ���� EventUpdate.
     */
    private void handleEventUpdate() {
        // ������� ������ � ���������������� �������� ���������
        final KeyBinding[] keys = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
                mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump,
                mc.gameSettings.keyBindSprint};

        // ���������, ������������ �� ����� ����  ��� ����� �������������� �����
        if (mc.currentScreen instanceof ChatScreen || mc.currentScreen instanceof EditSignScreen)
            return;
        // ���������� �� ������� ������
        for (KeyBinding keyBinding : keys) {
            // ������������� ��������� ������� �� ������ �������� ���������
            keyBinding.setPressed(InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), keyBinding.getDefault().getKeyCode()));
        }
    }
}
