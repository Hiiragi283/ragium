package hiiragi283.lib.data.recipe

import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import hiiragi283.lib.HTConstants
import hiiragi283.lib.material.HTMaterialAccess
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.resource.toId
import hiiragi283.ragium.api.tag.HTPart
import java.util.Optional
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.resources.RegistryOps
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.common.conditions.WithConditions

/**
 * Hiiragi Seriesで使用される，レシピ向けの[DataProvider]の抽象クラスです。
 * 参照 : [Minecraft - RecipeProvider][net.minecraft.data.recipes.RecipeProvider]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTRecipeProvider(packOutput: PackOutput, private val future: CompletableFuture<HolderLookup.Provider>, protected val modId: String) :
    HTRecipeProviderContext(),
    DataProvider {
    private val pathProvider: PackOutput.PathProvider = packOutput.createRegistryElementsPathProvider(Registries.RECIPE)

    override lateinit var exporter: HTRecipeExporter

    override lateinit var registries: HolderLookup.Provider

    final override fun run(output: CachedOutput): CompletableFuture<*> = future.thenCompose { registries: HolderLookup.Provider ->
        val recipes: MutableMap<RecipeKey, WithConditions<Recipe<*>>> = hashMapOf()
        this.registries = registries
        this.exporter = HTRecipeExporter { id: RecipeKey, recipe: Recipe<*>, conditions: List<ICondition> ->
            val fixedId: RecipeKey = id.let(::modifyId)
            check(recipes.put(fixedId, WithConditions(conditions, recipe)) == null) { "Duplicate recipe ${fixedId.identifier()}" }
        }

        buildRecipes()

        val dynamicOps: RegistryOps<JsonElement> = registries.createSerializationContext(JsonOps.INSTANCE)
        DataProvider.saveAll(
            output,
            { conditions: WithConditions<Recipe<*>> -> Recipe.CONDITIONAL_CODEC.encodeStart(dynamicOps, Optional.of(conditions)).orThrow },
            pathProvider::json,
            recipes,
        )
    }

    /**
     * レシピを生成します。
     */
    protected abstract fun buildRecipes()

    protected fun modifyId(key: RecipeKey): RecipeKey = RecipeKey(key.identifier().let(::modifyId))

    /**
     * 受け取った[id]を[exporter]内で変換します。
     */
    protected open fun modifyId(id: Identifier): Identifier = modId.toId(id.path)

    //    Extensions    //

    /**
     * 指定した[パス][path]から[ID][Identifier]を作成します。
     * @return [modId]を[名前空間][Identifier.getNamespace]とする[ID][Identifier]
     */
    protected fun id(path: String): Identifier = modId.toId(path)

    /**
     * 指定した[パス][path]から[ID][Identifier]を作成します。
     * @return [modId]を[名前空間][Identifier.getNamespace]とする[ID][Identifier]
     */
    protected fun id(vararg path: String): Identifier = modId.toId(*path)

    /**
     * @since 26.1.2
     */
    protected inline fun useItem(part: HTPart, material: HTMaterialKey, action: (HTMaterialContents.ItemEntry) -> Unit) {
        HTMaterialAccess.INSTANCE
            .getRegisteredContents()
            .items
            .getOrThrow(part, material)
            .let(action)
    }

    //    Integration    //

    abstract class Integration(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>, modId: String, integrationModId: String) : HTRecipeProvider(packOutput, future, modId) {
        val condition = ModLoadedCondition(integrationModId)
        private val builtInIds: Set<String> = HTConstants.getBuiltInIdSet(modId)

        final override fun modifyId(id: Identifier): Identifier {
            val namespace: String = id.namespace
            return if (namespace in builtInIds) {
                val path: List<String> = id.path.split("/", limit = 2)
                id(path[0], modId, path[1])
            } else {
                val path: List<String> = id.path.split("/", limit = 2)
                id(path[0], namespace, path[1])
            }
        }
    }
}
