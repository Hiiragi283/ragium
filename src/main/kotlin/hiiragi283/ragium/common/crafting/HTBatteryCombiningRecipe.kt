package hiiragi283.ragium.common.crafting

import hiiragi283.core.api.data.buildDataPatch
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.common.capability.HTEnergyCapabilities
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

class HTBatteryCombiningRecipe(category: CraftingBookCategory) : HTCustomRecipe(category) {
    override fun matches(input: ImmutableRecipeInput, level: Level): Boolean {
        for (stack: ItemStack in input) {
            if (stack.isEmpty) continue
            if (!stack.`is`(RagiumBlocks.BATTERY.asItem())) return false
        }
        return true
    }

    override fun assemble(input: ImmutableRecipeInput, registries: HolderLookup.Provider): ItemStack {
        var amount = 0
        var capacityScale = 0
        for (stack: ItemStack in input) {
            if (stack.isEmpty) continue
            // 中身の値を合算する
            HTEnergyCapabilities.getBattery(stack)?.getAmount()?.let { amount += it }
            // 容量の倍率を合算する
            capacityScale += stack.getOrDefault(RagiumDataComponents.CAPACITY_SCALE, 1)
        }
        return createItemStack(
            RagiumBlocks.BATTERY,
            patch = buildDataPatch {
                if (amount > 0) {
                    set(HCDataComponents.ENERGY, amount)
                }
                set(RagiumDataComponents.CAPACITY_SCALE, capacityScale)
            },
        )
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BATTERY_COMBINING
}
