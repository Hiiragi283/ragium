package hiiragi283.ragium.common.block.cauldron

import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.LayeredCauldronBlock
import net.minecraft.world.level.material.Fluid

class HTLatexCauldronBlock(properties: Properties) :
    LayeredCauldronBlock(Biome.Precipitation.NONE, RagiumCauldronInteractions.LATEX, properties) {
    override fun canReceiveStalactiteDrip(fluid: Fluid): Boolean = false

    override fun asItem(): Item = Items.CAULDRON
}
