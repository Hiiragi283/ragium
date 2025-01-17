package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor

enum class HTOreVariant(baseStoneName: String) {
    OVERWORLD("stone") {
        override fun createProperty(): BlockBehaviour.Properties = blockProperty()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(3f, 3f)
    },
    DEEP("deepslate") {
        override fun createProperty(): BlockBehaviour.Properties = blockProperty()
            .mapColor(MapColor.DEEPSLATE)
            .requiresCorrectToolForDrops()
            .strength(4.5f, 3f)
            .sound(SoundType.DEEPSLATE)
    },
    NETHER("netherrack") {
        override fun createProperty(): BlockBehaviour.Properties = blockProperty()
            .mapColor(MapColor.NETHER)
            .requiresCorrectToolForDrops()
            .strength(3f, 3f)
            .sound(SoundType.NETHER_ORE)
    },
    END("end_stone") {
        override fun createProperty(): BlockBehaviour.Properties = blockProperty()
            .mapColor(MapColor.SAND)
            .requiresCorrectToolForDrops()
            .strength(3f, 9f)
            .sound(SoundType.AMETHYST)
    },
    ;

    val baseStoneName: ResourceLocation = ResourceLocation.withDefaultNamespace(baseStoneName)

    abstract fun createProperty(): BlockBehaviour.Properties

    val translationKey = "ore_variant.ragium.$baseStoneName"

    fun createText(material: HTMaterialKey): MutableComponent = Component.translatable(translationKey, material.text)

    interface Content : HTBlockContent.Material {
        val oreVariant: HTOreVariant

        override val tagPrefix: HTTagPrefix
            get() = HTTagPrefix.ORE
    }
}
