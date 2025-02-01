package hiiragi283.ragium.api.machine.property

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeType
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level
import java.util.function.BiFunction
import java.util.function.Consumer

private typealias MaterialFactory = BiFunction<HTTypedMaterial, HTMaterialRegistry, RecipeHolder<HTMachineRecipe>?>

/**
 * [HTMachineRecipe]を取得するインターフェース
 */
fun interface HTMachineRecipeProxy {
    fun getRecipes(level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>>)

    companion object {
        /**
         * [recipeType]に基づいてすべてのレシピを返します。
         */
        @JvmStatic
        fun default(recipeType: HTMachineRecipeType): HTMachineRecipeProxy =
            HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>> ->
                level.recipeManager
                    .getAllRecipesFor(recipeType)
                    .stream()
                    .forEach(consumer)
            }

        /**
         * 素材データに基づいた[HTMachineRecipeProxy]を返します。
         */
        @JvmStatic
        fun material(recipeType: HTMachineRecipeType?, vararg builders: MaterialFactory): HTMachineRecipeProxy =
            HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>> ->
                recipeType?.let(::default)?.getRecipes(level, consumer)
                val registry: HTMaterialRegistry = RagiumAPI.materialRegistry
                registry
                    .typedMaterials
                    .flatMap { material: HTTypedMaterial ->
                        builders.mapNotNull { builder: MaterialFactory -> builder.apply(material, registry) }
                    }.forEach(consumer)
            }
    }
}
