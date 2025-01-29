package hiiragi283.ragium.api.machine.property

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
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
         * [RecipeType]に基づいてすべてのレシピを返します。
         */
        @JvmField
        val DEFAULT = HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>> ->
            level.recipeManager
                .getAllRecipesFor(RagiumRecipes.MACHINE_TYPE.get())
                .stream()
                .forEach(consumer)
        }

        /**
         * 指定した既存のレシピを[HTMachineRecipe]に変換します。
         * @param I [RecipeInput]を継承したクラス
         * @param T [Recipe]を継承したクラス
         * @param converter [T]を[HTMachineRecipe]に変換するブロック
         */
        @JvmStatic
        fun <I : RecipeInput, T : Recipe<I>> convert(
            allowFromManager: Boolean,
            recipeType: RecipeType<T>,
            converter: (RecipeHolder<T>, HolderLookup.Provider) -> RecipeHolder<HTMachineRecipe>,
        ): HTMachineRecipeProxy = HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>> ->
            if (allowFromManager) {
                DEFAULT.getRecipes(level, consumer)
            }
            level.recipeManager
                .getAllRecipesFor(recipeType)
                .map { converter(it, level.registryAccess()) }
                .forEach(consumer)
        }

        /**
         * 素材データに基づいた[HTMachineRecipeProxy]を返します。
         */
        @JvmStatic
        fun material(allowFromManager: Boolean, vararg builders: MaterialFactory): HTMachineRecipeProxy =
            HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>> ->
                if (allowFromManager) {
                    DEFAULT.getRecipes(level, consumer)
                }
                val registry: HTMaterialRegistry = RagiumAPI.materialRegistry
                registry
                    .typedMaterials
                    .flatMap { material: HTTypedMaterial ->
                        builders.mapNotNull { builder: MaterialFactory -> builder.apply(material, registry) }
                    }.forEach(consumer)
            }
    }
}
