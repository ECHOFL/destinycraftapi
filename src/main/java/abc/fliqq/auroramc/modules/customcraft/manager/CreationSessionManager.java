package abc.fliqq.auroramc.modules.customcraft.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import abc.fliqq.auroramc.core.util.LoggerUtil;
import abc.fliqq.auroramc.modules.customcraft.CreationSession;

public class CreationSessionManager {
    private static final Map<UUID, CreationSession> sessions = new HashMap<>();

    public static void addSession(Player player, CreationSession session) {
        sessions.put(player.getUniqueId(), session);
        LoggerUtil.info("Session ajoutée pour le joueur : " + player.getName());
    }

    public static CreationSession getSession(Player player) {
        CreationSession session = sessions.get(player.getUniqueId());
        return session;
    }

    public static void removeSession(Player player) {
        if (sessions.containsKey(player.getUniqueId())) {
            LoggerUtil.info("Suppression de la session pour le joueur : " + player.getName());
            sessions.remove(player.getUniqueId());
        } else {
            LoggerUtil.warning("Tentative de suppression d'une session inexistante pour le joueur : " + player.getName());
        }
    }

    public static boolean hasSession(Player player) {
        return sessions.containsKey(player.getUniqueId());
    }

}