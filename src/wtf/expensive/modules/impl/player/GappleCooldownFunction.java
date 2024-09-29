package wtf.expensive.modules.impl.player;

import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventCalculateCooldown;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.ClientUtil;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author dedinside
 * @since 09.06.2023
 */
@FunctionAnnotation(name = "Items Cooldown", type = Type.Player)
public class GappleCooldownFunction extends Function {

    public static final MultiBoxSetting items = new MultiBoxSetting("��������",
            new BooleanOption("�����", true),
            new BooleanOption("�����", true),
            new BooleanOption("������", true),
            new BooleanOption("�����", false));

    private static final SliderSetting gappleTime = new SliderSetting("������� �����", 4.5F, 1.0F, 10.0F, 0.05F)
            .setVisible(() -> items.get(0));
    private static final SliderSetting pearlTime = new SliderSetting("������� �����", 14.05F, 1.0F, 15.0F, 0.05F)
            .setVisible(() -> items.get(1));
    private static final SliderSetting horusTime = new SliderSetting("������� �������", 2.3F, 1.0F, 10.0F, 0.05F)
            .setVisible(() -> items.get(2));
    private static final SliderSetting enchantmentGappleTime = new SliderSetting("������� �����", 4.5F, 1.0F, 10.0F, 0.05F)
            .setVisible(() -> items.get(3));
    private BooleanOption onlyPvP = new BooleanOption("������ � PVP", false);

    public HashMap<Item, Long> lastUseItemTime = new HashMap<>();

    public GappleCooldownFunction() {
        addSettings(items, gappleTime, enchantmentGappleTime, pearlTime, horusTime, onlyPvP);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventCalculateCooldown calculateCooldown) {
            applyGoldenAppleCooldown(calculateCooldown);
        }
    }

    /**
     * ��������� �������� ������������� �������� ������.
     * �������� ������� ����� �������� � ������������� ��������������� ��������
     * ��� �������� ������.
     */
    private void applyGoldenAppleCooldown(EventCalculateCooldown calcCooldown) {
        List<Item> itemsToRemove = new ArrayList<>();

        for (Map.Entry<Item, Long> entry : lastUseItemTime.entrySet()) {
            ItemEnum itemEnum = ItemEnum.getItemEnum(entry.getKey());

            if (itemEnum == null || calcCooldown.itemStack != itemEnum.getItem() || !itemEnum.getActive().get() || isNotPvP()) {
                continue;
            }

            long time = System.currentTimeMillis() - entry.getValue();
            float timeSetting = itemEnum.getTime().get() * 1000.0F;

            if (time < timeSetting && itemEnum.getActive().get()) {
                calcCooldown.setCooldown(time / timeSetting);
            } else {
                itemsToRemove.add(itemEnum.getItem());
            }
        }

        itemsToRemove.forEach(lastUseItemTime::remove);
    }

    public boolean isNotPvP() {
        return onlyPvP.get() && !ClientUtil.isPvP();
    }

    public boolean isCurrentItem(ItemEnum item) {
        if (!item.getActive().get()) {
            return false;
        }

        return item.getActive().get() && Arrays.stream(ItemEnum.values()).anyMatch(e -> e == item);
    }

    @Getter
    public enum ItemEnum {
        CHORUS(Items.CHORUS_FRUIT,
                () -> items.get(2),
                () -> horusTime.getValue().floatValue()),
        GOLDEN_APPLE(Items.GOLDEN_APPLE,
                () -> items.get(0),
                () -> gappleTime.getValue().floatValue()),
        ENCHANTED_GOLDEN_APPLE(Items.ENCHANTED_GOLDEN_APPLE,
                () -> items.get(3),
                () -> enchantmentGappleTime.getValue().floatValue()),
        ENDER_PEARL(Items.ENDER_PEARL,
                () -> items.get(1),
                () -> pearlTime.getValue().floatValue());

        private final Item item;
        private final Supplier<Boolean> active;
        private final Supplier<Float> time;


        ItemEnum(Item item, Supplier<Boolean> active, Supplier<Float> time) {
            this.item = item;
            this.active = active;
            this.time = time;
        }

        public static ItemEnum getItemEnum(Item item) {
            return Arrays.stream(ItemEnum.values())
                    .filter(e -> e.getItem() == item)
                    .findFirst()
                    .orElse(null);
        }
    }

}