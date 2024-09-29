package wtf.expensive.modules.impl.player;

import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
/**
 * @author dedinside
 * @since 04.06.2023
 */
@FunctionAnnotation(name = "FastBreak", type = Type.Player)
public class FastBreakFunction extends Function {

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            // ���������� �������� ����� ����� ��� ������
            mc.playerController.blockHitDelay = 0;

            // ���������, ��������� �� ������� ���� ����� �������� 1.0F
            if (mc.playerController.curBlockDamageMP > 1.0F) {
                // ���� ���������, ������������� �������� ����� ����� ������ 1.0F
                mc.playerController.curBlockDamageMP = 1.0F;
            }
        }
    }
}
