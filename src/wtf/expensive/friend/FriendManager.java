package wtf.expensive.friend;

import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dedinside
 * @since 09.06.2023
 */
public class FriendManager {
    private final List<Friend> friends = new ArrayList<>();
    public static final File file = new File(Minecraft.getInstance().gameDir, "\\expensive\\friends.exp");

    /**
     * �������������� FriendManager, �������� ����, ���� �� �� ����������, ��� ������ ������ �� �����, ���� �� ����������.
     *
     * @throws IOException ���� ��������� ������ ��� �������� ����� ��� ������ ������
     */
    public void init() throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        } else {
            System.out.println("�������� ���� � ��������");
            readFriends();
        }
    }

    /**
     * ��������� ����� � ������ ������ � ��������� ����.
     *
     * @param name ��� �����
     */
    public void addFriend(String name) {
        friends.add(new Friend(name));
        updateFile();
    }

    /**
     * ���������, �������� �� ��������� ��� ������.
     *
     * @param friend ��� �����
     * @return true, ���� ��� ������������ � ������ ������, � ��������� ������ - false
     */
    public boolean isFriend(String friend) {
        return friends.stream()
                .anyMatch(isFriend -> isFriend.getName().equals(friend));
    }

    /**
     * ������� ����� �� ������ ������ � ��������� ����.
     *
     * @param name ��� �����
     */
    public void removeFriend(String name) {
        friends.removeIf(friend -> friend.getName().equalsIgnoreCase(name));
        updateFile();
    }

    /**
     * ������� ������ ������ � ��������� ����.
     */
    public void clearFriend() {
        friends.clear();
        updateFile();
    }

    /**
     * ���������� ������ ������.
     *
     * @return ������ ������
     */
    public List<Friend> getFriends() {
        return this.friends;
    }

    /**
     * ��������� ����, �������� ������ ������.
     */
    public void updateFile() {
        try {
            StringBuilder builder = new StringBuilder();
            friends.forEach(friend -> builder.append(friend.getName()).append("\n"));
            Files.write(file.toPath(), builder.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * ������ ������ �� ����� � ��������� ������ ������.
     */
    private void readFriends() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file.getAbsolutePath()))));
            String line;
            while ((line = reader.readLine()) != null) {
                friends.add(new Friend(line));
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}