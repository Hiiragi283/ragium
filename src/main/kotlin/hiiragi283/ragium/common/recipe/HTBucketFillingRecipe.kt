package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil

object HTBucketFillingRecipe : HTMachineRecipe() {
    override fun matches(input: HTMachineInput): Boolean {
        val stack: FluidStack = input.getFluidStack(HTStorageIO.INPUT, 0)
        if (stack.amount < 1000) return false
        val bucket: ItemStack = FluidUtil.getFilledBucket(stack)
        if (bucket.isEmpty) return false
        val emptyBucket: ItemStack = bucket.craftingRemainingItem
        return ItemStack.isSameItemSameComponents(emptyBucket, input.getItemStack(HTStorageIO.INPUT, 0))
    }

    override fun canProcess(input: HTMachineInput): Boolean {
        val stack: FluidStack = input.getFluidStack(HTStorageIO.INPUT, 0)
        val bucket: ItemStack = FluidUtil.getFilledBucket(stack)
        val outputSlot: HTItemSlot = input.getSlotOrNull(HTStorageIO.OUTPUT, 0) ?: return false
        return outputSlot.canInsert(bucket)
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BUCKET_FILLING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.INFUSING.get()
}
