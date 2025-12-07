package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.getCraftingRemainingItem
import hiiragi283.ragium.common.block.entity.generator.base.HTItemGeneratorBlockEntity
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class HTThermalGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemGeneratorBlockEntity(RagiumBlocks.THERMAL_GENERATOR, pos, state) {
    override fun canInsertFuel(stack: ImmutableItemStack): Boolean = stack.unwrap().getBurnTime(null) > 0

    override fun getRemainder(stack: ImmutableItemStack): ItemStack = stack.getCraftingRemainingItem()
    
    override fun playSound(level: ServerLevel, pos: BlockPos) {
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 0.5f)
    }

    override fun getNewBurnTime(level: ServerLevel, pos: BlockPos): Int = inputSlot.getStack()?.unwrap()?.getBurnTime(null) ?: 0
}
