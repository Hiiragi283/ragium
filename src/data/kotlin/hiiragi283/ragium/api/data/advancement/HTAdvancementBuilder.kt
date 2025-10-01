package hiiragi283.ragium.api.data.advancement

import hiiragi283.ragium.api.extension.wrapOptional
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.util.HTDslMarker
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.DisplayInfo
import net.minecraft.advancements.critereon.ConsumeItemTrigger
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.conditions.ICondition

@HTDslMarker
class HTAdvancementBuilder private constructor(private val parent: HTAdvancementKey?) {
    companion object {
        @JvmStatic
        fun root(): HTAdvancementBuilder = HTAdvancementBuilder(null)

        @JvmStatic
        fun child(parent: HTAdvancementKey): HTAdvancementBuilder = HTAdvancementBuilder(parent)

        @JvmStatic
        fun child(parent: AdvancementHolder): HTAdvancementBuilder = child(HTAdvancementKey(parent.id))
    }

    var display: DisplayInfo? = null
    var rewards: AdvancementRewards = AdvancementRewards.EMPTY
    private val criteria: MutableMap<String, Criterion<*>> = mutableMapOf()
    var requirements: AdvancementRequirements? = null
    var strategy: AdvancementRequirements.Strategy = AdvancementRequirements.Strategy.AND

    inline fun display(builderAction: HTDisplayInfoBuilder.() -> Unit): HTAdvancementBuilder = apply {
        this.display = HTDisplayInfoBuilder.create(builderAction)
    }

    //    Criteria    //

    fun addCriterion(key: String, criterion: Criterion<*>): HTAdvancementBuilder = apply {
        criteria[key] = criterion
    }

    fun hasItem(key: String, predicate: ItemPredicate.Builder): HTAdvancementBuilder =
        addCriterion(key, InventoryChangeTrigger.TriggerInstance.hasItems(predicate))

    fun hasAnyItem(item: HTItemHolderLike): HTAdvancementBuilder = hasAnyItem("has_${item.getPath()}", item)

    fun hasAnyItem(key: String, items: Collection<ItemLike>): HTAdvancementBuilder = hasAnyItem(key, *items.toTypedArray())

    fun hasAnyItem(key: String, vararg items: ItemLike): HTAdvancementBuilder = hasItem(key, ItemPredicate.Builder.item().of(*items))

    fun hasAnyItem(tagKey: TagKey<Item>): HTAdvancementBuilder =
        hasItem("has_${tagKey.location.toDebugFileName()}", ItemPredicate.Builder.item().of(tagKey))

    fun hasAllItem(key: String, vararg items: ItemLike): HTAdvancementBuilder =
        addCriterion(key, InventoryChangeTrigger.TriggerInstance.hasItems(*items))

    fun useItem(item: HTItemHolderLike): HTAdvancementBuilder =
        addCriterion("use_${item.getPath()}", ConsumeItemTrigger.TriggerInstance.usedItem(item))

    //    Conditions    //

    private val conditions: MutableList<ICondition> = mutableListOf()

    fun addConditions(vararg conditions: ICondition): HTAdvancementBuilder = apply {
        this.conditions.addAll(conditions)
    }

    fun save(output: HTAdvancementOutput, key: HTAdvancementKey): AdvancementHolder {
        val id: ResourceLocation = key.id
        val adv = Advancement(
            parent?.id.wrapOptional(),
            display.wrapOptional(),
            rewards,
            criteria,
            this.requirements ?: strategy.create(criteria.keys),
            true,
        )
        output.accept(id, adv, conditions)
        return AdvancementHolder(id, adv)
    }
}
