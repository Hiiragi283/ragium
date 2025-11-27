package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.recipe.HTVanillaCookingRecipe
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull

abstract class HTAbstractSmelterBlockEntity<RECIPE : Any>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity<RECIPE>(blockHolder, pos, state) {
    lateinit var catalystSlot: HTItemStackSlot
        private set
    lateinit var outputSlot: HTItemStackSlot
        private set

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // catalyst
        catalystSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)),
        )
        // output
        outputSlot = singleOutput(builder, listener)
    }

    private val smeltingCache: HTRecipeCache<SingleRecipeInput, HTVanillaCookingRecipe> =
        HTFinderRecipeCache { manager: RecipeManager, input: SingleRecipeInput, level: Level, lastRecipe: RecipeHolder<SmeltingRecipe>? ->
            manager.getRecipeFor(RecipeType.SMELTING, input, level, lastRecipe).getOrNull()
        }.wrap(::HTVanillaCookingRecipe)
    private val blastingCache: HTRecipeCache<SingleRecipeInput, HTVanillaCookingRecipe> =
        HTFinderRecipeCache { manager: RecipeManager, input: SingleRecipeInput, level: Level, lastRecipe: RecipeHolder<BlastingRecipe>? ->
            manager.getRecipeFor(RecipeType.BLASTING, input, level, lastRecipe).getOrNull()
        }.wrap(::HTVanillaCookingRecipe)
    private val smokingCache: HTRecipeCache<SingleRecipeInput, HTVanillaCookingRecipe> =
        HTFinderRecipeCache { manager: RecipeManager, input: SingleRecipeInput, level: Level, lastRecipe: RecipeHolder<SmokingRecipe>? ->
            manager.getRecipeFor(RecipeType.SMOKING, input, level, lastRecipe).getOrNull()
        }.wrap(::HTVanillaCookingRecipe)

    protected fun getRecipeCache(): HTRecipeCache<SingleRecipeInput, HTVanillaCookingRecipe> = when (catalystSlot.getStack()?.value()) {
        Items.BLAST_FURNACE -> blastingCache
        Items.SMOKER -> smokingCache
        else -> smeltingCache
    }

    final override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0
}
