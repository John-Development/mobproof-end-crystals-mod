package net.mobProofCrystals.util;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class CrystalPositions {
  public static final List<Vec3d> positions = new ArrayList<>();

  public static boolean isThereACrystal(Vec3d position) {
    return positions.contains(position);
  }
}

