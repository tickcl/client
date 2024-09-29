package wtf.expensive.ui.midnight;

import net.minecraft.util.math.MathHelper;
import wtf.expensive.util.render.ColorUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StyleManager {
    public List<Style> styles = new ArrayList<>();
    private Style currentStyle = null;

    public void init() {
        styles.addAll(Arrays.asList(
                        new Style("�������� ����������", HexColor.toColor("#FCFC36"), HexColor.toColor("#5D00B2")),
                        new Style("��������� �����", HexColor.toColor("#F4ECFF"), HexColor.toColor("#765AA5")),
                        new Style("����� �������", HexColor.toColor("#000000"), HexColor.toColor("#FFFFFF")),
                        new Style("�������� ����", HexColor.toColor("#07338A"), HexColor.toColor("#0078FF")),
                        new Style("������ �����", HexColor.toColor("#E3C3FF"), HexColor.toColor("#67FFEC")),
                        new Style("������� �����", HexColor.toColor("#FFC854"), HexColor.toColor("#4288FF")),
                        new Style("�������� ��������", HexColor.toColor("#FF00AA"), HexColor.toColor("#AA00FF")),
                        new Style("���������� ������", HexColor.toColor("#FF5C7D"), HexColor.toColor("#5CFF9E")),
                        new Style("������� ������", HexColor.toColor("#FFC300"), HexColor.toColor("#FF5800")),
                        new Style("������� ����", HexColor.toColor("#B7EFFF"), HexColor.toColor("#FFAAFF")),
                        new Style("���������� �������", HexColor.toColor("#FF007F"), HexColor.toColor("#00FFB7")),
                        new Style("������ �������", HexColor.toColor("#42275A"), HexColor.toColor("#734B6D")),
                        new Style("������� �����", HexColor.toColor("#343838"), HexColor.toColor("#005F6B")),
                        new Style("������ �����", HexColor.toColor("#2C3E50"), HexColor.toColor("#FD746C")),
                        new Style("���������", HexColor.toColor("#76ACD7"), HexColor.toColor("#F15FE9")),
                        new Style("��������", HexColor.toColor("#FD3A3A"), HexColor.toColor("#3A3A3A")),
                        new Style("���� �����", HexColor.toColor("#8B8DF6"), HexColor.toColor("#E60101")),
                        new Style("����� �����", HexColor.toColor("#F4ECFF"), HexColor.toColor("#76BEDF")),
                        new Style("����������", HexColor.toColor("#FF6347"), HexColor.toColor("#0044FF")),
                        new Style("������ �����", HexColor.toColor("#373b44"), HexColor.toColor("#4286f4")),
                        new Style("���� ����", HexColor.toColor("#765AA5"), HexColor.toColor("#F4ECFF"))
                )
        );
        currentStyle = styles.get(0);
    }

    public static Color astolfo(float yDist, float yTotal, float saturation, float speedt) {
        float speed = 1800f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * speedt;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.getHSBColor(hue, saturation, 1F);
    }

    public void setCurrentStyle(Style style) {
        currentStyle = style;
    }

    public Style getCurrentStyle() {
        return currentStyle;
    }

    public static class HexColor {
        public static int toColor(String hexColor) {
            int argb = Integer.parseInt(hexColor.substring(1), 16);
            return reAlphaInt(argb, 255);
        }

        public static int reAlphaInt(final int color, final int alpha) {
            return (MathHelper.clamp(alpha, 0, 255) << 24) | (color & 16777215);
        }
    }
}
