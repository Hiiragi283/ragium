package hiiragi283.ragium.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTEnchantPowerBlock(private val power: Float, properties: Properties) : Block(properties) {
    override fun getEnchantPowerBonus(state: BlockState, level: LevelReader, pos: BlockPos): Float = power
}
