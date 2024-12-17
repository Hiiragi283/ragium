package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import hiiragi283.ragium.api.util.TriConsumer
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Rarity
import java.util.function.BiConsumer
import java.util.function.Predicate

/**
 * A plugin interface for Ragium
 */
@JvmDefaultWithCompatibility
interface RagiumPlugin {
    companion object {
        const val SERVER_KEY = "ragium.plugin.server"
        const val CLIENT_KEY = "ragium.plugin.client"
    }

    val priority: Int

    fun shouldLoad(): Boolean = true

    fun registerMachineType(consumer: BiConsumer<HTMachineKey, HTMachineType>) {}

    fun registerMaterial(helper: MaterialHelper) {}

    fun setupMachineProperties(helper: PropertyHelper<HTMachineKey>) {}

    fun setupMaterialProperties(helper: PropertyHelper<HTMaterialKey>) {}

    fun bindMaterialToItem(consumer: TriConsumer<HTTagPrefix, HTMaterialKey, ItemConvertible>) {}

    fun afterRagiumInit(instance: RagiumAPI) {}

    fun registerRuntimeRecipe(exporter: RecipeExporter) {}

    /**
     * Register runtime recipes based on [HTMaterialRegistry]
     */
    fun registerRuntimeMaterialRecipes(
        exporter: RecipeExporter,
        key: HTMaterialKey,
        entry: HTMaterialRegistry.Entry,
        helper: RecipeHelper,
    ) {}

    //    MaterialHelper    //

    class MaterialHelper(
        private val consumer: (HTMaterialKey, HTMaterialType, Rarity) -> Unit,
        private val altConsumer: (HTMaterialKey, String) -> Unit,
    ) {
        fun register(key: HTMaterialKey, type: HTMaterialType, rarity: Rarity = Rarity.COMMON) {
            consumer(key, type, rarity)
        }

        fun addAltName(parent: HTMaterialKey, child: String) {
            altConsumer(parent, child)
        }
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
        /**
         * Check give [tagKey] is loaded
         */
        fun isPopulated(tagKey: TagKey<Item>): Boolean = ResourceConditions.tagsPopulated(tagKey).test(null)

        fun useItemIfPresent(entry: HTMaterialRegistry.Entry, prefix: HTTagPrefix, action: (Item) -> Unit) {
            entry.getFirstItem(prefix)?.let(action)
        }

        fun useItemFromMainPrefix(entry: HTMaterialRegistry.Entry, action: (Item) -> Unit) {
            entry.type.getMainPrefix()?.let { prefix: HTTagPrefix ->
                useItemIfPresent(entry, prefix, action)
            }
        }

        fun useItemFromRawPrefix(entry: HTMaterialRegistry.Entry, action: (Item) -> Unit) {
            entry.type.getRawPrefix()?.let { prefix: HTTagPrefix ->
                useItemIfPresent(entry, prefix, action)
            }
        }
    }
}
