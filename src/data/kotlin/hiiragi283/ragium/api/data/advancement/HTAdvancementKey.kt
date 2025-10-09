package hiiragi283.ragium.api.data.advancement

import hiiragi283.ragium.api.extension.toDescriptionKey
import net.minecraft.resources.ResourceLocation

@JvmInline
value class HTAdvancementKey(val id: ResourceLocation) {
    val titleKey: String get() = id.toDescriptionKey("advancements", "title")
    val descKey: String get() = id.toDescriptionKey("advancements", "desc")
}
