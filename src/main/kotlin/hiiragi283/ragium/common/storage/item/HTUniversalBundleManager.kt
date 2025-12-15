package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.inventory.HTMenuCallback
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor

class HTUniversalBundleManager private constructor(map: Map<DyeColor, HTItemHandler>) : HTValueSerializable {
    companion object {
        @JvmStatic
        fun createSlots(): List<HTItemSlot> = HTSimpleItemHandler.createSlots(3) { x: Int, y: Int -> HTBasicItemSlot.create(null, x, y) }
    }

    constructor() : this(mapOf())

    private val map: MutableMap<DyeColor, HTItemHandler> = map.toMutableMap()

    fun getHandler(color: DyeColor): HTItemHandler = map.computeIfAbsent(color) { _: DyeColor -> BundleHandler(createSlots()) }

    override fun serialize(output: HTValueOutput) {
        for (color: DyeColor in DyeColor.entries) {
            val outputIn: HTValueOutput = output.child(color.serializedName)
            val handler: HTItemHandler = getHandler(color)
            HTCapabilityCodec.ITEM.saveTo(outputIn, handler.getItemSlots(handler.getItemSideFor()))
        }
    }

    override fun deserialize(input: HTValueInput) {
        for (color: DyeColor in DyeColor.entries) {
            val inputIn: HTValueInput = input.child(color.serializedName) ?: continue
            val handler: HTItemHandler = getHandler(color)
            HTCapabilityCodec.ITEM.loadFrom(inputIn, handler.getItemSlots(handler.getItemSideFor()))
        }
    }

    private class BundleHandler(slots: List<HTItemSlot>) :
        HTSimpleItemHandler(slots),
        HTMenuCallback {
        override fun openMenu(player: Player) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.WOOL_PLACE, SoundSource.PLAYERS)
        }

        override fun closeMenu(player: Player) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.WOOL_BREAK, SoundSource.PLAYERS)
        }
    }
}
