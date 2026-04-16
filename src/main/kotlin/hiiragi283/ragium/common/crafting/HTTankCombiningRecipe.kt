package hiiragi283.ragium.common.crafting

import hiiragi283.core.api.data.buildDataPatch
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.storage.fluid.toStackOrEmpty
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.crafting.HTCustomRecipe
import hiiragi283.core.common.crafting.ImmutableRecipeInput
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.SimpleFluidContent

class HTTankCombiningRecipe(category: CraftingBookCategory) : HTCustomRecipe(category) {
    override fun matches(input: ImmutableRecipeInput, level: Level): Boolean {
        for (stack: ItemStack in input) {
            if (stack.isEmpty) continue
            if (!RagiumBlocks.TANK.isOf(stack)) return false
        }
        return true
    }

    override fun assemble(input: ImmutableRecipeInput, registries: HolderLookup.Provider): ItemStack {
        var resource: HTFluidResourceType? = null
        var amount = 0
        var capacityScale = 0
        for (stack: ItemStack in input) {
            if (stack.isEmpty) continue
            // 中身の値を合算する
            HTFluidCapabilities.getFirstTank(stack)?.let { view: HTFluidView ->
                val resourceIn: HTFluidResourceType = view.getResource() ?: return@let
                if (resource == null) {
                    resource = resourceIn
                } else if (resource != resourceIn) {
                    return ItemStack.EMPTY
                }
                amount += view.getAmount()
            }
            // 容量の倍率を合算する
            capacityScale += stack.getOrDefault(RagiumDataComponents.CAPACITY_SCALE, 1)
        }
        return createItemStack(
            RagiumBlocks.TANK,
            patch = buildDataPatch {
                val stack: FluidStack = resource.toStackOrEmpty(amount)
                if (!stack.isEmpty) {
                    set(HCDataComponents.FLUID, SimpleFluidContent.copyOf(stack))
                }
                set(RagiumDataComponents.CAPACITY_SCALE, capacityScale)
            },
        )
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.TANK_COMBINING
}
