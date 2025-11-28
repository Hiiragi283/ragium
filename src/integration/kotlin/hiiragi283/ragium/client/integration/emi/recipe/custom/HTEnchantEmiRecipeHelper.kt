package hiiragi283.ragium.client.integration.emi.recipe.custom

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.Tags
import java.util.Random
import kotlin.random.asKotlinRandom

object HTEnchantEmiRecipeHelper {
    @JvmStatic
    fun getTool(random: Random, index: Int): EmiIngredient {
        val items: EmiIngredient = EmiIngredient.of(Tags.Items.ENCHANTABLES)
        return when (index) {
            0 -> {
                items.emiStacks
                    .map(EmiStack::getItemStack)
                    .filterNot(ItemStack::isEmpty)
                    .onEach { stack: ItemStack ->
                        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                    }.map(EmiStack::of)
            }
            else -> items.emiStacks
        }.random(random.asKotlinRandom())
    }
}
