package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.math.toFraction
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.fluid.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.common.block.entity.processor.base.HTComplexBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.recipe.HTBasicExtractingRecipe
import hiiragi283.ragium.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.upgrade.RagiumUpgradeKeys
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

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) :
    HTComplexBlockEntity<HTExtractingRecipe>(
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
        fun createComposting(item: ItemStack): HTBasicExtractingRecipe? {
            val chance: Float = ComposterBlock.getValue(item)
            if (chance <= 0f) return null
            val crudeBio: ImmutableFluidStack = getCrudeBio(chance) ?: return null
            return HTBasicExtractingRecipe(
                HTItemIngredient(Ingredient.of(item), 1),
                Ior.Right(HTResultHelper.fluid(crudeBio)),
            )
        }
    }

    lateinit var inputSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1)),
        )
        // output
        outputSlot = upperOutput(builder, listener)
    }

    override fun buildRecipeInput(builder: HTRecipeInput.Builder) {
        builder.items += inputSlot.getStack()
    }

    private val recipeCache: HTFinderRecipeCache<HTRecipeInput, HTExtractingRecipe> =
        HTFinderRecipeCache(RagiumRecipeTypes.EXTRACTING)

    override fun getMatchedRecipe(input: HTRecipeInput, level: ServerLevel): HTExtractingRecipe? = when {
        hasUpgrade(RagiumUpgradeKeys.COMPOST_BIO) ->
            input
                .item(0)
                ?.unwrap()
                ?.let(::createComposting)
        hasUpgrade(RagiumUpgradeKeys.EXTRACT_EXPERIENCE) -> expExtracting(input)
        else -> recipeCache.getFirstRecipe(input, level)
    }

    private fun expExtracting(input: HTRecipeInput): HTExtractingRecipe? {
        val stack: ItemStack = input.item(0)?.unwrap() ?: return null
        val enchantments: ItemEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack)
        if (enchantments.isEmpty) return null
        val expFluid: ImmutableFluidStack = enchantments
            .let(HTExperienceHelper::getTotalMinCost)
            .let(HTExperienceHelper::fluidAmountFromExp)
            .let(RagiumFluidContents.EXPERIENCE::toImmutableStack)
            ?: return null
        stack.remove(EnchantmentHelper.getComponentType(stack))
        return HTBasicExtractingRecipe(
            HTItemIngredient(Ingredient.of(stack), 1),
            Ior.Both(resultHelper.item(stack), resultHelper.fluid(expFluid)),
        )
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTExtractingRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // 実際にインプットを減らす
        inputSlot.extract(recipe.getRequiredCount(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS, 1f, 0.5f)
    }
}
