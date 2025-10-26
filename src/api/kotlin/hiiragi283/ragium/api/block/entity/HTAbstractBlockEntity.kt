package hiiragi283.ragium.api.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

/**
 * @see mekanism.common.tile.interfaces.ITileWrapper
 */
interface HTAbstractBlockEntity {
    fun getLevel(): Level?

    fun getBlockPos(): BlockPos
}
