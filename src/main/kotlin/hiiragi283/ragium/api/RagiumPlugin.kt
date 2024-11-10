package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import hiiragi283.ragium.api.util.TriConsumer
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.item.Item
import net.minecraft.util.Rarity
import java.util.function.BiConsumer
import java.util.function.Predicate

@JvmDefaultWithCompatibility
interface RagiumPlugin {
    companion object {
        const val SERVER_KEY = "ragium.plugin.server"
        const val CLIENT_KEY = "ragium.plugin.client"
    }

    val priority: Int

    fun shouldLoad(): Boolean = true

    fun registerMachineType(consumer: BiConsumer<HTMachineKey, HTMachineType>) {}

    fun registerMaterial(consumer: TriConsumer<HTMaterialKey, HTMaterialKey.Type, Rarity>) {}

    fun setupMachineProperties(helper: PropertyHelper<HTMachineKey>) {}

    fun setupMaterialProperties(helper: PropertyHelper<HTMaterialKey>) {}

    fun bindMaterialToItem(consumer: TriConsumer<HTTagPrefix, HTMaterialKey, Item>) {}

    fun afterRagiumInit(instance: RagiumAPI) {}

    fun registerRuntimeRecipes(
        exporter: RecipeExporter,
        key: HTMaterialKey,
        entry: HTMaterialRegistry.Entry,
        helper: RecipeHelper,
    ) {
    }

    //    PropertyHelper    //

    class PropertyHelper<T : Any>(private val key: T, private val properties: HTMutablePropertyHolder) {
        fun modify(vararg keys: T, builderAction: HTMutablePropertyHolder.() -> Unit) {
            modify(keys::contains, builderAction)
        }

        fun modify(key: T, builderAction: HTMutablePropertyHolder.() -> Unit) {
            modify({ it == key }, builderAction)
        }

        fun modify(filter: Predicate<T>, builderAction: HTMutablePropertyHolder.() -> Unit) {
            if (filter.test(this.key)) {
                properties.builderAction()
            }
        }
    }

    //    RecipeHelper    //

    class RecipeHelper {
        fun register(entry: HTMaterialRegistry.Entry, vararg requiredPrefixes: HTTagPrefix, action: (Map<HTTagPrefix, Item>) -> Unit) {
            if (requiredPrefixes.all(entry.type::isValidPrefix)) {
                action(
                    buildMap {
                        requiredPrefixes.forEach { prefix: HTTagPrefix ->
                            entry.getFirstItem(prefix)?.let { put(prefix, it) }
                        }
                    },
                )
            }
        }
    }
}
