package hiiragi283.ragium.api.data.advancement

import net.minecraft.advancements.Advancement
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.conditions.ICondition
import kotlin.collections.toList

fun interface HTAdvancementOutput {
    fun accept(id: ResourceLocation, advancement: Advancement, vararg conditions: ICondition) {
        accept(id, advancement, conditions.toList())
    }

    fun accept(id: ResourceLocation, advancement: Advancement, conditions: List<ICondition>)
}
