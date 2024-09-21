package hiiragi283.ragium.client.util

import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i

//    MatrixStack    //

fun MatrixStack.translate(x: Int, y: Int, z: Int) {
    translate(x.toDouble(), y.toDouble(), z.toDouble())
}

fun MatrixStack.translate(pos: Vec3i) {
    translate(pos.x, pos.y, pos.z)
}

fun MatrixStack.translate(pos: Vec3d) {
    translate(pos.x, pos.y, pos.z)
}
