package hiiragi283.ragium.api.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * [BlockEntity]を抽象化するインターフェース
 * @see mekanism.common.tile.interfaces.ITileWrapper
 */
interface HTAbstractBlockEntity {
    fun getLevel(): Level?

    fun getBlockPos(): BlockPos
}
