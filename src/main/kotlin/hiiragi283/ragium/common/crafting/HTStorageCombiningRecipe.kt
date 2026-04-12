package hiiragi283.ragium.common.crafting

import hiiragi283.core.api.data.buildDataPatch
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.crafting.HTCustomRecipe
import hiiragi283.core.common.crafting.ImmutableRecipeInput
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class HTStorageCombiningRecipe(category: CraftingBookCategory) : HTCustomRecipe(category) {
    override fun matches(input: ImmutableRecipeInput, level: Level): Boolean {
        var primalStack: ItemStack = ItemStack.EMPTY
        var fluidResource: HTFluidResourceType? = null
        for (stack: ItemStack in input) {
            // 空のスタックは無視
            if (stack.isEmpty) continue
            // 最初のアイテムを基準に合体させる
            if (primalStack.isEmpty) {
                primalStack = stack.copyWithCount(1)
            } else if (!ItemStack.isSameItem(stack, primalStack)) {
                return false
            }
            // 最初の液体を基準に合体させる
            HTFluidCapabilities
                .getFirstTank(stack)
                ?.let { view: HTFluidView ->
                    val resourceIn: HTFluidResourceType = view.getResource() ?: return@let
                    when {
                        fluidResource == null -> fluidResource = resourceIn
                        fluidResource != resourceIn -> return false
                    }
                }
        }
        return !primalStack.isEmpty
    }

    override fun assemble(input: ImmutableRecipeInput, registries: HolderLookup.Provider): ItemStack {
        var primalStack: ItemStack = ItemStack.EMPTY
        var fluidResource: HTFluidResourceType? = null
        var fluidAmount = 0
        var capacityScale = 0
        for (stack: ItemStack in input) {
            // 空のスタックは無視
            if (stack.isEmpty) continue
            // 最初のアイテムを基準に合体させる
            if (primalStack.isEmpty) {
                primalStack = stack.copyWithCount(1)
            } else if (!ItemStack.isSameItem(stack, primalStack)) {
                return ItemStack.EMPTY
            }
            // 最初の液体を基準に合体させる
            HTFluidCapabilities
                .getFirstTank(stack)
                ?.let { view: HTFluidView ->
                    val resourceIn: HTFluidResourceType = view.getResource() ?: return@let
                    when (fluidResource) {
                        null -> {
                            fluidResource = resourceIn
                            fluidAmount = view.getAmount()
                        }
                        resourceIn -> {
                            fluidAmount += view.getAmount()
                        }
                        else -> {
                            return ItemStack.EMPTY
                        }
                    }
                }
            // 容量の倍率を合算する
            capacityScale += stack.getOrDefault(RagiumDataComponents.CAPACITY_SCALE, 1)
        }
        if (!primalStack.isEmpty) {
            primalStack.applyComponents(buildDataPatch { set(RagiumDataComponents.CAPACITY_SCALE, capacityScale) })
            return primalStack
        } else {
            return ItemStack.EMPTY
        }
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.STORAGE_COMBINING
}
