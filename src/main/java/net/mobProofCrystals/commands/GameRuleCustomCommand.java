package net.mobProofCrystals.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.ReloadCommand;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.permissions.Permissions;
import net.mobProofCrystals.util.PropertiesCache;

import java.io.IOException;
import java.util.Collection;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class GameRuleCustomCommand {
  private static class LazyHolder {
    private static final GameRuleCustomCommand INSTANCE = new GameRuleCustomCommand();
  }

  public static GameRuleCustomCommand getInstance() {
    return LazyHolder.INSTANCE;
  }

  public void init() {
    doEndCrystalsLimitSpawnInit();
  }

  // Command example: /gamerule configEndCrystalsLimitSpawn <radius> <lowDistance> <name>
  // Command example: /gamerule configDefaultEndCrystalsLimitSpawn <radius> <lowDistance> <name>
  private void doEndCrystalsLimitSpawnInit() {
    CommandRegistrationCallback.EVENT.register((dispatcher, _, _) ->
            dispatcher.register(literal("gamerule")
                    .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                    .then(literal("end_crystals_limit_spawn")
                            .then(argument("name", StringArgumentType.string())
                                    .executes((context) -> executeCrystal(context, PropertiesCache.getInstance()))
                            )
                            .then(argument("radius", IntegerArgumentType.integer(1, 64))
                                    .then(argument("lowDistance", IntegerArgumentType.integer(-64, 64))
                                            .executes((context) -> executeCrystal(context, PropertiesCache.getInstance()))
                                    )
                            )
                            .then(argument("radius", IntegerArgumentType.integer(1, 64))
                                    .then(argument("lowDistance", IntegerArgumentType.integer(-64, 64))
                                            .then(argument("name", StringArgumentType.string())
                                                    .executes((context) -> executeCrystal(context, PropertiesCache.getInstance()))
                                            )
                                    )
                            )
                    )
                    .then(literal("default_end_crystals_limit_spawn")
                            .then(argument("name", StringArgumentType.string())
                                    .executes((context) -> executeCrystal(context, PropertiesCache.getDefaultInstance()))
                            )
                            .then(argument("radius", IntegerArgumentType.integer(1, 64))
                                    .then(argument("lowDistance", IntegerArgumentType.integer(-64, 64))
                                            .executes((context) -> executeCrystal(context, PropertiesCache.getDefaultInstance()))
                                    )
                            )
                            .then(argument("radius", IntegerArgumentType.integer(1, 64))
                                    .then(argument("lowDistance", IntegerArgumentType.integer(-64, 64))
                                            .then(argument("name", StringArgumentType.string())
                                                    .executes((context) -> executeCrystal(context, PropertiesCache.getDefaultInstance()))
                                            )
                                    )
                            )
                    )
                    .then(literal("end_crystals_mobs")
                            .then(argument("list", StringArgumentType.string())
                                    .executes((context) -> executeCrystalMobList(context, PropertiesCache.getInstance()))
                            )
                    )
                    .then(literal("default_end_crystals_mobs")
                            .then(argument("list", StringArgumentType.string())
                                    .executes((context) -> executeCrystalMobList(context, PropertiesCache.getDefaultInstance()))
                            )
                    )
            )
    );
  }

  private int executeCrystal(CommandContext<CommandSourceStack> context, PropertiesCache cache) {
    int radius;
    int lowDistance;
    String name;

    try {
      radius = IntegerArgumentType.getInteger(context, "radius");
      cache.setProperty("radius", Integer.toString(radius));
    } catch (Exception ignored) {}
    try {
      lowDistance = IntegerArgumentType.getInteger(context, "lowDistance");
      cache.setProperty("lower-limit-distance", Integer.toString(lowDistance));
    } catch (Exception ignored) {}
    try {
      name = StringArgumentType.getString(context, "name");
      if (name.equals("-")) {
        cache.setProperty("crystal-name", "");
      } else {
        cache.setProperty("crystal-name", name);
      }
    } catch (Exception ignored) {}

    try {
      cache.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return reload(context);
  }

  private int executeCrystalMobList(CommandContext<CommandSourceStack> context, PropertiesCache cache) {
    String mobList;

    try {
      mobList = StringArgumentType.getString(context, "list");
      cache.setProperty("mobs", mobList);
    } catch (Exception ignored) {}

    try {
      cache.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return reload(context);
  }

  private static int reload(CommandContext<CommandSourceStack> context) {
    CommandSourceStack serverCommandSource = context.getSource();
    MinecraftServer minecraftServer = serverCommandSource.getServer();
    Collection<String> collection = getResourcePacks(minecraftServer);
    serverCommandSource.sendSuccess(() -> Component.translatable("commands.custom.reload.success"), true);
    ReloadCommand.reloadPacks(collection, serverCommandSource);

    return 1;
  }

  private static Collection<String> getResourcePacks(MinecraftServer minecraftServer) {
    PackRepository resourcePackManager = minecraftServer.getPackRepository();
    resourcePackManager.reload();
    Collection<String> collection2 = Lists.newArrayList(resourcePackManager.getSelectedIds());
    Collection<String> collection3 = minecraftServer.getWorldData().getDataConfiguration().dataPacks().getDisabled();

    for (String string : resourcePackManager.getAvailableIds()) {
      if (!collection3.contains(string) && !collection2.contains(string)) {
        collection2.add(string);
      }
    }

    return collection2;
  }
}
