package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.registry.HTSimpleFluidHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.registry.toStack
import hiiragi283.ragium.api.recipe.HTTankInteractingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.*
import kotlin.jvm.optionals.getOrNull

class HTSimpleTankInteractingRecipe(
    val emptyContainer: HTSimpleItemHolderLike,
    val filledContainer: HTSimpleItemHolderLike,
    val fluid: HTSimpleFluidHolderLike,
    override val amount: Int,
    val fluidTag: Optional<TagKey<Fluid>>,
) : HTTankInteractingRecipe {
    private val ingredient: HTFluidIngredient = listOfNotNull(
        FluidIngredient.of(fluid.get()),
        fluidTag.map(FluidIngredient::tag).getOrNull(),
    ).let { CompoundFluidIngredient.of(it) }
        .let { HTFluidIngredient(it, amount) }

    override fun canEmptyContainer(container: ItemStack): Boolean = filledContainer.isOf(container)

    override fun emptyContainer(container: ItemStack): Pair<ItemStack, FluidStack> = emptyContainer.toStack() to fluid.toStack(amount)

    override fun canFillContainer(container: ItemStack, fluidStack: FluidStack): Boolean =
        emptyContainer.isOf(container) && ingredient.test(fluidStack)

    override fun fillContainer(container: ItemStack, fluidStack: FluidStack): ItemStack = filledContainer.toStack()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.TANK_INTERACTING
}
