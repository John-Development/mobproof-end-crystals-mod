package net.mobProofCrystals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import net.fabricmc.api.ModInitializer;
// import net.minecraft.item.ItemStack;
// import net.minecraft.item.Items;
// import net.minecraft.text.Text;
// import net.minecraft.util.ActionResult;
// import com.mojang.blaze3d.platform.;
// import net.minecraft.util.Hand;

public class mobProofCrystals implements ModInitializer {

  final private static int DEF_RAD = 32;
  final private static int DEF_LIM_DISTANCE = 1;

  public static Map<String, Object> globalConfig = null;

  @Override
  public void onInitialize() {

    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    Yaml yaml = new Yaml(options);

    try {
      File configFile = new File("crystalConfig.yaml");

      if (configFile.createNewFile()) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("radius", DEF_RAD);
        data.put("lowerLimitDistance", DEF_LIM_DISTANCE);

        FileWriter outwriter = new FileWriter("crystalConfig.yaml");
        yaml.dump(data, outwriter);
        outwriter.close();

        globalConfig = data;
      } else {
        InputStream inputStream = new FileInputStream(configFile);
        globalConfig = yaml.load(inputStream);
        inputStream.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // nameTagCrystalCallback.EVENT.register((player, crystal) -> {

    //   System.out.println("patata" + crystal);
    //   crystal.setCustomNameVisible(true);
    //   crystal.setCustomName(Text.of("patata"));

    //   ItemStack itemStack = player.getStackInHand(Hand.MAIN_HAND);
    //   ItemStack itemStack2 = player.getStackInHand(Hand.OFF_HAND);

    //   // if (itemStack == Item.)
   
    //   return ActionResult.SUCCESS;
    // });
  }
}
