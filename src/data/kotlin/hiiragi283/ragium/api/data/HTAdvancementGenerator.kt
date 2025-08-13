package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.data.advancement.HTAdvancementBuilder
import hiiragi283.ragium.api.data.advancement.HTDisplayInfoBuilder
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.function.Consumer

abstract class HTAdvancementGenerator : AdvancementProvider.AdvancementGenerator {
    protected lateinit var root: AdvancementHolder

    protected lateinit var output: Consumer<AdvancementHolder>

    final override fun generate(
        registries: HolderLookup.Provider,
        saver: Consumer<AdvancementHolder>,
        existingFileHelper: ExistingFileHelper,
    ) {
        output = saver
        root = createRoot()
        generate(registries)
    }

    protected abstract fun createRoot(): AdvancementHolder

    protected abstract fun generate(registries: HolderLookup.Provider)

    //    Extension    //

    protected fun root(key: ResourceKey<Advancement>, builderAction: HTAdvancementBuilder.() -> Unit): AdvancementHolder =
        HTAdvancementBuilder.root().apply(builderAction).save(output, key)

    protected fun child(
        key: ResourceKey<Advancement>,
        parent: AdvancementHolder,
        builderAction: HTAdvancementBuilder.() -> Unit,
    ): AdvancementHolder = HTAdvancementBuilder.child(parent).apply(builderAction).save(output, key)

    protected fun createSimple(
        key: ResourceKey<Advancement>,
        parent: AdvancementHolder,
        item: ItemLike,
        tagKey: TagKey<Item>? = null,
        builderAction: HTDisplayInfoBuilder.() -> Unit = {},
    ): AdvancementHolder {
        val id: ResourceLocation = item.asItemHolder().idOrThrow
        return child(key, parent) {
            display {
                setIcon(item)
                setTitleFromKey(key)
                setDescFromKey(key)
                builderAction()
            }
            if (tagKey != null) {
                hasItemsIn("has_${tagKey.location.toDebugFileName()}", tagKey)
            } else {
                hasAllItem("has_${id.path}", item)
            }
        }
    }

    protected fun HTAdvancementBuilder.hasItem(key: String, predicate: ItemPredicate.Builder): HTAdvancementBuilder =
        addCriterion(key, InventoryChangeTrigger.TriggerInstance.hasItems(predicate))

    protected fun HTAdvancementBuilder.hasAnyItem(key: String, items: Collection<ItemLike>): HTAdvancementBuilder =
        hasAnyItem(key, *items.toTypedArray())

    protected fun HTAdvancementBuilder.hasAnyItem(key: String, vararg items: ItemLike): HTAdvancementBuilder =
        hasItem(key, ItemPredicate.Builder.item().of(*items))

    protected fun HTAdvancementBuilder.hasAllItem(key: String, vararg items: ItemLike): HTAdvancementBuilder =
        addCriterion(key, InventoryChangeTrigger.TriggerInstance.hasItems(*items))

    protected fun HTAdvancementBuilder.hasItemsIn(key: String, tagKey: TagKey<Item>): HTAdvancementBuilder =
        hasItem(key, ItemPredicate.Builder.item().of(tagKey))
}
