package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.integration.jei.addFluidStacks
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.util.HCPotionFluidHelper
import hiiragi283.ragium.api.integration.jei.HTTankInteractionCategoryExtension
import hiiragi283.ragium.common.data.tank.HTPotionTankInteraction
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidStack
import kotlin.streams.asSequence

object HTPotionTankInteractionCategoryExtension : HTTankInteractionCategoryExtension<HTPotionTankInteraction> {
    override fun <T : IIngredientAcceptor<T>> setEmptyContainer(recipe: HTPotionTankInteraction, accessor: T) {
        accessor.addItemLike(Items.GLASS_BOTTLE)
    }

    override fun <T : IIngredientAcceptor<T>> setFluid(recipe: HTPotionTankInteraction, accessor: T) {
        val stacks: List<FluidStack> = BuiltInRegistries.POTION
            .holders()
            .asSequence()
            .map { HTPotionContents.of(it, HTBottleType.DEFAULT) }
            .map { HCPotionFluidHelper.createFluid(it, recipe.amount) }
            .toList()
        accessor.addFluidStacks(stacks, false)
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTPotionTankInteraction,
        emptySlot: IRecipeSlotDrawable,
        filledSlot: IRecipeSlotDrawable,
        fluidSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {
        val fluidStack: FluidStack = fluidSlot.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK).orElse(FluidStack.EMPTY)
        if (fluidStack.isEmpty) return
        val bottle: ItemStack = HTPotionHelper.getContents(fluidStack)?.let(HTPotionHelper::createPotion) ?: return
        filledSlot.createDisplayOverrides().addItemStack(bottle)
    }
}
