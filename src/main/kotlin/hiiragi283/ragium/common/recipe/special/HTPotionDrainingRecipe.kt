package hiiragi283.ragium.common.recipe.special

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.util.Ior
import hiiragi283.core.util.HCPotionFluidHelper
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

data object HTPotionDrainingRecipe : HTCustomCanningRecipe() {
    @JvmStatic
    fun isPotion(stack: ItemStack): Boolean = HTBottleType.entries.any { stack.`is`(it.asItem()) }

    override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack = ItemStack(Items.GLASS_BOTTLE)

    override fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>> = Ior.Left(Predicate(::isPotion))

    override fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<Int, Int> = Ior.Left(1)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.POTION_DRAINING

    override fun assembleFluid(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): FluidStack = HTPotionHelper
        .getContents(input.item)
        ?.let { HCPotionFluidHelper.createFluid(it, HTConst.DEFAULT_FLUID_AMOUNT / 4) }
        ?: FluidStack.EMPTY
}
