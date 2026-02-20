package hiiragi283.ragium.common.recipe.special

import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import java.util.function.Predicate

data object HTBucketDrainingRecipe : HTItemOrFluidRecipe {
    @JvmStatic
    fun isFilledBucket(stack: ItemStack): Boolean {
        val bool1: Boolean = stack.item is BucketItem || stack.`is`(Tags.Items.BUCKETS)
        val bool2: Boolean = FluidUtil.getFluidContained(stack).isPresent
        return bool1 && bool2
    }

    override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack = ItemStack(Items.BUCKET)

    override fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>> = Ior.Left(Predicate(::isFilledBucket))

    override fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<Int, Int> = Ior.Left(1)

    override val time: Int = 20

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BUCKET_DRAINING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CANNING.get()

    override fun assembleFluid(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): FluidStack =
        FluidUtil.getFluidContained(input.item).orElse(FluidStack.EMPTY)
}
