package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import java.util.*

class HTGrowthChamberRecipe private constructor(
    group: String,
    input: HTItemIngredient,
    catalyst: Optional<Ingredient>,
    itemOutput: HTItemOutput,
) : HTSingleItemRecipe(
        group,
        input,
        catalyst,
        itemOutput,
    ) {
    companion object {
        @JvmField
        val SERIALIZER: Serializer<HTGrowthChamberRecipe> = Serializer(::HTGrowthChamberRecipe)
    }

    constructor(
        seed: ItemLike,
        soil: TagKey<Item>,
        crop: ItemLike,
        count: Int = 2,
    ) : this(
        "",
        HTItemIngredient.of(seed, 1),
        Optional.of(Ingredient.of(soil)),
        HTItemOutput.of(crop, count),
    )

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.GROWTH_CHAMBER
}
