package hiiragi283.ragium.integration

import blusunrize.immersiveengineering.api.tool.RailgunHandler
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.item.HTThrowableItem
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent

@HTAddon("immersiveengineering")
object RagiumIEAddon : RagiumAddon {
    //    RagiumAddon    //

    override val priority: Int = 0

    override fun onCommonSetup(event: FMLCommonSetupEvent) {
        // Dynamite
        RailgunHandler.registerProjectile(
            { Ingredient.of(RagiumItemTags.DYNAMITES) },
            object : RailgunHandler.IRailgunProjectile {
                override fun getProjectile(shooter: Player?, ammo: ItemStack, defaultProjectile: Entity): Entity? {
                    val dynamiteItem: HTThrowableItem? = ammo.item as? HTThrowableItem
                    if (dynamiteItem != null && shooter != null) {
                        val dynamite: Projectile = dynamiteItem.throwDynamite(shooter.level(), shooter, ammo)
                        dynamite.shootFromRotation(shooter, shooter.xRot, shooter.yRot, 0f, 3f, 0f)
                        ammo.consume(1, shooter)
                        return dynamite
                    }
                    return super.getProjectile(shooter, ammo, defaultProjectile)
                }
            },
        )
    }
}
