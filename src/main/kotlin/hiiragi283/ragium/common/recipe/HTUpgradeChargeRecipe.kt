package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.input.ImmutableRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.common.variant.HTChargeVariant
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.Tags

class HTUpgradeChargeRecipe(category: CraftingBookCategory) : CustomRecipe(category) {
    override fun matches(input: CraftingInput, level: Level): Boolean {
        var blastCharge = 0
        var gunpowder = 0
        for (stack: ImmutableItemStack? in ImmutableRecipeInput(input)) {
            if (stack == null) continue
            if (HTChargeVariant.entries.any { variant: HTChargeVariant -> variant.isOf(stack) }) {
                blastCharge++
            } else if (stack.isOf(Tags.Items.GUNPOWDERS)) {
                gunpowder++
            }
        }
        return blastCharge == 1 && gunpowder > 1
    }

    override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack {
        var blastCharge: ItemStack = ItemStack.EMPTY
        var gunpowder = 0
        for (stack: ImmutableItemStack? in ImmutableRecipeInput(input)) {
            if (stack == null) continue
            if (stack.has(RagiumDataComponents.BLAST_POWER)) {
                blastCharge = stack.unwrap()
            } else if (stack.isOf(Tags.Items.GUNPOWDERS)) {
                gunpowder++
            }
        }
        if (blastCharge.isEmpty || gunpowder < 0) {
            return ItemStack.EMPTY
        }
        blastCharge.update(RagiumDataComponents.BLAST_POWER, 4f) { it + gunpowder.toFloat() }
        return blastCharge
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.UPGRADE_CHARGE
}
