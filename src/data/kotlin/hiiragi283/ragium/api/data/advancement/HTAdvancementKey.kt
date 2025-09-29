package hiiragi283.ragium.api.data.advancement

import hiiragi283.ragium.api.extension.toDescriptionKey
import net.minecraft.resources.ResourceLocation

data class HTAdvancementKey(val id: ResourceLocation) {
    val titleKey: String = id.toDescriptionKey("advancements", "title")
    val descKey: String = id.toDescriptionKey("advancements", "desc")
}
