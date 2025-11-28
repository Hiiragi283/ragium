package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.common.block.entity.generator.base.HTItemGeneratorBlockEntity
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTThermalGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemGeneratorBlockEntity<Int>(RagiumBlocks.THERMAL_GENERATOR, pos, state) {
    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): Int? = input.item().getBurnTime(null).takeIf { it > 0 }

    override fun getEnergyToGenerate(recipe: Int): Int = recipe

    override fun onGenerationUpdated(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: Int,
    ) {
        super.onGenerationUpdated(level, pos, state, input, recipe)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 0.5f)
    }
}
