package hiiragi283.ragium.api.material

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyHolder
import io.netty.buffer.ByteBuf
import net.minecraft.component.ComponentType
import net.minecraft.item.Item
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.StringIdentifiable

class HTMaterialKey private constructor(val name: String) :
    Comparable<HTMaterialKey>,
    StringIdentifiable {
        companion object {
            private val instances: MutableMap<String, HTMaterialKey> = mutableMapOf()

            @JvmField
            val CODEC: Codec<HTMaterialKey> = Codec.STRING.xmap(Companion::of, HTMaterialKey::name)

            @JvmField
            val PACKET_CODEC: PacketCodec<ByteBuf, HTMaterialKey> =
                PacketCodecs.STRING.xmap(Companion::of, HTMaterialKey::name)

            @JvmField
            val COMPONENT_TYPE: ComponentType<HTMaterialKey> =
                ComponentType
                    .builder<HTMaterialKey>()
                    .codec(CODEC)
                    .packetCodec(PACKET_CODEC)
                    .build()

            @JvmStatic
            fun of(name: String): HTMaterialKey = instances.computeIfAbsent(name, ::HTMaterialKey)
        }

        val translationKey: String = "material.${RagiumAPI.MOD_ID}.$name"
        val text: MutableText
            get() = Text.translatable(translationKey)

        fun asProperties(): HTPropertyHolder = RagiumAPI.getInstance().materialRegistry.getProperty(this)

        fun getItem(prefix: HTTagPrefix): Item? = RagiumAPI
            .getInstance()
            .materialRegistry.items
            .get(prefix, this)

        //    Comparable    //

        override fun compareTo(other: HTMaterialKey): Int = name.compareTo(other.name)

        //    StringIdentifiable    //

        override fun asString(): String = name

        //    Type    //

        enum class Type(val validPrefixes: List<HTTagPrefix>) {
            ALLOY(HTTagPrefix.DUST, HTTagPrefix.INGOT, HTTagPrefix.PLATE, HTTagPrefix.STORAGE_BLOCK),
            DUST(HTTagPrefix.DUST),
            GEM(HTTagPrefix.DUST, HTTagPrefix.GEM, HTTagPrefix.ORE, HTTagPrefix.STORAGE_BLOCK),
            METAL(
                HTTagPrefix.DUST,
                HTTagPrefix.INGOT,
                HTTagPrefix.ORE,
                HTTagPrefix.PLATE,
                HTTagPrefix.RAW_MATERIAL,
                HTTagPrefix.STORAGE_BLOCK,
            ),
            MINERAL(HTTagPrefix.DUST, HTTagPrefix.ORE, HTTagPrefix.RAW_MATERIAL),
            PLASTIC(HTTagPrefix.PLATE),
            ;

            constructor(vararg prefixed: HTTagPrefix) : this(prefixed.toList())

            fun isValidPrefix(prefix: HTTagPrefix): Boolean = prefix in validPrefixes
        }
    }
