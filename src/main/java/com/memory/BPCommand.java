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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BPCommand implements CommandExecutor, Listener {

    private final BattlePassPlugin plugin;

    public BPCommand(BattlePassPlugin plugin) {
        this.plugin = plugin;
        // Регистрируем обработчик событий
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

        ItemStack item1 = new ItemStack(Material.APPLE, 4);
        ItemStack item2 = new ItemStack(Material.OAK_LOG, 16);

        inventory.setItem(9, item1);
        inventory.setItem(10, item2);

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Проверяем, что инвентарь - это наш кастомный инвентарь
        if (event.getView().getTitle().equals(ChatColor.GOLD + "Battle Pass Menu")) {
            if (event.getClickedInventory() == event.getView().getTopInventory()) {
                // Если игрок взаимодействует с кастомным инвентарем, отменяем все действия
                event.setCancelled(true);

                Player player = (Player) event.getWhoClicked();
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
            } else if (event.getClick().isShiftClick()) {
                // Предотвращаем перемещение предметов с помощью Shift + ЛКМ в кастомный инвентарь
                if (event.getView().getTopInventory().equals(event.getClickedInventory())) {
                    event.setCancelled(true);
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
