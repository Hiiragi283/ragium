package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.critereon.ConsumeItemTrigger
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

    protected fun create(key: ResourceKey<Advancement>, builderAction: Advancement.Builder.() -> Unit): AdvancementHolder =
        Advancement.Builder
            .advancement()
            .apply(builderAction)
            .save(key)

    @Suppress("DEPRECATION", "removal")
    protected fun create(
        key: ResourceKey<Advancement>,
        parent: ResourceLocation,
        builderAction: Advancement.Builder.() -> Unit,
    ): AdvancementHolder = Advancement.Builder
        .advancement()
        .parent(parent)
        .apply(builderAction)
        .save(key)

    protected fun create(
        key: ResourceKey<Advancement>,
        parent: AdvancementHolder,
        builderAction: Advancement.Builder.() -> Unit,
    ): AdvancementHolder = Advancement.Builder
        .advancement()
        .parent(parent)
        .apply(builderAction)
        .save(key)

    protected fun createSimple(
        key: ResourceKey<Advancement>,
        parent: AdvancementHolder,
        item: ItemLike,
        tagKey: TagKey<Item>? = null,
        builderAction: HTDisplayInfoBuilder.() -> Unit = {},
    ): AdvancementHolder {
        val id: ResourceLocation = item.asItemHolder().idOrThrow
        return create(key, parent) {
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

    protected inline fun Advancement.Builder.display(builderAction: HTDisplayInfoBuilder.() -> Unit): Advancement.Builder =
        display(HTDisplayInfoBuilder.create(builderAction))

    protected fun Advancement.Builder.hasItem(key: String, predicate: ItemPredicate.Builder): Advancement.Builder =
        addCriterion(key, InventoryChangeTrigger.TriggerInstance.hasItems(predicate))

    protected fun Advancement.Builder.hasAnyItem(key: String, items: List<ItemLike>): Advancement.Builder =
        hasAnyItem(key, *items.toTypedArray())

    protected fun Advancement.Builder.hasAnyItem(key: String, vararg items: ItemLike): Advancement.Builder =
        hasItem(key, ItemPredicate.Builder.item().of(*items))

    protected fun Advancement.Builder.hasAllItem(key: String, vararg items: ItemLike): Advancement.Builder =
        addCriterion(key, InventoryChangeTrigger.TriggerInstance.hasItems(*items))

    protected fun Advancement.Builder.hasItemsIn(key: String, tagKey: TagKey<Item>): Advancement.Builder =
        hasItem(key, ItemPredicate.Builder.item().of(tagKey))

    protected fun Advancement.Builder.useItem(key: String, item: ItemLike): Advancement.Builder =
        addCriterion(key, ConsumeItemTrigger.TriggerInstance.usedItem(item))

    protected fun Advancement.Builder.save(key: ResourceKey<Advancement>): AdvancementHolder = save(output, key.location().toString())
}
