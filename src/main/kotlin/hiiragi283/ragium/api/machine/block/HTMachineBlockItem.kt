package hiiragi283.ragium.api.machine.block

import hiiragi283.ragium.api.extension.machineKey
import hiiragi283.ragium.api.extension.tier
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class HTMachineBlockItem(block: HTMachineBlock, settings: Settings) :
    BlockItem(
        block,
        settings
            .machineKey(block.key)
            .tier(block.tier)
            .rarity(block.tier.rarity),
    ) {
    val key: HTMachineKey = block.key
    val tier: HTMachineTier = block.tier

    override fun getName(): Text = tier.createPrefixedText(key)

    override fun getName(stack: ItemStack): Text = name
}
