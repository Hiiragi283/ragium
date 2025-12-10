package hiiragi283.ragium.common.crafting

import hiiragi283.ragium.api.math.plus
import hiiragi283.ragium.api.recipe.HTCustomRecipe
import hiiragi283.ragium.api.recipe.input.ImmutableRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.common.HTChargeType
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.Tags

class HTUpgradeChargeRecipe(category: CraftingBookCategory) : HTCustomRecipe(category) {
    override fun matches(input: ImmutableRecipeInput, level: Level): Boolean {
        var blastCharge = 0
        var gunpowder = 0
        for (stack: ImmutableItemStack? in input) {
            if (stack == null) continue
            if (HTChargeType.entries.any { chargeType: HTChargeType -> chargeType.isOf(stack) }) {
                blastCharge++
            } else if (stack.isOf(Tags.Items.GUNPOWDERS)) {
                gunpowder++
            }
        }
        return blastCharge == 1 && gunpowder > 1
    }

    override fun assemble(input: ImmutableRecipeInput, registries: HolderLookup.Provider): ItemStack {
        var blastCharge: ItemStack = ItemStack.EMPTY
        var gunpowder = 0
        for (stack: ImmutableItemStack? in input) {
            if (stack == null) continue
            if (stack.has(RagiumDataComponents.CHARGE_POWER)) {
                blastCharge = stack.unwrap()
            } else if (stack.isOf(Tags.Items.GUNPOWDERS)) {
                gunpowder++
            }
        }
        if (blastCharge.isEmpty || gunpowder < 0) {
            return ItemStack.EMPTY
        }
        blastCharge.update(RagiumDataComponents.CHARGE_POWER, HTChargeType.DEFAULT_POWER) { it + gunpowder }
        return blastCharge
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.UPGRADE_CHARGE
}
