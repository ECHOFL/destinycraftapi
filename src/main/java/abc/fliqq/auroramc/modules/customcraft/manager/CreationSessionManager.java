package abc.fliqq.auroramc.modules.customcraft.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import abc.fliqq.auroramc.modules.customcraft.CreationSession;

public class CreationSessionManager {
    private static final Map<UUID, CreationSession> sessions = new HashMap<>();

    public static void addSession(Player player, CreationSession session) {
        sessions.put(player.getUniqueId(), session);
    }

    public static CreationSession getSession(Player player) {
        return sessions.get(player.getUniqueId());
    }

    public static void removeSession(Player player) {
        sessions.remove(player.getUniqueId());
    }

    public static boolean hasSession(Player player) {
        return sessions.containsKey(player.getUniqueId());
    }

}