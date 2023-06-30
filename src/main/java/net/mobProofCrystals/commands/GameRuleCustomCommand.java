package net.mobProofCrystals.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ReloadCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.mobProofCrystals.util.PropertiesCache;

import java.io.IOException;
import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

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
    CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) ->
      dispatcher.register(literal("gamerule")
      .requires(source -> source.hasPermissionLevel(4))
        .then(literal("configEndCrystalsLimitSpawn")
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
        .then(literal("configDefaultEndCrystalsLimitSpawn")
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
      )
    );
  }

  private int executeCrystal(CommandContext<ServerCommandSource> context, PropertiesCache cache) {
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

  private static int reload(CommandContext<ServerCommandSource> context) {
    ServerCommandSource serverCommandSource = context.getSource();
    MinecraftServer minecraftServer = serverCommandSource.getServer();
    Collection<String> collection = getResourcePacks(minecraftServer);
    serverCommandSource.sendFeedback(() -> Text.translatable("commands.custom.reload.success"), true);
    ReloadCommand.tryReloadDataPacks(collection, serverCommandSource);

    return 1;
  }

  private static Collection<String> getResourcePacks(MinecraftServer minecraftServer) {
    ResourcePackManager resourcePackManager = minecraftServer.getDataPackManager();
    resourcePackManager.scanPacks();
    Collection<String> collection2 = Lists.newArrayList(resourcePackManager.getEnabledNames());
    Collection<String> collection3 = minecraftServer.getSaveProperties().getDataConfiguration().dataPacks().getDisabled();

    for (String string : resourcePackManager.getNames()) {
      if (!collection3.contains(string) && !collection2.contains(string)) {
        collection2.add(string);
      }
    }

    return collection2;
  }
}
