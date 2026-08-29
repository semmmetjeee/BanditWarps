package nl.bandit.warps.menu;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import nl.bandit.warps.BanditWarpsPlugin;
import nl.bandit.warps.model.Warp;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

/**
 * Opens Paper's native Minecraft dialogs for warp name and description input.
 * No inventory is created or cast, so this cannot conflict with custom inventory
 * implementations used by newer Paper builds.
 */
public final class TextInputService {
    private static final String INPUT_KEY = "value";
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    private final BanditWarpsPlugin plugin;

    public TextInputService(BanditWarpsPlugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, Warp draft, boolean name) {
        open(player, draft, name, name ? draft.name() : draft.description());
    }

    private void open(Player player, Warp draft, boolean name, String initialValue) {
        String section = "text-input." + (name ? "name" : "description");
        int maxLength = name
                ? plugin.getConfig().getInt("validation.name-max-length", 20)
                : plugin.getConfig().getInt("validation.description-max-length", 100);

        TextDialogInput.Builder input = DialogInput.text(INPUT_KEY, component(section + ".label", name ? "&fNieuwe warpnaam" : "&fBeschrijving"))
                .width(clamp(plugin.menus().dialogInt(section + ".input-width", 340), 1, 1024))
                .labelVisible(true)
                .initial(initialValue == null ? "" : initialValue)
                .maxLength(Math.max(1, maxLength));

        if (!name) {
            int maxLines = Math.max(1, plugin.menus().dialogInt(section + ".max-lines", 4));
            int height = clamp(plugin.menus().dialogInt(section + ".height", 100), 1, 512);
            input.multiline(TextDialogInput.MultilineOptions.create(maxLines, height));
        }

        DialogAction confirmAction = DialogAction.customClick(
                (response, audience) -> {
                    if (!(audience instanceof Player callbackPlayer)) return;
                    String submitted = response.getText(INPUT_KEY);
                    handleSubmit(callbackPlayer, draft, name, submitted == null ? "" : submitted);
                },
                ClickCallback.Options.builder().uses(1).build()
        );

        int buttonWidth = clamp(plugin.menus().dialogInt(section + ".button-width", 150), 1, 1024);
        ActionButton confirm = ActionButton.create(
                component(section + ".confirm", "&aOpslaan"),
                component(section + ".confirm-tooltip", "&7Sla deze invoer op"),
                buttonWidth,
                confirmAction
        );
        ActionButton cancel = ActionButton.create(
                component(section + ".cancel", "&cAnnuleren"),
                component(section + ".cancel-tooltip", "&7Ga terug zonder op te slaan"),
                buttonWidth,
                null
        );

        List<String> bodyLines = plugin.menus().dialogLines(section + ".body");
        Component body = LEGACY.deserialize(bodyLines.isEmpty()
                ? (name ? "&7Kies een unieke, herkenbare naam voor je warp." : "&7Vertel spelers kort wat ze bij deze warp kunnen vinden.")
                : String.join("\n", bodyLines));

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(component(section + ".title", name ? "&6&lWarpnaam wijzigen" : "&6&lBeschrijving wijzigen"))
                        .canCloseWithEscape(true)
                        .body(List.of(DialogBody.plainMessage(body)))
                        .inputs(List.of(input.build()))
                        .build())
                .type(DialogType.confirmation(confirm, cancel))
        );

        player.showDialog(dialog);
    }

    private void handleSubmit(Player player, Warp draft, boolean name, String rawValue) {
        String value = rawValue.trim();
        if (name) {
            int min = plugin.getConfig().getInt("validation.name-min-length", 3);
            int max = plugin.getConfig().getInt("validation.name-max-length", 20);
            if (value.length() < min || value.length() > max || !matchesConfiguredNamePattern(value)) {
                plugin.message(player, "invalid-name", Map.of("%min%", String.valueOf(min), "%max%", String.valueOf(max)));
                reopen(player, draft, true, rawValue);
                return;
            }
            if (!plugin.warps().nameAvailable(value, draft.id())) {
                plugin.message(player, "name-taken", Map.of("%name%", value));
                reopen(player, draft, true, rawValue);
                return;
            }
            draft.name(value);
        } else {
            int max = plugin.getConfig().getInt("validation.description-max-length", 100);
            if (value.length() > max) {
                plugin.message(player, "description-too-long", Map.of("%max%", String.valueOf(max)));
                reopen(player, draft, false, rawValue);
                return;
            }
            draft.description(value);
        }

        player.closeDialog();
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (player.isOnline()) plugin.menus().openSettings(player);
        });
    }

    private boolean matchesConfiguredNamePattern(String value) {
        String regex = plugin.getConfig().getString("validation.name-regex", "[A-Za-z0-9_-]+");
        try {
            return value.matches(regex == null ? "[A-Za-z0-9_-]+" : regex);
        } catch (PatternSyntaxException exception) {
            plugin.getLogger().warning("Ongeldige validation.name-regex in config.yml; standaardvalidatie wordt gebruikt.");
            return value.matches("[A-Za-z0-9_-]+");
        }
    }

    private void reopen(Player player, Warp draft, boolean name, String value) {
        player.closeDialog();
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (player.isOnline() && plugin.menus().edit(player) != null) open(player, draft, name, value);
        });
    }

    private Component component(String path, String fallback) {
        return LEGACY.deserialize(plugin.menus().dialogText(path, fallback));
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
