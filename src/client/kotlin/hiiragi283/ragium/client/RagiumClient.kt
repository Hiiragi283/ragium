package hiiragi283.ragium.client

import hiiragi283.lib.HTConstants
import hiiragi283.lib.client.fluid.HTFluidModelRegister
import hiiragi283.lib.client.mod.HTClientMod
import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.vanillaId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.fluid.RagiumFluids
import java.awt.Color
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.world.level.material.FluidState
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.fluid.FluidTintSource
import net.neoforged.neoforge.fluids.FluidStack

@Mod(value = RagiumAPI.MOD_ID, dist = [Dist.CLIENT])
data object RagiumClient : HTClientMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {}

    override fun registerFluidModels(register: HTFluidModelRegister) {
        for ((color: HTDefaultColor, content: HTFluidContent) in RagiumFluids.DYES.asSequenceWithColor()) {
            register.register(content) {
                setDull()
                color.color.let(::colorTint)
            }
        }

        register.register(RagiumFluids.HONEY) {
            still = Material(vanillaId(HTConstants.BLOCK, "honey_block_top"), true)
            copyStillToFlowing()
        }
        register.register(RagiumFluids.POTION) {
            setDull()
            tintSource = object : FluidTintSource {
                override fun color(state: FluidState): Int = -1

                override fun colorAsStack(stack: FluidStack): Int = "ff000000".hexToInt() or HTPotionHelper.getPotion(stack).color
            }
        }
        register.register(RagiumFluids.OMINOUS_FLUX) {
            setMolten()
            colorTint(Color(0x003366))
        }

        register.register(RagiumFluids.MOLTEN_GLASS) {
            setMolten()
            colorTint(Color(0xffffff))
        }
        register.register(RagiumFluids.MOLTEN_REDSTONE) {
            setMolten()
            colorTint(Color(0xcc0000))
        }
        register.register(RagiumFluids.MOLTEN_GLOWSTONE) {
            setMolten()
            colorTint(Color(0xffcc66))
        }
        register.register(RagiumFluids.MOLTEN_ENDER) {
            setMolten()
            colorTint(Color(0x006666))
        }
        register.register(RagiumFluids.MOLTEN_BLAZE) {
            setMolten()
            colorTint(Color(0xff9900))
        }
    }
}
