package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.attachment.HTValueAttachmentSerializer
import hiiragi283.ragium.api.registry.impl.HTDeferredAttachmentRegister
import hiiragi283.ragium.api.registry.impl.HTDeferredAttachmentType
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.energy.HTEnergyNetwork
import hiiragi283.ragium.common.storage.item.HTUniversalBundleManager
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.Level
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder

object RagiumAttachmentTypes {
    @JvmField
    val REGISTER = HTDeferredAttachmentRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    private val UNIVERSAL_BUNDLE: HTDeferredAttachmentType<HTUniversalBundleManager> =
        REGISTER.registerType("universal_bundle") {
            AttachmentType
                .builder(::HTUniversalBundleManager)
                .serialize(object : HTValueAttachmentSerializer<HTUniversalBundleManager> {
                    override fun read(holder: IAttachmentHolder, input: HTValueInput): HTUniversalBundleManager {
                        val manager = HTUniversalBundleManager()
                        for (color: DyeColor in DyeColor.entries) {
                            val inputIn: HTValueInput = input.child(color.serializedName) ?: continue
                            val handler: HTItemHandler = manager.getHandler(color)
                            HTCapabilityCodec.ITEM.loadFrom(inputIn, handler.getItemSlots(handler.getItemSideFor()))
                        }
                        return manager
                    }

                    override fun write(attachment: HTUniversalBundleManager, output: HTValueOutput) {
                        for (color: DyeColor in DyeColor.entries) {
                            val outputIn: HTValueOutput = output.child(color.serializedName)
                            val handler: HTItemHandler = attachment.getHandler(color)
                            HTCapabilityCodec.ITEM.saveTo(outputIn, handler.getItemSlots(handler.getItemSideFor()))
                        }
                    }
                })
        }

    @JvmField
    val ENERGY_NETWORK: HTDeferredAttachmentType<HTEnergyNetwork> =
        REGISTER.registerSerializable("energy_network", ::HTEnergyNetwork)

    //    Helper    //

    @JvmStatic
    fun getBundleManager(server: MinecraftServer): HTUniversalBundleManager = server.overworld().getData(UNIVERSAL_BUNDLE)

    @JvmStatic
    fun getEnergyNetwork(level: Level?): HTEnergyNetwork? = when (level) {
        is ServerLevel -> level.getData(ENERGY_NETWORK)
        else -> level?.dimension()?.let(::getEnergyNetwork)
    }

    @JvmStatic
    fun getEnergyNetwork(key: ResourceKey<Level>): HTEnergyNetwork? = RagiumAPI
        .getInstance()
        .getCurrentServer()
        ?.getLevel(key)
        ?.getData(ENERGY_NETWORK)
}
