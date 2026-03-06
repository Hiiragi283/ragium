package hiiragi283.ragium.common.recipe.special

import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.util.Ior
import hiiragi283.core.api.util.getOrEmpty
import hiiragi283.ragium.api.recipe.FluidAmount
import hiiragi283.ragium.api.recipe.ItemAmount
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import java.util.function.Predicate

data object HTBucketDrainingRecipe : HTCustomCanningRecipe() {
    @JvmStatic
    fun isFilledBucket(stack: ItemStack): Boolean {
        val bool1: Boolean = stack.item is BucketItem || stack.`is`(Tags.Items.BUCKETS)
        val bool2: Boolean = FluidUtil.getFluidContained(stack).isPresent
        return bool1 && bool2
    }

    override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack = ItemStack(Items.BUCKET)

    override fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>> = Ior.Left(Predicate(::isFilledBucket))

    override fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<ItemAmount, FluidAmount> = Ior.Left(1)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BUCKET_DRAINING

    override fun assembleFluid(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): FluidStack =
        FluidUtil.getFluidContained(input.item).getOrEmpty()
}
