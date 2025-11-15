package hiiragi283.ragium.common.block.entity.consumer.base

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.recipe.HTVanillaCookingRecipe
import hiiragi283.ragium.common.recipe.VanillaRecipeTypes
import hiiragi283.ragium.common.recipe.manager.HTFinderRecipeCache
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTAbstractSmelterBlockEntity<RECIPE : Any>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity<RECIPE>(blockHolder, pos, state) {
    lateinit var catalystSlot: HTItemStackSlot
        private set
    lateinit var outputSlot: HTItemStackSlot
        private set

    final override fun initializeItemHandler(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
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

    private val smeltingCache: HTFinderRecipeCache<SingleRecipeInput, HTVanillaCookingRecipe> =
        HTFinderRecipeCache(VanillaRecipeTypes.SMELTING)
    private val blastingCache: HTFinderRecipeCache<SingleRecipeInput, HTVanillaCookingRecipe> =
        HTFinderRecipeCache(VanillaRecipeTypes.BLASTING)
    private val smokingCache: HTFinderRecipeCache<SingleRecipeInput, HTVanillaCookingRecipe> =
        HTFinderRecipeCache(VanillaRecipeTypes.SMOKING)

    protected fun getRecipeCache(): HTFinderRecipeCache<SingleRecipeInput, HTVanillaCookingRecipe> =
        when (catalystSlot.getStack()?.value()) {
            Items.BLAST_FURNACE -> blastingCache
            Items.SMOKER -> smokingCache
            else -> smeltingCache
        }
}
