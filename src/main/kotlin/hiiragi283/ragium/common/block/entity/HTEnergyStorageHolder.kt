package hiiragi283.ragium.common.block.entity

import net.minecraft.util.math.Direction
import team.reborn.energy.api.EnergyStorage

fun interface HTEnergyStorageHolder {
    fun getEnergyStorage(direction: Direction?): EnergyStorage?
}
