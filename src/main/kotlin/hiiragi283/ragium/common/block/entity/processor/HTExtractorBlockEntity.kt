package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.math.toFraction
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.upgrade.RagiumUpgradeKeys
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.common.block.entity.processor.base.HTItemWithCatalystBlockEntity
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.util.HTExperienceHelper
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.ComposterBlock
import net.minecraft.world.level.block.state.BlockState
import java.util.*

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemWithCatalystBlockEntity(
        RagiumBlocks.EXTRACTOR,
        pos,
        state,
    ) {
    companion object {
        @JvmStatic
        fun getCrudeBio(chance: Float): ImmutableFluidStack? {
            if (chance <= 0f) return null
            return RagiumFluidContents.CRUDE_BIO.toImmutableStack((1000 * chance.toFraction()).toInt())
        }

        @JvmStatic
        fun createComposting(item: ItemStack): HTExtractingRecipe? {
            val chance: Float = ComposterBlock.getValue(item)
            if (chance <= 0f) return null
            val crudeBio: ImmutableFluidStack = getCrudeBio(chance) ?: return null
            return HTExtractingRecipe(
                HTItemIngredient(Ingredient.of(item), 1),
                Optional.empty(),
                Ior.Right(HTResultHelper.fluid(crudeBio)),
            )
        }
    }

    private val recipeCache: HTRecipeCache<HTRecipeInput, HTItemWithCatalystRecipe> =
        HTFinderRecipeCache(RagiumRecipeTypes.EXTRACTING)

    override fun getMatchedRecipe(input: HTRecipeInput, level: ServerLevel): HTItemWithCatalystRecipe? = when {
        hasUpgrade(RagiumUpgradeKeys.COMPOSTING) ->
            input
                .item(0)
                ?.unwrap()
                ?.let(::createComposting)
        hasUpgrade(RagiumUpgradeKeys.EXP_DRAIN) -> expExtracting(input)
        else -> recipeCache.getFirstRecipe(input, level)
    }

    private fun expExtracting(input: HTRecipeInput): HTItemWithCatalystRecipe? {
        val stack: ItemStack = input.item(0)?.unwrap() ?: return null
        val enchantments: ItemEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack)
        if (enchantments.isEmpty) return null
        val expFluid: ImmutableFluidStack = enchantments
            .let(HTExperienceHelper::getTotalMinCost)
            .let(HTExperienceHelper::fluidAmountFromExp)
            .let(RagiumFluidContents.EXPERIENCE::toImmutableStack)
            ?: return null
        stack.remove(EnchantmentHelper.getComponentType(stack))
        return HTExtractingRecipe(
            HTItemIngredient(Ingredient.of(stack), 1),
            Optional.empty(),
            Ior.Both(resultHelper.item(stack), resultHelper.fluid(expFluid)),
        )
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTItemWithCatalystRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS, 1f, 0.5f)
    }
}
