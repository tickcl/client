package wtf.expensive.modules.impl.render;

import wtf.expensive.events.Event;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.SliderSetting;

@FunctionAnnotation(name = "Click Gui", type = Type.Render)
public class ClickGui extends Function {


    public BooleanOption blur = new BooleanOption("��������", true);
    public SliderSetting blurVal = new SliderSetting("���� ��������", 15, 5, 20, 1);
    public BooleanOption glow = new BooleanOption("��������", false);

    public ClickGui() {
        super();
        addSettings(blur,blurVal,glow);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        setState(false);
    }

    @Override
    public void onEvent(Event event) {

    }
}
