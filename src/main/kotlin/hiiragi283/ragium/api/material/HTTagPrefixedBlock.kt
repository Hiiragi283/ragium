package hiiragi283.ragium.api.material

import net.minecraft.block.Block
import net.minecraft.text.MutableText

open class HTTagPrefixedBlock(val prefix: HTTagPrefix, val key: HTMaterialKey, settings: Settings) : Block(settings) {
    override fun getName(): MutableText = prefix.getText(key)
}
