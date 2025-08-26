package hiiragi283.ragium.api.data.advancement

import hiiragi283.ragium.api.util.HTDslMarker
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.DisplayInfo
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.conditions.ICondition
import java.util.Optional

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

    fun addCriterion(key: String, criterion: Criterion<*>): HTAdvancementBuilder = apply {
        criteria[key] = criterion
    }

    private val conditions: MutableList<ICondition> = mutableListOf()

    fun addConditions(vararg conditions: ICondition): HTAdvancementBuilder = apply {
        this.conditions.addAll(conditions)
    }

    fun save(output: HTAdvancementOutput, key: HTAdvancementKey): AdvancementHolder {
        val id: ResourceLocation = key.id
        val adv = Advancement(
            Optional.ofNullable(parent?.id),
            Optional.ofNullable(display),
            rewards,
            criteria,
            this.requirements ?: strategy.create(criteria.keys),
            true,
        )
        output.accept(id, adv, conditions)
        return AdvancementHolder(id, adv)
    }
}
