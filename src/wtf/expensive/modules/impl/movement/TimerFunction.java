package wtf.expensive.modules.impl.movement;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.network.play.client.CConfirmTransactionPacket;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector4i;
import wtf.expensive.Initilization;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.game.EventKey;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventMotion;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BindSetting;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.ui.midnight.Style;
import wtf.expensive.ui.midnight.StyleManager;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.drag.Dragging;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.misc.TimerUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import static wtf.expensive.util.render.ColorUtil.rgba;

/**
 * @author dedinside
 * @since 05.06.2023
 */
@FunctionAnnotation(name = "Timer", type = Type.Movement)
public class TimerFunction extends Function {


    public ModeSetting mode = new ModeSetting("Mode", "Matrix", "Matrix", "Grim");

    public BindSetting grimBind = new BindSetting("������ �����", 0).setVisible(() -> mode.is("Grim"));

    public SliderSetting timerAmount = new SliderSetting("��������", 2, 1, 10, 0.01f);

    public BooleanOption smart = new BooleanOption("�����", true);
    public BooleanOption movingUp = new BooleanOption("��������� � ��������", false).setVisible(() -> !mode.is("Grim"));
    public SliderSetting upValue = new SliderSetting("��������", 0.02f, 0.01f, 0.5f, 0.01f).setVisible(() -> movingUp.get());

    public SliderSetting ticks = new SliderSetting("�������� ��������", 1.0f, 0.15f, 3.0f, 0.1f).setVisible(() -> !mode.is("Grim"));


    public final Dragging timerHUD = Initilization.createDrag(this, "TimerHUD", 500, 300);

    public float maxViolation = 100.0F;
    private float violation = 0.0F;
    private double prevPosX, prevPosY, prevPosZ;
    private float yaw;
    private float pitch;
    public float animWidth;

    private boolean isBoost;

    private TimerUtil timerUtil = new TimerUtil();

    public TimerFunction() {
        addSettings(mode,timerAmount, grimBind, smart, movingUp, upValue, ticks);
    }

    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventKey e) {
            if (e.key == grimBind.getKey()) {
                isBoost = true;
            }
        }
        if (event instanceof EventMotion eventPre && mode.is("Grim")) {
            updateTimer(eventPre.getYaw(), eventPre.getPitch(), eventPre.getX(), eventPre.getY(), eventPre.getZ());
        }
        if (event instanceof EventUpdate) {
            handleEventUpdate();
        }
        if (event instanceof EventPacket e) {
            handlePacketEvent(e);
        }
    }

    private void handlePacketEvent(EventPacket e) {
        if (!mode.is("Grim")) {
            return;
        }

        if (e.isReceivePacket()) {
            isFlagged(e);
            isDamaged(e);
        }
        if (e.isSendPacket()) {
            cancelTransaction(e);
        }
    }

    private void cancelTransaction(EventPacket e) {
        if (e.getPacket() instanceof CConfirmTransactionPacket p) {
            e.setCancel(true);
        }
    }

    private void isDamaged(EventPacket e) {
        if (e.getPacket() instanceof SEntityVelocityPacket p) {
            if (p.getEntityID() == mc.player.getEntityId()) {
                reset();
                resetSpeed();
            }
        }
    }

    private void isFlagged(EventPacket e) {
        if (e.getPacket() instanceof SPlayerPositionLookPacket p) {
            if (isBoost) {
                resetSpeed();
                reset();
            }
        }
    }

    /**
     * ������������ ������� ���� EventUpdate.
     */
    private void handleEventUpdate() {
        if (timerUtil.hasTimeElapsed(25000)) {
            reset();
            timerUtil.reset();
        }
        if (!mc.player.isOnGround() && !isBoost) {
            this.violation += 0.1f;
            this.violation = MathHelper.clamp(this.violation, 0.0F, this.maxViolation / (mode.is("Grim") ? 1 : this.timerAmount.getValue().floatValue()));
        }
        if (mode.is("Grim") && !isBoost) {
            return;
        }
        // ������������� �������� �������� �������
        mc.timer.timerSpeed = this.timerAmount.getValue().floatValue();

        // ��������� ������� ��� ������ ������ ��� ���� �������� ������� <= 1.0F
        if (!this.smart.get() || mc.timer.timerSpeed <= 1.0F) {
            return;
        }
        // ���������� �������� ticks � ���������� speed
        if (this.violation < (this.maxViolation) / (this.timerAmount.getValue().floatValue())) {
            this.violation += mode.is("Grim") ? 0.05f : this.ticks.getValue().floatValue();
            this.violation = MathHelper.clamp(this.violation, 0.0F, this.maxViolation / (mode.is("Grim") ? 1 : this.timerAmount.getValue().floatValue()));
        } else {
            // ���������� speed, ���� ��������� ������������ ��������
            this.resetSpeed();
        }
    }

    /**
     * ��������� ������ � ������������� ����� �������� ��������� � �������� ������.
     *
     * @param yaw   �������� �������� �� �����������
     * @param pitch �������� �������� �� ���������
     * @param posX  ���������� X ��������� ������
     * @param posY  ���������� Y ��������� ������
     * @param posZ  ���������� Z ��������� ������
     */
    public void updateTimer(float yaw, float pitch, double posX, double posY, double posZ) {
        // ���������, ��������� �� ����� � ��� �� ��������������
        if (this.notMoving()) {
            // ��������� speed �� ������ ticks � ��������� 0.4
            if (mode.is("Grim")) {
                this.violation = (float) ((double) this.violation - 0.05f);
            } else {
                this.violation = (float) ((double) this.violation - ((double) this.ticks.getValue().floatValue() + 0.4));
            }
        } else if (this.movingUp.get() && !mode.is("Grim")) {
            // ��������� speed, ���� �������� ����� ��������
            this.violation -= this.upValue.getValue().floatValue();
        }

        // ������������ speed � �������� ��������
        this.violation = (float) MathHelper.clamp(this.violation, 0.0, Math.floor(this.maxViolation));

        // ������������� ����� �������� ��������� � ��������
        this.prevPosX = posX;
        this.prevPosY = posY;
        this.prevPosZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * ���������, ��������� �� ����� � ��������.
     */
    private boolean notMoving() {
        // ���������, ��������� �� ������� ���������� � ������� � �������
        return this.prevPosX == mc.player.getPosX()
                && this.prevPosY == mc.player.getPosY()
                && this.prevPosZ == mc.player.getPosZ()
                && this.yaw == mc.player.rotationYaw
                && this.pitch == mc.player.rotationPitch;
    }

    public void renderTimer(MatrixStack stack, MainWindow window) {
        if (ClientUtil.legitMode) return;
        float quotient = maxViolation / timerAmount.getValue().floatValue();
        float minimumValue = Math.min(getViolation(), quotient);

        float width = 70;
        float height = 24;

        timerHUD.setHeight(height);
        timerHUD.setWidth(width);




        float x = timerHUD.getX();
        float y = timerHUD.getY();

        float timerWidth = width - 10;
        float targetWidth = ((quotient - minimumValue) / quotient) * timerWidth;

        Style current = Managment.STYLE_MANAGER.getCurrentStyle();
        Vector4i colors = new Vector4i(
                current.getColor(0),
                current.getColor(90),
                current.getColor(180),
                current.getColor(270)
        );

        int value = (int) Math.round(MathHelper.clamp((animWidth / timerWidth) * 100.0D, 0, 100));
        animWidth = AnimationMath.fast(animWidth, targetWidth, 10);

        RenderUtil.Render2D.drawShadow(x, y, width, height, 10, colors.x, colors.y, colors.w, colors.z);

        RenderUtil.Render2D.drawGradientRound(x, y, width, height, 3,  colors.x, colors.y, colors.w, colors.z);

        RenderUtil.Render2D.drawRoundedRect(x, y, width, height, 2.5f, rgba(21,21,21, 200));

        RenderUtil.Render2D.drawRoundedRect(x + 5, y + 14, timerWidth, 6, 1, StyleManager.HexColor.toColor("#33302e"));
        RenderUtil.Render2D.drawGradientRound(x + 5, y + 14, animWidth, 6, 1, colors.x, colors.y, colors.w, colors.z);
        Fonts.gilroyBold[16].drawString(stack, "Timer", x + 5, y + 4.5f, RenderUtil.IntColor.rgba(200, 200, 200, 255));
        Fonts.gilroy[12].drawCenteredString(stack, value + "%", x + (width / 2), y + 14 + 6 / 2 - 1, -1);
    }

    private float calculateXPosition(MainWindow window, float width) {
        return window.scaledWidth() / 2f - width / 2f;
    }

    private float calculateYPosition(MainWindow window, float height) {
        return window.scaledHeight() - height / 2 - 90;
    }

    /**
     * ���������� ������� �������� violation.
     */
    public float getViolation() {
        return this.violation;
    }

    /**
     * ���������� �������� � ���������.
     */
    public void resetSpeed() {
        // ���������� �������� � ���������
        this.setState(false);
        mc.timer.timerSpeed = 1.0F;
    }

    public void reset() {
        if (mode.is("Grim")) {
            violation = this.maxViolation / this.timerAmount.getValue().floatValue();
            this.isBoost = false;
        }
    }

    @Override
    public void onDisable() {
        reset();
        // ���������� �������� ������� ��� ����������
        mc.timer.timerSpeed = 1;
        timerUtil.reset();
        super.onDisable();
    }

    @Override
    public void onEnable() {
        reset();
        // ������������� �������� ������� � 1.0F ��� ���������
        mc.timer.timerSpeed = 1.0F;
        super.onEnable();
    }
}