package hiiragi283.ragium.api.data.advancement

import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.material.HTItemMaterialVariant
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.critereon.ConsumeItemTrigger
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.data.ExistingFileHelper

abstract class HTAdvancementGenerator {
    protected lateinit var root: AdvancementHolder

    protected lateinit var output: HTAdvancementOutput

    fun generate(provider: HolderLookup.Provider, output: HTAdvancementOutput, helper: ExistingFileHelper) {
        this.output = output
        root = createRoot()
        generate(provider)
    }

    protected abstract fun createRoot(): AdvancementHolder

    protected abstract fun generate(registries: HolderLookup.Provider)

    //    Extension    //

    protected fun root(key: HTAdvancementKey, builderAction: HTAdvancementBuilder.() -> Unit): AdvancementHolder =
        HTAdvancementBuilder.root().apply(builderAction).save(output, key)

    protected fun child(
        key: HTAdvancementKey,
        parent: AdvancementHolder,
        builderAction: HTAdvancementBuilder.() -> Unit,
    ): AdvancementHolder = HTAdvancementBuilder.child(parent).apply(builderAction).save(output, key)

    protected fun createSimple(
        key: HTAdvancementKey,
        parent: AdvancementHolder,
        variant: HTItemMaterialVariant,
        material: HTMaterialType,
        builderAction: HTDisplayInfoBuilder.() -> Unit = {},
    ): AdvancementHolder = createSimple(
        key,
        parent,
        RagiumItems.getMaterial(variant, material),
        variant.itemTagKey(material),
        builderAction,
    )

    protected fun createSimple(
        key: HTAdvancementKey,
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

    protected fun <ITEM> HTAdvancementBuilder.hasAnyItem(item: ITEM): HTAdvancementBuilder where ITEM : HTHolderLike, ITEM : ItemLike =
        hasAnyItem("has_${item.getPath()}", item)

    protected fun HTAdvancementBuilder.hasAnyItem(key: String, vararg items: ItemLike): HTAdvancementBuilder =
        hasItem(key, ItemPredicate.Builder.item().of(*items))

    protected fun HTAdvancementBuilder.hasAllItem(key: String, vararg items: ItemLike): HTAdvancementBuilder =
        addCriterion(key, InventoryChangeTrigger.TriggerInstance.hasItems(*items))

    protected fun HTAdvancementBuilder.hasItemsIn(key: String, tagKey: TagKey<Item>): HTAdvancementBuilder =
        hasItem(key, ItemPredicate.Builder.item().of(tagKey))

    protected fun <ITEM> HTAdvancementBuilder.useItem(item: ITEM): HTAdvancementBuilder where ITEM : HTHolderLike, ITEM : ItemLike =
        addCriterion("use_${item.getPath()}", ConsumeItemTrigger.TriggerInstance.usedItem(item))
}
