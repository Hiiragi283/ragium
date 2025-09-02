package hiiragi283.ragium.integration.immersive

import blusunrize.immersiveengineering.api.tool.RailgunHandler
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.common.entity.HTThrownCaptureEgg
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent

@HTAddon(RagiumConst.IMMERSIVE)
object RagiumImmersiveAddon : RagiumAddon {
    override fun onCommonSetup(event: FMLCommonSetupEvent) {
        // Eldritch Egg
        RailgunHandler.registerProjectile(
            { Ingredient.of(RagiumItems.ELDRITCH_EGG) },
            object : RailgunHandler.IRailgunProjectile {
                override fun getProjectile(shooter: Player?, ammo: ItemStack, defaultProjectile: Entity): Entity? {
                    if (shooter != null) {
                        val egg = HTThrownCaptureEgg(shooter.level(), shooter)
                        egg.item = ammo
                        egg.shootFromRotation(shooter, shooter.xRot, shooter.yRot, 0f, 2.5f, 0f)
                        return egg
                    }
                    return super.getProjectile(shooter, ammo, defaultProjectile)
                }
            },
        )
    }
}
