package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.getFoodProperties
import hiiragi283.ragium.common.block.entity.generator.base.HTItemGeneratorBlockEntity
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull

class HTCulinaryGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemGeneratorBlockEntity(RagiumBlocks.CULINARY_GENERATOR, pos, state) {
    companion object {
        @JvmStatic
        fun getTime(food: FoodProperties): Int = food.nutrition() * 200
    }

    override fun canInsertFuel(stack: ImmutableItemStack): Boolean = stack.getFoodProperties(null) != null

    override fun getRemainder(stack: ImmutableItemStack): ItemStack =
        stack.getFoodProperties(null)?.usingConvertsTo()?.getOrNull() ?: ItemStack.EMPTY

    override fun playSound(level: ServerLevel, pos: BlockPos) {
        level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 1f, 0.5f)
    }

    override fun getNewBurnTime(level: ServerLevel, pos: BlockPos): Int = inputSlot
        .getStack()
        ?.getFoodProperties(null)
        ?.let(::getTime) ?: 0
}
