package hiiragi283.ragium.api.material

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.MutableComponent
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

data class HTMaterialDefinition(val tagPrefix: HTTagPrefix, val material: HTMaterialKey) {
    companion object {
        @JvmField
        val CODEC: Codec<HTMaterialDefinition> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTTagPrefix.FIELD_CODEC.forGetter(HTMaterialDefinition::tagPrefix),
                    HTMaterialKey.FIELD_CODEC.forGetter(HTMaterialDefinition::material),
                ).apply(instance, ::HTMaterialDefinition)
        }
    }

    val tagKey: TagKey<Item> = tagPrefix.createTag(material)
    val text: MutableComponent = tagPrefix.createText(material)
}
