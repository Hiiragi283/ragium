package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import net.fabricmc.fabric.impl.resource.conditions.ResourceConditionsImpl
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
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

    fun registerMaterial(consumer: BiConsumer<HTMaterialKey, HTMaterialKey.Type>) {}

    fun setupCommonMachineProperties(helper: PropertyHelper<HTMachineKey>) {}

    fun setupClientMachineProperties(helper: PropertyHelper<HTMachineKey>) {}

    fun setupCommonMaterialProperties(helper: PropertyHelper<HTMaterialKey>) {}

    fun setupClientMaterialProperties(helper: PropertyHelper<HTMaterialKey>) {}

    fun bindMaterialToItem(consumer: (HTTagPrefix, HTMaterialKey, Item) -> Unit) {}

    fun afterRagiumInit(instance: RagiumAPI) {}

    fun registerRuntimeRecipes(
        exporter: RecipeExporter,
        key: HTMaterialKey,
        entry: HTMaterialRegistry.Entry,
        helper: RecipeHelper,
    ) {
    }

    @Suppress("UnstableApiUsage")
    fun isPopulated(tagKey: TagKey<Item>): Boolean = ResourceConditionsImpl.tagsPopulated(RegistryKeys.ITEM.value, listOf(tagKey.id))

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
        @Suppress("UnstableApiUsage")
        fun isPopulated(tagKey: TagKey<Item>): Boolean = ResourceConditionsImpl.tagsPopulated(RegistryKeys.ITEM.value, listOf(tagKey.id))

        fun register(
            exporter: RecipeExporter,
            key: HTMaterialKey,
            entry: HTMaterialRegistry.Entry,
            vararg requiredPrefixes: HTTagPrefix,
            action: (List<TagKey<Item>>) -> Unit,
        ) {
            if (requiredPrefixes.all(entry.type::isValidPrefix)) {
                val tagKeys: List<TagKey<Item>> = requiredPrefixes.map { it.createTag(key) }
                if (tagKeys.all(::isPopulated)) {
                    action(tagKeys)
                }
            }
        }
    }
}
