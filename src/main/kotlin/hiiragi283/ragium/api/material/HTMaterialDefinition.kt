package hiiragi283.ragium.api.material

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.DisableOverwriteMerger
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.MutableComponent
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType
import net.neoforged.neoforge.registries.datamaps.DataMapType

data class HTMaterialDefinition(val tagPrefix: HTTagPrefix, val material: HTMaterialKey) {
    companion object {
        @JvmField
        val CODEC: Codec<HTMaterialDefinition> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTTagPrefix.CODEC.fieldOf("prefix").forGetter(HTMaterialDefinition::tagPrefix),
                    HTMaterialKey.CODEC.fieldOf("material").forGetter(HTMaterialDefinition::material),
                ).apply(instance, ::HTMaterialDefinition)
        }

        @JvmField
        val DATA_MAP_TYPE: DataMapType<Item, HTMaterialDefinition> =
            AdvancedDataMapType
                .builder(RagiumAPI.id("material"), Registries.ITEM, CODEC)
                .synced(CODEC, false)
                .merger(DisableOverwriteMerger())
                .build()
    }

    val tagKey: TagKey<Item> = tagPrefix.createTag(material)
    val text: MutableComponent = tagPrefix.createText(material)
}
