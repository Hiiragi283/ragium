package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.base.*
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import java.util.*

class HTGrinderRecipe(
    group: String,
    input: HTItemIngredient,
    catalyst: Optional<Ingredient>,
    itemOutput: HTItemOutput,
) : HTSingleItemRecipe(group, input, catalyst, itemOutput) {
    /*fun getSecondOutput(machine: HTMachineAccess): Optional<ItemStack> {
        val chanced: HTChancedItemStack = this.secondOutput.getOrNull() ?: return Optional.empty()
        val level: Level = machine.levelAccess ?: return Optional.empty()
        val fortune: Int = machine.getEnchantmentLevel(Enchantments.FORTUNE)
        val chance: Double = max(
            1.0,
            chanced.chance + when (fortune) {
                2 -> 0.66
                3 -> 1.0
                else -> 0.33
            },
        )
        return when {
            level.random.nextFloat() > (1 - chance) -> Optional.of(chanced.toStack())
            else -> Optional.empty()
        }
    }*/

    override val itemOutputs: List<HTItemOutput> = listOf(itemOutput)

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean = this.input.test(input, 0)

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.GRINDER
}
