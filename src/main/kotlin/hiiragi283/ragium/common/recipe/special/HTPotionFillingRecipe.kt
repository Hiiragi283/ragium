package hiiragi283.ragium.common.recipe.special

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

data object HTPotionFillingRecipe : HTCustomCanningRecipe() {
    override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack =
        HTPotionHelper.getContents(input.fluid)?.let(HTPotionHelper::createPotion) ?: ItemStack.EMPTY

    override fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>> = Ior.Both(
        Predicate { stack: ItemStack -> stack.`is`(Items.GLASS_BOTTLE) },
        Predicate { stack: FluidStack -> HTPotionHelper.getContents(stack) != null },
    )

    override fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<Int, Int> = Ior.Both(1, HTConst.DEFAULT_FLUID_AMOUNT / 4)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.POTION_FILLING

    override fun assembleFluid(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): FluidStack = FluidStack.EMPTY
}
