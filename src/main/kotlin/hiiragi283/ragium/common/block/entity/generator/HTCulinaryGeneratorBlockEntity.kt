package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.math.plus
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.math.toFraction
import hiiragi283.ragium.common.block.entity.generator.base.HTItemGeneratorBlockEntity
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCulinaryGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemGeneratorBlockEntity<FoodProperties>(RagiumBlocks.CULINARY_GENERATOR, pos, state) {
    companion object {
        @JvmStatic
        fun getEnergy(food: FoodProperties): Int = (food.nutrition() * (1 + food.saturation().toFraction()) * 10).toInt()
    }

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): FoodProperties? = input.item().getFoodProperties(null)

    override fun getEnergyToGenerate(recipe: FoodProperties): Int = getEnergy(recipe)

    override fun onGenerationUpdated(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: FoodProperties,
    ) {
        super.onGenerationUpdated(level, pos, state, input, recipe)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 1f, 0.5f)
    }
}
