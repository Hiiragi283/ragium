package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.material.HTMaterialKey
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor

/**
 * 鉱石の母岩部分を管理するクラス
 */
enum class HTOreVariant(baseStoneName: String) {
    OVERWORLD("stone") {
        override fun createId(key: HTMaterialKey): String = "${key.name}_ore"

        override fun createProperty(): BlockBehaviour.Properties = blockProperty()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(3f, 3f)
    },
    DEEPSLATE("deepslate") {
        override fun createId(key: HTMaterialKey): String = "deepslate_${key.name}_ore"

        override fun createProperty(): BlockBehaviour.Properties = blockProperty()
            .mapColor(MapColor.DEEPSLATE)
            .requiresCorrectToolForDrops()
            .strength(4.5f, 3f)
            .sound(SoundType.DEEPSLATE)
    },
    NETHER("netherrack") {
        override fun createId(key: HTMaterialKey): String = "nether_${key.name}_ore"

        override fun createProperty(): BlockBehaviour.Properties = blockProperty()
            .mapColor(MapColor.NETHER)
            .requiresCorrectToolForDrops()
            .strength(3f, 3f)
            .sound(SoundType.NETHER_ORE)
    },
    END("end_stone") {
        override fun createId(key: HTMaterialKey): String = "end_${key.name}_ore"

        override fun createProperty(): BlockBehaviour.Properties = blockProperty()
            .mapColor(MapColor.SAND)
            .requiresCorrectToolForDrops()
            .strength(3f, 9f)
            .sound(SoundType.AMETHYST)
    },
    ;

    val baseStoneName: ResourceLocation = vanillaId(baseStoneName)

    abstract fun createId(key: HTMaterialKey): String

    abstract fun createProperty(): BlockBehaviour.Properties

    val translationKey = "ore_variant.ragium.$baseStoneName"

    fun createText(material: HTMaterialKey): MutableComponent = Component.translatable(translationKey, material.text)
}
