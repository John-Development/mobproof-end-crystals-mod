package net.mobProofCrystals.renderer;

import me.x150.MessageSubscription;
import me.x150.renderer.event.RenderEvent;
import me.x150.renderer.render.Renderer3d;
import net.minecraft.util.math.Vec3d;
import net.mobProofCrystals.util.PropertiesCache;

import java.awt.*;

import static net.mobProofCrystals.util.CrystalPositions.positions;

public class Listener {

  @MessageSubscription
  public void onWorldRendered(RenderEvent.World world) {
    // Quad at (0, 0, 0):
//    Renderer3d.renderFilled(world.getMatrixStack(), Color.RED, Vec3d.ZERO, new Vec3d(1, 1, 1));
    // Quad outline at (0, 0, 0):

    PropertiesCache cache = PropertiesCache.getInstance();
    if (cache.getBoolProperty("render")) {
      positions.forEach(pos -> renderCrystalOutline(world, pos));
    }
  }

  void renderCrystalOutline(RenderEvent.World world, Vec3d pos) {
    PropertiesCache cache = PropertiesCache.getInstance();

    int radius = cache.getIntProperty("radius");
    int lowerLimit = cache.getIntProperty("lower-limit-distance");
    double size = 1 + 2 * radius;

    Vec3d origin = new Vec3d(pos.x - 0.5 - radius, pos.y + lowerLimit - 1, pos.z - 0.5 - radius);
    Vec3d dimension = new Vec3d(size, size, size);


    Renderer3d.renderOutline(
      world.getMatrixStack(),
      Color.RED,
      origin,
      dimension
    );

    int lines = radius / 8;

    for(int i = 1; i <= lines; i++) {
      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(origin.x + 8 * i, origin.y, origin.z),
        new Vec3d(origin.x + 8 * i, origin.y + size, origin.z)
      );
    }
    for(int i = lines; i >= 1; i--) {
      double x = origin.x + size - 8 * i;

      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(x, origin.y, origin.z),
        new Vec3d(x, origin.y + size, origin.z)
      );
    }

    for(int i = 1; i <= lines; i++) {
      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(origin.x, origin.y, origin.z + 8 * i),
        new Vec3d(origin.x, origin.y + size, origin.z + 8 * i)
      );
    }
    for(int i = lines; i >= 1; i--) {
      double z = origin.z + size - 8 * i;

      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(origin.x, origin.y, z),
        new Vec3d(origin.x, origin.y + size, z)
      );
    }

    for(int i = 1; i <= lines; i++) {
      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(origin.x + 8 * i, origin.y, origin.z + size),
        new Vec3d(origin.x + 8 * i, origin.y + size, origin.z + size)
      );
    }
    for(int i = lines; i >= 1; i--) {
      double x = origin.x + size - 8 * i;

      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(x, origin.y, origin.z + size),
        new Vec3d(x, origin.y + size, origin.z + size)
      );
    }

    for(int i = 1; i <= lines; i++) {
      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(origin.x + size, origin.y, origin.z + 8 * i),
        new Vec3d(origin.x + size, origin.y + size, origin.z + 8 * i)
      );
    }
    for(int i = lines; i >= 1; i--) {
      double z = origin.z + size - 8 * i;

      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(origin.x + size, origin.y, z),
        new Vec3d(origin.x + size, origin.y + size, z)
      );
    }

    // abajo

    for(int i = 1; i <= lines; i++) {
      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(origin.x + 8 * i, origin.y, origin.z),
        new Vec3d(origin.x + 8 * i, origin.y, origin.z + size)
      );
    }
    for(int i = lines; i >= 1; i--) {
      double x = origin.x + size - 8 * i;

      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(x, origin.y, origin.z),
        new Vec3d(x, origin.y, origin.z + size)
      );
    }

    for(int i = 1; i <= lines; i++) {
      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(origin.x, origin.y, origin.z + 8 * i),
        new Vec3d(origin.x + size, origin.y, origin.z + 8 * i)
      );
    }
    for(int i = lines; i >= 1; i--) {
      double z = origin.z + size - 8 * i;

      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(origin.x, origin.y, z),
        new Vec3d(origin.x + size, origin.y, z)
      );
    }

    for(int i = 1; i <= lines; i++) {
      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(origin.x + 8 * i, origin.y + size, origin.z),
        new Vec3d(origin.x + 8 * i, origin.y + size, origin.z + size)
      );
    }
    for(int i = lines; i >= 1; i--) {
      double x = origin.x + size - 8 * i;

      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(x, origin.y + size, origin.z),
        new Vec3d(x, origin.y + size, origin.z + size)
      );
    }

    for(int i = 1; i <= lines; i++) {
      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(origin.x, origin.y + size, origin.z + 8 * i),
        new Vec3d(origin.x + size, origin.y + size, origin.z + 8 * i)
      );
    }
    for(int i = lines; i >= 1; i--) {
      double z = origin.z + size - 8 * i;

      Renderer3d.renderLine(
        world.getMatrixStack(),
        Color.RED,
        new Vec3d(origin.x, origin.y + size, z),
        new Vec3d(origin.x + size, origin.y + size, z)
      );
    }

//    Renderer3d.renderEdged(
//      world.getMatrixStack(),
//      new Color(0x33FF0000, true),
//      Color.RED,
//      new Vec3d(pos.x - 0.5001 - radius, pos.y + lowerLimit - 1.0001, pos.z - 0.5001 - radius),
//      new Vec3d(size, size, size)
//    );
  }
}