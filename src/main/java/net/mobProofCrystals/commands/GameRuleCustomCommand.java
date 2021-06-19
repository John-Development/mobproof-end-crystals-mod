package net.mobProofCrystals.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ReloadCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.SaveProperties;
import net.mobProofCrystals.util.PropertiesCache;

import static net.minecraft.server.command.CommandManager.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

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

  // Command example: /gamerule doEndCrystalsLimitSpawn <radius> <lowDistance> <name>
  private void doEndCrystalsLimitSpawnInit() {
    CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
      dispatcher.register(literal("gamerule")
      .requires(source -> source.hasPermissionLevel(4))
        .then(literal("doEndCrystalsLimitSpawn")
          .then(argument("name", StringArgumentType.string())
            .executes(context -> {
              return executeCrystal(context);
            })
          )
          .then(argument("radius", IntegerArgumentType.integer(1, 64))
            .then(argument("lowDistance", IntegerArgumentType.integer(-64, 64))
              .executes(context -> {
                return executeCrystal(context);
              })
            )
          )
          .then(argument("radius", IntegerArgumentType.integer(1, 64))
            .then(argument("lowDistance", IntegerArgumentType.integer(-64, 64))
              .then(argument("name", StringArgumentType.string())
                .executes(context -> {
                  return executeCrystal(context);
                })
              )
            )
          )
        )
      );
    });
  }

  private int executeCrystal(CommandContext<ServerCommandSource> context) {
    PropertiesCache cache = PropertiesCache.getInstance();

    Integer radius = null;
    Integer lowDistance = null;
    String name = null;
    
    try {
      radius = IntegerArgumentType.getInteger(context, "radius");
      cache.setProperty("radius", radius.toString());
    } catch (Exception e) {}
    try {
      lowDistance = IntegerArgumentType.getInteger(context, "lowDistance");
      cache.setProperty("lower-limit-distance", lowDistance.toString());
    } catch (Exception e) {}
    try {
      name = StringArgumentType.getString(context, "name");
      if (name.equals("-")) {
        cache.setProperty("crystal-name", "");
      } else {
        cache.setProperty("crystal-name", name);
      }
    } catch (Exception e) {}
    
    try {
      cache.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return reload(context);
  }

  private static int reload(CommandContext<ServerCommandSource> context) {
    ServerCommandSource serverCommandSource = (ServerCommandSource) context.getSource();
    MinecraftServer minecraftServer = serverCommandSource.getMinecraftServer();
    ResourcePackManager resourcePackManager = minecraftServer.getDataPackManager();
    SaveProperties saveProperties = minecraftServer.getSaveProperties();
    Collection<String> collection = resourcePackManager.getEnabledNames();
    Collection<String> collection2 = getResourcePacks(resourcePackManager, saveProperties, collection);
    serverCommandSource.sendFeedback(new TranslatableText("commands.custom.reload.success"), true);
    ReloadCommand.tryReloadDataPacks(collection2, serverCommandSource);
    return 1;
  }

  private static Collection<String> getResourcePacks(ResourcePackManager resourcePackManager, SaveProperties saveProperties, Collection<String> collection) {
    resourcePackManager.scanPacks();
    Collection<String> collection2 = Lists.newArrayList(collection);
    Collection<String> collection3 = saveProperties.getDataPackSettings().getDisabled();
    Iterator<String> var5 = resourcePackManager.getNames().iterator();

    while(var5.hasNext()) {
       String string = (String)var5.next();
       if (!collection3.contains(string) && !collection2.contains(string)) {
          collection2.add(string);
       }
    }

    return collection2;
 }
}
