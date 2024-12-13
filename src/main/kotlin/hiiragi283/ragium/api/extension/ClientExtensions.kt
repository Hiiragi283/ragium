package hiiragi283.ragium.api.extension

import net.minecraft.world.World

//    World    //

/**
 * Run [action] if [this] world is on client side
 */
fun World.ifClient(action: World.() -> Unit): World = apply {
    if (this.isClient) this.action()
}
