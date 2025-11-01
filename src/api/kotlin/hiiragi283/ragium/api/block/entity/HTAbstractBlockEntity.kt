package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumPlatform
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * [BlockEntity]を抽象化するインターフェース
 * @see mekanism.common.tile.interfaces.ITileWrapper
 */
interface HTAbstractBlockEntity {
    fun getLevel(): Level?

    fun getServerLevel(): ServerLevel? = this.getLevel() as? ServerLevel

    fun getRegistryAccess(): RegistryAccess? = getLevel()?.registryAccess() ?: RagiumPlatform.INSTANCE.getRegistryAccess()

    fun getBlockPos(): BlockPos
}
