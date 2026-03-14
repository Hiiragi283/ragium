package hiiragi283.ragium.common.recipe.special

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.util.Ior
import hiiragi283.ragium.api.recipe.FluidAmount
import hiiragi283.ragium.api.recipe.ItemAmount
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import java.util.function.Predicate

data object HTBucketFillingRecipe : HTCustomCanningRecipe() {
    override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack =
        FluidUtil.getFilledBucket(input.fluid)

    override fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>> = Ior.Both(
        Predicate { stack: ItemStack -> stack.`is`(Tags.Items.BUCKETS_EMPTY) },
        Predicate { stack: FluidStack -> stack.amount >= HTConst.DEFAULT_FLUID_AMOUNT },
    )

    override fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<ItemAmount, FluidAmount> =
        Ior.Both(1, HTConst.DEFAULT_FLUID_AMOUNT)

    override fun getSerializer(): RecipeSerializer<*> = TODO()

    override fun assembleFluid(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): FluidStack = FluidStack.EMPTY
}
