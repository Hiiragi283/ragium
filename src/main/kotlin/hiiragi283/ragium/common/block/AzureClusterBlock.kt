package hiiragi283.ragium.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.AmethystClusterBlock
import net.minecraft.world.level.block.state.BlockState

class AzureClusterBlock(properties: Properties) : AmethystClusterBlock(7f, 3f, properties) {
    override fun getEnchantPowerBonus(state: BlockState, level: LevelReader, pos: BlockPos): Float = 5f
}
