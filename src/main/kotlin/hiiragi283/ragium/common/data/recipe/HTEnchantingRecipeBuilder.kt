package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.toLike
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import net.minecraft.core.Holder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments

class HTEnchantingRecipeBuilder : HTProcessingRecipeBuilder(RagiumConst.ENCHANTING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTEnchantingRecipeBuilder.() -> Unit) {
            HTEnchantingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    var book: HTItemIngredient = HTItemIngredient(Ingredient.of(Items.BOOK), 1)
    lateinit var ingredient: HTItemIngredient
    val result: ResultHolder = ResultHolder()

    inner class ResultHolder {
        lateinit var content: Either<Holder<Enchantment>, ItemEnchantments>
            private set

        operator fun plusAssign(holder: Holder<Enchantment>) {
            check(!::content.isInitialized) { "Enchantment result already initialized!" }
            this.content = Either.Left(holder)
        }

        operator fun plusAssign(enchantments: ItemEnchantments) {
            check(!::content.isInitialized) { "Enchantment result already initialized!" }
            this.content = Either.Right(enchantments)
        }
    }

    //    HTProcessingRecipeBuilder    //

    override fun getPrimalId(): ResourceLocation = result.content
        .getLeft()
        ?.toLike()
        ?.getId() ?: error("Could not create default recipe id from ItemEnchantments")

    override fun createRecipe(): HTEnchantingRecipe = HTEnchantingRecipe(
        book,
        ingredient,
        result.content,
        subParameters(),
    )
}
