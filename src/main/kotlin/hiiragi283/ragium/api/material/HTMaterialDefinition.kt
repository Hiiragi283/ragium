package hiiragi283.ragium.api.material

import com.mojang.serialization.Codec
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

data class HTMaterialDefinition(val prefix: HTTagPrefix, val material: HTMaterialKey) {
    companion object {
        @JvmField
        val FLAT_CODEC: Codec<HTMaterialDefinition> = ResourceLocation.CODEC
            .comapFlatMap(
                { id: ResourceLocation ->
                    val material: HTMaterialKey = HTMaterialKey.of(id.path)
                    HTTagPrefix.fromSerializedName(id.namespace).map { prefix: HTTagPrefix ->
                        HTMaterialDefinition(prefix, material)
                    }
                },
                { definition: HTMaterialDefinition ->
                    ResourceLocation.fromNamespaceAndPath(definition.prefix.serializedName, definition.material.name)
                },
            )
    }

    val tagKey: TagKey<Item> = prefix.createTag(material)
    val text: MutableComponent = prefix.createText(material)
}
