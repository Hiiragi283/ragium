package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.util.HTIngredientHelper
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.impl.recipe.manager.HTSimpleRecipeCache
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.min

class HTMultiSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, HTMultiSmelterBlockEntity.MultiSmeltingRecipe>(
        HTMachineVariant.MULTI_SMELTER,
        pos,
        state,
    ) {
    private lateinit var inputSlot: HTItemSlot.Mutable
    private lateinit var catalystSlot: HTItemSlot
    private lateinit var outputSlot: HTItemSlot

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // input
        inputSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        // catalyst
        catalystSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2))
        // output
        outputSlot = HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
        return HTSimpleItemSlotHolder(this, listOf(inputSlot), listOf(outputSlot), catalystSlot)
    }

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        smeltingCache.serialize(output)
        blastingCache.serialize(output)
        smokingCache.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        smeltingCache.deserialize(input)
        blastingCache.deserialize(input)
        smokingCache.deserialize(input)
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.SMELTER.openMenu(player, title, this, ::writeExtraContainerData)

    private val smeltingCache: HTSimpleRecipeCache<SingleRecipeInput, SmeltingRecipe> =
        HTSimpleRecipeCache(RecipeType.SMELTING)
    private val blastingCache: HTSimpleRecipeCache<SingleRecipeInput, BlastingRecipe> =
        HTSimpleRecipeCache(RecipeType.BLASTING)
    private val smokingCache: HTSimpleRecipeCache<SingleRecipeInput, SmokingRecipe> =
        HTSimpleRecipeCache(RecipeType.SMOKING)

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inputSlot.getStack())

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): MultiSmeltingRecipe? {
        val cache: HTSimpleRecipeCache<SingleRecipeInput, out AbstractCookingRecipe> = when (catalystSlot.getStack().item) {
            Items.BLAST_FURNACE -> blastingCache
            Items.SMOKER -> smokingCache
            else -> smeltingCache
        }
        val baseRecipe: AbstractCookingRecipe = cache.getFirstRecipe(input, level) ?: return null
        val result: ItemStack = baseRecipe.assemble(input, level.registryAccess())
        if (result.isEmpty) return null
        val resultMaxSize: Int = result.maxStackSize

        var inputCount: Int = min(inputSlot.getAmountAsInt(), getMaxParallel())
        val maxParallel: Int = min(inputCount, getMaxParallel())
        var outputCount: Int = result.count * maxParallel
        if (outputCount > resultMaxSize) {
            outputCount = resultMaxSize - (resultMaxSize % maxParallel)
            inputCount = outputCount / maxParallel
        }
        if (inputCount <= 0 || outputCount <= 0) return null
        return MultiSmeltingRecipe(baseRecipe, outputCount)
    }

    private fun getMaxParallel(): Int = when (upgradeHandler.getTier()) {
        HTComponentTier.BASIC -> 2
        HTComponentTier.ADVANCED -> 4
        HTComponentTier.ELITE -> 8
        HTComponentTier.ULTIMATE -> 16
        HTComponentTier.ETERNAL -> inputSlot.getStack().maxStackSize
        null -> 1
    }

    override fun getRecipeTime(recipe: MultiSmeltingRecipe): Int = recipe.recipe.cookingTime

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: MultiSmeltingRecipe): Boolean =
        outputSlot.insert(recipe.assemble(input, level.registryAccess()), true, HTStorageAccess.INTERNAl).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: MultiSmeltingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.assemble(input, level.registryAccess()), false, HTStorageAccess.INTERNAl)
        // インプットを減らす
        HTIngredientHelper.shrinkStack(inputSlot, recipe::getRequiredCount, false)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1f)
    }

    data class MultiSmeltingRecipe(val recipe: AbstractCookingRecipe, val count: Int) : HTItemIngredient.CountGetter {
        fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
            recipe.assemble(input, registries).copyWithCount(count)

        override fun getRequiredCount(stack: ItemStack): Int = when {
            recipe.ingredients[0].test(stack) -> count
            else -> 0
        }
    }
}
