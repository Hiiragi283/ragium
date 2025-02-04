package hiiragi283.ragium.api.machine.property

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.api.recipe.HTMachineRecipeBase
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.function.BiFunction
import java.util.function.Consumer

typealias MaterialFactoryNew = BiFunction<HTTypedMaterial, HTMaterialRegistry, RecipeHolder<HTMachineRecipeBase>?>

fun interface HTMachineRecipeProxyNew {
    fun getRecipes(level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipeBase>>)

    companion object {
        /**
         * [recipeType]に基づいてすべてのレシピを返します。
         */
        @JvmStatic
        fun default(recipeType: RecipeType<HTMachineRecipeBase>): HTMachineRecipeProxyNew =
            HTMachineRecipeProxyNew { level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipeBase>> ->
                level.recipeManager
                    .getAllRecipesFor(recipeType)
                    .stream()
                    .forEach(consumer)
            }

        /**
         * 素材データに基づいた[HTMachineRecipeProxyNew]を返します。
         */
        @JvmStatic
        fun material(recipeType: RecipeType<HTMachineRecipeBase>?, vararg builders: MaterialFactoryNew): HTMachineRecipeProxyNew =
            HTMachineRecipeProxyNew { level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipeBase>> ->
                recipeType?.let(::default)?.getRecipes(level, consumer)
                val registry: HTMaterialRegistry = RagiumAPI.materialRegistry
                registry
                    .typedMaterials
                    .flatMap { material: HTTypedMaterial ->
                        builders.mapNotNull { builder: MaterialFactoryNew -> builder.apply(material, registry) }
                    }.forEach(consumer)
            }
    }
}
