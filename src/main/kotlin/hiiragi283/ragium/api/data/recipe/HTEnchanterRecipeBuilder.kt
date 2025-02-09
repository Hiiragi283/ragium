package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.keyOrThrow
import hiiragi283.ragium.api.recipe.HTEnchanterRecipe
import net.minecraft.advancements.Criterion
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.Optional

class HTEnchanterRecipeBuilder(val enchantment: Holder<Enchantment>) :
    HTMachineRecipeBuilderBase<HTEnchanterRecipeBuilder, HTEnchanterRecipe>() {
    constructor(lookup: HolderLookup<Enchantment>, key: ResourceKey<Enchantment>) : this(lookup.getOrThrow(key))

    private var group: String? = null
    private lateinit var firstInput: SizedIngredient
    private var secondInput: SizedIngredient? = null

    override fun itemInput(ingredient: Ingredient, count: Int): HTEnchanterRecipeBuilder = apply {
        if (!::firstInput.isInitialized) {
            this.firstInput = SizedIngredient(ingredient, count)
            return@apply
        }
        check(secondInput == null) { "Input is already initialized" }
        this.secondInput = SizedIngredient(ingredient, count)
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTEnchanterRecipeBuilder = throw UnsupportedOperationException()

    override fun itemOutput(stack: ItemStack): HTEnchanterRecipeBuilder = throw UnsupportedOperationException()

    override fun fluidOutput(stack: FluidStack): HTEnchanterRecipeBuilder = throw UnsupportedOperationException()

    override fun getPrimalId(): ResourceLocation = enchantment.keyOrThrow.location()

    override val prefix: String = "enchanter"

    override fun createRecipe(): HTEnchanterRecipe = HTEnchanterRecipe(
        group ?: "",
        firstInput,
        Optional.ofNullable(secondInput),
        enchantment,
    )

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = this

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }

    override fun getResult(): Item = throw UnsupportedOperationException()
}
