package hiiragi283.ragium.api.data.advancement

import hiiragi283.ragium.api.data.advancement.HTDisplayInfoBuilder
import hiiragi283.ragium.api.util.HTDslMarker
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.DisplayInfo
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import java.util.*
import java.util.function.Consumer

@HTDslMarker
class HTAdvancementBuilder private constructor(private val parent: ResourceKey<Advancement>?) {
    companion object {
        @JvmStatic
        fun root(): HTAdvancementBuilder = HTAdvancementBuilder(null)

        @JvmStatic
        fun child(parent: ResourceKey<Advancement>): HTAdvancementBuilder = HTAdvancementBuilder(parent)

        @JvmStatic
        fun child(parent: AdvancementHolder): HTAdvancementBuilder = child(ResourceKey.create(Registries.ADVANCEMENT, parent.id))
    }

    var display: DisplayInfo? = null
    var rewards: AdvancementRewards = AdvancementRewards.EMPTY
    private val criteria: MutableMap<String, Criterion<*>> = mutableMapOf()
    var requirements: AdvancementRequirements? = null
    var strategy: AdvancementRequirements.Strategy = AdvancementRequirements.Strategy.AND

    inline fun display(builderAction: HTDisplayInfoBuilder.() -> Unit): HTAdvancementBuilder = apply {
        this.display = HTDisplayInfoBuilder.create(builderAction)
    }

    fun addCriterion(key: String, criterion: Criterion<*>): HTAdvancementBuilder = apply {
        criteria[key] = criterion
    }

    fun build(key: ResourceKey<Advancement>): AdvancementHolder {
        val requirements: AdvancementRequirements = this.requirements ?: strategy.create(criteria.keys)
        return AdvancementHolder(
            key.location(),
            Advancement(
                Optional.ofNullable(parent?.location()),
                Optional.ofNullable(display),
                rewards,
                criteria,
                requirements,
                true,
            ),
        )
    }

    fun save(consumer: Consumer<AdvancementHolder>, key: ResourceKey<Advancement>): AdvancementHolder = build(key).apply(consumer::accept)
}
