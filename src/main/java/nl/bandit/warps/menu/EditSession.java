package nl.bandit.warps.menu;
import nl.bandit.warps.model.Warp;
public record EditSession(Warp original, Warp draft, boolean newWarp) {}
