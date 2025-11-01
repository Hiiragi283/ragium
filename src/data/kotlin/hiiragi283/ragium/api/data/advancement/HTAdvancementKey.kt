package hiiragi283.ragium.api.data.advancement

import hiiragi283.ragium.api.registry.toDescriptionKey
import net.minecraft.advancements.Advancement
import net.minecraft.resources.ResourceKey

typealias HTAdvancementKey = ResourceKey<Advancement>

val HTAdvancementKey.titleKey: String get() = this.toDescriptionKey("advancements", "title")
val HTAdvancementKey.descKey: String get() = this.toDescriptionKey("advancements", "desc")
