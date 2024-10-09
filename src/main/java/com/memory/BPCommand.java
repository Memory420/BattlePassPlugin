package com.memory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class BPCommand implements CommandExecutor, Listener {

    private final BattlePassPlugin plugin;
    private final BattlePassManager battlePassManager;

    public BPCommand(BattlePassPlugin plugin, BattlePassManager bpn) {
        this.battlePassManager = bpn;
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Уточните подкоманду: menu, info или help.");
            return true;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            switch (args[0].toLowerCase()) {
                case "menu":
                    player.sendMessage(ChatColor.GREEN + "Открываем Battle Pass...");
                    openMenu(player);
                    break;

                case "info":
                    player.sendMessage(ChatColor.YELLOW + "Версия плагина BattlePass: " + ChatColor.GOLD + plugin.getDescription().getVersion());
                    break;

                case "help":
                    player.sendMessage(ChatColor.AQUA + "Battle Pass команды:");
                    player.sendMessage(ChatColor.AQUA + "/bp menu - Открывает BP меню");
                    player.sendMessage(ChatColor.AQUA + "/bp info - Отображает информацию о команде");
                    player.sendMessage(ChatColor.AQUA + "/bp help - Отображает справочник команды");
                    player.sendMessage(ChatColor.AQUA + "/bp rewards - Отображает награды загруженного БП");
                    player.sendMessage(ChatColor.AQUA + "/bp reload - Загружает файл с данными о наградах БП");
                    player.sendMessage(ChatColor.AQUA + "/bp admin save - Сохраняет прогресс Battle Pass");
                    player.sendMessage(ChatColor.AQUA + "/bp admin load - Загружает прогресс Battle Pass");
                    break;

                case "rewards":
                    BattlePassManager manager = plugin.getBattlePassManager();
                    player.sendMessage(ChatColor.DARK_GREEN + "Текущие награды БП");
                    for (Reward reward : manager.getRewardList()) {
                        player.sendMessage("Lvl " + reward.level + " Item: " + reward.rewardItem + " Amount: " + reward.rewardAmount);
                        player.sendMessage("------------------------------------");
                    }
                    break;

                case "reload":
                    player.sendMessage("Загрузка наград БП...");
                    battlePassManager.setRewardList(BattlePassConfigLoader.loadRewards("BattlePassRewards.yml"));
                    player.sendMessage("Количество наград в текущем файле: " + battlePassManager.getRewardList().size());
                    for (Reward reward : battlePassManager.getRewardList()) {
                        player.sendMessage("Lvl " + reward.level + " Item: " + reward.rewardItem + " Amount: " + reward.rewardAmount);
                        player.sendMessage("------------------------------------");
                    }
                    break;

                case "admin":
                    if (args.length < 2) {
                        player.sendMessage(ChatColor.RED + "Укажите подкоманду для admin: save или load.");
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "save":
                                player.sendMessage(ChatColor.GREEN + "Сохранение прогресса Battle Pass...");
                                battlePassManager.saveAllProgress();
                                break;

                            case "load":
                                player.sendMessage(ChatColor.GREEN + "Загрузка прогресса Battle Pass...");
                                battlePassManager.loadAllProgress();
                                break;
                            case "addxp":
                                if (args.length < 4) {
                                    player.sendMessage(ChatColor.RED + "Использование: /bp admin addxp <ник> <кол-во>");
                                } else {
                                    String targetPlayerName = args[2];
                                    String xpString = args[3];

                                    Player targetPlayer = plugin.getServer().getPlayer(targetPlayerName);
                                    if (targetPlayer == null) {
                                        player.sendMessage(ChatColor.RED + "Игрок с ником " + targetPlayerName + " не найден.");
                                    } else {
                                        try {
                                            int xp = Integer.parseInt(xpString);
                                            if (xp < 0) {
                                                player.sendMessage(ChatColor.RED + "Количество XP должно быть положительным числом.");
                                            } else {
                                                battlePassManager.addXp(targetPlayer, xp);
                                                player.sendMessage(ChatColor.GREEN + "Добавлено " + xp + " XP игроку " + targetPlayer.getName() + ".");
                                                targetPlayer.sendMessage(ChatColor.GREEN + "Вам добавлено " + xp + " XP для Battle Pass!");
                                            }
                                        } catch (NumberFormatException e) {
                                            player.sendMessage(ChatColor.RED + "Количество XP должно быть целым числом.");
                                        }
                                    }
                                }
                                break;
                            default:
                                player.sendMessage(ChatColor.RED + "Неизвестная подкоманда admin. Используйте save или load.");
                                break;
                        }
                    }
                    break;

                default:
                    player.sendMessage(ChatColor.RED + "Неизвестная подкоманда. Используйте /bp help для списка команд.");
                    break;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Эта команда может использоваться только игроком.");
        }

        return true;
    }


    private void openMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 27, ChatColor.GOLD + "Battle Pass Menu");

        NamespacedKey key = new NamespacedKey(plugin, "battle_pass_reward");

        for (Reward reward : battlePassManager.getRewardList()) {
            ItemStack item = new ItemStack(reward.rewardItem.getType(), reward.rewardAmount);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "reward_level_" + reward.level);
                item.setItemMeta(meta);
            }

            inventory.setItem(8 + reward.level, item);
        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Проверяем, что инвентарь - это наш кастомный инвентарь
        if (event.getView().getTitle().equals(ChatColor.GOLD + "Battle Pass Menu")) {
            Inventory clickedInventory = event.getClickedInventory();
            Player player = (Player) event.getWhoClicked();
            NamespacedKey key = new NamespacedKey(plugin, "battle_pass_reward");

            // Если игрок взаимодействует с кастомным инвентарем
            if (clickedInventory != null && clickedInventory.equals(event.getView().getTopInventory())) {
                event.setCancelled(true); // Отменяем все действия в кастомном инвентаре

                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                    // Проверяем, есть ли у предмета заданный тег
                    if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                        player.sendMessage(ChatColor.RED + "Нельзя взаимодействовать с этим предметом!");
                    } else {
                        // Проверяем, какой предмет был нажат
                        switch (clickedItem.getType()) {
                            case APPLE:
                                player.sendMessage(ChatColor.GREEN + "Вы нажали на яблоко!");
                                // Логика для "нажатия" на яблоко
                                break;

                            case OAK_LOG:
                                player.sendMessage(ChatColor.GREEN + "Вы нажали на дубовое бревно!");
                                // Логика для "нажатия" на бревно
                                break;

                            default:
                                break;
                        }
                    }
                }
            } else if (clickedInventory != null && clickedInventory.equals(event.getView().getBottomInventory())) {
                // Проверка для инвентаря игрока
                if (event.getClick().isShiftClick() || event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                    // Если игрок использует Shift-клик или перемещение в другой инвентарь, отменяем действие
                    event.setCancelled(true);
                } else {
                    // Разрешаем обычное взаимодействие, если это не связано с перемещением в кастомный инвентарь
                    event.setCancelled(false);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        // Проверяем, что инвентарь - это наш кастомный инвентарь
        if (event.getView().getTitle().equals(ChatColor.GOLD + "Battle Pass Menu")) {
            NamespacedKey key = new NamespacedKey(plugin, "battle_pass_reward");

            // Проверяем все перетаскиваемые предметы
            for (ItemStack item : event.getNewItems().values()) {
                if (item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    // Если хотя бы один предмет имеет наш тег, отменяем событие
                    event.setCancelled(true);
                    return;
                }
            }

            // Если хотя бы один слот попадает в кастомный инвентарь, отменяем событие
            if (event.getRawSlots().stream().anyMatch(slot -> slot < event.getView().getTopInventory().getSize())) {
                event.setCancelled(true);
            }
        }
    }


}
