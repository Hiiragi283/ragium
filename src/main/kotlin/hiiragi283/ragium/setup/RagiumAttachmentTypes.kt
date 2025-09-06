package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.energy.HTEnergyNetwork
import hiiragi283.ragium.common.storage.item.HTUniversalBundleManager
import hiiragi283.ragium.common.storage.nbt.HTTagValueInput
import hiiragi283.ragium.common.storage.nbt.HTTagValueOutput
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.DyeColor
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.attachment.IAttachmentSerializer
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object RagiumAttachmentTypes {
    @JvmField
    val REGISTER: DeferredRegister<AttachmentType<*>> =
        DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Any> register(
        name: String,
        supplier: () -> T,
        codec: BiCodec<in RegistryFriendlyByteBuf, T>,
    ): Supplier<AttachmentType<T>> = REGISTER.register(name) { _: ResourceLocation ->
        AttachmentType
            .builder(supplier)
            .sync(codec.streamCodec)
            .serialize(codec.codec)
            .build()
    }

    @JvmStatic
    fun getBundleManager(server: MinecraftServer): HTUniversalBundleManager = server.overworld().getData(UNIVERSAL_BUNDLE)

    @JvmStatic
    private val UNIVERSAL_BUNDLE: Supplier<AttachmentType<HTUniversalBundleManager>> =
        REGISTER.register("universal_bundle") { _: ResourceLocation ->
            AttachmentType
                .builder(::HTUniversalBundleManager)
                .serialize(object : IAttachmentSerializer<CompoundTag, HTUniversalBundleManager> {
                    override fun read(
                        holder: IAttachmentHolder,
                        tag: CompoundTag,
                        provider: HolderLookup.Provider,
                    ): HTUniversalBundleManager {
                        val manager = HTUniversalBundleManager()

                        val input: HTValueInput = HTTagValueInput.create(provider, tag)
                        for (color: DyeColor in DyeColor.entries) {
                            val inputIn: HTValueInput = input.child(color.serializedName) ?: continue
                            val handler: HTItemHandler = manager.getHandler(color)
                            HTCapabilityCodec.ITEM.loadFrom(inputIn, handler.getItemSlots(handler.getItemSideFor()))
                        }

                        return manager
                    }

                    override fun write(attachment: HTUniversalBundleManager, provider: HolderLookup.Provider): CompoundTag? {
                        val tag = CompoundTag()
                        val output = HTTagValueOutput(provider, tag)
                        for (color: DyeColor in DyeColor.entries) {
                            val outputIn: HTValueOutput = output.child(color.serializedName)
                            val handler: HTItemHandler = attachment.getHandler(color)
                            HTCapabilityCodec.ITEM.saveTo(outputIn, handler.getItemSlots(handler.getItemSideFor()))
                        }
                        return when {
                            tag.isEmpty -> null
                            else -> tag
                        }
                    }
                })
                .build()
        }

    @JvmField
    val ENERGY_NETWORK: Supplier<AttachmentType<HTEnergyNetwork>> =
        register("energy_network", ::HTEnergyNetwork, HTEnergyNetwork.CODEC)
}
