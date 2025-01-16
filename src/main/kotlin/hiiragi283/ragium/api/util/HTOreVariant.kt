package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

enum class HTOreVariant(val baseStone: Block, baseStoneName: String) {
    OVERWORLD(Blocks.STONE, "stone"),
    DEEP(Blocks.DEEPSLATE, "deepslate"),
    NETHER(Blocks.NETHERRACK, "netherrack"),
    END(Blocks.END_STONE, "end_stone"),
    ;

    val baseStoneName: ResourceLocation = ResourceLocation.withDefaultNamespace(baseStoneName)

    val translationKey = "ore_variant.ragium.$baseStoneName"

    fun createText(material: HTMaterialKey): MutableComponent = Component.translatable(translationKey, material.text)

    interface Content : HTBlockContent.Material {
        val oreVariant: HTOreVariant

        override val tagPrefix: HTTagPrefix
            get() = HTTagPrefix.ORE
    }
}
