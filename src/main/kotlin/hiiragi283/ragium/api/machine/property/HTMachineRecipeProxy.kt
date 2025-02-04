package hiiragi283.ragium.api.machine.property

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.api.recipe.HTMachineRecipeBase
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * [HTMachineRecipeBase]を取得するインターフェース
 */
fun interface HTMachineRecipeProxy {
    fun getRecipes(level: Level, consumer: Consumer<RecipeHolder<out HTMachineRecipeBase>>)

    companion object {
        /**
         * [recipeType]に基づいてすべてのレシピを返します。
         */
        @JvmStatic
        fun <T : HTMachineRecipeBase> default(recipeType: RecipeType<T>): HTMachineRecipeProxy =
            HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<out HTMachineRecipeBase>> ->
                level.recipeManager
                    .getAllRecipesFor(recipeType)
                    .stream()
                    .forEach(consumer)
            }

        @JvmStatic
        fun <I : RecipeInput, R : Recipe<I>> convert(
            recipeType: RecipeType<R>,
            transform: (RecipeHolder<R>, HolderLookup.Provider) -> RecipeHolder<out HTMachineRecipeBase>,
        ): HTMachineRecipeProxy = HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<out HTMachineRecipeBase>> ->
            val provider: HolderLookup.Provider = level.registryAccess()
            level.recipeManager
                .getAllRecipesFor(recipeType)
                .stream()
                .map { holder: RecipeHolder<R> -> transform(holder, provider) }
                .forEach(consumer)
        }

        /**
         * 素材データに基づいた[HTMachineRecipeProxy]を返します。
         */
        @JvmStatic
        fun <T : HTMachineRecipeBase> material(
            recipeType: Supplier<RecipeType<T>>,
            vararg builders: BiFunction<HTTypedMaterial, HTMaterialRegistry, RecipeHolder<T>?>,
        ): HTMachineRecipeProxy = HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<out HTMachineRecipeBase>> ->
            recipeType.get().let(::default).getRecipes(level, consumer)
            val registry: HTMaterialRegistry = RagiumAPI.materialRegistry
            registry
                .typedMaterials
                .flatMap { material: HTTypedMaterial ->
                    builders.mapNotNull { builder ->
                        builder.apply(
                            material,
                            registry,
                        )
                    }
                }.forEach(consumer)
        }
    }
}
