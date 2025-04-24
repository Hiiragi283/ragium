package hiiragi283.ragium.common.recipe.custom

import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.material.Fluids

object HTBucketExtractingRecipe : HTMachineRecipe() {
    override fun matches(input: HTMachineInput): Boolean = input.getItemStack(HTStorageIO.INPUT, 0).item is BucketItem

    override fun canProcess(input: HTMachineInput): Boolean {
        val inputSlot: HTItemSlot = input.getSlotOrNull(HTStorageIO.INPUT, 0) ?: return false
        val bucketStack: ItemStack = inputSlot.stack
        val bucketItem: BucketItem = bucketStack.item as BucketItem
        // Item Output
        if (bucketStack.hasCraftingRemainingItem()) {
            val outputSlot: HTItemSlot = input.getSlotOrNull(HTStorageIO.OUTPUT, 0) ?: return false
            if (!outputSlot.canInsert(bucketStack.craftingRemainingItem)) return false
        }
        // Fluid Output
        if (bucketItem.content == Fluids.EMPTY) return false
        val outputTank: HTFluidTank = input.getTankOrNull(HTStorageIO.OUTPUT, 0) ?: return false
        if (!outputTank.canInsert(bucketItem.content, 1000)) return false
        // Item input
        return inputSlot.canExtract(1)
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BUCKET_EXTRACTING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTING.get()
}
