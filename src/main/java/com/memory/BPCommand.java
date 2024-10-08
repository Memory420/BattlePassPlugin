package com.memory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

public class BPCommand implements CommandExecutor, Listener {

    private final BattlePassPlugin plugin;
    private BattlePassManager battlePassManager;

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

                    break;

                case "rewards":
                    BattlePassManager manager = plugin.getBattlePassManager();
                    player.sendMessage(ChatColor.DARK_GREEN + "Текущие награды БП");
                    for (Reward reward : manager.getRewardList()){
                        player.sendMessage(reward.toString());
                    }
                    break;
                case "reload":
                    player.sendMessage("Загрузка наград БП...");
                    battlePassManager.setRewardList(BattlePassConfigLoader.loadRewards("BattlePassRewards.yml"));
                    player.sendMessage("Количество наград в текущем файле: " + battlePassManager.getRewardList().size());
                    for (Reward reward : battlePassManager.getRewardList()){
                        player.sendMessage("Lvl " + reward.level + " Item: " + reward.rewardItem + " Amount: " + reward.rewardAmount);
                        player.sendMessage("------------------------------------");
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

        for (Reward reward : battlePassManager.getRewardList()){
            ItemStack item = new ItemStack(reward.rewardItem.getType(), reward.rewardAmount);
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

            // Если игрок взаимодействует с кастомным инвентарем
            if (clickedInventory != null && clickedInventory.equals(event.getView().getTopInventory())) {
                event.setCancelled(true); // Отменяем все действия в кастомном инвентаре

                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && clickedItem.getType() != Material.AIR) {
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
            // Если хотя бы один слот попадает в кастомный инвентарь, отменяем событие
            if (event.getRawSlots().stream().anyMatch(slot -> slot < event.getView().getTopInventory().getSize())) {
                event.setCancelled(true);
            }
        }
    }


}
